# **Käyttöohje**

Lataa tiedosto [WeatherBot.jar](https://github.com/qubelka/ot-harjoitustyo/releases/download/loppupalautus/WeatherBot.jar)

## **Konfigurointi**

Kaikki ohjelman tarvitsemat konfiguraatiotiedostot (*bot.properties*, *database.properties* ja *logging.properties*) sekä lokitustasosta vastaava *logback.xml*
ovat pakattu jar-tiedostoon valmiiksi. 

## **Ohjelman käynnistäminen**

Ohjelma käynnistetään komennolla 

`java -jar WeatherBot.jar`

Sovellus käynnistyy info-näkymään:

![info](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko6/start.png)

Kun työpöytäsovellus on käynnistetty, botin voi avata telegrammissa (löytyy nimellä @HarjoitustyoWeatherbot):

![bot](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko6/open.png)

## **Toiminnot**

Sovelluksen päänäkymä sisältää neljä nappia ("help", "units", "my locations" ja "search by city name"). Navigointi tapahtuu painamalla nappeja tai antamalla botille syötteitä. Esimerkiksi napin "help" painallus vastaa syötettä */help* tai ilman kenoviivaa *help*. Oletusarvoisesti kaikki komennot (paitsi edellä mainitut neljä) tulkitaan säähakuina: 

![haku](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko6/weather.png)

Yllä olevassa kuvassa sääinfon viimeisin rivi on linkki png-tiedostoon. Oletusarvoisesti botti on suunniteltu käytettäväksi puhelimella, jolloin kuvalinkin sijaan näkyy säätilaa havainnollistava kuva. 

My locations -toiminnon avulla käyttäjä voi tallentaa haluamansa sijainnit sovellukseen, jolloin säätä voi hakea nopeammin: 

![add](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko6/my_location.png)

![add2](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko6/add_location.png)

Kun uusi sijainti on lisätty, sovellus ehdottaa sitä, kun käyttäjä valitsee komennon "my locations":

![added](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko6/locations_listed.png)

Units -toiminnon avulla voi määrittää, mitä lämpötilayksikköä käyttää:

![units](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko6/units_selection.png)

## **Sovelluksen sulkeminen**

Sovelluksen pääsee sulkemaan painamalla CTRL+Z terminaalissa tai jos sovellus on avattu NetBeansin kautta, kirjoittamalla komennon "close".

