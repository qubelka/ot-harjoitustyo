# **Käyttöohje**

Lataa tiedosto [WeatherBot.jar](https://github.com/qubelka/ot-harjoitustyo/releases/download/viikko6/WeatherBot.jar)

## **Konfigurointi**

Kaikki ohjelman tarvitsemat konfiguraatiotiedostot (*bot.properties* ja *database.properties*) sekä lokitustasosta vastaava *logback.xml*
ovat pakattu jar-tiedostoon valmiiksi. 

## **Ohjelman käynnistäminen**

Ohjelma käynnistetään komennolla 

`java -jar WeatherBot.jar`

Sovellus käynnistyy info-näkymään:

![info]()

Kun työpöytäsovellus on käynnistetty, botin voi avata telegrammissa (löytyy nimellä @HarjoitustyoWeatherbot):

![bot]()

## **Toiminnot**

Sovelluksen päänäkymä sisältää neljä nappia ("help", "units", "my locations" ja "search by city name"). Navigointi tapahtuu painamalla nappeja tai antamalla botille syötteitä. Esimerkiksi napin "help" painallus vastaa syötettä */help* tai ilman kenoviivaa *help*. Oletusarvoisesti kaikki komennot (paitsi edellä mainitut neljä) tulkitaan säähakuina: 

![haku]()

Yllä olevassa kuvassa sääinfon viimeisin rivi on linkki png-tiedostoon. Oletusarvoisesti botti on suunniteltu käytettäväksi puhelimella, jolloin kuvalinkin sijaan näkyy säätilaa havainnollistava kuva. 

My locations -toiminnon avulla käyttäjä voi tallentaa haluamansa sijainnit sovellukseen, jolloin säätä voi hakea nopeammin: 

![add]()

Kun uusi sijainti on lisätty, sovellus ehdottaa sitä, kun käyttäjä valitsee komennon "my locations":

![added]()

Units -toiminnon avulla voi määrittää, mitä lämpötilayksikköä käyttää:


## **Sovelluksen sulkeminen**

Sovelluksen pääsee sulkemaan painamalla CTRL+Z terminaalissa tai jos sovellus on avattu NetBeansin kautta, painamalla punaista ruutua. 

