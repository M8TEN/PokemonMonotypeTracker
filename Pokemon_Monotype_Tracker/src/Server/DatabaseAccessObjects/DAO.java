package Server.DatabaseAccessObjects;

/* CRUD
    CREATE,
    READ,
    UPDATE,
    DELETE
 */

import java.sql.SQLException;

public interface DAO<T, ID> {
    public void createEntry(T from, ID id) throws SQLException;
    public T get(ID id) throws SQLException;
    public void update(T newData, ID id) throws SQLException;
    public void delete(ID id) throws SQLException;
}
