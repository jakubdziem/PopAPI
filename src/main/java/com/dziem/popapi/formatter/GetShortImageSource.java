package com.dziem.popapi.formatter;

import com.dziem.popapi.model.History;
import com.dziem.popapi.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class GetShortImageSource {
    private final HistoryRepository historyRepository;

    public void printShortSource() {
        List<ShortSource> apartamentSource = getApartamentSource();
        List<String> emptySourceIds = new ArrayList<>();
        List<ShortSource> toDo = new ArrayList<>();
        for (ShortSource source : apartamentSource) {
            if (source.shortSource.isEmpty()) {
                emptySourceIds.add(source.name);
            } else if (!source.worked) {
                  toDo.add(source);
            } else {
                System.out.printf("UPDATE HISTORY SET IMAGE_SOURCE_SHORT = '%s' WHERE NAME = '%s';\n", source.shortSource, source.name);
            }
        }
        System.out.println("\n\n TO DO \n \n");
        for(ShortSource shortSource : toDo) {
            System.out.printf("UPDATE HISTORY SET IMAGE_SOURCE_SHORT = '' WHERE NAME = '%s';\n %s\n", shortSource.name, shortSource.shortSource);
        }
        System.out.println("\n\nEMPTY\n\n");
        for (String name : emptySourceIds) {
            System.out.println(name);
        }
    }

    public List<ShortSource> getApartamentSource() {
        List<ShortSource> shortSources = new ArrayList<>();
        List<History> allSource = historyRepository.findAll();
        for (History driver : allSource) {
            String source = driver.getImageSource();
            SBReturn sbReturn = getShortSource(source);
            shortSources.add(sbReturn.name.isEmpty() ? new ShortSource(driver.getName(), "", sbReturn.worked)
                    : new ShortSource(driver.getName(), sbReturn.name, sbReturn.worked));

        }
        return shortSources;

    }

    private static SBReturn getShortSource(String source) {
        String shortSource;
        boolean worked = true;
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
        else if(source.contains("metmuseum")) {
            shortSource = "metmuseum.github.io";
        }
        else if(source.contains("egypttoursportal")) {
            shortSource = "egypttoursportal.com";
        }
        else if(source.contains("humanoriginproject")) {
            shortSource = "humanoriginproject.com";
        }
        else if(source.contains("natgeofe") || source.contains("nationalgeographic")) {
            shortSource = "National Geographic";
        }
        else if(source.contains("creation")) {
            shortSource = "creation.com";
        }
        else if(source.contains("worldhistory")) {
            shortSource = "worldhistory.org";
        }
        else if(source.contains("thediplomat")) {
            shortSource = "thediplomat.com";
        }
        else if(source.contains("arkeonews")) {
            shortSource = "arkeonews.net";
        }
        else if(source.contains("arkeonews")) {
            shortSource = "arkeonews.net";
        }
        else if(source.contains("armstronginstitute")) {
            shortSource = "armstronginstitute.org";
        }
        else if(source.contains("meccacenter")) {
            shortSource = "meccacenter.org";
        }
        else if(source.contains("aetnd")) {
            shortSource = "aetnd.com";
        }
        else if(source.contains("silkqin")) {
            shortSource = "silkqin.com";
        }
        else if(source.contains("romanempiretimes")) {
            shortSource = "romanempiretimes.com";
        }
        else if(source.contains("latinitium")) {
            shortSource = "latinitium.com";
        }
        else if(source.contains("thefederalist")) {
            shortSource = "thefederalist.com";
        }
        else if(source.contains("historydefined")) {
            shortSource = "historydefined.net";
        }
        else if(source.contains("warfarehistorynetwork")) {
            shortSource = "warfarehistorynetwork.com";
        }
        else if(source.contains("miro.medium")) {
            shortSource = "Medium";
        }
        else if(source.contains("hurstwic")) {
            shortSource = "hurstwic.org";
        }
        else if(source.contains("worldhistory")) {
            shortSource = "worldhistory.org";
        }
        else if(source.contains("historic-uk")) {
            shortSource = "historic-uk.com";
        }
        else if(source.contains("ancient-origins")) {
            shortSource = "ancient-origins.net";
        }
        else if(source.contains("bologna-experience")) {
            shortSource = "bologna-experience.eu";
        }
        else if(source.contains("thoughtco")) {
            shortSource = "thoughtco.com";
        }
        else if(source.contains("images.ohmyhosting")) {
            shortSource = "historia.dorzeczy.pl";
        }
        else if(source.contains("thechinaproject")) {
            shortSource = "thechinaproject.com";
        }
        else if(source.contains("ytimg")) {
            shortSource = "i.ytimg.com";
        }
        else if(source.contains("nobility")) {
            shortSource = "nobility.org";
        }
        else if(source.contains("thoughtco")) {
            shortSource = "thoughtco.com";
        }
        else if(source.contains("historytoday")) {
            shortSource = "historytoday.com";
        }
        else if(source.contains("cdc") && source.contains("gov")) {
            shortSource = "cdc.gov";
        }
        else if(source.contains("aetnd")) {
            shortSource = "aetnd.com";
        }
        else if(source.indexOf("media.iwm.org.uk") > 0) {
            shortSource = "Imperial War Museum";
        }
        else if(source.contains("aetnd")) {
            shortSource = "Facebook";
        }
        else if(source.indexOf("www.loc.gov") > 0) {
            shortSource = "loc.gov";
        }
        else {
            shortSource = source.substring(source.indexOf("://") + 3, source.indexOf("/", source.indexOf("://") + 3));
            worked = false;
        }
        return new SBReturn(shortSource,worked);
    }

    public record ShortSource(String name, String shortSource, boolean worked){}
    public record SBReturn(String name, boolean worked){}
}
