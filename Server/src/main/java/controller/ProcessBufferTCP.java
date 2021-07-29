/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.ProcessFrame.geraBufferDataHora;
import static controller.ProcessFrame.geraBufferString;
import static controller.ProcessFrame.geraCRCDataHora;
import static controller.ProcessFrame.geraCRCString;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import crc.ControleCRC;
import java.util.Arrays;
import model.Data;
import model.Registration;
import util.LogGenerator;

/**
 *
 * @author RaelH
 */
public class ProcessBufferTCP {
    
    public ProcessBufferTCP(){
    }
    
   
    public void  ProcessBufferTCP(InputStream inputStream, OutputStream  outputStream, LogGenerator log ) throws IOException{
           
               //----------------- INIT -----------------------//
               ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
               byte[] bytes = new byte[buffer.limit()];
               inputStream.read(bytes);
               buffer.put(bytes).position(0);
               int rpcCodeInit = buffer.getInt();
                
               String hexaRPC = Integer.toHexString(rpcCodeInit).toUpperCase();
               System.out.println(hexaRPC);
               log.inserInfo("Init received : " + hexaRPC);
               
               if(rpcCodeInit == 10){ ///0x0A              

                    //----------------- BYTES -----------------------//
                    ByteBuffer buffer2 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                    byte[] bytes2 = new byte[buffer2.limit()];
                    inputStream.read(bytes2);
                
                    buffer2.put(bytes2).position(0);
                    int tamanhoBytes = buffer2.getInt();
                
                    String hexatamanhoBytes = Integer.toHexString(tamanhoBytes).toUpperCase();
                    System.out.println(hexatamanhoBytes);
                    log.inserInfo("Byte received : " + hexatamanhoBytes);
                
                    //----------------- FRAME -----------------------//
                    ByteBuffer buffer3 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                    byte[] bytes3 = new byte[buffer2.limit()];
                    inputStream.read(bytes3);
                
                    buffer3.put(bytes3).position(0);
                    int frame = buffer3.getInt();
                
                    String hexaFrame = Integer.toHexString(frame).toUpperCase();
                    System.out.println(hexaFrame);
                    log.inserInfo("Frame received : " + hexaFrame);
                
                    //----------------- DATA -----------------------//
                    ByteBuffer buffer4 = ByteBuffer.allocate(tamanhoBytes).order(ByteOrder.LITTLE_ENDIAN);
                    byte[] data = new byte[buffer4.limit()];
                    inputStream.read(data);
                    
                    System.out.println(data);
                    String req = Arrays.toString(data);
                    log.inserInfo("Data received : " + req);
                    //----------------- CRC -----------------------//
                    ByteBuffer buffer5 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                    byte[] bytes5 = new byte[buffer5.limit()];
                    inputStream.read(bytes5);
                
                    buffer5.put(bytes5).position(0);
                    int CRC = buffer5.getInt();
                
                    String hexaCRC = Integer.toHexString(CRC).toUpperCase();
                    System.out.println(hexaCRC);    
                    log.inserInfo("CRC received : " + hexaCRC);
                    
                    //----------------- END -----------------------//
                    ByteBuffer buffer6 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                    byte[] bytes6 = new byte[buffer6.limit()];
                    inputStream.read(bytes6);
                
                    buffer6.put(bytes6).position(0);
                    int end = buffer6.getInt();
                
                    String hexaEnd = Integer.toHexString(end).toUpperCase();
                    System.out.println(hexaEnd); 
                    log.inserInfo("END received : " + hexaEnd);
                    
                    if(validaCRC(data, tamanhoBytes, frame, CRC)){
                        if(validaEND(end)){
                            ProcessFrame pFrame = new ProcessFrame();
                            pFrame.process(frame, data, outputStream, log);
                        }else{
                            log.inserError("Error ao validar FIM");
                        }
                    }else{
                        log.inserError("Error ao validar CRC");
                    }
             
             }else{
                   byte[] bytesDescarte = new byte[inputStream.available()];
                   System.out.println("limpando o inputstream, total bytes descartados ");
                   log.inserWarn("Pacote errado, limpando o inputstream, total bytes descartados ");
               }
    }
    
   public Boolean validaCRC(byte[] data, int tamanhoBytes, int frame, int CRC){
       ControleCRC crc8 = new ControleCRC();
       ByteBuffer buffer;
       byte[] value = new byte[tamanhoBytes+8];
       value[0] = (byte)tamanhoBytes;
       value[4] = (byte)frame;
       
       System.out.println(crc8.calculaCRC8(new byte[]{9, 1, 31, 32,  33,   44}));
       String aux="";
       if( (data.length ) > tamanhoBytes){
           return false;
       }else{
           String mensagem = new String(data, StandardCharsets.UTF_8);
           
           for(int i=0; i< data.length; i++){
                value[8+ i] = data[i];
           }
           
     
       }
       
       int result = crc8.calculaCRC8(value);
       if(CRC == result){
           return true;
       }else{
           return false;
       }
       
   }
   
   public Boolean validaEND(int end){
      if(end == 13){
           return true;
       }else{
           return false;
       }
   }
     
   
  public static void sendAck(OutputStream outputStream, LogGenerator log) {
        byte[] data = null;  
        try {
            String msg = "Mensagem foi recebida e armazenada no banco ";
            log.inserInfo(msg);
            geraCRCString(msg, 0x0a);
           
            int crc = geraCRCString(msg, 0x0a);
            data    = geraBufferString(msg,  0x0a, crc);
            
            // sending data to server
            outputStream.write(data);
            outputStream.flush();
            
            String req = Arrays.toString(data);
            log.inserInfo("Sent to client : " + req);

        } catch (IOException i) {
            System.out.println(i);
        }
    }
    
    public static void sendAck(OutputStream outputStream, Registration r, LogGenerator log) {
        byte[] data = null;  
        try {
            String msg = "Mensagem foi recebida e armazenada no banco - " + r.getData();
            log.inserInfo(msg);
            geraCRCString(msg, 0x0a);
           
            int crc = geraCRCString(msg, 0x0a);
            data    = geraBufferString(msg,  0x0a, crc);
            
            // sending data to server
            outputStream.write(data);
            outputStream.flush();
            
            String req = Arrays.toString(data);
            log.inserInfo("Sent to client : " + req);
            
        } catch (IOException i) {
            System.out.println(i);
        }
    }
    
    public static void sendDataHora(OutputStream outputStream, Data d, LogGenerator log) {
        byte[] data = null;  
        try {
            log.inserInfo("Retornando data e hora atual! ");
            int crc = geraCRCDataHora(d, 0xa3);
            data    = geraBufferDataHora(d,  0xa3, crc);
            
            // sending data to server
            outputStream.write(data);
            outputStream.flush();
            
            String req = Arrays.toString(data);
            log.inserInfo("Sent to client : " + req);

        } catch (IOException i) {
            System.out.println(i);
        }
    }
}
