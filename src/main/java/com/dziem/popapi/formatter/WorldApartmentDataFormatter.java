package com.dziem.popapi.formatter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class WorldApartmentDataFormatter {
    public static void main(String[] args) {
//        worldApartment();
        polandApartment();
    }

    private static void polandApartment() {
        try (Stream<String> stream = Files.lines(Paths.get("src/main/resources/data/apartmentPricesPoland.txt"))) {
            List<String> lines = stream.toList();
            String name = "";
            for(int i = 0; i < lines.size(); i++) {
                if(i%2==0) {
                    name = lines.get(i);
                }
                else {
                    Float price = Float.parseFloat(lines.get(i));
                    String format = String.format("%.2f", price);
                    System.out.printf("INSERT INTO APARTMENT (NAME, PRICE, CATEGORY, IMAGE_URL) VALUES ('%s', %s, 'Poland','/images/city/poland/%s.png');\n", name, format.replace(",", "."), name);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void worldApartment() {
        try (Stream<String> stream = Files.lines(Paths.get("src/main/resources/data/apartmentPricesWorld.txt"))) {
            List<String> lines = stream.toList();
            for(String line : lines) {
                String[] split = line.split(";");
                String name = split[0];
                Float price = Float.parseFloat(split[1].substring(0, split[1].length()-2).replace(",","").replace(" ", ""));

                System.out.printf("INSERT INTO APARTMENT (NAME, PRICE, COUNTRY, IMAGE_URL) VALUES (%s, %.2f, World,'');\n", name, price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
