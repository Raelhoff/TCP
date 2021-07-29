/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverTCP;
import controller.ProcessBufferTCP;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import static java.lang.System.in;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.LogGenerator;

/**
 *
 * @author RaelH
 */
public class Server_X_Client {
    
    
    public static void main(String args[]){


        Socket s=null;
        ServerSocket ss2=null;
        System.out.println("Server Listening......");
        LogGenerator log = new LogGenerator();
        try{
            ss2 = new ServerSocket(4447); // can also use static final PORT_NUM , when defined
            log.inserInfo("ServerTCP Listening: " + 4447);
        }
        catch(IOException e){
          e.printStackTrace();
          System.out.println("Server error");
          log.inserError("Server error");
        }

        while(true){
            try{
                s= ss2.accept();
                System.out.println("connection Established");
                log.inserInfo("connection Established (address): " + s.getLocalAddress());
                ServerThread st=new ServerThread(s);
                st.start();
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Error");
                log.inserError("Connection error ");
            }
        }
    }
}



class ServerThread extends Thread{  

    String line=null;
    BufferedReader  is = null;
    PrintWriter os=null;
    Socket s=null;
    
    public ServerThread(Socket s){
        this.s=s;
    }

    public void run() {
        InputStream inputStream  = null; 
        OutputStream outputStream = null;
        LogGenerator log = new LogGenerator();
    try{
        //reader on client socket
        inputStream = new DataInputStream(s.getInputStream()); 
              
        //writes on client socket
         outputStream=new ObjectOutputStream(s.getOutputStream());
        
    }catch(IOException e){
        System.out.println("IO error in server thread");
        log.inserError("IO error in server thread");
    }

    try {
        while(true){
           int datagramSize = inputStream.available();
           if(datagramSize >= 4){
               ProcessBufferTCP process = new ProcessBufferTCP();
               log.inserInfo("Processing message");
               process.ProcessBufferTCP(inputStream, s.getOutputStream(), log);
           } 
           
        }
    } catch (IOException e) {

        line=this.getName(); //reused String line for getting thread name
        System.out.println("IO Error/ Client "+line+" terminated abruptly");
        log.inserError("IO Error/ Client "+line+" terminated abruptly");
    }
    catch(NullPointerException e){
        line=this.getName(); //reused String line for getting thread name
        System.out.println("Client "+line+" Closed");
        log.inserError("Client "+line+" Closed");
    }

    finally{    
        try{
            System.out.println("Connection Closing..");
            log.inserInfo("Connection Closing..");
            if (is!=null){
                is.close(); 
                System.out.println(" Socket Input Stream Closed");
                log.inserInfo(" Socket Input Stream Closed");
            }

            if(os!=null){
                os.close();
                System.out.println("Socket Out Closed");
                log.inserInfo("Socket Out Closed");
            }
            if (s!=null){
                log.inserInfo("Socket Closed (address): " + s.getLocalAddress());
                s.close();
                System.out.println("Socket Closed");
            }

            }catch(IOException ie){
                System.out.println("Socket Close Error");
                log.inserInfo("Socket Close Error");
            }
        }//end finally
    }
    
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
