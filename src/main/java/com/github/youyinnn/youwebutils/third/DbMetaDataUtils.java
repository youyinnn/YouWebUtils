package com.github.youyinnn.youwebutils.third;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author youyinnn
 */
public class DbMetaDataUtils {

    public static boolean isTableExist(Connection dataSourceConn, String tableName) throws SQLException {
        ResultSet tables = dataSourceConn.getMetaData().getTables(null, null, tableName, null);
        return !tables.isClosed();
    }

}
