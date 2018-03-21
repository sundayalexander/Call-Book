/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package contactsaver;

//Import required Classes


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.String.valueOf;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



/**
 *
 * @author Sunnyblaze
 */
public class DatabaseConnector {
    //properties/variables declaration
    private Connection conn;
    private Statement statement;
    private String query;
    private static String URL;
    private static Properties LOGIN_DETAIL; 
    private PreparedStatement preparedStatement;
    private ResultSet result;
    private AppSettings property;
    private final JFrame frame;
    
    /*>>>>>>>>>>>>>>>>>>>>>>>>>>                      <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    >>>>>>>>>>>>>>>>>>>>>>>>>>>> Method Constructions <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    >>>>>>>>>>>>>>>>>>>>>>>>>>>>                      <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*/
    
    //Constructor
    public DatabaseConnector(JFrame frame){
        this.frame = frame;
        String property = null;
        try {
            this.property = new AppSettings();
            property = this.property.getProperty("db_url");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.frame, ex.getMessage());
        }
        
        //Initialize Constants
        DatabaseConnector.URL = (property == null || property.equals("\"\"") || 
                                 property.equals(""))?"jdbc:derby://localhost:"
                                 + "1527/contactSaver":property;
        DatabaseConnector.LOGIN_DETAIL = new Properties();
        DatabaseConnector.LOGIN_DETAIL.put("user", "root");
        DatabaseConnector.LOGIN_DETAIL.put("password", "ContactSaver");
       
    }
    
    //Establish Database Connection
    private void connect() throws SQLException{
        String username = this.property.getProperty("db_username");
        String password = this.property.getProperty("db_password");
        if(username.equals("\"\"") || password.equals("\"\"") || username.equals("")){
            this.conn = DriverManager.getConnection(DatabaseConnector.URL, DatabaseConnector.LOGIN_DETAIL);
        }else{
            this.conn = DriverManager.getConnection(DatabaseConnector.URL, username, password);
        }
        
    }
    
    //Create Databse table
    public void createTable(){
        boolean status = false;
        this.query = "CREATE TABLE contactSaver ("
                + "id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
                + "surname VARCHAR(20) NOT NULL,"
                + "otherName VARCHAR(100) NOT NULL,"
                + "occupation VARCHAR(50) NOT NULL,"
                + "officeAddress VARCHAR(50) NOT NULL,"
                + "homeAddress VARCHAR(50) NOT NULL,"
                + "email VARCHAR(50) NOT NULL,"
                + "homePhone VARCHAR(50) NOT NULL,"
                + "officePhone VARCHAR(50) NOT NULL )";
        try {
            this.connect();
            this.statement = this.conn.createStatement();
            status = this.statement.execute(query);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.frame, ex.getMessage());
        }finally{
            try {
                this.statement.close();
                this.conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this.frame, ex.getMessage());
            }
        }
    }
    
    //Insert Data into table
    public boolean insert(String value1,String value2,String value3,String value4,String value5,
            String value6,String value7,String value8, InputStream passport){
        boolean status = false;
        try {
            this.connect();
            this.query = "INSERT INTO contactSaver(surname,otherName,occupation,officeAddress,homeAddress,"
                    + "email,homePhone,officePhone,passport) VALUES(?,?,?,?,?,?,?,?,?)";
            this.preparedStatement = conn.prepareStatement(this.query);
            this.preparedStatement.setString(1, value1);
            this.preparedStatement.setString(2, value2);
            this.preparedStatement.setString(3, value3);
            this.preparedStatement.setString(4, value4);
            this.preparedStatement.setString(5, value5);
            this.preparedStatement.setString(6, value6);
            this.preparedStatement.setString(7, value7);
            this.preparedStatement.setString(8, value8);
            this.preparedStatement.setBlob(9, passport);
            this.preparedStatement.executeUpdate();
            status = true;
        } catch (SQLException ex) {
            status = false;
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }finally{
            try{
                this.preparedStatement.close();
               this.conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        return status;
    }
    
    //Insert file Data into table
    public boolean insert(String surname,String otherName,String phone){
        boolean status = false;
        InputStream passport = getClass().getResourceAsStream("..\\images\\avatar.png");
        try {
            this.connect();
            this.query = "INSERT INTO contactSaver(surname,otherName,occupation,officeAddress,homeAddress,"
                    + "email,homePhone,officePhone,passport) VALUES(?,?,?,?,?,?,?,?,?)";
            this.preparedStatement = conn.prepareStatement(this.query);
            this.preparedStatement.setString(1, surname);
            this.preparedStatement.setString(2, otherName);
            this.preparedStatement.setString(3, "");
            this.preparedStatement.setString(4, "");
            this.preparedStatement.setString(5, "");
            this.preparedStatement.setString(6, "");
            this.preparedStatement.setString(7, phone);
            this.preparedStatement.setString(8, "");
            this.preparedStatement.setBlob(9, passport);
            this.preparedStatement.executeUpdate();
            status = true;
        } catch (SQLException ex) {
            status = false;
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }finally{
            try{
               this.preparedStatement.close();
               passport.close();
               this.conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        return status;
    }
    
    //Get saved data
    public Vector getSavedContact(){
        Vector row = new Vector();
        try {
            this.connect();
            this.statement = this.conn.createStatement();
            this.query = "SELECT * FROM contactSaver";
            this.result = this.statement.executeQuery(this.query);
            int counter = 0;
            while(this.result.next()){
                counter++;
                Vector<String> rowData = new Vector<String>();
                rowData.add(valueOf(counter));
                rowData.add(this.result.getString(1));
                rowData.add(this.result.getString(2));
                rowData.add(this.result.getString(3));
                rowData.add(this.result.getString(4));
                rowData.add(this.result.getString(5));
                rowData.add(this.result.getString(6));
                rowData.add(this.result.getString(7));
                rowData.add(this.result.getString(8));
                row.add(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }finally{
            try {
                this.result.close();
                this.statement.close();
                this.conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        return row;
    }
    
    //Get Database columun name
    public Vector getColumnName(){
        Vector<String> columnName = new Vector<String>();
        try {
            this.connect();
            this.statement = this.conn.createStatement();
            this.statement.setFetchSize(1);
            this.query = "SELECT * FROM contactSaver";
            this.result = this.statement.executeQuery(this.query);
            ResultSetMetaData metaData = this.result.getMetaData();
            columnName.add("#");
            columnName.add(metaData.getColumnName(1));
            columnName.add(metaData.getColumnName(2));
            columnName.add(metaData.getColumnName(3));
            columnName.add(metaData.getColumnName(4));
            columnName.add(metaData.getColumnName(5));
            columnName.add(metaData.getColumnName(6));
            columnName.add(metaData.getColumnName(7));
            columnName.add(metaData.getColumnName(8));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return columnName; 
    }
    
    //Delete Selected Datas
     public boolean deleteContact(String surname,String otherName,String occupation,String officeAddress,
             String homeAddress,String email,String homePhone,String officePhone){
        boolean status = false;
        try {
            this.connect();
            this.query = "DELETE FROM contactSaver WHERE surname = ? AND otherName = ? AND "
                    + "occupation = ? AND officeAddress = ? AND homeAddress = ? AND "
                    + "email = ? AND homePhone = ? AND officePhone = ?";
            this.preparedStatement = conn.prepareStatement(this.query);
            this.preparedStatement.setString(1, surname);
            this.preparedStatement.setString(2, otherName);
            this.preparedStatement.setString(3, occupation);
            this.preparedStatement.setString(4, officeAddress);
            this.preparedStatement.setString(5, homeAddress);
            this.preparedStatement.setString(6, email);
            this.preparedStatement.setString(7, homePhone);
            this.preparedStatement.setString(8, officePhone);
            this.preparedStatement.executeUpdate();
            status = true;
        } catch (SQLException ex) {
            status = false;
        }finally{
            try{
                this.preparedStatement.close();
               this.conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        return status;
    }
    
     //Update Selected Datas
     public boolean update(String columnName,String columnData,ArrayList<String> columnNames, ArrayList<String> columnDatas){
        boolean status = false;
        try {
            this.connect();
            this.query = "UPDATE contactSaver SET "+columnName+" = ? WHERE "+columnNames.get(0)+" = ? AND "
                    + columnNames.get(1)+" = ? AND "+columnNames.get(2)+" = ? AND "+columnNames.get(3)+" = ? "
                    + "AND "+columnNames.get(4)+ " = ? AND "+columnNames.get(5)+" = ?";
            this.preparedStatement = conn.prepareStatement(this.query);
            this.preparedStatement.setString(1, columnData);
            this.preparedStatement.setString(2, columnDatas.get(0));
            this.preparedStatement.setString(3, columnDatas.get(1));
            this.preparedStatement.setString(4, columnDatas.get(2));
            this.preparedStatement.setString(5, columnDatas.get(3));
            this.preparedStatement.setString(6, columnDatas.get(4));
            this.preparedStatement.setString(7, columnDatas.get(5));
            this.preparedStatement.executeUpdate();
            status = true;
        } catch (SQLException ex) {
            status = false;
        }finally{
            try{
                this.preparedStatement.close();
               this.conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        return status;
    }
    
     //Get user's passport
    public String getPassport(ArrayList<String> field){
        String filePath = "";
        InputStream file = null;
        FileOutputStream passport;
        File newFile = new File("..\\passport.jpg");
        try {
            newFile.createNewFile();
            this.connect();
            this.query = "SELECT passport FROM contactSaver WHERE surname = ? AND otherName = ? AND "
                    + "occupation = ? AND officeAddress = ? AND homeAddress = ? AND "
                    + "email = ? AND homePhone = ? AND officePhone = ?";
            this.preparedStatement = this.conn.prepareStatement(this.query);
            this.preparedStatement.setString(1, field.get(0));
            this.preparedStatement.setString(2, field.get(1));
            this.preparedStatement.setString(3, field.get(2));
            this.preparedStatement.setString(4, field.get(3));
            this.preparedStatement.setString(5, field.get(4));
            this.preparedStatement.setString(6, field.get(5));
            this.preparedStatement.setString(7, field.get(6));
            this.preparedStatement.setString(8, field.get(7));
            this.result = this.preparedStatement.executeQuery();
            while(this.result.next()){
                file = this.result.getBlob(1).getBinaryStream();
                passport = new FileOutputStream(newFile.getAbsolutePath());
                int read;
                while((read = file.read()) != -1){
                    passport.write(read);
                }
                file.close();
                passport.close();
                filePath = newFile.getAbsolutePath();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.frame, ex.getMessage());
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this.frame, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.frame, ex.getMessage());
        }finally{
            try {
                this.preparedStatement.close();
                this.result.close();
                this.conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this.frame, ex.getMessage());
            }
        }
        return filePath;
    }
}
