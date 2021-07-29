package util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import DAO.RegistrationDAO;
import DAO.UserDAO;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement;  
import model.Registration;
import model.User;
/**
 *
 * @author RaelH
 */
public class H2jdbcInsertREGISTRATION { 
  public static void main(String[] args) { 
      //Instanciando a classe DAO do RegistrationDAO
       RegistrationDAO rdao = new RegistrationDAO();
       Registration    r    = new Registration();
       
       r.setMensagem("Ok");
       r.setData("27/07/2021");
 
       if(rdao.add_registor(r)){
           System.out.println("registro cadastrado com sucesso");
       }else{
            System.out.println("Erro ao cadastrar novo registro");
       }

  }
}