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
    private final static String SQL_FIND_BY_EMAIL = "SELECT * FROM USUARIO WHERE email = ?";
    private final static String SQL_VALIDATE_LOGIN = "SELECT * FROM USUARIO WHERE email = ? AND password = ?";
    private final static String SQL_INSERT = "INSERT INTO USUARIO (email, password) VALUES (?, ?)";
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
}
