/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crc;

import static java.lang.System.out;

/**
 *
 * @author RaelH
 */
public class ControleCRC {
        private AlgoParams         Crc8 = null; 
        private CrcCalculator      calculator = null; 
    
    public ControleCRC(){
            Crc8 = new AlgoParams("CRC-8", 8, 0x7, 0x0, false, false, 0x0, 0xF4);
            calculator = new CrcCalculator(Crc8);
    }
    
    public String retornaCRC( byte[] bytes){
            long result = calculator.Calc(bytes, 0, bytes.length);
            String hexa = Long.toHexString(result).toUpperCase();
       
            return    hexa;
    }
    
    public int calculaCRC8( byte[] bytes){
        long result = calculator.Calc(bytes, 0, bytes.length);
       
        return (int) (long)  result;
    }
}
