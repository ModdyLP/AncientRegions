package de.moddylp.AncientRegions.loader;

import de.moddylp.AncientRegions.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySQLConfigDriver {

    private Connection conn = null;

    public boolean buildConnection() {
        try {
            String host = Main.getInstance().getMainConfig().get("main.db.host", "localhost").toString();
            String port = Main.getInstance().getMainConfig().get("main.db.port", "3306").toString();
            String database = Main.getInstance().getMainConfig().get("main.db.database", "default").toString();
            String user = Main.getInstance().getMainConfig().get("main.db.user", "default").toString();
            String password = Main.getInstance().getMainConfig().get("main.db.password", "default").toString();

            conn =
                    DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?" +
                            "user=" + user + "&password=" + password + "");

            // Do something with the Connection

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    public void execute(String query) {
        if (conn != null || buildConnection()) {
            Statement stmt = null;

            try {
                stmt = conn.createStatement();
                stmt.execute(query);
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            } finally {
                // it is a good idea to release
                // resources in a finally{} block
                // in reverse-order of their creation
                // if they are no-longer needed
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ignored) {
                    } // ignore

                    stmt = null;
                }
            }
        }
    }

    public List<HashMap<String, Object>> getResultFromQuery(String query) {
        List<HashMap<String, Object>> list = new ArrayList<>();
        if (conn != null || buildConnection()) {
            Statement stmt = null;
            ResultSet rs = null;

            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
                ResultSetMetaData md = rs.getMetaData();
                int columns = md.getColumnCount();
                while (rs.next()) {
                    HashMap<String, Object> row = new HashMap<String, Object>(columns);
                    for (int i = 1; i <= columns; ++i) {
                        row.put(md.getColumnName(i), rs.getObject(i));
                    }
                    list.add(row);
                }
                return list;
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            } finally {
                // it is a good idea to release
                // resources in a finally{} block
                // in reverse-order of their creation
                // if they are no-longer needed

                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ignored) {
                    } // ignore

                    rs = null;
                }

                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ignored) {
                    } // ignore

                    stmt = null;
                }
            }
        }
        return list;
    }
}
