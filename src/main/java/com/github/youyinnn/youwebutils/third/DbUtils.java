package com.github.youyinnn.youwebutils.third;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author youyinnn
 */
public class DbUtils {

    public static boolean isTableExist(Connection conn, String tableName) throws SQLException {
        ArrayList<String> tablesFromDB = getTablesFromDB(conn);
        return tablesFromDB.contains(tableName.toLowerCase());
    }

    public static ArrayList<String> getTablesFromDB(Connection conn) throws SQLException {
        ArrayList<String> tables = new ArrayList<>();
        String dataBaseName = getDbName(conn);
        String sql = "SHOW TABLES FROM " + dataBaseName;
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            tables.add(resultSet.getString("tables_in_" + dataBaseName).toLowerCase());
        }
        resultSet.close();
        return tables;
    }

    public static String getDbName(Connection conn) throws SQLException {
        String rootUrl = conn.getMetaData().getURL().split("\\?")[0];
        String[] strings = rootUrl.split("/");
        String dataBaseName = strings[strings.length - 1];
        return dataBaseName.toLowerCase();
    }

    public static void runSqlScript(Connection connection, String scriptFilePath) throws IOException, SQLException {
        ScriptRunner scriptRunner = new ScriptRunner(connection, true, true);
        scriptRunner.setLogWriter(null);
        InputStreamReader reader = new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(scriptFilePath));
        scriptRunner.runScript(reader);
        reader.close();
    }

    public static ArrayList<String> getColumnsFromTable(Connection conn, String tableName) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        String sql = "SHOW COLUMNS FROM " + tableName;
        ResultSet resultSet = conn.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            columns.add(resultSet.getString("Field"));
        }
        resultSet.close();
        return columns;
    }

    public static String turnToAlibabaDataBaseNamingRules(String name) {
        for (int i = 1 ; i < name.length() ; ++i) {
            char c = name.charAt(i);
            if (c >= 65 && c <=90) {
                name = name.replaceAll(c+"", "_" + (char)(c+32));
            }
        }
        return name.toLowerCase();
    }
}
