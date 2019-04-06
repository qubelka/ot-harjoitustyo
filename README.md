# **Telegram weather-bot**

Sääbotin avulla käyttäjä voi hakea tämänhetkiset säätiedot yhdestä kaupungista ympäri maailmaa. 
Sovellus näyttää tämänhetkisen lämpötilan, kosteusprosentin, pilvisyyden ja säätilaa havainnollistavan kuvan.

[WeatherBot](WeatherBot)

## **Dokumentaatio**

[Vaatimusmäärittely](dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus]

[Työaikakirjanpito](dokumentaatio/tuntikirjanpito.md)

## **Komentorivitoiminnot**

### **Ohjelman suoritus**

`mvn compile exec:java -Dexec.mainClass=domain.BotApp` 

### **Suoritettavan jarin generointi**

Komento

`mvn package`

generoi hakemistoon *target* kolme jar-tiedostoa, joista suoritetaan se, jonka nimessä mainitaan **spring**. 
Eli tiedosto nimeltä WeatherBot-1.0-SNAPSHOT-spring-boot.jar

### **Testaus**

Testit suoritetaan komennolla

`mvn test`

Testikattavuusraportti luodaan komennolla

`mvn jacoco:report`

Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto *target/site/jacoco/index.html*
 
### **Checkstyle**

Tiedostoon checkstyle.xml määrittelemät tarkistukset suoritetaan komennolla

`mvn jxr:jxr checkstyle:checkstyle`

Mahdolliset virheilmoitukset selviävät avaamalla selaimella tiedosto target/site/checkstyle.html


