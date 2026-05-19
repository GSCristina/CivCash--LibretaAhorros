package org.libretaahorros.libretaahorros.DAO;


public interface GenericDAO<T> {
    boolean add(T objeto);
    boolean update(T objeto);
    boolean delete(int id);

}
