package org.libretaahorros.libretaahorros.DAO;

/**
 * Interfaz genérica que define el contrato de persistencia fundamental para el sistema DAO.
 * Establece las operaciones estructurales del ciclo de vida CRUD (Create, Update, Delete)
 * utilizando parametrización de tipos (Generics). Esto garantiza la homogeneidad arquitectónica
 * en las clases de acceso a datos, forzando un diseño desacoplado, reutilizable y seguro en
 * tiempo de compilación.
 * @param <T> El tipo de entidad de modelo sobre el cual se ejecutarán las operaciones de persistencia.
 */
public interface GenericDAO<T> {
    boolean add(T objeto);
    boolean update(T objeto);
    boolean delete(int id);

}
