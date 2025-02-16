package com.dziem.popapi.formatter;

import java.util.Arrays;
import java.util.List;

public class DriversGPInsertGenerator {
        public static void main(String[] args) {
            String s = """
                    <a href="/en/fernando-alonso.aspx"><span class="CurChpDriver" title="World Champion">ALONSO Fernando</span></a></td><a href="/en/fernando-alonso/grand-prix.aspx">401</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/lewis-hamilton.aspx"><span class="CurChpDriver" title="World Champion">HAMILTON Lewis</span></a></td><a href="/en/lewis-hamilton/grand-prix.aspx">356</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/kimi-raikkonen.aspx"><span class="WorldChp" title="World Champion">RAIKKONEN Kimi</span></a></td><a href="/en/kimi-raikkonen/grand-prix.aspx">349</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/rubens-barrichello.aspx">BARRICHELLO Rubens</a></td><a href="/en/rubens-barrichello/grand-prix.aspx">323</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/michael-schumacher.aspx"><span class="WorldChp" title="World Champion">SCHUMACHER Michael</span></a></td><a href="/en/michael-schumacher/grand-prix.aspx">307</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/jenson-button.aspx"><span class="WorldChp" title="World Champion">BUTTON Jenson</span></a></td><a href="/en/jenson-button/grand-prix.aspx">306</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/sebastian-vettel.aspx"><span class="WorldChp" title="World Champion">VETTEL Sebastian</span></a></td><a href="/en/sebastian-vettel/grand-prix.aspx">299</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/sergio-perez.aspx">PEREZ Sergio</a></td><a href="/en/sergio-perez/grand-prix.aspx">281</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/felipe-massa.aspx">MASSA Felipe</a></td><a href="/en/felipe-massa/grand-prix.aspx">269</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/daniel-ricciardo.aspx">RICCIARDO Daniel</a></td><a href="/en/daniel-ricciardo/grand-prix.aspx">257</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/riccardo-patrese.aspx">PATRESE Riccardo</a></td><a href="/en/riccardo-patrese/grand-prix.aspx">256</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/jarno-trulli.aspx">TRULLI Jarno</a></td><a href="/en/jarno-trulli/grand-prix.aspx">252</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/david-coulthard.aspx">COULTHARD David</a></td><a href="/en/david-coulthard/grand-prix.aspx">246</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/valtteri-bottas.aspx">BOTTAS Valtteri</a></td><a href="/en/valtteri-bottas/grand-prix.aspx">246</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/giancarlo-fisichella.aspx">FISICHELLA Giancarlo</a></td><a href="/en/giancarlo-fisichella/grand-prix.aspx">229</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/nico-hulkenberg.aspx"><span class="CurDriver">HULKENBERG Nico</span></a></td><a href="/en/nico-hulkenberg/grand-prix.aspx">227</a></td><strong>1</strong></td><td align="right" sorttable_customkey="4">4</td><a href="/en/mark-webber.aspx">WEBBER Mark</a></td><a href="/en/mark-webber/grand-prix.aspx">215</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/gerhard-berger.aspx">BERGER Gerhard</a></td><a href="/en/gerhard-berger/grand-prix.aspx">210</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/max-verstappen.aspx"><span class="CurChpDriver" title="World Champion">VERSTAPPEN Max</span></a></td><a href="/en/max-verstappen/grand-prix.aspx">209</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/andrea-de-cesaris.aspx">De CESARIS Andrea</a></td><a href="/en/andrea-de-cesaris/grand-prix.aspx">208</a></td><strong>1</strong></td><td align="right" sorttable_customkey="2">2</td><a href="/en/nico-rosberg.aspx"><span class="WorldChp" title="World Champion">ROSBERG Nico</span></a></td><a href="/en/nico-rosberg/grand-prix.aspx">206</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/carlos-sainz.aspx"><span class="CurDriver">SAINZ Carlos</span></a></td><a href="/en/carlos-sainz/grand-prix.aspx">206</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/nelson-piquet.aspx"><span class="WorldChp" title="World Champion">PIQUET Nelson</span></a></td><a href="/en/nelson-piquet/grand-prix.aspx">204</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/jean-alesi.aspx">ALESI Jean</a></td><a href="/en/jean-alesi/grand-prix.aspx">201</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/alain-prost.aspx"><span class="WorldChp" title="World Champion">PROST Alain</span></a></td><a href="/en/alain-prost/grand-prix.aspx">199</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/michele-alboreto.aspx">ALBORETO Michele</a></td><a href="/en/michele-alboreto/grand-prix.aspx">194</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/nigel-mansell.aspx"><span class="WorldChp" title="World Champion">MANSELL Nigel</span></a></td><a href="/en/nigel-mansell/grand-prix.aspx">187</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/kevin-magnussen.aspx">MAGNUSSEN Kevin</a></td><a href="/en/kevin-magnussen/grand-prix.aspx">185</a></td>4</td><td align="right" sorttable_customkey="2">2</td><a href="/en/nick-heidfeld.aspx">HEIDFELD Nick</a></td><a href="/en/nick-heidfeld/grand-prix.aspx">183</a></td><strong>1</strong></td><td align="right" sorttable_customkey="2">2</td><a href="/en/ralf-schumacher.aspx">SCHUMACHER Ralf</a></td><a href="/en/ralf-schumacher/grand-prix.aspx">180</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/romain-grosjean.aspx">GROSJEAN Romain</a></td><a href="/en/romain-grosjean/grand-prix.aspx">179</a></td>2</td><td align="right" sorttable_customkey="2">2</td><a href="/en/jacques-laffite.aspx">LAFFITE Jacques</a></td><a href="/en/jacques-laffite/grand-prix.aspx">176</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/graham-hill.aspx"><span class="WorldChp" title="World Champion">HILL Graham</span></a></td><a href="/en/graham-hill/grand-prix.aspx">175</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/niki-lauda.aspx"><span class="WorldChp" title="World Champion">LAUDA Niki</span></a></td><a href="/en/niki-lauda/grand-prix.aspx">171</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/lance-stroll.aspx"><span class="CurDriver">STROLL Lance</span></a></td><a href="/en/lance-stroll/grand-prix.aspx">166</a></td><strong>1</strong></td><td align="right" sorttable_customkey="3">3</td><a href="/en/thierry-boutsen.aspx">BOUTSEN Thierry</a></td><a href="/en/thierry-boutsen/grand-prix.aspx">163</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/jacques-villeneuve.aspx"><span class="WorldChp" title="World Champion">VILLENEUVE Jacques</span></a></td><a href="/en/jacques-villeneuve/grand-prix.aspx">163</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/ayrton-senna.aspx"><span class="WorldChp" title="World Champion">SENNA Ayrton</span></a></td><a href="/en/ayrton-senna/grand-prix.aspx">161</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/johnny-herbert.aspx">HERBERT Johnny</a></td><a href="/en/johnny-herbert/grand-prix.aspx">161</a></td>4</td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/mika-hakkinen.aspx"><span class="WorldChp" title="World Champion">HAKKINEN Mika</span></a></td><a href="/en/mika-hakkinen/grand-prix.aspx">161</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/martin-brundle.aspx">BRUNDLE Martin</a></td><a href="/en/martin-brundle/grand-prix.aspx">158</a></td>3</td><td align="right" sorttable_customkey="2">2</td><a href="/en/olivier-panis.aspx">PANIS Olivier</a></td><a href="/en/olivier-panis/grand-prix.aspx">158</a></td>3</td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/heinz-harald-frentzen.aspx">FRENTZEN Heinz-Harald</a></td><a href="/en/heinz-harald-frentzen/grand-prix.aspx">156</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/esteban-ocon.aspx"><span class="CurDriver">OCON Esteban</span></a></td><a href="/en/esteban-ocon/grand-prix.aspx">156</a></td>3</td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/pierre-gasly.aspx"><span class="CurDriver">GASLY Pierre</span></a></td><a href="/en/pierre-gasly/grand-prix.aspx">153</a></td>2</td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/john-watson.aspx">WATSON John</a></td><a href="/en/john-watson/grand-prix.aspx">152</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/rene-arnoux.aspx">ARNOUX René</a></td><a href="/en/rene-arnoux/grand-prix.aspx">149</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/charles-leclerc.aspx"><span class="CurDriver">LECLERC Charles</span></a></td><a href="/en/charles-leclerc/grand-prix.aspx">147</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/carlos-reutemann.aspx">REUTEMANN Carlos</a></td><a href="/en/carlos-reutemann/grand-prix.aspx">146</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/derek-warwick.aspx">WARWICK Derek</a></td><a href="/en/derek-warwick/grand-prix.aspx">146</a></td>3</td><td align="right" sorttable_customkey="2">2</td><a href="/en/eddie-irvine.aspx">IRVINE Eddie</a></td><a href="/en/eddie-irvine/grand-prix.aspx">145</a></td>2</td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/emerson-fittipaldi.aspx"><span class="WorldChp" title="World Champion">FITTIPALDI Emerson</span></a></td><a href="/en/emerson-fittipaldi/grand-prix.aspx">144</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/jean-pierre-jarier.aspx">JARIER Jean-Pierre</a></td><a href="/en/jean-pierre-jarier/grand-prix.aspx">134</a></td><strong>1</strong></td><td align="right" sorttable_customkey="3">3</td><a href="/en/clay-regazzoni.aspx">REGAZZONI Clay</a></td><a href="/en/clay-regazzoni/grand-prix.aspx">132</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/eddie-cheever.aspx">CHEEVER Eddie</a></td><a href="/en/eddie-cheever/grand-prix.aspx">132</a></td>2</td><td align="right" sorttable_customkey="2">2</td><a href="/en/mario-andretti.aspx"><span class="WorldChp" title="World Champion">ANDRETTI Mario</span></a></td><a href="/en/mario-andretti/grand-prix.aspx">128</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/adrian-sutil.aspx">SUTIL Adrian</a></td><a href="/en/adrian-sutil/grand-prix.aspx">128</a></td>2</td><td align="right" sorttable_customkey="4">4</td><a href="/en/george-russell.aspx"><span class="CurDriver">RUSSELL George</span></a></td><a href="/en/george-russell/grand-prix.aspx">128</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/lando-norris.aspx"><span class="CurDriver">NORRIS Lando</span></a></td><a href="/en/lando-norris/grand-prix.aspx">128</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/jack-brabham.aspx"><span class="WorldChp" title="World Champion">BRABHAM Jack</span></a></td><a href="/en/jack-brabham/grand-prix.aspx">123</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/ronnie-peterson.aspx">PETERSON Ronnie</a></td><a href="/en/ronnie-peterson/grand-prix.aspx">123</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/pierluigi-martini.aspx">MARTINI Pierluigi</a></td><a href="/en/pierluigi-martini/grand-prix.aspx">118</a></td>2</td><td align="right" sorttable_customkey="4">4</td><a href="/en/alan-jones.aspx"><span class="WorldChp" title="World Champion">JONES Alan</span></a></td><a href="/en/alan-jones/grand-prix.aspx">116</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/damon-hill.aspx"><span class="WorldChp" title="World Champion">HILL Damon</span></a></td><a href="/en/damon-hill/grand-prix.aspx">115</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/jacky-ickx.aspx">ICKX Jacky</a></td><a href="/en/jacky-ickx/grand-prix.aspx">114</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/keke-rosberg.aspx"><span class="WorldChp" title="World Champion">ROSBERG Keke</span></a></td><a href="/en/keke-rosberg/grand-prix.aspx">114</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/patrick-tambay.aspx">TAMBAY Patrick</a></td><a href="/en/patrick-tambay/grand-prix.aspx">114</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/denny-hulme.aspx"><span class="WorldChp" title="World Champion">HULME Denny</span></a></td><a href="/en/denny-hulme/grand-prix.aspx">112</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/jody-scheckter.aspx"><span class="WorldChp" title="World Champion">SCHECKTER Jody</span></a></td><a href="/en/jody-scheckter/grand-prix.aspx">112</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/john-surtees.aspx"><span class="WorldChp" title="World Champion">SURTEES John</span></a></td><a href="/en/john-surtees/grand-prix.aspx">111</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/heikki-kovalainen.aspx">KOVALAINEN Heikki</a></td><a href="/en/heikki-kovalainen/grand-prix.aspx">111</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/mika-salo.aspx">SALO Mika</a></td><a href="/en/mika-salo/grand-prix.aspx">110</a></td>4</td><td align="right" sorttable_customkey="2">2</td><a href="/en/daniil-kvyat.aspx">KVYAT Daniil</a></td><a href="/en/daniil-kvyat/grand-prix.aspx">110</a></td>4</td><td align="right" sorttable_customkey="2">2</td><a href="/en/philippe-alliot.aspx">ALLIOT Philippe</a></td><a href="/en/philippe-alliot/grand-prix.aspx">109</a></td>5</td><td align="right" sorttable_customkey="5">5</td><a href="/en/elio-de-angelis.aspx">De ANGELIS Elio</a></td><a href="/en/elio-de-angelis/grand-prix.aspx">108</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/jos-verstappen.aspx">VERSTAPPEN Jos</a></td><a href="/en/jos-verstappen/grand-prix.aspx">107</a></td>6</td><td align="right" sorttable_customkey="3">3</td><a href="/en/jochen-mass.aspx">MASS Jochen</a></td><a href="/en/jochen-mass/grand-prix.aspx">105</a></td>4</td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/pedro-de-la-rosa.aspx">de la ROSA Pedro</a></td><a href="/en/pedro-de-la-rosa/grand-prix.aspx">105</a></td>4</td><td align="right" sorttable_customkey="2">2</td><a href="/en/jo-bonnier.aspx">BONNIER Jo</a></td><a href="/en/jo-bonnier/grand-prix.aspx">104</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/alexander-albon.aspx"><span class="CurDriver">ALBON Alexander</span></a></td><a href="/en/alexander-albon/grand-prix.aspx">104</a></td>4</td><td align="right" sorttable_customkey="3">3</td><a href="/en/jackie-stewart.aspx"><span class="WorldChp" title="World Champion">STEWART Jackie</span></a></td><a href="/en/jackie-stewart/grand-prix.aspx">99</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/robert-kubica.aspx">KUBICA Robert</a></td><a href="/en/robert-kubica/grand-prix.aspx">99</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/bruce-mclaren.aspx">McLAREN Bruce</a></td><a href="/en/bruce-mclaren/grand-prix.aspx">98</a></td>2</td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/pedro-diniz.aspx">DINIZ Pedro</a></td><a href="/en/pedro-diniz/grand-prix.aspx">98</a></td>8</td><td align="right" sorttable_customkey="5">5</td><a href="/en/marcus-ericsson.aspx">ERICSSON Marcus</a></td><a href="/en/marcus-ericsson/grand-prix.aspx">97</a></td>6</td><td align="right" sorttable_customkey="8">8</td><a href="/en/jo-siffert.aspx">SIFFERT Jo</a></td><a href="/en/jo-siffert/grand-prix.aspx">96</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/chris-amon.aspx">AMON Chris</a></td><a href="/en/chris-amon/grand-prix.aspx">96</a></td><strong>1</strong></td><td align="right" sorttable_customkey="2">2</td><a href="/en/patrick-depailler.aspx">DEPAILLER Patrick</a></td><a href="/en/patrick-depailler/grand-prix.aspx">95</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/ukyo-katayama.aspx">KATAYAMA Ukyo</a></td><a href="/en/ukyo-katayama/grand-prix.aspx">95</a></td>5</td><td align="right" sorttable_customkey="5">5</td><a href="/en/pastor-maldonado.aspx">MALDONADO Pastor</a></td><a href="/en/pastor-maldonado/grand-prix.aspx">95</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/juan-pablo-montoya.aspx">MONTOYA Juan-Pablo</a></td><a href="/en/juan-pablo-montoya/grand-prix.aspx">94</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/ivan-capelli.aspx">CAPELLI Ivan</a></td><a href="/en/ivan-capelli/grand-prix.aspx">93</a></td>3</td><td align="right" sorttable_customkey="2">2</td><a href="/en/james-hunt.aspx"><span class="WorldChp" title="World Champion">HUNT James</span></a></td><a href="/en/james-hunt/grand-prix.aspx">92</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/timo-glock.aspx">GLOCK Timo</a></td><a href="/en/timo-glock/grand-prix.aspx">91</a></td>2</td><td align="right" sorttable_customkey="2">2</td><a href="/en/takuma-sato.aspx">SATO Takuma</a></td><a href="/en/takuma-sato/grand-prix.aspx">90</a></td>2</td><td align="right" sorttable_customkey="3">3</td><a href="/en/yuki-tsunoda.aspx"><span class="CurDriver">TSUNODA Yuki</span></a></td><a href="/en/yuki-tsunoda/grand-prix.aspx">87</a></td>3</td><td align="right" sorttable_customkey="4">4</td><a href="/en/dan-gurney.aspx">GURNEY Dan</a></td><a href="/en/dan-gurney/grand-prix.aspx">86</a></td><strong>1</strong></td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/jean-pierre-beltoise.aspx">BELTOISE Jean-Pierre</a></td><a href="/en/jean-pierre-beltoise/grand-prix.aspx">85</a></td>2</td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td><a href="/en/jonathan-palmer.aspx">PALMER Jonathan</a></td><a href="/en/jonathan-palmer/grand-prix.aspx">83</a></td>9</td><td align="right" sorttable_customkey="4">4</td><a href="/en/maurice-trintignant.aspx">TRINTIGNANT Maurice</a></td><a href="/en/maurice-trintignant/grand-prix.aspx">81</a></td>3</td><td align="right" sorttable_customkey="1"><strong>1<!--<strong--></strong></td>
                    """;
            List<String> lines = Arrays.stream(s.split("</td>")).toList();
//            for(String line : lines) {
//                System.out.println(line);
//            }
            String name = "";
            String score = "";
            int driver = 0;
            List<String> hardCodedNames = List.of("von TRIPS", "De ANGELIS Elio", "De CESARIS Andrea", "de la ROSA Pedro");
            for (String line : lines) {
                if (line.contains(".aspx") && !line.contains("grand-prix.aspx")) { //name
                    String nameWord = "";
                    if (line.contains("von TRIPS")) {
                        name = "von Trips Wolfgang";
                    } else if(line.contains("De CESARIS Andrea")) {
                        name = "De Cesaris Andrea";
                    }else if(line.contains("De ANGELIS Elio")) {
                        name = "De Angelis Elio";
                    }else if(line.contains("de la ROSA Pedro")) {
                        name = "de la Rosa Pedro";
                    }
                    else if (line.contains("</span>") && line.contains("title=\"World Champion\">")) {
                        nameWord = line.substring(line.indexOf("title=\"World Champion\">") + "title=\"World Champion\">".length(), line.indexOf("</span>"));
                    } else if (line.contains("<span class=\"CurChpDriver\">")) {
                        nameWord = line.substring(line.indexOf("<span class=\"CurChpDriver\">") + "<span class=\"CurChpDriver\">".length(), line.indexOf("</span>"));
                    } else if (line.contains("<span class=\"CurDriver\">")) {
                        nameWord = line.substring(line.indexOf("<span class=\"CurDriver\">") + "<span class=\"CurDriver\">".length(), line.indexOf("</span>"));
                    }
                    else {
                        nameWord = line.substring(line.indexOf(">") + 1, line.indexOf("<", line.indexOf(">")));
                    }
                    String[] split = nameWord.split(" ");
                    boolean contains = false;
                    for(String hardcodedName : hardCodedNames) {
                        if(line.contains(hardcodedName)) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        name = split[0].charAt(0) + split[0].substring(1).toLowerCase() + " " + split[1];
                    }
                } else if (line.contains("grand-prix.aspx")) { //gp
                    String gp = line;
                    score = gp.substring(gp.indexOf(">") + 1, gp.indexOf("<", gp.indexOf(">")));
                } else if (line.contains("sorttable_customkey")) {
                    driver++;
                    System.out.printf("INSERT INTO DRIVERS_GP (NAME, GP, IMAGE_URL, TIER, IMAGE_SOURCE_SHORT, IMAGE_SOURCE) VALUES ('%s', %s, '/images/f1/%s.png', %d, '', '');\n"
                            , name, score, name.replace(" ", "_"), driver < 25 ? 1 : 2);
                }
            }
        }

    }
