package org.itp.interfaces;

import java.util.Properties;

public interface IDatabaseConnection {

    IDatabaseConnection openConnection(Properties properties);

    void createAllTables();

    void truncateAllTables();

    void removeAllTables();

    void closeConnection();
}
