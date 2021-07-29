/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.client;

/**
 *https://www.devzoneoriginal.com/2020/07/java-socket-example-for-sending-and.html
 * @author RaelH
 */
// A Java program for a SendPackage 
import crc.ControleCRC;
import java.net.*;
import java.util.Arrays;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import model.User;
import util.LogGenerator;

public class SendPackage {
 
// initialize socket and input output streams
 private Socket socket    = null;
 private OutputStream out = null;
 private InputStream in   = null;

   
  public static int geraCRCString(String valor, int frame) {
         /*
            buffer total - CRC
            data={
                byte    = 4
                frame   = 4=> 8 (INT)
                                +
                valor      
            }
        */
        byte[] nome = valor.getBytes();
        byte[] data = new byte[valor.length() + 8];
        
        data[0] =  (byte)(valor.length());
        data[4] =  (byte)frame;
        for (int i = 0; i < valor.length(); i++){
          data[i+8] = nome[i]; 
        }

        ControleCRC crc8 = new ControleCRC();
        int crc = crc8.calculaCRC8(data);
        System.out.println(crc8.retornaCRC(data));
    return crc;
  }
    
     
  public static int geraCRCA2(User u) {
        byte[] nome = u.getNome().getBytes();
        /*
            buffer total - CRC
            data={
                byte    = 4
                frame   = 4=> 8 (INT)
        
                idade   = 4
                peso    = 4
                altura  = 4
                tamanho = 4   => 16 (INT)
                                 8 + 16 = 24 (INT)
                                +
                nome      
            }
        */
        byte[] data = new byte[u.getNome().length() + 24]; 
        
                /*
                Tamanho Data
                data {
                    idade   = 4
                    peso    = 4
                    altura  = 4
                    tamanho = 4   = 16 (INT)
                              +  
                    nome      
                }
        */
        
        data[0] =  (byte)(u.getNome().length() + 16);
        data[4] =  (byte)0xa2;
        
        data[8]  =  (byte)u.getIdade();
        data[12] =  (byte)u.getPeso();
        data[16] =  (byte)u.getAltura();
        data[20] =  (byte)u.getTamanhoNome();
        
        for (int i = 0; i < u.getNome().length(); i++){
          data[i+24] = nome[i]; 
        }

        ControleCRC crc8 = new ControleCRC();
        int crc = crc8.calculaCRC8(data);
        System.out.println(crc8.retornaCRC(data));
        
    return crc;
  }
    
  public static byte[] geraBufferString(String valor, int frame, int crc) {
        byte[] nome = valor.getBytes();
        
        /*
            buffer total 
                init    = 4
                byte    = 4
                frame   = 4  =>  12
                          +
                valor      
                
                CRC     = 4
                END     = 4 =>    8
                                  12 + 8 = 20 (INT)
        */
        byte[] data2 = new byte[valor.length() + 20];
        data2[0] =  0x0a;
        data2[4] =  (byte)(valor.length());
        data2[8] =  (byte)frame;
        for (int i = 0; i < valor.length(); i++){
          data2[i+12] = nome[i]; 
        }
        data2[valor.length() + 12] =  (byte)crc;
        data2[valor.length() + 16] =  (byte)0x0d;
      return data2;
  }
  
     public static byte[] geraBufferA2(User u, int crc) throws IOException {
        byte[] nome = u.getNome().getBytes();
        
        /*
            buffer total 
                init    = 4
                byte    = 4
                frame   = 4  =>  12
        
                idade   = 4
                peso    = 4
                altura  = 4
                tamanho = 4   => 16
                                +
                nome      
                
                CRC     = 4
                END     = 4 => 8
        
        */
        byte[] data2 = new byte[u.getNome().length() + 36];
        data2[0] =  0x0a;
        data2[4] =  (byte)(u.getNome().length() + 16);
        data2[8] =  (byte)0xa2;
        
        data2[12] =  (byte)u.getIdade();
        data2[16] =  (byte)u.getPeso();
        data2[20] =  (byte)u.getAltura();
        data2[24] =  (byte)u.getTamanhoNome();
        
        for (int i = 0; i < u.getNome().length(); i++){
          data2[i+28] = nome[i]; 
        }
        data2[u.getNome().length() + 28] =  (byte)crc;
        data2[u.getNome().length() + 32] =  (byte)0x0d;
        
      return data2;
  }
  
   
  
 // constructor to put ip address and port
 public SendPackage(String address, int port, int tipo, LogGenerator log ) {
    // establish a connection
    try {
        socket = new Socket(address, port);
        if (socket.isConnected()) {
            System.out.println("Connected");
            log.inserInfo("Connected");
        }

        // sends output to the socket
        out = new DataOutputStream(socket.getOutputStream());
   
        //takes input from socket
        in = new DataInputStream(socket.getInputStream());
    } catch (UnknownHostException u) {
        System.out.println(u);
        log.inserError(u.toString());
    } catch (IOException i) {
        System.out.println(i);
        log.inserError(i.toString());
    }

    try {
        byte[] data = null;  
        if(tipo == 1){
            log.inserInfo("Enviando frame: A1");
            String valor = "Rafael Hoffmann";
            int crc = geraCRCString(valor, 0xa1);
            data    = geraBufferString(valor,  0xa1, crc);
        }else
        if(tipo == 2){
            log.inserInfo("Enviando frame: A2");
            String nome = "Rafael Hoffmann";
            User u = new User(30, 80, 170, nome.length(), nome );
            int crc = geraCRCA2(u);
            data =geraBufferA2(u, crc);
        }else
        if(tipo == 3){
            log.inserInfo("Enviando frame: A3");
            String valor = "America/Sao_Paulo";
            int crc = geraCRCString(valor, 0xa3);
            data    = geraBufferString(valor,  0xa3, crc);
        }        
     
    // sending data to server
    out.write(data);

    String req = Arrays.toString(data);
    //printing request to console
    System.out.println("Sent to server : " + req);
    log.inserInfo("Sent to server : " + req);
   
    // Receiving reply from server
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte buffer[] = new byte[1024];
    baos.write(buffer, 0 , in.read(buffer));
   
    byte result[] = baos.toByteArray();

    String res = Arrays.toString(result);

    // printing reply to console
    System.out.println("Recieved from server : " + res);
    log.inserInfo("Recieved from server : " + req);
  } catch (IOException i) {
        System.out.println(i);
        log.inserWarn(i.toString());
  }

  // close the connection
  try {
        in.close();
        out.close();
        socket.close();
  } catch (IOException i) {
        System.out.println(i);
        log.inserWarn(i.toString());
  }
 }
   
 public static Scanner getScanner() {
        return new Scanner(System.in);
 }
 
  public static void menu() {
        
        System.out.println("1) Buffer A1");
        System.out.println("2) Buffer A2");
        System.out.println("3) Buffer A3");
        System.out.println("4) Sair");
 }

 public static void main(String args[]) {
        int op   = -1,
            tipo = -1;
        LogGenerator log = new LogGenerator();
    
        String addrees = "127.0.0.1";
        int    port    = 4447;
                
        log.inserInfo("Escolha a opção:\n"
                + "1) Buffer A1\n"
                + "2) Buffer A2\n"
                + "3) Buffer A3\n"
                + "4) Sair\n");
        
        System.out.println("1) Buffer A1");
        System.out.println("2) Buffer A2");
        System.out.println("3) Buffer A3");
        System.out.println("4) Sair");
        
        System.out.print("Opção: ");
        op   = getScanner().nextInt();
        tipo = -1;
        switch (op) {

            case 1:
                tipo = 1;
                break;
            case 2:
                tipo = 2;
                break;
            case 3:
                tipo = 3;
                break;
            case 4:
                System.exit(0);
            default:
                System.out.println("Digite uma opção valida!");
                log.inserWarn("Digite uma opção valida!");
                menu();    
        }
        
        new SendPackage(addrees, port, tipo, log);
        System.out.println("Process closed");
        log.inserWarn("Process closed");
 }
}

