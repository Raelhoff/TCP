/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author RaelH
 */
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
public class LogTest {
    private static Logger logger = Logger.getLogger(LogTest.class);
    private LogTest() { }
    
    public static void main(final String[] args) {
        //PropertiesConfigurator is used to configure logger from properties file
        PropertyConfigurator.configure("log4j.properties");
 
        //Log in console in and log file
        logger.debug("Log4j appender configuration is successful !!");
         //logging in different levels
        logger.trace("This is a Trace");
        logger.debug("This is a Debug");
        logger.info("This is an Info");
        logger.warn("This is a Warn");
        logger.error("This is an Error");
        logger.fatal("This is a Fatal");
    }
}
