package org.itp.interfaces;

import java.sql.SQLException;
import java.util.Properties;

public interface IDatabaseConnection {

    IDatabaseConnection openConnection(Properties properties) throws SQLException;

    void createAllTables();

    void truncateAllTables();

    void removeAllTables();

    void closeConnection();
}
