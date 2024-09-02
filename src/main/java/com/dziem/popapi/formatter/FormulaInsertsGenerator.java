package com.dziem.popapi.formatter;

import java.util.Arrays;
import java.util.List;

public class FormulaInsertsGenerator {
    public static void main(String[] args) {
        String s = """
               <a href="/en/lewis-hamilton.aspx"><span class="CurChpDriver" title="World Champion">HAMILTON Lewis > ></td><td align="right">4 803.50</td><td align="right">13.80</td>
               <a href="/en/sebastian-vettel.aspx"><span class="WorldChp" title="World Champion">VETTEL Sebastian > ></td><td align="right">3 098 >.00 ></td><td align="right">10.36</td>
               <a href="/en/max-verstappen.aspx"><span class="CurChpDriver" title="World Champion">VERSTAPPEN Max > ></td><td align="right">2 889.50</td><td align="right">14.38</td>
               <a href="/en/fernando-alonso.aspx"><span class="CurChpDriver" title="World Champion">ALONSO Fernando > ></td><td align="right">2 317 >.00 ></td><td align="right">5.88</td>
               <a href="/en/kimi-raikkonen.aspx"><span class="WorldChp" title="World Champion">RAIKKONEN Kimi > ></td><td align="right">1 873 >.00 ></td><td align="right">5.35</td>
               <a href="/en/valtteri-bottas.aspx"><span class="CurDriver">BOTTAS Valtteri > ></td><td align="right">1 797 >.00 ></td><td align="right">7.55</td>
               <a href="/en/sergio-perez.aspx"><span class="CurDriver">PEREZ Sergio > ></td><td align="right">1 629 >.00 ></td><td align="right">5.97</td>
               <a href="/en/nico-rosberg.aspx"><span class="WorldChp" title="World Champion">ROSBERG Nico > ></td><td align="right">1 594.50</td><td align="right">7.74</td>
               <a href="/en/michael-schumacher.aspx"><span class="WorldChp" title="World Champion">SCHUMACHER Michael > ></td><td align="right">1 566 >.00 ></td><td align="right">5.10</td>
               <a href="/en/daniel-ricciardo.aspx"><span class="CurDriver">RICCIARDO Daniel > ></td><td align="right">1 329 >.00 ></td><td align="right">5.21</td>
               <a href="/en/charles-leclerc.aspx"><span class="CurDriver">LECLERC Charles > ></td><td align="right">1 291 >.00 ></td><td align="right">9.29</td>
               <a href="/en/jenson-button.aspx"><span class="WorldChp" title="World Champion">BUTTON Jenson > ></td><td align="right">1 235 >.00 ></td><td align="right">4.04</td>
               <a href="/en/felipe-massa.aspx">MASSA Felipe ></td><td align="right">1 167 >.00 ></td><td align="right">4.34</td>
               <a href="/en/carlos-sainz.aspx"><span class="CurDriver">SAINZ Carlos > ></td><td align="right">1 166.50</td><td align="right">5.89</td>
               <a href="/en/mark-webber.aspx">WEBBER Mark ></td><td align="right">1 047.50</td><td align="right">4.87</td>
               <a href="/en/lando-norris.aspx"><span class="CurDriver">NORRIS Lando > ></td><td align="right"> 874 >.00 ></td><td align="right">7.28</td>
               <a href="/en/alain-prost.aspx"><span class="WorldChp" title="World Champion">PROST Alain > ></td><td align="right"> 798.50</td><td align="right">4.01</td>
               <a href="/en/rubens-barrichello.aspx">BARRICHELLO Rubens ></td><td align="right"> 658 >.00 ></td><td align="right">2.04</td>
               <a href="/en/ayrton-senna.aspx"><span class="WorldChp" title="World Champion">SENNA Ayrton > ></td><td align="right"> 614 >.00 ></td><td align="right">3.81</td>
               <a href="/en/george-russell.aspx"><span class="CurDriver">RUSSELL George > ></td><td align="right"> 597 >.00 ></td><td align="right">4.98</td>
               <a href="/en/nico-hulkenberg.aspx"><span class="CurDriver">HULKENBERG Nico > ></td><td align="right"> 552 >.00 ></td><td align="right">2.52</td>
               <a href="/en/david-coulthard.aspx">COULTHARD David ></td><td align="right"> 535 >.00 ></td><td align="right">2.17</td>
               <a href="/en/nelson-piquet.aspx"><span class="WorldChp" title="World Champion">PIQUET Nelson > ></td><td align="right"> 485.50</td><td align="right">2.38</td>
               <a href="/en/nigel-mansell.aspx"><span class="WorldChp" title="World Champion">MANSELL Nigel > ></td><td align="right"> 482 >.00 ></td><td align="right">2.58</td>
               <a href="/en/esteban-ocon.aspx"><span class="CurDriver">OCON Esteban > ></td><td align="right"> 427 >.00 ></td><td align="right">2.87</td>
               <a href="/en/niki-lauda.aspx"><span class="WorldChp" title="World Champion">LAUDA Niki > ></td><td align="right"> 420.50</td><td align="right">2.46</td>
               <a href="/en/mika-hakkinen.aspx"><span class="WorldChp" title="World Champion">HAKKINEN Mika > ></td><td align="right"> 420 >.00 ></td><td align="right">2.61</td>
               <a href="/en/pierre-gasly.aspx"><span class="CurDriver">GASLY Pierre > ></td><td align="right"> 402 >.00 ></td><td align="right">2.77</td>
               <a href="/en/romain-grosjean.aspx">GROSJEAN Romain ></td><td align="right"> 391 >.00 ></td><td align="right">2.18</td>
               <a href="/en/gerhard-berger.aspx">BERGER Gerhard ></td><td align="right"> 385 >.00 ></td><td align="right">1.83</td>
               <a href="/en/jackie-stewart.aspx"><span class="WorldChp" title="World Champion">STEWART Jackie > ></td><td align="right"> 360 >.00 ></td><td align="right">3.64</td>
               <a href="/en/damon-hill.aspx"><span class="WorldChp" title="World Champion">HILL Damon > ></td><td align="right"> 360 >.00 ></td><td align="right">3.13</td>
               <a href="/en/ralf-schumacher.aspx">SCHUMACHER Ralf ></td><td align="right"> 329 >.00 ></td><td align="right">1.83</td>
               <a href="/en/carlos-reutemann.aspx">REUTEMANN Carlos ></td><td align="right"> 310 >.00 ></td><td align="right">2.12</td>
               <a href="/en/juan-pablo-montoya.aspx">MONTOYA Juan-Pablo ></td><td align="right"> 307 >.00 ></td><td align="right">3.27</td>
               <a href="/en/oscar-piastri.aspx"><span class="CurDriver">PIASTRI Oscar > ></td><td align="right"> 294 >.00 ></td><td align="right">7.74</td>
               <a href="/en/lance-stroll.aspx"><span class="CurDriver">STROLL Lance > ></td><td align="right"> 292 >.00 ></td><td align="right">1.84</td>
               <a href="/en/graham-hill.aspx"><span class="WorldChp" title="World Champion">HILL Graham > ></td><td align="right"> 289 >.00 ></td><td align="right">1.65</td>
               <a href="/en/emerson-fittipaldi.aspx"><span class="WorldChp" title="World Champion">FITTIPALDI Emerson > ></td><td align="right"> 281 >.00 ></td><td align="right">1.95</td>
               <a href="/en/riccardo-patrese.aspx">PATRESE Riccardo ></td><td align="right"> 281 >.00 ></td><td align="right">1.10</td>
               <a href="/en/juan-manuel-fangio.aspx"><span class="WorldChp" title="World Champion">FANGIO Juan Manuel > ></td><td align="right"> 277.64</td><td align="right">5.44</td>
               <a href="/en/giancarlo-fisichella.aspx">FISICHELLA Giancarlo ></td><td align="right"> 275 >.00 ></td><td align="right">1.20</td>
               <a href="/en/jim-clark.aspx"><span class="WorldChp" title="World Champion">CLARK Jim > ></td><td align="right"> 274 >.00 ></td><td align="right">3.81</td>
               <a href="/en/robert-kubica.aspx">KUBICA Robert ></td><td align="right"> 274 >.00 ></td><td align="right">2.77</td>
               <a href="/en/jack-brabham.aspx"><span class="WorldChp" title="World Champion">BRABHAM Jack > ></td><td align="right"> 261 >.00 ></td><td align="right">2.12</td>
               <a href="/en/nick-heidfeld.aspx">HEIDFELD Nick ></td><td align="right"> 259 >.00 ></td><td align="right">1.42</td>
               <a href="/en/jody-scheckter.aspx"><span class="WorldChp" title="World Champion">SCHECKTER Jody > ></td><td align="right"> 255 >.00 ></td><td align="right">2.28</td>
               <a href="/en/denny-hulme.aspx"><span class="WorldChp" title="World Champion">HULME Denny > ></td><td align="right"> 248 >.00 ></td><td align="right">2.21</td>
               <a href="/en/jarno-trulli.aspx">TRULLI Jarno ></td><td align="right"> 246.50</td><td align="right">0.98</td>
               <a href="/en/jean-alesi.aspx">ALESI Jean ></td><td align="right"> 241 >.00 ></td><td align="right">1.20</td>
               <a href="/en/jacques-villeneuve.aspx"><span class="WorldChp" title="World Champion">VILLENEUVE Jacques > ></td><td align="right"> 235 >.00 ></td><td align="right">1.44</td>
               <a href="/en/alexander-albon.aspx"><span class="CurDriver">ALBON Alexander > ></td><td align="right"> 234 >.00 ></td><td align="right">2.41</td>
               <a href="/en/jacques-laffite.aspx">LAFFITE Jacques ></td><td align="right"> 228 >.00 ></td><td align="right">1.30</td>
               <a href="/en/clay-regazzoni.aspx">REGAZZONI Clay ></td><td align="right"> 212 >.00 ></td><td align="right">1.61</td>
               <a href="/en/ronnie-peterson.aspx">PETERSON Ronnie ></td><td align="right"> 206 >.00 ></td><td align="right">1.67</td>
               <a href="/en/alan-jones.aspx"><span class="WorldChp" title="World Champion">JONES Alan > ></td><td align="right"> 206 >.00 ></td><td align="right">1.78</td>
               <a href="/en/daniil-kvyat.aspx">KVYAT Daniil ></td><td align="right"> 202 >.00 ></td><td align="right">1.84</td>
               <a href="/en/bruce-mclaren.aspx">McLAREN Bruce ></td><td align="right"> 196.50</td><td align="right">2.01</td>
               <a href="/en/kevin-magnussen.aspx"><span class="CurDriver">MAGNUSSEN Kevin > ></td><td align="right"> 192 >.00 ></td><td align="right">1.07</td>
               <a href="/en/eddie-irvine.aspx">IRVINE Eddie ></td><td align="right"> 191 >.00 ></td><td align="right">1.31</td>
               <a href="/en/stirling-moss.aspx">MOSS Stirling ></td><td align="right"> 186.64</td><td align="right">2.83</td>
               <a href="/en/michele-alboreto.aspx">ALBORETO Michele ></td><td align="right"> 186.50</td><td align="right">0.96</td>
               <a href="/en/jacky-ickx.aspx">ICKX Jacky ></td><td align="right"> 181 >.00 ></td><td align="right">1.59</td>
               <a href="/en/rene-arnoux.aspx">ARNOUX René ></td><td align="right"> 181 >.00 ></td><td align="right">1.21</td>
               <a href="/en/john-surtees.aspx"><span class="WorldChp" title="World Champion">SURTEES John > ></td><td align="right"> 180 >.00 ></td><td align="right">1.62</td>
               <a href="/en/mario-andretti.aspx"><span class="WorldChp" title="World Champion">ANDRETTI Mario > ></td><td align="right"> 180 >.00 ></td><td align="right">1.41</td>
               <a href="/en/james-hunt.aspx"><span class="WorldChp" title="World Champion">HUNT James > ></td><td align="right"> 179 >.00 ></td><td align="right">1.95</td>
               <a href="/en/heinz-harald-frentzen.aspx">FRENTZEN Heinz-Harald ></td><td align="right"> 174 >.00 ></td><td align="right">1.12</td>
               <a href="/en/john-watson.aspx">WATSON John ></td><td align="right"> 169 >.00 ></td><td align="right">1.11</td>
               <a href="/en/keke-rosberg.aspx"><span class="WorldChp" title="World Champion">ROSBERG Keke > ></td><td align="right"> 159.50</td><td align="right">1.40</td>
               <a href="/en/patrick-depailler.aspx">DEPAILLER Patrick ></td><td align="right"> 141 >.00 ></td><td align="right">1.48</td>
               <a href="/en/alberto-ascari.aspx"><span class="WorldChp" title="World Champion">ASCARI Alberto > ></td><td align="right"> 140.14</td><td align="right">4.38</td>
               <a href="/en/dan-gurney.aspx">GURNEY Dan ></td><td align="right"> 133 >.00 ></td><td align="right">1.55</td>
               <a href="/en/thierry-boutsen.aspx">BOUTSEN Thierry ></td><td align="right"> 132 >.00 ></td><td align="right">0.81</td>
               <a href="/en/mike-hawthorn.aspx"><span class="WorldChp" title="World Champion">HAWTHORN Mike > ></td><td align="right"> 127.64</td><td align="right">2.84</td>
               <a href="/en/giuseppe-farina.aspx"><span class="WorldChp" title="World Champion">FARINA Giuseppe > ></td><td align="right"> 127.33</td><td align="right">3.86</td>
               <a href="/en/kamui-kobayashi.aspx">KOBAYASHI Kamui ></td><td align="right"> 125 >.00 ></td><td align="right">1.67</td>
               <a href="/en/adrian-sutil.aspx">SUTIL Adrian ></td><td align="right"> 124 >.00 ></td><td align="right">0.97</td>
               <a href="/en/elio-de-angelis.aspx">De ANGELIS Elio ></td><td align="right"> 122 >.00 ></td><td align="right">1.13</td>
               <a href="/en/paul-di-resta.aspx">di RESTA Paul ></td><td align="right"> 121 >.00 ></td><td align="right">2.05</td>
               <a href="/en/jochen-rindt.aspx"><span class="WorldChp" title="World Champion">RINDT Jochen > ></td><td align="right"> 109 >.00 ></td><td align="right">1.82</td>
               <a href="/en/richie-ginther.aspx">GINTHER Richie ></td><td align="right"> 107 >.00 ></td><td align="right">2.06</td>
               <a href="/en/gilles-villeneuve.aspx">VILLENEUVE Gilles ></td><td align="right"> 107 >.00 ></td><td align="right">1.60</td>
               <a href="/en/heikki-kovalainen.aspx">KOVALAINEN Heikki ></td><td align="right"> 105 >.00 ></td><td align="right">0.95</td>
               <a href="/en/patrick-tambay.aspx">TAMBAY Patrick ></td><td align="right"> 103 >.00 ></td><td align="right">0.90</td>
               <a href="/en/didier-pironi.aspx">PIRONI Didier ></td><td align="right"> 101 >.00 ></td><td align="right">1.44</td>
               <a href="/en/phil-hill.aspx"><span class="WorldChp" title="World Champion">HILL Phil > ></td><td align="right"> 98 >.00 ></td><td align="right">2.09</td>
               <a href="/en/martin-brundle.aspx">BRUNDLE Martin ></td><td align="right"> 98 >.00 ></td><td align="right">0.62</td>
               <a href="/en/johnny-herbert.aspx">HERBERT Johnny ></td><td align="right"> 98 >.00 ></td><td align="right">0.61</td>
               <a href="/en/francois-cevert.aspx">CEVERT François ></td><td align="right"> 89 >.00 ></td><td align="right">1.93</td>
               <a href="/en/stefan-johansson.aspx">JOHANSSON Stefan ></td><td align="right"> 88 >.00 ></td><td align="right">1.11</td>
               <a href="/en/chris-amon.aspx">AMON Chris ></td><td align="right"> 83 >.00 ></td><td align="right">0.86</td>
               <a href="/en/yuki-tsunoda.aspx"><span class="CurDriver">TSUNODA Yuki > ></td><td align="right"> 83 >.00 ></td><td align="right">1.05</td>
               <a href="/en/jose-froilan-gonzalez.aspx">GONZALEZ Jose-Froilan ></td><td align="right"> 77.64</td><td align="right">2.99</td>
               <a href="/en/jean-pierre-beltoise.aspx">BELTOISE Jean-Pierre ></td><td align="right"> 77 >.00 ></td><td align="right">0.91</td>
               <a href="/en/olivier-panis.aspx">PANIS Olivier ></td><td align="right"> 76 >.00 ></td><td align="right">0.48</td>
               <a href="/en/pastor-maldonado.aspx">MALDONADO Pastor ></td><td align="right"> 76 >.00 ></td><td align="right">0.80</td>
               <a href="/en/tony-brooks.aspx">BROOKS Tony ></td><td align="right"> 75 >.00 ></td><td align="right">1.97</td>
               <a href="/en/maurice-trintignant.aspx">TRINTIGNANT Maurice ></td><td align="right"> 72.33</td><td align="right">0.89</td>
               <a href="/en/pedro-rodriguez.aspx">RODRIGUEZ Pedro ></td><td align="right"> 71 >.00 ></td><td align="right">1.31</td>
                """;
            List<String> lines = Arrays.stream(s.split(">")).filter(line -> !line.contains("<a") && !line.startsWith("<") && !(line.length() <= 1)).toList();
            String name = "";
            String score = "";
            int counter = 0;
            for(int i = 0;  i < lines.size(); i++) {
                if(counter%3==0) {
                    if(!lines.get(i).contains(".")) {
                        String[] line = lines.get(i).split(" ");
                        if(line.length==2) {
                            name = line[0].charAt(0) + line[0].substring(1).toLowerCase() + " " + line[1];
                        } else {
                            name = line[0] + " " + line[1].charAt(0) + line[1].substring(1).toLowerCase() + " " + line[2];
                        }
                        counter++;
                    }
                }else if(counter%3==1) {
                    String[] line = lines.get(i).split("<");
                    String scoreWord = line[0].replace(" ", "").replace(",", ".");
                    score = scoreWord.contains(".")? scoreWord.substring(0, scoreWord.indexOf('.')+2) : scoreWord + ".0";

                    counter++;
                }else if(counter%3==2) {
                    System.out.printf("INSERT INTO DRIVER (NAME, SCORE, IMAGE_URL) VALUES ('%s', %s, '/images/f1/%s.png');\n", name, score, name.replace(" ", "_"));
                    counter++;
                }
            }
    }
}
