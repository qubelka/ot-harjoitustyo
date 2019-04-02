# **Telegram weather-bot**

Sääbotin avulla käyttäjä voi hakea tämänhetkiset säätiedot yhdestä kaupungista ympäri maailmaa. 
Sovellus näyttää tämänhetkisen lämpötilan, kosteusprosentin, pilvisyyden ja säätilaa havainnollistavan kuvan.

[WeatherBot](WeatherBot)

### **Dokumentaatio**

[Vaatimusmäärittely](dokumentaatio/vaatimusmaarittely.md)

[Työaikakirjanpito](dokumentaatio/tuntikirjanpito.md)

### **Viikko3**

[Viikko 3](laskarit/viikko3)

### **Komentorivitoiminnot**

## **Ohjelman suoritus**

`mvn compile exec:java -Dexec.mainClass=tgbots.weatherbot.Main` 

## **Suoritettavan jarin generointi**

Komento

`mvn package`

generoi hakemistoon *target* kaksi jar-tiedostoa, joista suoritetaan se, jonka nimessä **ei** ole sanaa *original*. 
Eli tiedosto nimeltä WeatherBot-1.0-SNAPSHOT.jar 
