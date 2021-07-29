/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author RaelH
 */
public class LogGenerator {
    private static Logger logger = Logger.getLogger(LogGenerator.class);
    
    public LogGenerator(){
         PropertyConfigurator.configure("log4j.properties");
    }
    
    public void  insertDebug(String valor){
        //Log in console in and log file
        logger.debug(valor);
    }
    
    public void  insertTrace(String valor){
        //Log in console in and log file
        logger.trace(valor);
    }
    
    public void  inserInfo(String valor){
        //Log in console in and log file
        logger.info(valor);
    }
    
    public void  inserWarn(String valor){
        //Log in console in and log file
        logger.warn(valor);
    }
    
    public void  inserError(String valor){
        //Log in console in and log file
        logger.warn(valor);
    }
    
    public void  FatalError(String valor){
        //Log in console in and log file
        logger.fatal(valor);
    }  
     
}
