/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author me-aydin
 */
public class JdbcReadOnly {

    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;

    //String query = null;
    public JdbcReadOnly(String query) {
        //this.query = query;
    }

    public JdbcReadOnly() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void connect(Connection con) {
        connection = con;
    }

    //START_RETRIEVE
    /*
    * Sets the ReturnSet to the rows in the DB that meet criteria
    * specified in the param.
     */
    private void select(String tableName) {
        String query = "SELECT * FROM " + tableName;

        this.executeSelect(query);
    }

    /*
    * Sets the ReturnSet to the rows in the DB that meet criteria
    * specified in the param.
     */
    private void select(String tableName, String colName, String value,
            boolean usePar) {
        String query;
        
        if (usePar == true){
            // Parenthesis will surrond the value making it a string
            query = "SELECT * FROM " + tableName + " WHERE " + colName + " = \'" + value + "\'";
        }
        else{
            // Parenthesis will not surround the value
            query = "SELECT * FROM " + tableName + " WHERE " + colName + " = " + value;
        }
        
        this.executeSelect(query);
    }

    /*
    * Executes the select query.
    */
    private void executeSelect(String query){
         try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }       
    }
    
    /*
    * Returns a list of hashmaps of rows from the DB that meet criteria 
    * specified in the params.
     */
    public ArrayList<HashMap<String, String>> retrieve(String tableName) {

        //GET Qualifiing Rows from DB
        select(tableName);

        return this.processRetrieve();
    }

    /*
    * Returns a list of hashmaps of rows from the DB that meet criteria 
    * specified in the params.
     */
    public ArrayList<HashMap<String, String>> retrieve(String tableName, long id) {

        //GET Qualifiing Rows from DB
        select(tableName, "Id", String.valueOf(id), false);

        return this.processRetrieve();
    }

    /*
    * Returns a list of hashmaps of rows from the DB that meet criteria 
    * specified in the params. The field value must be a long.
    */
    public ArrayList<HashMap<String, String>> retrieve(String tableName, String colName, long value) {
        //GET Qualifiing Rows from DB
        select(tableName, colName, String.valueOf(value), false);
        
        return this.processRetrieve();
    }

    /*
    * Returns a list of hashmaps of rows from the DB that meet criteria 
    * specified in the params. The field value must be a string.
    */
    public ArrayList<HashMap<String, String>> retrieve(String tableName, String colName, String value) {
        //GET Qualifiing Rows from DB
        select(tableName, colName, value, true);

        return this.processRetrieve();
    }

    /*
    * Processes the response of a retrieve request. Results are returned as
    * a list of hashmaps.
    */
    private ArrayList<HashMap<String, String>> processRetrieve() {
        //Transform and Return Rows to a List of HashMaps
        try {
            if (rs == null) {
                System.out.println("rs is null");
            } else {
                return rsToMaps();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }
    //END_RETRIEVE

    public void closeAll() {
        try {
            rs.close();
            statement.close();
            //connection.close();                                         
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //START_MAPPING
    private ArrayList rsToList() throws SQLException {
        ArrayList aList = new ArrayList();

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] s = new String[cols];
            for (int i = 1; i <= cols; i++) {
                s[i - 1] = rs.getString(i);
            }
            aList.add(s);
        }
        return aList;
    }

    private ArrayList<HashMap<String, String>> rsToMaps() throws SQLException {
        ArrayList<HashMap<String, String>> ret = new ArrayList<>();
        HashMap<String, String> rowMap;

        int nCols = rs.getMetaData().getColumnCount();
        String[] colNames = new String[nCols];
        for (int c = 0; c < nCols; c++) {
            colNames[c] = rs.getMetaData().getColumnName(c + 1);
        }

        while (rs.next()) {
            rowMap = new HashMap<>();
            for (int c = 0; c < nCols; c++) {
                rowMap.put(colNames[c], rs.getString(c + 1));
            }
            ret.add(rowMap);
        }
        return ret;
    }
    //END_MAPPING
}
