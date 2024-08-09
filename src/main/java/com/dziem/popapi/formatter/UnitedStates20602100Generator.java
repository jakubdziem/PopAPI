package com.dziem.popapi.formatter;

import java.math.BigDecimal;
import java.util.ArrayList;

public class UnitedStates20602100Generator {
    public static void main(String[] args) {
        ArrayList<String> res = new ArrayList<>();
        res.add("""
                    "United States","2061,United States","US","2061","404483055","0,40"\n
                """);

        for(int i = 0; i < 40; i++) {
            String[] split = res.get(i).split(",");
            BigDecimal populationBD = new BigDecimal(split[split.length-3].replace("\"", ""));
            BigDecimal populationCalculated = populationBD.add(populationBD.multiply(new BigDecimal(0.40/100)));
            String population = String.valueOf(populationCalculated.intValue());
            String year = String.valueOf(2060+i+1);
            String r = String.format("""
                    "United States","%s,United States","US","%s","%s","0,40"\n
                    """, year,year, population);
            res.add(r);

        }
        for(String r : res) {
            System.out.print(r);
        }
    }
}
