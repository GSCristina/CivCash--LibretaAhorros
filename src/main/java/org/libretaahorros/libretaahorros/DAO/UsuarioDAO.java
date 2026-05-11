package org.libretaahorros.libretaahorros.DAO;

import org.libretaahorros.libretaahorros.dataAccess.ConnectionDB;
import org.libretaahorros.libretaahorros.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private final static String SQL_ALL = "SELECT * FROM USUARIO";
    private final static String SQL_FIND_BY_ID = "SELECT * FROM USUARIO WHERE id_usuario = ?";
    private final static String SQL_FIND_BY_EMAIL = "SELECT * FROM USUARIO WHERE email = ?";
    private final static String SQL_VALIDATE_LOGIN = "SELECT * FROM USUARIO WHERE email = ? AND password = ?";
    private final static String SQL_INSERT = "INSERT INTO USUARIO (email, password) VALUES (?, ?)";
    private final static String SQL_UPDATE = "UPDATE USUARIO SET email = ?, password = ? WHERE id_usuario = ?";
    private final static String SQL_DELETE = "DELETE FROM USUARIO WHERE id_usuario = ?";
    /**
     * Comprueba si un email y contraseña existen en la base de datos.
     * @param email Pasamos el email del usuario
     * @param password Ingresamos la contraseña del usuario
     * @return Devuelve el objeto Usuario o null si los datos estan mal.
     */
    public static Usuario validarLogin(String email, String password){
        Usuario usuario = null;
        try (PreparedStatement ps = ConnectionDB.getInstance().prepareStatement(SQL_VALIDATE_LOGIN)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                String emailDB = rs.getString("email");
                String passwordDB = rs.getString("password");
                usuario = new Usuario(idUsuario, emailDB, passwordDB);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }
    /**
     * Devuelve una lista con todos los usuarios almacenados en la tabla.
     */
    public static List<Usuario> findAll() {
        Usuario usuario = null;
        List<Usuario> usuarios = new ArrayList<>();
        Connection con;
        try {
            con = ConnectionDB.getInstance();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_ALL);

            while (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                String email = rs.getString("email");
                String password = rs.getString("password");
                usuario = new Usuario(idUsuario, email, password);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuarios;
    }

    /**
     * Método que devuelve un objeto Usuario por su ID.
     */
    public static Usuario findById(int idUsuario) {
        Usuario usuario = null;
        try (PreparedStatement ps = ConnectionDB.getInstance().prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idObtenido = rs.getInt("id_usuario");
                String email = rs.getString("email");
                String password = rs.getString("password");
                usuario = new Usuario(idObtenido, email, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }

    /**
     * Equivalente al findByName del profesor, pero usando el Email (nuestra clave natural).
     */
    public static Usuario findByEmail(String email) {
        Usuario usuario = null;
        try (PreparedStatement ps = ConnectionDB.getInstance().prepareStatement(SQL_FIND_BY_EMAIL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                String emailDB = rs.getString("email");
                String password = rs.getString("password");
                usuario = new Usuario(idUsuario, emailDB, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }

    /**
     * Añade un nuevo usuario verificando antes que no exista ese email.
     */
    public static Usuario addUsuario(Usuario usuario) {
        if ((usuario != null) && findByEmail(usuario.getEmail()) == null) {
            try (PreparedStatement ps = ConnectionDB.getInstance().prepareStatement(SQL_INSERT)) {
                ps.setString(1, usuario.getEmail());
                ps.setString(2, usuario.getPassword());
                ps.executeUpdate();
                // Una vez insertado, lo buscamos para devolverlo con el ID autogenerado
                usuario = findByEmail(usuario.getEmail());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            usuario = null;
        }
        return usuario;
    }

    /**
     * Actualiza un usuario comprobando que el nuevo email no esté pisando a otro existente.
     */
    public static boolean updateUsuario(Usuario usuarioNuevo, Usuario usuarioActual) {
        boolean updated = false;
        if ((usuarioActual != null) && (usuarioNuevo != null) &&
                findByEmail(usuarioActual.getEmail()) != null &&
                findByEmail(usuarioNuevo.getEmail()) == null) {

            try (PreparedStatement ps = ConnectionDB.getInstance().prepareStatement(SQL_UPDATE)) {
                ps.setString(1, usuarioNuevo.getEmail());
                ps.setString(2, usuarioNuevo.getPassword());
                ps.setInt(3, usuarioActual.getIdUsuario());
                ps.executeUpdate();
                updated = true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return updated;
    }

    /**
     * Borra un usuario por su ID comprobando antes que exista.
     */
    public static boolean deleteUsuarioById(int idUsuario) {
        boolean deleted = false;
        if (findById(idUsuario) != null) {
            try (PreparedStatement ps = ConnectionDB.getInstance().prepareStatement(SQL_DELETE)) {
                ps.setInt(1, idUsuario);
                ps.executeUpdate();
                deleted = true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return deleted;
    }
}
