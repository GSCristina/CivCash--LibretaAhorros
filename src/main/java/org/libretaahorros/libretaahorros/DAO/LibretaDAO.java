package org.libretaahorros.libretaahorros.DAO;

import org.libretaahorros.libretaahorros.dataAccess.ConnectionDB;
import org.libretaahorros.libretaahorros.model.Libreta;
import org.libretaahorros.libretaahorros.model.UsuarioLibreta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos (DAO) para gestionar la persistencia de las libretas
 * y sus relaciones relacionales en el sistema. Implemeta el patrón Singleton.
 */
public class LibretaDAO implements GenericDAO<Libreta> {

    private static LibretaDAO instance;

    private LibretaDAO() {}

    /**
     * Devuelve la instancia única de LibretaDAO aplicando el patrón Singleton.
     * @return Instancia única de la clase.
     */
    public static LibretaDAO getInstance() {
        if (instance == null) {
            instance = new LibretaDAO();
        }
        return instance;
    }

    private final static String SQL_INSERT = "INSERT INTO LIBRETA (nombre, saldo_actual) VALUES (?, ?)";
    private final static String SQL_INSERT_INTERMEDIA = "INSERT INTO USUARIO_LIBRETA (id_usuario, id_libreta, rol) VALUES (?, ?, ?)";
    private final static String SQL_FIND_BY_USER = "SELECT L.*, UL.rol FROM LIBRETA L " +
            "INNER JOIN USUARIO_LIBRETA UL ON L.id_libreta = UL.id_libreta " +
            "WHERE UL.id_usuario = ?";
    private final static String SQL_UPDATE_SALDO = "UPDATE LIBRETA SET saldo_actual = ? WHERE id_libreta = ?";
    private final static String SQL_UPDATE_NOMBRE = "UPDATE LIBRETA SET nombre = ? WHERE id_libreta = ?";
    private final static String SQL_DELETE_LIBRETA = "DELETE FROM LIBRETA WHERE id_libreta = ?";

    /**
     * Inserta una libreta de manera genérica en la base de datos (CRUD básico).
     * @param libreta Objeto Libreta que contiene los datos a registrar.
     * @return true si se registró correctamente o false en caso contrario.
     */
    @Override
    public boolean add(Libreta libreta) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, libreta.getNombre());
            ps.setDouble(2, libreta.getSaldoActual());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        libreta.setIdLibreta(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Modifica el nombre de una libreta existente en el sistema.
     * @param libreta Objeto Libreta mapeado con los nuevos cambios.
     * @return true si se actualizó al menos un registro o false si falló.
     */
    @Override
    public boolean update(Libreta libreta) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_NOMBRE)) {
            ps.setString(1, libreta.getNombre());
            ps.setInt(2, libreta.getIdLibreta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Elimina una libreta de la persistencia mediante su identificador único.
     * @param idLibreta Identificador numérico de la libreta a suprimir.
     * @return true si la fila fue borrada con éxito o false si no.
     */
    @Override
    public boolean delete(int idLibreta) {
        try (Connection con = ConnectionDB.getInstance();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE_LIBRETA)) {
            ps.setInt(1, idLibreta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Registra una libreta insertando la libreta y mapeando sus relaciones en la tabla intermedia N:M.
     * Soporta la invitación de múltiples usuarios invitados simultáneamente.
     * @param libreta El objeto Libreta con los datos iniciales.
     * @param idUsuario El identificador numérico del usuario creador (Propietario).
     * @param emailsCompartidos Cadena de caracteres con los correos de los invitados separados por comas.
     * @return {@code true} si la inserción en bloque fue exitosa; {@code false} si falló.
     */
    public boolean addLibretaCompartida(Libreta libreta, int idUsuario, String emailsCompartidos) {
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

                        UsuarioLibreta relacionCreador = new UsuarioLibreta(idUsuario, libreta, "Propietario");
                        try (PreparedStatement psInter = con.prepareStatement(SQL_INSERT_INTERMEDIA)) {
                            psInter.setInt(1, relacionCreador.getIdUsuario());
                            psInter.setInt(2, relacionCreador.getLibreta().getIdLibreta());
                            psInter.setString(3, relacionCreador.getRol());
                            psInter.executeUpdate();
                        }
                        if (emailsCompartidos != null && !emailsCompartidos.trim().isEmpty()) {
                            String[] arrayEmails = emailsCompartidos.split(",");

                            for (String email : arrayEmails) {
                                String emailSaneado = email.trim();

                                if (!emailSaneado.isEmpty()) {
                                    String sqlBuscarAmigo = "SELECT id_usuario FROM USUARIO WHERE email = ?";
                                    try (PreparedStatement psAmigo = con.prepareStatement(sqlBuscarAmigo)) {
                                        psAmigo.setString(1, emailSaneado);
                                        try (ResultSet rsAmigo = psAmigo.executeQuery()) {
                                            if (rsAmigo.next()) {
                                                int idAmigo = rsAmigo.getInt("id_usuario");
                                                UsuarioLibreta relacionInvitado = new UsuarioLibreta(idAmigo, libreta, "Invitado");

                                                try (PreparedStatement psInterAmigo = con.prepareStatement(SQL_INSERT_INTERMEDIA)) {
                                                    psInterAmigo.setInt(1, relacionInvitado.getIdUsuario());
                                                    psInterAmigo.setInt(2, relacionInvitado.getLibreta().getIdLibreta());
                                                    psInterAmigo.setString(3, relacionInvitado.getRol());
                                                    psInterAmigo.executeUpdate();
                                                }
                                            }
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
    /**
     * Recupera y mapea la colección de libretas asociadas a un usuario junto a su rol correspondiente.
     * @param idUsuario Identificador numérico del usuario consultado.
     * @return Lista con los objetos intermedios UsuarioLibreta cargados desde la base de datos.
     */
    public List<UsuarioLibreta> findAllByUsuarioLazy(int idUsuario) {
        List<UsuarioLibreta> lista = new ArrayList<>();
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
                    UsuarioLibreta ul = new UsuarioLibreta();
                    ul.setIdUsuario(idUsuario);
                    ul.setLibreta(l);
                    ul.setRol(rs.getString("rol"));
                    lista.add(ul);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    /**
     * Actualiza el saldo contable actual de una libreta específica.
     * @param idLibreta  Identificador único de la libreta afectada.
     * @param nuevoSaldo Monto monetario double que reemplazará al saldo anterior.
     * @return true si la operación modificó la fila o false si falló.
     */
    public boolean updateSaldo(int idLibreta, double nuevoSaldo) {
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
    /**
     * Realiza un cambio directo sobre el nombre asignado a una libreta mediante consultas directas.
     * @param idLibreta   Identificador único de la libreta a renombrar.
     * @param nuevoNombre Cadena de caracteres con el nuevo título de la libreta.
     * @return true si la modificación impactó en la base de datos o false de lo contrario.
     */
    public boolean updateNombreLibreta(int idLibreta, String nuevoNombre) {
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
}
