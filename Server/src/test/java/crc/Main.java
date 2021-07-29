package crc;

import java.util.Scanner;

import static java.lang.System.out;

public class Main {

    //http://www.sunshine2k.de/coding/javascript/crc/crc_js.html
    public static void main(String[] args) {
        Check(Crc8.Params);

       // Check(Crc16.Params);

       // Check(Crc32.Params);

       // Check(Crc64.Params);
    }

    private static void Check(AlgoParams[] params)
    {
        for (int i = 0; i < params.length; i++) {
            CrcCalculator calculator = new CrcCalculator(params[i]);
            long result = calculator.Calc(CrcCalculator.TestBytes, 0, CrcCalculator.TestBytes.length);
            if (result != calculator.Parameters.Check)
                out.println(calculator.Parameters.Name + " - BAD ALGO!!! " + Long.toHexString(result).toUpperCase());
        }
    }
}