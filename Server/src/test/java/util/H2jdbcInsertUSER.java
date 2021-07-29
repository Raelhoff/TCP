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
public class H2jdbcInsertUSER { 
  public static void main(String[] args) { 
      //Instanciando a classe DAO do RegistrationDAO
       UserDAO udao = new UserDAO();
       User    u    = new User();
       
       u.setIdade(30);
       u.setPeso(80);
       u.setAltura(170);
       u.setTamanhoNome(6);
       u.setNome("Rafael");
       
       if(udao.add_user(u)){
           System.out.println("Usuario cadastrado com sucesso");
       }else{
            System.out.println("Erro ao cadastrar usuario");
       }
  }
}