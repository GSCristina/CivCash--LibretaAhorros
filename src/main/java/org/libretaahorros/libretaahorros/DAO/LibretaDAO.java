package org.libretaahorros.libretaahorros.DAO;

import org.libretaahorros.libretaahorros.dataAccess.ConnectionDB;
import org.libretaahorros.libretaahorros.model.Libreta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibretaDAO {
    private final static String SQL_INSERT = "INSERT INTO LIBRETA (nombre, saldo_actual) VALUES (?, ?)";
    private final static String SQL_INSERT_INTERMEDIA = "INSERT INTO USUARIO_LIBRETA (id_usuario, id_libreta) VALUES (?, ?)";
    private final static String SQL_FIND_BY_USER = "SELECT L.* FROM LIBRETA L " +
                    "INNER JOIN USUARIO_LIBRETA UL ON L.id_libreta = UL.id_libreta " +
                    "WHERE UL.id_usuario = ?";
    private final static String SQL_UPDATE_SALDO = "UPDATE LIBRETA SET saldo_actual = ? WHERE id_libreta = ?";

    /**
     * Crea una nueva libreta y la asigna a un usuario.
     * @param libreta El objeto libreta con nombre y saldo inicial.
     * @param idUsuario El ID del usuario que crea la libreta.
     * @return true si se creó correctamente en ambas tablas.
     */
    public static boolean addLibreta(Libreta libreta, int idUsuario) {
        Connection con = null;
        try {
            con = ConnectionDB.getInstance();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, libreta.getNombre());
                ps.setDouble(2, libreta.getSaldoActual());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    libreta.setIdLibreta(idGenerado);

                    try (PreparedStatement psInter = con.prepareStatement(SQL_INSERT_INTERMEDIA)) {
                        psInter.setInt(1, idUsuario);
                        psInter.setInt(2, idGenerado);
                        psInter.executeUpdate();
                    }
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Busca todas las libretas que pertenecen a un usuario.
     */
    public static List<Libreta> findAllByUsuario(int idUsuario) {
        List<Libreta> lista = new ArrayList<>();
        try (PreparedStatement ps = ConnectionDB.getInstance().prepareStatement(SQL_FIND_BY_USER)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Libreta l = new Libreta(
                        rs.getInt("id_libreta"),
                        rs.getString("nombre"),
                        rs.getDouble("saldo_actual")
                );
                lista.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Actualiza el saldo de la libreta (se llamará cada vez que añadamos un movimiento)
     */
    public static boolean updateSaldo(int idLibreta, double nuevoSaldo) {
        try (PreparedStatement ps = ConnectionDB.getInstance().prepareStatement(SQL_UPDATE_SALDO)) {
            ps.setDouble(1, nuevoSaldo);
            ps.setInt(2, idLibreta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
