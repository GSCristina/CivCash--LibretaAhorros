package org.libretaahorros.libretaahorros.DAO;

import org.libretaahorros.libretaahorros.dataAccess.ConnectionDB;
import org.libretaahorros.libretaahorros.model.Categoria;
import org.libretaahorros.libretaahorros.model.Movimiento;
import org.libretaahorros.libretaahorros.model.Ingreso;
import org.libretaahorros.libretaahorros.model.Gasto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase de Acceso a Datos (DAO) encargada de la persistencia de los movimientos financieros en la base de datos.
 * <p>
 * Implementa la interfaz {@code GenericDAO} parametrizada para la entidad {@code Movimiento}.
 * Utiliza el patrón de diseño Singleton para centralizar las conexiones y mitigar la sobrecarga
 * en el pool de conexiones de MySQL. Resuelve la persistencia de estructuras jerárquicas (herencia)
 * mediante el mapeo selectivo de atributos especializados (procedencia y metodo de pago) en una única tabla.
 */
public class MovimientoDAO implements GenericDAO<Movimiento> {

    private static MovimientoDAO instance;
    /**
     * Constructor privado para restringir la instanciacion externa y garantizar el patrón Singleton.
     */
    private MovimientoDAO() {}
    /**
     * Recupera la instancia única y centralizada del objeto de acceso a datos.
     * * @return La instancia única de {@code MovimientoDAO}.
     */
    public static MovimientoDAO getInstance() {
        if (instance == null) {
            instance = new MovimientoDAO();
        }
        return instance;
    }

    private final static String SQL_INSERT = "INSERT INTO MOVIMIENTO (id_libreta_fk, fecha, concepto, cantidad, tipo, categoria, procedencia, metodo_pago, responsable) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final static String SQL_FIND_BY_LIBRETA = "SELECT * FROM MOVIMIENTO WHERE id_libreta_fk = ? ORDER BY fecha DESC";
    private final static String SQL_DELETE_MOVIMIENTO = "DELETE FROM MOVIMIENTO WHERE id_movimiento = ?";
    private final static String SQL_DELETE_ALL_BY_LIBRETA = "DELETE FROM MOVIMIENTO WHERE id_libreta_fk = ?";
    private final static String SQL_UPDATE_MOVIMIENTO = "UPDATE MOVIMIENTO SET concepto = ?, cantidad = ?, fecha = ?, categoria = ?, tipo = ?, procedencia = ?, metodo_pago = ?, responsable = ? WHERE id_movimiento = ?";

    /**
     * Inserta un nuevo movimiento (Ingreso o Gasto) de forma persistente en el sistema.
     * <p>
     * Evalúa dinámicamente la especialización de la instancia mediante el operador {@code instanceof}
     * para mapear de manera polimórfica las columnas específicas correspondientes en la base de datos.
     * * @param m La instancia de {@code Movimiento} (u objeto hijo) a persistir.
     * @return {@code true} si la inserción alteró con éxito las filas de la base de datos; {@code false} en caso contrario.
     */
    @Override
    public boolean add(Movimiento m) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, m.getIdLibreta());
            ps.setDate(2, Date.valueOf(m.getFecha()));
            ps.setString(3, m.getConcepto());
            ps.setDouble(4, m.getCantidad());

            if (m instanceof Ingreso) {
                ps.setString(5, "Ingreso");
                ps.setString(6, m.getCategoria().name());
                ps.setString(7, ((Ingreso) m).getProcedencia());
                ps.setNull(8, Types.VARCHAR);
            } else {
                ps.setString(5, "Gasto");
                ps.setString(6, m.getCategoria().name());
                ps.setNull(7, Types.VARCHAR);
                ps.setString(8, ((Gasto) m).getMetodoPago());
            }

            ps.setString(9, m.getResponsable());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Actualiza los valores de un registro de movimiento existente en la base de datos.
     * <p>
     * Sincroniza las modificaciones efectuadas en la interfaz discriminando el tipo de objeto
     * de forma segura para setear valores nulos en los campos que no correspondan a su naturaleza.
     * * @param m La instancia de {@code Movimiento} modificada que porta el identificador único.
     * @return {@code true} si se localizó y actualizó el registro con éxito; {@code false} si falló la operación.
     */
    @Override
    public boolean update(Movimiento m) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_MOVIMIENTO)) {

            ps.setString(1, m.getConcepto());
            ps.setDouble(2, m.getCantidad());
            ps.setDate(3, Date.valueOf(m.getFecha()));
            ps.setString(4, m.getCategoria().name());
            ps.setString(5, m.getTipo());

            if (m instanceof Ingreso) {
                ps.setString(6, ((Ingreso) m).getProcedencia());
                ps.setNull(7, Types.VARCHAR);
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setString(7, ((Gasto) m).getMetodoPago());
            }

            ps.setString(8, m.getResponsable());
            ps.setInt(9, m.getIdMovimiento());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Elimina físicamente un movimiento individual de la tabla mediante su clave primaria.
     * * @param idMovimiento Identificador único de la transacción a eliminar.
     * @return {@code true} si la operación eliminó el registro; {@code false} si hubo errores.
     */
    @Override
    public boolean delete(int idMovimiento) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE_MOVIMIENTO)) {
            ps.setInt(1, idMovimiento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Recupera el historial completo de transacciones asociadas a una libreta de ahorros.
     * <p>
     * Consulta los registros ordenándolos de manera descendente por fecha. Transforma los campos
     * planos de la base de datos reconstruyendo objetos especializados de tipo {@code Ingreso}
     * o {@code Gasto} según la naturaleza de la fila, aplicando un control de seguridad en el parseo del Enum.
     * * @param idLibreta Clave primaria de la libreta de ahorros de la cual extraer el historial.
     * @return Una colección {@code List} de objetos de tipo {@code Movimiento}.
     */
    public List<Movimiento> findAllByLibretaEagle(int idLibreta) {
        List<Movimiento> lista = new ArrayList<>();
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_LIBRETA)) {

            ps.setInt(1, idLibreta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    String responsable = rs.getString("responsable");

                    String categoriaTexto = rs.getString("categoria");
                    Categoria categoriaEnum = Categoria.OTROS;
                    if (categoriaTexto != null) {
                        try {
                            categoriaEnum = Categoria.valueOf(categoriaTexto.toUpperCase().trim());
                        } catch (IllegalArgumentException e) {
                            categoriaEnum = Categoria.OTROS;
                        }
                    }
                    if ("Ingreso".equalsIgnoreCase(tipo)) {
                        lista.add(new Ingreso(
                                rs.getInt("id_movimiento"),
                                rs.getString("concepto"),
                                rs.getDouble("cantidad"),
                                rs.getDate("fecha").toLocalDate(),
                                categoriaEnum,
                                rs.getInt("id_libreta_fk"),
                                responsable,
                                rs.getString("procedencia")
                        ));
                    } else {
                        lista.add(new Gasto(
                                rs.getInt("id_movimiento"),
                                rs.getString("concepto"),
                                rs.getDouble("cantidad"),
                                rs.getDate("fecha").toLocalDate(),
                                categoriaEnum,
                                rs.getInt("id_libreta_fk"),
                                responsable,
                                rs.getString("metodo_pago")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Elimina masivamente y en cascada todos los movimientos pertenecientes a una libreta específica.
     * * @param idLibreta Clave primaria de la libreta que se pretende vaciar.
     * @return {@code true} si la purga se completó con éxito; {@code false} si falló.
     */
    public boolean deleteAllByLibreta(int idLibreta) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE_ALL_BY_LIBRETA)) {
            ps.setInt(1, idLibreta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}