# **Testausdokumentti**

Ohjelmaa on testattu sekä automatisoiduin yksikkö- ja integraatiotestein JUnitilla sekä manuaalisesti tapahtunein järjestelmätason testein.

## **Yksikkö- ja integraatiotestaus**

### **sovelluslogiikka**

Sovelluslogiikkaa eli pakkauksen weatherbot.domain luokkia testataan sekä yksikkötestien (kuten luokkien Location, User, Weather kohdalla), että integraatiotestien avulla (kuten luokkien Bot, ReplyMessage ja WeatherService kohdalla). Integraatiotesteissä hyödynnetään Mockiton valmiita työkaluja, jotka simuloivat testauksessa tarvittavien luokkien toiminnallisuuksia. 

Testeihin, joissa hyödynnetään Springin testaustyökaluja, on lisätty annotaatio @RunWith, joka lataa Springin konfiguraation parametrina annetun luokan käynnistyksen yhteydessä (luokan siis täytyy olla SpringBootApplication-luokka). Tässä tapauksessa parametrina annetaan juuri testejä varten luotu luokka FakeBotApp, joka ei vaadi käyttäjän syöttämiä komentoja toisin kuin alkuperäinen BotApp (tai tarkemmin BotUi, joka sulkee sovelluksen, kun käyttäjä syöttää komennon "close"). 
  
![runwith](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko7/runwith.png)

Mockiton mockeja käytetään luokissa, joiden toiminta nojaa Telegram-apiin. Esimerkiksi luokka Bot vastaanottaa ja käsittelee telegram-botin kautta tulevia viestejä. Tämän toiminnon testaamista varten testeissä simuloidaan tilanteita, joissa botti saa erityyppisiä viestejä. Testit varmistavat, että botti reagoi viesteihin oikealla tavalla. Esimerkiksi, jos käyttäjä haluaa vaihtaa lämpötila-asteikon, botin täytyy palauttaa siihen liittyvä viesti ja näppäimistö, jonka avulla käyttäjä pääsee tekemään valintaansa.  

### **DAO-luokat**

Springin testauksesta näyttää olevan kahdenlaisia versioita: joko testataan alkuperäistä dao-objektia tai luodaan siitä mock. Tässä projektissa testataan alkuperäisiä dao-luokkia eli kaikki testit ajetaan oikealla tietokannalla. Tämä on toimiva ratkaisu h2-tietokannan kohdalla, koska toisin kuin monet muut tietokannat, h2 tallentaa tietoa keskusmuistiin. Tämä tarkoittaa sitä, että testaus ei vie paljon aikaa, vaikka testit ajetaan oikealla tietokannalla. Hyvänä puolena alkuperäisen daon testaamisessa on se, että testit oikeasti testaavat tietokannan toimintaa ja muun muassa kyselyjen oikeellisuutta. Huonona puolena on taas se, että tietokantaan joudutaan luomaan lukuisia yhteyksiä.

### **Testauskattavuus**

Käyttöliittymäkerrosta lukuunottamatta sovelluksen testauksen rivikattavuus on 92% ja haarautumakattavuus 81%. 

![tests](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko7/tests.png)

Testaamatta jäi BotApp-luokka, koska testeissä sitä korvaa FakeBotApp. Lisäksi testaamatta jäivät hashCode() - kokonaan, equals() - tietyt tapaukset sekä tietyt poikkeustilanteet, esimerkiksi jos onUpdateReceived()-metodin suorituksen aikana ohjelma epäonnistuu luomaan json-objektin.  

## **Järjestelmätestaus**

Sovelluksen järjestelmätestaus on suoritettu manuaalisesti.

### **Asennus ja konfigurointi**

Sovellus on haettu ja sitä on testattu [käyttöohjeen]() kuvaamalla tavalla Linux-ympäristöön. Käyttöohjeessa on linkki ladattavaan jar-tiedostoon, johon kaikki sovelluksen tarvittamat konfiguraatiotiedostot on pakattu valmiiksi. Sovellus luo tietokantatiedoston automaattisesti siihen kansioon, missä .jar suoritetaan. 

### **Toiminnallisuudet**

Kaikki [määrittelydokumentin]() ja käyttöohjeen listaamat toiminnallisuudet on käyty läpi. Kaikkien toiminnallisuuksien yhteydessä on syötekentät yritetty täyttää myös virheellisillä arvoilla kuten tyhjillä. 

## **Sovellukseen jääneet laatuongelmat**

Sovelluksen logging.properties on määritelty tällä hetkellä niin, että vain info-viestit tulostetaan eli käyttäjä esimerkiksi saa ilmoituksen onnistuneesta database.properties -tiedoston lukemisesta. Osa poikkeus-tyyppisistä viesteistä (esimerkiksi jos käyttäjä kirjoittaa kaupungin nimen väärin) tulee tg-botin kautta, joten ne on tarkoituksella jätetty tulostamatta terminaalin ikkunaan. Tämän lisäksi on olemassa virhetyyppejä, joihin sovellus ei anna mitään järkeviä ilmoituksia. Tähän kategoriaan lukeutuvat esimerkiksi virheilmoitukset siitä, että tiedon lukeminen tietokannasta epäonnistui.  

Sovelluksen testit ajetaan samalla tietokannalla, joka on käytössä sovelluksessa, joten alkuperäinen tietokannassa oleva tieto voi vääristyä tai tuhoutua. Projektissa ei käytetä testitietokantaa, koska sovellus joka tapauksessa resettaa tietokannan botin joka käynnistyksen yhteydessä.

 
