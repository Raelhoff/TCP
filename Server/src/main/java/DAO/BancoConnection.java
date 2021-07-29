/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement; 
/**
 *
 * @author RaelH
 */
public class BancoConnection {
     //Classe responsavel de abrir e fechar conexão do banco.
    // JDBC driver name and database URL 
   static final String JDBC_DRIVER = "org.h2.Driver";   
   static final String DB_URL = "jdbc:h2:~/test";  
   
//  Database credentials 
   static final String USER = "sa"; 
   static final String PASS = ""; 

       //Abre conexão com o banco.
    public static Connection getConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Erro: " + ex);
            return null;
        }
    }
    
    
    //Fecha conexão com o banco.
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                System.err.println("Erro: " + ex);
            }
        }
    }
}
