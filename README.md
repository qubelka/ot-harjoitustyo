# **Telegram weather-bot**

Sääbotin avulla käyttäjä voi hakea tämänhetkiset säätiedot yhdestä kaupungista ympäri maailmaa. 
Sovellus näyttää tämänhetkisen lämpötilan, kosteusprosentin, pilvisyyden ja säätilaa havainnollistavan kuvan.

[WeatherBot](WeatherBot)

## **Dokumentaatio**

[Käyttöohje](dokumentaatio/kayttoohje.md)

[Vaatimusmäärittely](dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](dokumentaatio/arkkitehtuuri.md)

[Työaikakirjanpito](dokumentaatio/tuntikirjanpito.md)

## **Releaset**

[Viikko 5](https://github.com/qubelka/ot-harjoitustyo/releases/tag/viikko5)

[Viikko 6](https://github.com/qubelka/ot-harjoitustyo/releases/tag/viikko6)

[loppupalautus](https://github.com/qubelka/ot-harjoitustyo/releases/tag/loppupalautus)

## **Duplikoidut viestit**

Botti on suunniteltu käynnistettäväksi yhdeltä koneelta. Jos botti on käynnissä samaan aikaan usealla koneella, tämä aiheuttaa häiriöitä tg:n puolella ja viestit tulee duplikoituina. Yksi tapa
välttää tätä ongelmaa: rekisteröidä oma botti telegrammissa ja antaa sen tiedot (nimi ja token) weatherbot-sovellukselle.  

## **Komentorivitoiminnot**

### **Ohjelman suoritus**

`mvn compile exec:java -Dexec.mainClass=weatherbot.domain.BotApp` 

### **Suoritettavan jarin generointi**

Komento

`mvn package`

generoi hakemistoon *target* kolme jar-tiedostoa, joista suoritetaan se, jonka nimessä mainitaan **spring**. 
Eli tiedosto nimeltä WeatherBot-1.0-SNAPSHOT-spring-boot.jar
Sovellus olettaa, että suoritushakemistossa on tiedosto users.txt

### **Testaus**

Testit suoritetaan komennolla

`mvn test`

Testikattavuusraportti luodaan komennolla

`mvn jacoco:report`

Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto *target/site/jacoco/index.html*
 
### **Checkstyle**

Tiedostoon checkstyle.xml määrittelemät tarkistukset suoritetaan komennolla

`mvn jxr:jxr checkstyle:checkstyle`

Mahdolliset virheilmoitukset selviävät avaamalla selaimella tiedosto *target/site/checkstyle.html*

**Huom!** 
*Bot*-luokan onUpdateReceived(Update update) -metodi ylittää maksimaalisen sallitun pituuden (20 riviä), mutta sen pilkkominen osiksi olisi 
epäkäytännöllistä, koska se on switch-tyyppinen luokka, ja se ohjaa sovelluksen toimintaa kutsumalla joka päivitykselle oman metodin. 

### **JavaDoc**

JavaDoc generoidaan komennolla

`mvn javadoc:javadoc`

JavaDocia voi tarkastella avaamalla selaimella tiedosto *target/site/apidocs/index.html*

