package com.dziem.popapi.formatter;

import java.util.Arrays;
import java.util.List;

public class CountriesGPInsertGenerator {
    public static void main(String[] args) {
        String s = """
                    <a href="/en/united-kingdom.aspx">United Kingdom</a></td><td align="right">1109</td><a href="/en/france.aspx">France</a></td><td align="right">989</td><a href="/en/germany.aspx">Germany</a></td><td align="right">906</td><a href="/en/italy.aspx">Italy</a></td><td align="right">845</td><a href="/en/brazil.aspx">Brazil</a></td><td align="right">793</td><a href="/en/australia.aspx">Australia</a></td><td align="right">747</td><a href="/en/finland.aspx">Finland</a></td><td align="right">744</td><a href="/en/spain.aspx">Spain</a></td><td align="right">557</td><a href="/en/austria.aspx">Austria</a></td><td align="right">554</td><a href="/en/usa.aspx">USA</a></td><td align="right">495</td><a href="/en/japan.aspx">Japan</a></td><td align="right">488</td><a href="/en/netherlands.aspx">Netherlands</a></td><td align="right">471</td><a href="/en/canada.aspx">Canada</a></td><td align="right">425</td><a href="/en/belgium.aspx">Belgium</a></td><td align="right">415</td><a href="/en/sweden.aspx">Sweden</a></td><td align="right">410</td><a href="/en/switzerland.aspx">Switzerland</a></td><td align="right">387</td><a href="/en/mexico.aspx">Mexico</a></td><td align="right">383</td><a href="/en/argentina.aspx">Argentina</a></td><td align="right">257</td><a href="/en/new-zealand.aspx">New Zealand</a></td><td align="right">220</td><a href="/en/denmark.aspx">Denmark</a></td><td align="right">217</td><a href="/en/russia.aspx">Russia</a></td><td align="right">209</td><a href="/en/monaco.aspx">Monaco</a></td><td align="right">171</td><a href="/en/south-africa.aspx">South Africa</a></td><td align="right">146</td><a href="/en/thailand.aspx">Thailand</a></td><td align="right">123</td><a href="/en/colombia.aspx">Colombia</a></td><td align="right">115</td><a href="/en/venezuela.aspx">Venezuela</a></td><td align="right">114</td><a href="/en/poland.aspx">Poland</a></td><td align="right">99</td><a href="/en/portugal.aspx">Portugal</a></td><td align="right">73</td><a href="/en/china.aspx">China</a></td><td align="right">68</td><a href="/en/irlande.aspx">Irlande</a></td><td align="right">65</td><a href="/en/india.aspx">India</a></td><td align="right">57</td><a href="/en/chile.aspx">Chile</a></td><td align="right">24</td><a href="/en/hungary.aspx">Hungary</a></td><td align="right">20</td><a href="/en/malaysia.aspx">Malaysia</a></td><td align="right">14</td><a href="/en/indonesia.aspx">Indonesia</a></td><td align="right">12</td><a href="/en/liechtenstein.aspx">Liechtenstein</a></td><td align="right">10</td><a href="/en/rhodesia.aspx">Rhodesia</a></td><td align="right">9</td><a href="/en/uruguay.aspx">Uruguay</a></td><td align="right">5</td><a href="/en/czech-republic.aspx">Czech Republic</a></td><td align="right">3</td>
                    """;
        List<String> lines = Arrays.stream(s.split("</td>")).toList();
            for(String line : lines) {
                System.out.println(line);
            }
        String name = "";
        String gp;
        int country = 0;
        for (int i = 0; i < lines.size()-1; i++) {
            String line = lines.get(i);
            if (i%2==0) { //name
                name = line.substring(line.indexOf(">")+1, line.indexOf("</a>"));
            } else { //gp
                gp = line.substring(line.indexOf(">") + 1);
                country++;
                System.out.printf("INSERT INTO COUNTRIES_GP (NAME, GP, IMAGE_URL, TIER, IMAGE_SOURCE_SHORT, IMAGE_SOURCE) VALUES ('%s', %s, '/images/%s.png', %d, '', '');\n"
                        , name, gp, name.replace(" ", "_"), country < 10 ? 1 : 2);
            }
        }
    }

}
