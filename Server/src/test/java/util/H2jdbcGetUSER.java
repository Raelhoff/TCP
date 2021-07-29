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
public class H2jdbcGetUSER { 
  public static void main(String[] args) { 
      //Instanciando a classe DAO do RegistrationDAO
       UserDAO udao = new UserDAO();
       
        for (User u : udao.mostrar_User()) {
            System.out.println("ID: " + u.getId());
            System.out.println("Idade: " + u.getIdade());
            System.out.println("Peso: " + u.getPeso());
            System.out.println("Altura: " + u.getAltura());
            System.out.println("Tamanho do nome: " + u.getTamanhoNome());
            System.out.println("Nome: " + u.getNome());
        }
  }
}