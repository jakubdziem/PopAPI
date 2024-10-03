package com.dziem.popapi.formatter;

import com.dziem.popapi.model.Driver;
import com.dziem.popapi.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class GetShortImageSource {
    private final DriverRepository driverRepository;

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
                System.out.printf("UPDATE DRIVER SET IMAGE_SOURCE_SHORT = '%s' WHERE NAME = '%s';\n", source.shortSource, source.name);
            }
        }
        System.out.println("\n\n TO DO \n \n");
        for(ShortSource shortSource : toDo) {
            System.out.printf("UPDATE DRIVER SET IMAGE_SOURCE_SHORT = '' WHERE NAME = '%s';\n %s\n", shortSource.name, shortSource.shortSource);
        }
        System.out.println("\n\nEMPTY\n\n");
        for (String name : emptySourceIds) {
            System.out.println(name);
        }
    }

    public List<ShortSource> getApartamentSource() {
        List<ShortSource> shortSources = new ArrayList<>();
        List<Driver> allSource = driverRepository.findAll();
        for (Driver driver : allSource) {
            String source = driver.getImageSource();
            String shortSource = getShortSource(source);
            shortSources.add(shortSource.isEmpty() ? new ShortSource(driver.getName(), "")
                    : new ShortSource(driver.getName(), shortSource));

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
        } else if(source.contains("motorsport")) {
            shortSource = "MotorSport";
        } else if(source.contains("ferrari")) {
            shortSource = "Ferrari";
        }
        else if(source.contains("racingnews365")) {
            shortSource = "RacingNews365";
        }
        else if(source.contains("formula1.com")) {
            shortSource = "Formula 1";
        }
        else if(source.contains("professionalmoron.com")) {
            shortSource = "professionalmoron.com";
        }
        else if(source.contains("racefans")) {
            shortSource = "Racefans";
        }
        else if(source.contains("independent")) {
            shortSource = "The Independent";
        }
        else if(source.contains(" thinkingheads.com")) {
            shortSource = "thinkingheads.com";
        }
        else if(source.contains("autosport")) {
            shortSource = "Autosport";
        }
        else if(source.contains("f1-fansite.com")) {
            shortSource = "f1-fansite.com";
        }
        else if(source.contains("topgear.com")) {
            shortSource = "TopGear";
        }
        else if(source.contains("dealerinspire.com")) {
            shortSource = "dealerinspire.com";
        }
        else if(source.contains("ctvnews")) {
            shortSource = "CTV News";
        }
        else {
            shortSource = source.substring(source.indexOf("://") + 3, source.indexOf("/", source.indexOf("://") + 3));
        }
        return shortSource;
    }

    public record ShortSource(String name, String shortSource){}
}
