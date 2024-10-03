package com.dziem.popapi.formatter;

import com.dziem.popapi.model.Apartment;
import com.dziem.popapi.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class GetShortImageSource {
    private final ApartmentRepository apartmentRepository;

    public void printShortSource() {
        List<ShortSource> apartamentSource = getApartamentSource();
        List<String> emptySourceIds = new ArrayList<>();
        List<ShortSource> toDo = new ArrayList<>();
        for (ShortSource source : apartamentSource) {
            if (source.shortSource.isEmpty()) {
                emptySourceIds.add(source.name);
            } else if (source.shortSource.contains(".")) {
                  toDo.add(source);
            } else {
                System.out.printf("UPDATE APARTMENT SET IMAGE_SOURCE_SHORT = '%s' WHERE NAME = '%s';\n", source.shortSource, source.name);
            }
        }
        System.out.println("\n\n TO DO \n \n");
        for(ShortSource shortSource : toDo) {
            System.out.printf("UPDATE APARTMENT SET IMAGE_SOURCE_SHORT = '' WHERE NAME = '%s';\n %s\n", shortSource.name, shortSource.shortSource);
        }
        System.out.println("\n\nEMPTY\n\n");
        for (String name : emptySourceIds) {
            System.out.println(name);
        }
    }

    public List<ShortSource> getApartamentSource() {
        List<ShortSource> shortSources = new ArrayList<>();
        List<Apartment> allSource = apartmentRepository.findAll();
        for (Apartment apartment : allSource) {
            String source = apartment.getImageSource();
            String shortSource = getShortSource(source);
            shortSources.add(shortSource.isEmpty() ? new ShortSource(apartment.getCountryOrCityName(), "")
                    : new ShortSource(apartment.getCountryOrCityName(), shortSource));

        }
        return shortSources;

    }

    private static String getShortSource(String source) {
        String shortSource;
        if (source.contains("wiki")) {
            shortSource = "Wikipedia";
        } else if (source.contains("amazon")) {
            shortSource = "Amazon";
        } else if (source.contains("britannica")) {
            shortSource = "Brittanica";
        } else if (source.contains("ebay")) {
            shortSource = "Ebay";
        } else if (source.contains("vecteezy")) {
            shortSource = "Vecteezy";
        } else if (source.contains("istockphoto")) {
            shortSource = "iStock";
        } else if (source.contains("shutterstock")) {
            shortSource = "Shutterstock";
        } else if (source.contains("pinterest")) {
            shortSource = "Pinterest";
        } else if (source.contains("facebook")) {
            shortSource = "Facebook";
        } else if(source.contains("quora")) {
            shortSource = "Quora";
        } else if(source.contains("stock.adobe")) {
            shortSource = "Adobe Stock";
        } else if(source.contains("lastdodo")) {
            shortSource = "LastDodo";
        } else if(source.contains("iflagi")) {
            shortSource = "iFlagi";
        } else if(source.contains("mobileworldlive")) {
            shortSource = "MobileWorldLive";
        }
        else {
            shortSource = source.substring(source.indexOf("://") + 3, source.indexOf("/", source.indexOf("://") + 3));
        }
        return shortSource;
    }

    public record ShortSource(String name, String shortSource){}
}
