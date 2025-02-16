package com.dziem.popapi.formatter;

import java.util.Arrays;
import java.util.List;

public class TeamsPointsInsertGenerator {
    public static void main(String[] args) {
        String s = """
                    <a href="/en/ferrari.aspx"><span class="CurChpConstructor" title="World Champion">Ferrari</span></a></td><td align="right">10 324<span style="visibility:hidden">.00</span></td><a href="/en/red-bull.aspx"><span class="CurChpConstructor" title="World Champion">Red Bull</span></a></td><td align="right">7 837<span style="visibility:hidden">.00</span></td><a href="/en/mercedes.aspx"><span class="CurChpConstructor" title="World Champion">Mercedes</span></a></td><td align="right">7 690.50</td><a href="/en/mclaren.aspx"><span class="CurChpConstructor" title="World Champion">McLaren</span></a></td><td align="right">6 957.50</td><a href="/en/williams.aspx"><span class="CurChpConstructor" title="World Champion">Williams</span></a></td><td align="right">3 637<span style="visibility:hidden">.00</span></td><a href="/en/lotus.aspx"><span class="WorldChp" title="World Champion">Lotus</span></a></td><td align="right">2 074<span style="visibility:hidden">.00</span></td><a href="/en/renault.aspx"><span class="WorldChp" title="World Champion">Renault</span></a></td><td align="right">1 777<span style="visibility:hidden">.00</span></td><a href="/en/force-india.aspx">Force India</a></td><td align="right">1 039<span style="visibility:hidden">.00</span></td><a href="/en/brabham.aspx"><span class="WorldChp" title="World Champion">Brabham</span></a></td><td align="right"> 864<span style="visibility:hidden">.00</span></td><a href="/en/benetton.aspx"><span class="WorldChp" title="World Champion">Benetton</span></a></td><td align="right"> 851.50</td><a href="/en/tyrrell.aspx"><span class="WorldChp" title="World Champion">Tyrrell</span></a></td><td align="right"> 621<span style="visibility:hidden">.00</span></td><a href="/en/sauber.aspx">Sauber</a></td><td align="right"> 513<span style="visibility:hidden">.00</span></td><a href="/en/alpine.aspx"><span class="CurConstructor">Alpine</span></a></td><td align="right"> 513<span style="visibility:hidden">.00</span></td><a href="/en/aston-martin.aspx"><span class="CurConstructor">Aston Martin</span></a></td><td align="right"> 506<span style="visibility:hidden">.00</span></td><a href="/en/toro-rosso.aspx">Toro Rosso</a></td><td align="right"> 500<span style="visibility:hidden">.00</span></td><a href="/en/brm.aspx"><span class="WorldChp" title="World Champion">BRM</span></a></td><td align="right"> 433<span style="visibility:hidden">.00</span></td><a href="/en/ligier.aspx">Ligier</a></td><td align="right"> 388<span style="visibility:hidden">.00</span></td><a href="/en/bmw-sauber.aspx">BMW Sauber</a></td><td align="right"> 352<span style="visibility:hidden">.00</span></td><a href="/en/cooper.aspx"><span class="WorldChp" title="World Champion">Cooper</span></a></td><td align="right"> 342<span style="visibility:hidden">.00</span></td><a href="/en/alphatauri.aspx">AlphaTauri</a></td><td align="right"> 309<span style="visibility:hidden">.00</span></td><a href="/en/haas.aspx"><span class="CurConstructor">Haas</span></a></td><td align="right"> 307<span style="visibility:hidden">.00</span></td><a href="/en/jordan.aspx">Jordan</a></td><td align="right"> 291<span style="visibility:hidden">.00</span></td><a href="/en/toyota.aspx">Toyota</a></td><td align="right"> 278.50</td><a href="/en/racing-point.aspx">Racing Point</a></td><td align="right"> 268<span style="visibility:hidden">.00</span></td><a href="/en/bar.aspx">BAR</a></td><td align="right"> 227<span style="visibility:hidden">.00</span></td><a href="/en/alfa-romeo.aspx">Alfa Romeo</a></td><td align="right"> 199<span style="visibility:hidden">.00</span></td><a href="/en/march.aspx">March</a></td><td align="right"> 173.50</td><a href="/en/brawn-gp.aspx"><span class="WorldChp" title="World Champion">Brawn GP</span></a></td><td align="right"> 172<span style="visibility:hidden">.00</span></td><a href="/en/matra.aspx"><span class="WorldChp" title="World Champion">Matra</span></a></td><td align="right"> 163<span style="visibility:hidden">.00</span></td><a href="/en/honda.aspx">Honda</a></td><td align="right"> 154<span style="visibility:hidden">.00</span></td><a href="/en/arrows.aspx">Arrows</a></td><td align="right"> 142<span style="visibility:hidden">.00</span></td><a href="/en/wolf.aspx">Wolf</a></td><td align="right"> 79<span style="visibility:hidden">.00</span></td><a href="/en/shadow.aspx">Shadow</a></td><td align="right"> 67.50</td><a href="/en/vanwall.aspx"><span class="WorldChp" title="World Champion">Vanwall</span></a></td><td align="right"> 57<span style="visibility:hidden">.00</span></td><a href="/en/surtees.aspx">Surtees</a></td><td align="right"> 53<span style="visibility:hidden">.00</span></td><a href="/en/jaguar.aspx">Jaguar</a></td><td align="right"> 49<span style="visibility:hidden">.00</span></td><a href="/en/porsche.aspx">Porsche</a></td><td align="right"> 48<span style="visibility:hidden">.00</span></td><a href="/en/hesketh.aspx">Hesketh</a></td><td align="right"> 48<span style="visibility:hidden">.00</span></td><a href="/en/stewart.aspx">Stewart</a></td><td align="right"> 47<span style="visibility:hidden">.00</span></td><a href="/en/rb.aspx">RB</a></td><td align="right"> 46<span style="visibility:hidden">.00</span></td><a href="/en/lola.aspx">Lola</a></td><td align="right"> 43<span style="visibility:hidden">.00</span></td><a href="/en/minardi.aspx">Minardi</a></td><td align="right"> 38<span style="visibility:hidden">.00</span></td><a href="/en/prost.aspx">Prost</a></td><td align="right"> 35<span style="visibility:hidden">.00</span></td><a href="/en/copersucar.aspx">Copersucar</a></td><td align="right"> 32<span style="visibility:hidden">.00</span></td><a href="/en/toleman.aspx">Toleman</a></td><td align="right"> 26<span style="visibility:hidden">.00</span></td><a href="/en/footwork.aspx">Footwork</a></td><td align="right"> 25<span style="visibility:hidden">.00</span></td><a href="/en/penske.aspx">Penske</a></td><td align="right"> 23<span style="visibility:hidden">.00</span></td><a href="/en/ensign.aspx">Ensign</a></td><td align="right"> 19<span style="visibility:hidden">.00</span></td><a href="/en/eagle.aspx">Eagle</a></td><td align="right"> 17<span style="visibility:hidden">.00</span></td><a href="/en/dallara.aspx">Dallara</a></td><td align="right"> 15<span style="visibility:hidden">.00</span></td>
                    """;
        List<String> lines = Arrays.stream(s.split("</td>")).toList();
            for(String line : lines) {
                System.out.println(line);
            }
        String name = "";
        String score;
        int driver = 0;
        for (int i = 0; i < lines.size()-1; i++) {
            String line = lines.get(i);
            if (i%2==0) { //name
                if (line.contains("title=\"World Champion\">")) {
                    name = line.substring(line.indexOf("title=\"World Champion\">") + "title=\"World Champion\">".length(), line.indexOf("</span>"));
                } else if(line.contains("class=\"CurConstructor\">")) {
                    name = line.substring(line.indexOf("class=\"CurConstructor\">") + "class=\"CurConstructor\">".length(), line.indexOf("</span>"));
                }
                else {
                    name = line.substring(line.indexOf(">") + 1, line.indexOf("<", line.indexOf(">")));
                }
            } else { //points
                if(line.contains("\"visibility:hidden\"")) {
                    score = line.substring(line.indexOf(">") + 1, line.indexOf("<", line.indexOf(">"))).replace(" ", "");
                } else {
                    score = line.substring(line.indexOf(">") + 1).replace(" ", "");
                }
                driver++;
                System.out.printf("INSERT INTO F1_TEAMS_POINTS (NAME, GP, IMAGE_URL, TIER, IMAGE_SOURCE_SHORT, IMAGE_SOURCE)" +
                                " VALUES ('%s', %s, '/images/f1_teams/%s.png', %d, '', '');\n"
                        , name, score, name.replace(" ", "_"), driver < 14 ? 1 : 2);
            }
        }
    }

}
