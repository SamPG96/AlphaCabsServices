/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Sam
 */
public class DBConnector {
    /*
    Create a connection to a database and return an object that can access it
    */
    public static JdbcReadOnly connect(String dbPath, String dbUser, String dbPass){
        Connection conn;
        JdbcReadOnly jdbc;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(dbPath, dbUser, dbPass);
            jdbc = new JdbcReadOnly();
            jdbc.connect(conn);
            
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
        
    return jdbc;
    }    
}
