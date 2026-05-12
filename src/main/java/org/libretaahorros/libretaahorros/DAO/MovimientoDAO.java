package org.libretaahorros.libretaahorros.DAO;

import org.libretaahorros.libretaahorros.dataAccess.ConnectionDB;
import org.libretaahorros.libretaahorros.model.Movimiento;
import org.libretaahorros.libretaahorros.model.Ingreso;
import org.libretaahorros.libretaahorros.model.Gasto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    private final static String SQL_INSERT = "INSERT INTO MOVIMIENTO (id_libreta_fk, fecha, concepto, cantidad, tipo, categoria, procedencia, metodo_pago) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private final static String SQL_FIND_BY_LIBRETA = "SELECT * FROM MOVIMIENTO WHERE id_libreta_fk = ? ORDER BY fecha DESC";

    /**
     * Inserta un movimiento (Ingreso o Gasto) en la BD.
     */
    public static boolean addMovimiento(Movimiento m) {
        try (PreparedStatement ps = ConnectionDB.getInstance().prepareStatement(SQL_INSERT)) {
            ps.setInt(1, m.getIdLibreta());
            ps.setDate(2, Date.valueOf(m.getFecha()));
            ps.setString(3, m.getConcepto());
            ps.setDouble(4, m.getCantidad());

            if (m instanceof Ingreso) {
                ps.setString(5, "Ingreso");
                ps.setString(6, m.getCategoria());
                ps.setString(7, ((Ingreso) m).getProcedencia());
                ps.setNull(8, Types.VARCHAR);
            } else {
                ps.setString(5, "Gasto");
                ps.setString(6, m.getCategoria());
                ps.setNull(7, Types.VARCHAR);
                ps.setString(8, ((Gasto) m).getMetodoPago());
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera todos los movimientos de una libreta y reconstruye los objetos hijos.
     */
    public static List<Movimiento> findAllByLibreta(int idLibreta) {
        List<Movimiento> lista = new ArrayList<>();
        try (PreparedStatement ps = ConnectionDB.getInstance().prepareStatement(SQL_FIND_BY_LIBRETA)) {
            ps.setInt(1, idLibreta);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String tipo = rs.getString("tipo");
                if ("Ingreso".equalsIgnoreCase(tipo)) {
                    lista.add(new Ingreso(
                            rs.getInt("id_movimiento"),
                            rs.getString("concepto"),
                            rs.getDouble("cantidad"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getString("categoria"),
                            rs.getInt("id_libreta_fk"),
                            rs.getString("procedencia")
                    ));
                } else {
                    lista.add(new Gasto(
                            rs.getInt("id_movimiento"),
                            rs.getString("concepto"),
                            rs.getDouble("cantidad"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getString("categoria"),
                            rs.getInt("id_libreta_fk"),
                            rs.getString("metodo_pago")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
