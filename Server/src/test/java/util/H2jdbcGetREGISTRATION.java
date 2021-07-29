/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import DAO.RegistrationDAO;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement; 
import model.Registration;
/**
 *
 * @author RaelH
 */
public class H2jdbcGetREGISTRATION {
    
  public static void main(String[] args) { 
      //Instanciando a classe DAO do RegistrationDAO
       RegistrationDAO rdao = new RegistrationDAO();
  
        for (Registration r : rdao.mostrar_Registration()) {
            System.out.println("ID: " + r.getId());
            System.out.println("Mensagem: " + r.getMensagem());
            System.out.println("Data: " + r.getData());
        }
  }
}
