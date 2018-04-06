package com.github.youyinnn.youwebutils.third;

import com.github.youyinnn.youwebutils.fourth.NestedRuntimeException;
import com.github.youyinnn.youwebutils.fourth.Resources;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.*;

/**
 * 这个类用于执行SQL脚本,搬运自:
 *  gid:com.ibatis
 *  aid:ibatis2-common
 *  ver:2.1.7.597
 * @author youyinnn
 */
public class ScriptRunner {
    private Connection connection;
    private String driver;
    private String url;
    private String username;
    private String password;
    private boolean stopOnError;
    private boolean autoCommit;
    private PrintWriter logWriter;
    private PrintWriter errorLogWriter;

    public ScriptRunner(Connection connection, boolean autoCommit, boolean stopOnError) {
        this.logWriter = new PrintWriter(System.out);
        this.errorLogWriter = new PrintWriter(System.err);
        this.connection = connection;
        this.autoCommit = autoCommit;
        this.stopOnError = stopOnError;
    }

    public ScriptRunner(String driver, String url, String username, String password, boolean autoCommit, boolean stopOnError) {
        this.logWriter = new PrintWriter(System.out);
        this.errorLogWriter = new PrintWriter(System.err);
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.autoCommit = autoCommit;
        this.stopOnError = stopOnError;
    }

    public void setLogWriter(PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    public void setErrorLogWriter(PrintWriter errorLogWriter) {
        this.errorLogWriter = errorLogWriter;
    }

    public void runScript(Reader reader) throws IOException, SQLException {
        try {
            if (this.connection == null) {
                DriverManager.registerDriver((Driver) Resources.classForName(this.driver).newInstance());
                Connection conn = DriverManager.getConnection(this.url, this.username, this.password);

                try {
                    if (conn.getAutoCommit() != this.autoCommit) {
                        conn.setAutoCommit(this.autoCommit);
                    }

                    this.runScript(conn, reader);
                } finally {
                    conn.close();
                }
            } else {
                boolean originalAutoCommit = this.connection.getAutoCommit();

                try {
                    if (originalAutoCommit != this.autoCommit) {
                        this.connection.setAutoCommit(this.autoCommit);
                    }

                    this.runScript(this.connection, reader);
                } finally {
                    this.connection.setAutoCommit(originalAutoCommit);
                }
            }

        } catch (IOException | SQLException var17) {
            throw var17;
        } catch (Exception var19) {
            throw new NestedRuntimeException("Error running script.  Cause: " + var19, var19);
        }
    }

    private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
        StringBuffer command = null;

        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;

            while(true) {
                while((line = lineReader.readLine()) != null) {
                    if (command == null) {
                        command = new StringBuffer();
                    }

                    String trimmedLine = line.trim();
                    if (trimmedLine.startsWith("--")) {
                        this.println(trimmedLine);
                    } else if (trimmedLine.length() >= 1 && !trimmedLine.startsWith("//") && trimmedLine.length() >= 1 && !trimmedLine.startsWith("--")) {
                        if (trimmedLine.endsWith(";")) {
                            command.append(line.substring(0, line.lastIndexOf(";")));
                            command.append(" ");
                            Statement statement = conn.createStatement();
                            this.println(command);
                            boolean hasResults = false;
                            if (this.stopOnError) {
                                hasResults = statement.execute(command.toString());
                            } else {
                                try {
                                    statement.execute(command.toString());
                                } catch (SQLException var21) {
                                    var21.fillInStackTrace();
                                    this.printlnError("Error executing: " + command);
                                    this.printlnError(var21);
                                }
                            }

                            if (this.autoCommit && !conn.getAutoCommit()) {
                                conn.commit();
                            }

                            ResultSet rs = statement.getResultSet();
                            if (hasResults && rs != null) {
                                ResultSetMetaData md = rs.getMetaData();
                                int cols = md.getColumnCount();

                                int i;
                                String value;
                                for(i = 0; i < cols; ++i) {
                                    value = md.getColumnName(i);
                                    this.print(value + "\t");
                                }

                                this.println("");

                                while(rs.next()) {
                                    for(i = 0; i < cols; ++i) {
                                        value = rs.getString(i);
                                        this.print(value + "\t");
                                    }

                                    this.println("");
                                }
                            }

                            command = null;

                            try {
                                statement.close();
                            } catch (Exception var20) {
                                ;
                            }

                            Thread.yield();
                        } else {
                            command.append(line);
                            command.append(" ");
                        }
                    }
                }

                if (!this.autoCommit) {
                    conn.commit();
                }
                break;
            }
        } catch (SQLException var22) {
            var22.fillInStackTrace();
            this.printlnError("Error executing: " + command);
            this.printlnError(var22);
            throw var22;
        } catch (IOException var23) {
            var23.fillInStackTrace();
            this.printlnError("Error executing: " + command);
            this.printlnError(var23);
            throw var23;
        } finally {
            conn.rollback();
            this.flush();
        }

    }

    private void print(Object o) {
        if (this.logWriter != null) {
            System.out.print(o);
        }

    }

    private void println(Object o) {
        if (this.logWriter != null) {
            this.logWriter.println(o);
        }

    }

    private void printlnError(Object o) {
        if (this.errorLogWriter != null) {
            this.errorLogWriter.println(o);
        }

    }

    private void flush() {
        if (this.logWriter != null) {
            this.logWriter.flush();
        }

        if (this.errorLogWriter != null) {
            this.errorLogWriter.flush();
        }

    }
}