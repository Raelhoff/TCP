/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.RegistrationDAO;
import DAO.UserDAO;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.TimeZone;
import model.Data;
import model.Registration;
import model.User;
import crc.ControleCRC;
import util.LogGenerator;

/**
 *
 * @author RaelH
 */
public class ProcessFrame {

    public ProcessFrame() {

    }

    
    public static int geraCRCACKDefault(int frame) {
         /*
            buffer total - CRC
            data={
                byte    = 4
                frame   = 4=> 8 (INT)
      
            }
        */

        byte[] data = new byte[ 8];
        
        data[0] =  (byte)(0x05);
        data[4] =  (byte)frame;

        ControleCRC crc8 = new ControleCRC();
        int crc = crc8.calculaCRC8(data);
        System.out.println(crc8.retornaCRC(data));
        return crc;
     }
    
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
  
       public static byte[] geraBufferACKDefault( int frame, int crc) {
        
        /*
            buffer total 
                init    = 4
                byte    = 4
                frame   = 4  =>  12

                
                CRC     = 4
                END     = 4 =>    8
                                  12 + 8 = 20 (INT)
        */
        byte[] data2 = new byte[20];
        data2[0] =  0x0a;
        data2[4] =  (byte)(5);
        data2[8] =  (byte)frame;
        data2[12] =  (byte)crc;
        data2[16] =  (byte)0x0d;
      return data2;
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
        
      
    public static int geraCRCDataHora(Data data,  int frame) {
        /*
            buffer total - CRC
            data={
                byte    = 4
                frame   = 4=> 8 (INT)
        
                dia     = 4
                mes     = 4
                ano     = 4
                hora    = 4  
                minuto  = 4
                segundo = 4 => 24 (INT)
                                 8 + 16 = 32 (INT)
            }
        */
        byte[] buffer = new byte[32]; 
        
                /*
                Tamanho Data
                data {
                    dia     = 4
                    mes     = 4
                    ano     = 4
                    hora    = 4  
                    minuto  = 4
                    segundo = 4 => 24 (INT) 
                }
        */
        
        buffer[0] =  (byte)(24);
        buffer[4] =  (byte)frame;
        
        buffer[8]  =  (byte)data.getDia();
        buffer[12] =  (byte)data.getMes();
        buffer[16] =  (byte)data.getAno();
        buffer[20] =  (byte)data.getHora();
        buffer[24] =  (byte)data.getMinuto();
        buffer[28] =  (byte)data.getSegundos();

        ControleCRC crc8 = new ControleCRC();
        int crc = crc8.calculaCRC8(buffer);
        System.out.println(crc8.retornaCRC(buffer));
        
    return crc;
  }
    
   public static byte[] geraBufferDataHora(Data d, int frame, int crc) throws IOException {
        
        /*
            buffer total 
                init    = 4
                byte    = 4
                frame   = 4  =>  12
        
                dia     = 4
                mes     = 4
                ano     = 4
                hora    = 4  
                minuto  = 4
                segundo = 4  => 24
          
                CRC     = 4
                END     = 4 => 8
                               12 + 24 + 8 = 44 
        */
        byte[] data2 = new byte[44];
        data2[0] =  0x0a;
        
        /*
                 data {
                    dia     = 4
                    mes     = 4
                    ano     = 4
                    hora    = 4  
                    minuto  = 4
                    segundo = 4 => 24 (INT) 
                }
        */
        data2[4] =  (byte)(24);
        data2[8] =  (byte)frame;
        
        data2[12]  = (byte)d.getDia();
        data2[16] =  (byte)d.getMes();
        data2[20] =  (byte)d.getAno();
        data2[24] =  (byte)d.getHora();
        data2[28] =  (byte)d.getMinuto();
        data2[32] =  (byte)d.getSegundos();
        
        data2[36] =  (byte)crc;
        data2[40] =  (byte)0x0d;
        
      return data2;
  }
   


    public void process(int frame, byte[] data, OutputStream outputStream, LogGenerator log) {
        switch (frame) {
            case 161: // A1
                log.inserInfo("Processing Frame A1 ");
                RegistrationDAO rdao = new RegistrationDAO();
                Registration r = new Registration();

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                System.out.println("yyyy/MM/dd HH:mm:ss-> " + dtf.format(LocalDateTime.now()));

                String dataHora = dtf.format(LocalDateTime.now());
                String mensagem = new String(data, StandardCharsets.UTF_8);
                r.setMensagem(mensagem);
                r.setData(dataHora);

                if (rdao.add_registor(r)) {
                    System.out.println("registro cadastrado com sucesso");
                    log.inserInfo("Registro cadastrado com sucesso");
                    ProcessBufferTCP.sendAck(outputStream, r, log);
                } else {
                    System.out.println("Erro ao cadastrar novo registro");
                    log.inserWarn("Erro ao cadastrar novo registro");
                }

                break;

            case 162: // A2
                log.inserInfo("Processing Frame A2 ");
                // ----------------------------------IDADE---------------------------------
                byte[] byteidade = new byte[] {
                    (byte) data[0], (byte) data[1], (byte) data[2], (byte) data[3]
                };
                ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                buffer.put(byteidade).position(0);
                int idade = buffer.getInt();
                String hexaIdade = Integer.toHexString(idade).toUpperCase();

                // ---------------------------------- PESO  ---------------------------------
                byte[] bytepeso = new byte[] {
                    (byte) data[4], (byte) data[5], (byte) data[6], (byte) data[7]
                };
                buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                buffer.put(bytepeso).position(0);
                int peso = buffer.getInt();
                String hexaPeso = Integer.toHexString(peso).toUpperCase();

                // ---------------------------------- ALTURA ---------------------------------
                byte[] alturabyte = new byte[] {
                    (byte) data[8], (byte) data[9], (byte) data[10], (byte) data[11]
                };
                buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                buffer.put(alturabyte).position(0);
                int altura = buffer.getInt();
                String hexaAltura = Integer.toHexString(altura).toUpperCase();

                // ---------------------------------- Tamanho Nome ---------------------------------
                byte[] tamanhobyte = new byte[] {
                    (byte) data[12], (byte) data[13], (byte) data[14], (byte) data[15]
                };
                buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                buffer.put(tamanhobyte).position(0);
                int tamanho = buffer.getInt();
                String hexaTamanho = Integer.toHexString(tamanho).toUpperCase();


                // ---------------------------------- Nome ---------------------------------
                byte[] byteNome = new byte[tamanho];
                for (int i = 0; i < tamanho; i++) {
                    byteNome[i] = data[i + 16];
                }
                String nome = new String(byteNome, StandardCharsets.UTF_8);

                System.out.println(nome);

                //---------------- INSERT BANCO --------------------------------
                UserDAO udao = new UserDAO();
                User u = new User();

                u.setIdade(idade);
                u.setPeso(peso);
                u.setAltura(altura);
                u.setTamanhoNome(tamanho);
                u.setNome(nome);

                if (udao.add_user(u)) {
                    System.out.println("Usuario cadastrado com sucesso");
                    log.inserInfo("Usuario cadastrado com sucesso");
                    ProcessBufferTCP.sendAck(outputStream, log);
                } else {
                    System.out.println("Erro ao cadastrar usuario");
                    log.inserWarn("Erro ao cadastrar usuario");
                }

                break;

            case 163: // A3
                log.inserInfo("Processing Frame A3 ");
                // agora buscando e aplicando o time zone
                Data   _data = new Data();
                String timezone = new String(data, StandardCharsets.UTF_8);
                ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of(timezone)); //"Europe/Berlin"
                
                DateTimeFormatter dtfDia = DateTimeFormatter.ofPattern( "d" );
                DateTimeFormatter dtfMes = DateTimeFormatter.ofPattern( "M" );
                DateTimeFormatter dtfAno = DateTimeFormatter.ofPattern( "yy" );
                DateTimeFormatter dtfHora    = DateTimeFormatter.ofPattern( "HH" );
                DateTimeFormatter dtfMinuto  = DateTimeFormatter.ofPattern( "mm" );
                DateTimeFormatter dtfSegundo = DateTimeFormatter.ofPattern( "ss" );
                        
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                System.out.println(dateTimeFormatter.format(zdt));
                
                _data.setDia((Byte.valueOf(dtfDia.format(zdt))));
                System.out.println("Dia:  " + _data.getDia());
                
                _data.setMes((Byte.valueOf(dtfMes.format(zdt))));
                System.out.println("Mes:  " + _data.getMes());
                
                _data.setAno((Byte.valueOf(dtfAno.format(zdt))));
                System.out.println("Ano:  " + _data.getAno());
                
                _data.setHora((Byte.valueOf(dtfHora.format(zdt))));
                System.out.println("Hora:  " + _data.getHora());
                
                _data.setMinuto((Byte.valueOf(dtfMinuto.format(zdt))));
                System.out.println("Minuto:  " + _data.getMinuto());
                
                _data.setSegundos((Byte.valueOf(dtfSegundo.format(zdt))));
                System.out.println("Segundos:  " + _data.getSegundos());
                
                System.out.println(dateTimeFormatter.format(zdt));
                
                ProcessBufferTCP.sendDataHora(outputStream, _data, log);

                break;
            default:
                log.inserWarn("Unexpected frame: " + frame);
                break;
        }
    }
}
