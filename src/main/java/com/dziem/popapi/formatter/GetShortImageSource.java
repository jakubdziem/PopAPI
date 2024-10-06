package com.dziem.popapi.formatter;

import com.dziem.popapi.model.Artist;
import com.dziem.popapi.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class GetShortImageSource {
    private final ArtistRepository artistRepository;

    public void printShortSource() {
        List<ShortSource> sources = getSource();
        List<String> emptySourceIds = new ArrayList<>();
        List<ShortSource> toDo = new ArrayList<>();
        for (ShortSource source : sources) {
            if (source.shortSource.isEmpty()) {
                emptySourceIds.add(source.name);
            } else if (!source.worked) {
                  toDo.add(source);
            } else {
                System.out.printf("UPDATE ARTIST SET IMAGE_SOURCE_SHORT = '%s' WHERE ARTIST_NAME = '%s';\n", source.shortSource, source.name);
            }
        }
        System.out.println("\n\n TO DO \n \n");
        for(ShortSource shortSource : toDo) {
            System.out.printf("UPDATE ARTIST SET IMAGE_SOURCE_SHORT = '' WHERE ARTIST_NAME = '%s';\n %s\n", shortSource.name, shortSource.shortSource);
        }
        System.out.println("\n\nEMPTY\n\n");
        for (String name : emptySourceIds) {
            System.out.println(name);
        }
    }

    public List<ShortSource> getSource() {
        List<ShortSource> shortSources = new ArrayList<>();
        List<Artist> allSource = artistRepository.findAll();
        for (Artist country : allSource) {
            String source = country.getImageSource();
            SBReturn sbReturn = getShortSource(source);
            shortSources.add(sbReturn.name.isEmpty() ? new ShortSource(country.getArtistName(), "", sbReturn.worked)
                    : new ShortSource(country.getArtistName(), sbReturn.name, sbReturn.worked));

        }
        return shortSources;

    }

    private static SBReturn getShortSource(String source) {
        String shortSource;
        boolean worked = true;
        if (source.contains("wiki")) {
            shortSource = "Wikipedia";
        } else if (source.contains("m.media-amazon.com")) {
            shortSource = "IMDb";
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
            shortSource = "Youtube";
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
        else if(source.indexOf("nasa") > 0) {
            shortSource = "nasa.gov";
        }
        else if(source.indexOf("www.loc.gov") > 0) {
            shortSource = "loc.gov";
        }
        else if(source.indexOf("tiktok") > 0) {
            shortSource = "TikTok";
        }
        else if(source.indexOf("yt3") > 0) {
            shortSource = "Youtube";
        }else if(source.indexOf("pbs.twimg.com") > 0) {
            shortSource = "X.com";
        }
        else if(source.indexOf("wattpad") > 0) {
            shortSource = "Wattpad";
        }else if(source.indexOf("i.pinimg.com") > 0) {
            shortSource = "TikTok";
        }else if(source.indexOf("www.rollingstone.com") > 0) {
            shortSource = "rollingstone.com";
        }else if(source.indexOf("dailymail.co.uk") > 0) {
            shortSource = "DailyMail";
        }else if(source.indexOf("i.pinimg.com") > 0) {
            shortSource = "TikTok";
        }else if(source.indexOf("yimg.com") > 0) {
            shortSource = "Yahoo";
        }else if(source.indexOf("vogue") > 0) {
            shortSource = "Vogue";
        }else if(source.indexOf("nike") > 0) {
            shortSource = "Nike";
        }else if(source.indexOf("people.com") > 0) {
            shortSource = "people.com";
        }else if(source.indexOf("hollywoodreporter.com") > 0) {
            shortSource = "hollywoodreporter.com";
        }else if(source.indexOf("millmentor.com") > 0) {
            shortSource = "millmentor.com";
        }else if(source.indexOf("guim.co.uk") > 0) {
            shortSource = "guim.co.uk";
        }else if(source.indexOf("variety.com") > 0) {
            shortSource = "variety.com";
        }else if(source.indexOf("google") > 0) {
            shortSource = "Google";
        }else if(source.indexOf("hulu.com") > 0) {
            shortSource = "Hulu";
        }else if(source.indexOf("buffaloriverworks.com") > 0) {
            shortSource = "buffaloriverworks.com";
        }else if(source.indexOf("billboard.com") > 0) {
            shortSource = "billboard.com";
        }else if(source.indexOf("deadline.com") > 0) {
            shortSource = "deadline.com";
        }else if(source.indexOf("fortune.com") > 0) {
            shortSource = "fortune.com";
        }else if(source.indexOf("tumblr.com") > 0) {
            shortSource = "tumblr.com";
        }else if(source.indexOf("firstpost.com") > 0) {
            shortSource = "firstpost.com";
        }else if(source.indexOf("cricfit.com") > 0) {
            shortSource = "cricfit.com";
        }else if(source.indexOf("hearstapps.com") > 0) {
            shortSource = "univision.com";
        }else if(source.indexOf("s7d1.scene7.com") > 0) {
            shortSource = "Instragram";
        }else if(source.indexOf("originalfilmart.com") > 0) {
            shortSource = "originalfilmart.com";
        }else if(source.indexOf("deepfocusreview.com") > 0) {
            shortSource = "deepfocusreview.com";
        }else if(source.indexOf("s7d1.scene7.com") > 0) {
            shortSource = "Instragram";
        }else if(source.indexOf("hbo.com") > 0) {
            shortSource = "HBO";
        }else if(source.indexOf("justwatch.com") > 0) {
            shortSource = "justwatch.com";
        }else if(source.indexOf("resizing.flixster.com") > 0 || source.contains("imdb")) {
            shortSource = "IMDb";
        } else if(source.indexOf("justwatch.com") > 0) {
            shortSource = "justwatch.com";
        }else if(source.indexOf("hotstarext.com") > 0) {
            shortSource = "hoststar.com";
        }else if(source.indexOf("bbcearth.com") > 0) {
            shortSource = "bbcearth.com";
        }else if(source.indexOf("vanityfair.com") > 0) {
            shortSource = "vanityfair.com";
        }else if(source.indexOf("nbcnews") > 0) {
            shortSource = "nbcnews.com";
        }else if(source.indexOf("tcm.com") > 0) {
            shortSource = "tcm.com";
        }else if(source.indexOf("nflximg.net") > 0) {
            shortSource = "Netflix";
        }else if(source.indexOf("hotstarext.com") > 0) {
            shortSource = "hoststar.com";
        }else if(source.indexOf("xboxlive.com") > 0) {
            shortSource = "Amazon";
        }else if(source.indexOf("bike-eu.com") > 0) {
            shortSource = "bike-eu.com";
        }else if(source.indexOf("termedia.pl") > 0) {
            shortSource = "termedia.pl";
        }else if(source.indexOf("ispoint.org") > 0) {
            shortSource = "ispoint.org";
        }else if(source.indexOf("5mglobal.com") > 0) {
            shortSource = "5mglobal.com";
        }else if(source.indexOf("mylomza.pl") > 0) {
            shortSource = "mylomza.pl";
        }else if(source.indexOf("flypak.pl") > 0) {
            shortSource = "flypak.pl";
        }else if(source.indexOf("allegro.pl") > 0) {
            shortSource = "allegro.pl";
        }else if(source.indexOf("itvnextra.pl") > 0) {
            shortSource = "itvnextra.pl";
        }else if(source.indexOf("skapiec") > 0) {
            shortSource = "skapiec.pl";
        }else if(source.indexOf("neutralzone.com") > 0) {
            shortSource = "neutralzone.com";
        }else if(source.indexOf("flaticon.com") > 0) {
            shortSource = "neutralzone.com";
        }else if(source.indexOf("yara.com") > 0) {
            shortSource = "yara.com";
        }else if(source.indexOf("sklepharcerski.pl") > 0) {
            shortSource = "sklepharcerski.pl";
        }else if(source.indexOf("empik") > 0) {
            shortSource = "empik.com";
        }else if(source.indexOf("psr.org.pl") > 0) {
            shortSource = "psr.org.pl";
        }else if(source.indexOf("empik.com") > 0) {
            shortSource = "empik.com";
        }else if(source.indexOf("doyouneedvisa.com") > 0) {
            shortSource = "doyouneedvisa.com";
        }else if(source.indexOf("commonwealth-community-heritage.net") > 0) {
            shortSource = "commonwealth-community-heritage.net";
        }else if(source.indexOf("legitimateinterests.com.ua") > 0) {
            shortSource = "legitimateinterests.com.ua";
        }else if(source.indexOf("boeckmann.com") > 0) {
            shortSource = "boeckmann.com";
        }else if(source.indexOf("youtube.com") > 0) {
            shortSource = "Youtube";
        }else if(source.indexOf("iicba.unesco.org") > 0) {
            shortSource = "iicba.unesco.org";
        }else if(source.indexOf("slaskie.naszemiasto.pl") > 0) {
            shortSource = "slaskie.naszemiasto.pl";
        }else if(source.indexOf("linkedin") > 0) {
            shortSource = "Linkedin";
        }else if(source.indexOf("eurowindykacjafonsuris.pl") > 0) {
            shortSource = "eurowindykacjafonsuris.pl";
        }else if(source.indexOf("jakieszczepionki.pl") > 0) {
            shortSource = "jakieszczepionki.pl";
        }else if(source.indexOf("123rf.com") > 0) {
            shortSource = "123rf.com";
        }else if(source.indexOf("sjw.uw.edu.pl") > 0) {
            shortSource = "sjw.uw.edu.pl";
        }else if(source.indexOf("123rf.com") > 0) {
            shortSource = "123rf.com";
        }else if(source.indexOf("pgcareers.com") > 0) {
            shortSource = "P&G Careers";
        }else if(source.indexOf("mobileworldlive.com") > 0) {
            shortSource = "mobileworldlive.com";
        }else if(source.indexOf("social.sbrick.com") > 0) {
            shortSource = "social.sbrick.com";
        }else if(source.indexOf("ministryofcreativity.pl") > 0) {
            shortSource = "ministryofcreativity.pl";
        }else if(source.indexOf("character.ai") > 0) {
            shortSource = "character.ai";
        }else if(source.indexOf("sacreee.org") > 0) {
            shortSource = "sacreee.org";
        }else if(source.indexOf("character.ai") > 0) {
            shortSource = "character.ai";
        }else if(source.indexOf("3dflagsplus.com") > 0) {
            shortSource = "3dflagsplus.com";
        }else if(source.indexOf("adwokat-wasowicz.pl") > 0) {
            shortSource = "adwokat-wasowicz.pl";
        }else if(source.indexOf("anhre.org") > 0) {
            shortSource = "anhre.org";
        }else if(source.indexOf("olx.pl") > 0) {
            shortSource = "OLX";
        }else if(source.indexOf("polandasia.com") > 0) {
            shortSource = "polandasia.com";
        }else if(source.indexOf("worldflags.net") > 0) {
            shortSource = "worldflags.net";
        }else if(source.indexOf("flagi-i-hymny.pl") > 0) {
            shortSource = "flagi-i-hymny.pl";
        }else if(source.indexOf("trans.info") > 0) {
            shortSource = "trans.info";
        }else if(source.indexOf("flaticon.com") > 0) {
            shortSource = "flaticon.com";
        }else if(source.indexOf("youremployerofrecord.com") > 0) {
            shortSource = "youremployerofrecord.com";
        }else if(source.indexOf("migrant-integration.ec.europa.eu") > 0) {
            shortSource = "migrant-integration.ec.europa.eu";
        }else if(source.indexOf("biuroakademia.pl") > 0) {
            shortSource = "biuroakademia.pl";
        }else if(source.indexOf("imei24.com") > 0) {
            shortSource = "imei24.com";
        }else if(source.indexOf("reddit") > 0) {
            shortSource = "Reddit";
        }else if(source.indexOf("rejestrowaniesamochodu.pl") > 0) {
            shortSource = "rejestrowaniesamochodu.pl";
        }else if(source.indexOf("neutralzone.com") > 0) {
            shortSource = "neutralzone.com";
        }else if(source.indexOf("ceneo.pl") > 0) {
            shortSource = "ceneo.pl";
        }else if(source.indexOf("doyouneedvisa.com") > 0) {
            shortSource = "doyouneedvisa.com";
        }else if(source.indexOf("interia") > 0) {
            shortSource = "interia.pl";
        }else if(source.indexOf("greenplantation.pl") > 0) {
            shortSource = "greenplantation.pl";
        }else if(source.indexOf("cost.eu") > 0) {
            shortSource = "cost.eu";
        }else if(source.indexOf("okumafishing.com") > 0) {
            shortSource = "okumafishing.com";
        }else if(source.indexOf("osculati.com") > 0) {
            shortSource = "osculati.com";
        }else if(source.indexOf("igfmining.org") > 0) {
            shortSource = "igfmining.org";
        }else if(source.indexOf("hcch.net") > 0) {
            shortSource = "hcch.net";
        }else if(source.indexOf("flagi-panstw.pl") > 0) {
            shortSource = "flagi-panstw.pl";
        }else if(source.indexOf("publicdomainvectors.org") > 0) {
            shortSource = "publicdomainvectors.org";
        }else if(source.indexOf("ego-plone.uni-trier.de") > 0) {
            shortSource = "ego-plone.uni-trier.de";
        }else if(source.indexOf("redd.it") > 0) {
            shortSource = "Reddit";
        }else if(source.indexOf("shop.samsmithworld.com") > 0) {
            shortSource = "shop.samsmithworld.com";
        }else if(source.indexOf("scdn.co") > 0) {
            shortSource = "Spotify";
        }else if(source.indexOf("tidal") > 0) {
            shortSource = "Tidal";
        }else if(source.indexOf("sndcdn.com") > 0) {
            shortSource = "Soundcloud";
        }else if(source.indexOf("usatoday.com") > 0) {
            shortSource = "usatoday.com";
        }else if(source.indexOf("avicii.com") > 0) {
            shortSource = "avicii.com";
        }else if(source.indexOf("jazzsoul.pl") > 0) {
            shortSource = "jazzsoul.pl";
        }else if(source.indexOf("motownrecords.com") > 0) {
            shortSource = "motownrecords.com";
        }else if(source.indexOf("pyxis.nymag.com") > 0) {
            shortSource = "Vulture";
        }else if(source.indexOf("ca-times.brightspotcdn.com") > 0) {
            shortSource = "moveconcerts.com";
        }else if(source.indexOf("ebilet.pl") > 0) {
            shortSource = "ebilet.pl";
        }else if(source.indexOf("welcometomontero.com") > 0) {
            shortSource = "welcometomontero.com";
        }else if(source.indexOf("media.gq.com") > 0) {
            shortSource = "gq.com";
        }else if(source.indexOf("abcnewsfe.com") > 0) {
            shortSource = "abcnews.go.com";
        }else if(source.indexOf("t-mobilecenter.com") > 0) {
            shortSource = "t-mobilecenter.com";
        }else if(source.indexOf("nederlanderconcerts.com") > 0) {
            shortSource = "nederlanderconcerts.com";
        }else if(source.indexOf("rockhall.com") > 0) {
            shortSource = "rockhall.com";
        }else if(source.indexOf("sunsetmarquis.com") > 0) {
            shortSource = "sunsetmarquis.com";
        }else if(source.indexOf("acllive.com") > 0) {
            shortSource = "acllive.com";
        }else if(source.indexOf("spotifycdn.com") > 0) {
            shortSource = "spotifycdn.com";
        }else if(source.indexOf("meghan-trainor.com") > 0) {
            shortSource = "meghan-trainor.com";
        }else if(source.indexOf("www.nirvana.com") > 0) {
            shortSource = "nirvana.com";
        }else if(source.indexOf("slate.com") > 0) {
            shortSource = "slate.com";
        }else if(source.indexOf("slate.com") > 0) {
            shortSource = "slate.com";
        }else if(source.indexOf("wired.com") > 0) {
            shortSource = "wired.com";
        }else if(source.indexOf("cnn.com") > 0) {
            shortSource = "cnn.com";
        }else if(source.indexOf("espncdn.com") > 0) {
            shortSource = "espn.com";
        }else if(source.indexOf("2pac.com") > 0) {
            shortSource = "2pac.com";
        }else if(source.indexOf("bustle.com") > 0) {
            shortSource = "bustle.com";
        }else if(source.indexOf("npr.org") > 0) {
            shortSource = "npr.org";
        }else if(source.indexOf("cloudfront-us-east-2.images.arcpublishing.com") > 0) {
            shortSource = "investing.com";
        }else if(source.indexOf("dynamicmedia.livenationinternational.com") > 0) {
            shortSource = "livenation.pl";
        }else if(source.indexOf("d3vhc53cl8e8km.cloudfront.net") > 0) {
            shortSource = "insomaniac.com";
        }else if(source.indexOf("d3vhc53cl8e8km.cloudfront.net") > 0) {
            shortSource = "insomaniac.com";
        }else if(source.indexOf("cdn-p.smehost.net") > 0) {
            shortSource = "sonymusic.co.uk";
        }else if(source.indexOf("media.them.us") > 0) {
            shortSource = "them.us";
        }else if(source.indexOf("media.them.us") > 0) {
            shortSource = "them.us";
        }else if(source.indexOf("bunny-wp-pullzone-cjamrcljf0.b-cdn.net") > 0) {
            shortSource = "georgiaencyclopedia.org";
        }else {
            shortSource = source.substring(source.indexOf("://") + 3, source.indexOf("/", source.indexOf("://") + 3));
            worked = false;
        }
        return new SBReturn(shortSource,worked);
    }

    public record ShortSource(String name, String shortSource, boolean worked){}
    public record SBReturn(String name, boolean worked){}
}
