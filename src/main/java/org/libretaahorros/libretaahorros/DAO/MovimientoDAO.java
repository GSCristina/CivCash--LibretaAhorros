package org.libretaahorros.libretaahorros.DAO;

import org.libretaahorros.libretaahorros.dataAccess.ConnectionDB;
import org.libretaahorros.libretaahorros.model.Movimiento;
import org.libretaahorros.libretaahorros.model.Ingreso;
import org.libretaahorros.libretaahorros.model.Gasto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    private final static String SQL_INSERT = "INSERT INTO MOVIMIENTO (id_libreta_fk, fecha, concepto, cantidad, tipo, categoria, procedencia, metodo_pago, responsable) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final static String SQL_FIND_BY_LIBRETA = "SELECT * FROM MOVIMIENTO WHERE id_libreta_fk = ? ORDER BY fecha DESC";
    private final static String SQL_DELETE_MOVIMIENTO = "DELETE FROM MOVIMIENTO WHERE id_movimiento = ?";
    private final static String SQL_DELETE_ALL_BY_LIBRETA = "DELETE FROM MOVIMIENTO WHERE id_libreta_fk = ?";
    private final static String SQL_UPDATE_MOVIMIENTO =
            "UPDATE MOVIMIENTO SET concepto = ?, cantidad = ?, fecha = ?, categoria = ?, tipo = ?, procedencia = ?, metodo_pago = ?, responsable = ? WHERE id_movimiento = ?";

    public static boolean addMovimiento(Movimiento m) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {

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

            ps.setString(9, m.getResponsable());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Movimiento> findAllByLibreta(int idLibreta) {
        List<Movimiento> lista = new ArrayList<>();
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_LIBRETA)) {

            ps.setInt(1, idLibreta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    String responsable = rs.getString("responsable");

                    if ("Ingreso".equalsIgnoreCase(tipo)) {
                        lista.add(new Ingreso(
                                rs.getInt("id_movimiento"),
                                rs.getString("concepto"),
                                rs.getDouble("cantidad"),
                                rs.getDate("fecha").toLocalDate(),
                                rs.getString("categoria"),
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
                                rs.getString("categoria"),
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

    public static boolean deleteMovimiento(int idMovimiento) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE_MOVIMIENTO)) {
            ps.setInt(1, idMovimiento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteAllByLibreta(int idLibreta) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE_ALL_BY_LIBRETA)) {
            ps.setInt(1, idLibreta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateMovimiento(Movimiento m) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_MOVIMIENTO)) {

            ps.setString(1, m.getConcepto());
            ps.setDouble(2, m.getCantidad());
            ps.setDate(3, Date.valueOf(m.getFecha()));
            ps.setString(4, m.getCategoria());
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
}
