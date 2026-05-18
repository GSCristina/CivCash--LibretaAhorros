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
    private final static String SQL_UPDATE_NOMBRE = "UPDATE LIBRETA SET nombre = ? WHERE id_libreta = ?";
    private final static String SQL_DELETE_LIBRETA = "DELETE FROM LIBRETA WHERE id_libreta = ?";

    public static boolean addLibreta(Libreta libreta, int idUsuario, String emailAmigo) {
        try (Connection con = ConnectionDB.getInstance()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, libreta.getNombre());
                ps.setDouble(2, libreta.getSaldoActual());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        libreta.setIdLibreta(idGenerado);

                        try (PreparedStatement psInter = con.prepareStatement(SQL_INSERT_INTERMEDIA)) {
                            psInter.setInt(1, idUsuario);
                            psInter.setInt(2, idGenerado);
                            psInter.executeUpdate();
                        }

                        if (emailAmigo != null && !emailAmigo.trim().isEmpty()) {
                            String sqlBuscarAmigo = "SELECT id_usuario FROM USUARIO WHERE email = ?";
                            try (PreparedStatement psAmigo = con.prepareStatement(sqlBuscarAmigo)) {
                                psAmigo.setString(1, emailAmigo.trim());
                                try (ResultSet rsAmigo = psAmigo.executeQuery()) {
                                    if (rsAmigo.next()) {
                                        int idAmigo = rsAmigo.getInt("id_usuario");
                                        try (PreparedStatement psInterAmigo = con.prepareStatement(SQL_INSERT_INTERMEDIA)) {
                                            psInterAmigo.setInt(1, idAmigo);
                                            psInterAmigo.setInt(2, idGenerado);
                                            psInterAmigo.executeUpdate();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Libreta> findAllByUsuario(int idUsuario) {
        List<Libreta> lista = new ArrayList<>();
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_USER)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Libreta l = new Libreta(
                            rs.getInt("id_libreta"),
                            rs.getString("nombre"),
                            rs.getDouble("saldo_actual")
                    );
                    lista.add(l);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean updateSaldo(int idLibreta, double nuevoSaldo) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_SALDO)) {

            ps.setDouble(1, nuevoSaldo);
            ps.setInt(2, idLibreta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateNombreLibreta(int idLibreta, String nuevoNombre) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_NOMBRE)) {

            ps.setString(1, nuevoNombre);
            ps.setInt(2, idLibreta);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteLibreta(int idLibreta) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE_LIBRETA)) {

            ps.setInt(1, idLibreta);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
