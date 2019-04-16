# **Arkkitehtuurikuvaus**

## **Rakenne**

Koodin pakkausrakenne on seuraava: 

![pakkauskaavio](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko4/pakkauskaavio.jpg)

Pakkaus *ui* sisältää tekstikäyttöliittymän, *domain* sovelluslogiikan ja *dao* tietojen pysyväistallennuksesta 
vastaavan koodin. Luokka *BotApp* käynnistää sovelluksen kutsumalla käyttöliittymästä vastaavan luokan *BotUi*.

## **Käyttöliittymä**

Käyttöliittymä on toteutettu tekstikäyttöliittymänä, ja se sisältää ohjeistuksen siitä, kuinka weather-bottiin kirjaudutaan telegrammista. Varsinainen käyttöliittymä, jonka kautta käyttäjä pääsee
kommunikoimaan sovelluksen kanssa on telegrammin puolella, ja se on toteutettu käyttäen telegram apin valmiita työkaluja, kuten ReplyKeyboardMarkup. 

Käyttöliittymä on pyritty eristämään sovelluslogiikasta: *BotApp* käynnistää sovelluksen, käyttöliittymäluokka *BotUi* luo uuden botin ja kaikki sovelluslogiikan toiminnot toteutuu luokassa *Bot*.
Kun sovellus käynnistetään *BotApp*:n avulla, *BotApp* luo käyttöliittymän ja käyttöliittymä puolestaan luo uuden botin telegram apin avulla. Botin luominen vaatii, että bot on rekisteröity telegrammissa
ja sillä on nimi ja avain. WeatherBotissa nämä tiedot on tallennettu valmiiksi .properties -tiedostoon, josta käyttöliittymä lataa ne botin käyttöön. Kun uusi bot on rekisteröity TG-apissa, kaikki
muutokset botin tilassa havaitaan luokan *Bot* avulla. Bot luokka toimii tässä käyttöliittymän tavoin: se reagoi käyttäjän komentoihin kutsumalla tarvittavia sovelluslogiikan metodeja.      

## **Sovelluslogiikka**

Bot-luokka yhdistää käyttöliittymän ja sovelluslogiikan ominaisuuksia. *Bot* perii tg-apin valmiin abstraktin luokan telegramLongPollingBot, jonka valmiita metodeja ovat mm. 
onUpdateReceived(Update update). Kyseinen metodi reagoi käyttäjän komentoihin (vastaanottaa komennon, luo vastauksen ja siihen liittyvän graafisen ilmentymän sekä lähettää vastauksen käyttäjälle).  
Luokka muodostaa sovelluksen loogisen datamallin. Toiminnallisista kokonaisuuksista vastaa luokat *KeyboardBuilder*, *ReplyMessage* ja *WeatherService*. 
Näiden luokkien tarjoamia keskeisiä metodeja botin toiminnan kannalta ovat:
 
..* getMainMenuKeyboard()
..* getUnitsKeyboard()
..* sendDefaultReply(Message received, String reply)
..* sendUnitsReply(Message received, String reply)  
..* getWeather(String city, long userID)

*WeatherService* hakee säätietoja openweathermap-apista, lisäksi luokka pääsee käsiksi käyttäjiin ja säätietoihin tietojen tallennuksesta vastaavassa pakkauksessa *dao* sijaitsevien rajapinnan *dao*
toteuttavien luokkien *WeatherDao* ja *UserDao* kautta. Luokkien toteutukset injektoidaan sovelluslogiikalle konstruktorikutsun yhteydessä.   

## **Tietojen pysyväistallennus**

Pakkauksen *dao* luokka *WeatherDao* huolehtii tietojen tallettamisesta h2-tietokantaan. Säätiedot talletetaan tietokantaan 10 minuutiksi. Tietoja poistetaan tietokannasta aikaleiman perusteella.  
Luokka *UserDao* tallettaa käyttäjäkohtaisia tietoja tiedostoon. Luokat noudattavat Data Access Object-suunnittelumallia ja ne on tarvittaessa mahdollista korvata uusilla
toteutuksilla, jos sovelluksen datan talletustapaa päätetään vaihtaa. Luokat on eristetty rajapinnan *dao* taakse. Dao-toteutuksen ansiosta sovelluslogiikan ei tarvitse käsitellä käyttäjien 
tietoja tai säähän liittyvää tietoa suoraan, eikä tietää tiedon tallennuksen käytännön toteutuksesta.   

### **Tiedostot**

Sovellus tallettaa käyttäjien tiedot tiedostoon users.txt. Tiedostoon tulee tieto käyttäjän tunnuksesta sekä valitusta lämpötilayksiköstä. Tiedot talletetaan seuraavassa formaatissa

```
90908789,1
23567891,2
```

Pitempi long-tyyppinen numero viittaa käyttäjän tunnukseen ja integer lämpötilayksikköön (1 = °C, 2 = °F). Tunnuksen ja lämpötilayksikön väliin tulee pilkku. 


## **Päätoiminnallisuudet**

Kuvataan seuraavaksi sovelluksen toimintalogiikka muutaman päätoiminnallisuuden osalta sekvenssikaaviona.

**säätietojen haku**

Säätietoja voi hakea joko komennolla "search by city name" tai suoraan kirjoittamalla kaupungin nimen tekstikenttään. Eli sovelluksen default-toiminto on säätietojen haku, joten kaikkia käyttäjän
syötteietä (lukuun ottamatta joukkoa sovellukseen määriteltyjä komentoja kuten /help) tulkitaan oletusarvoisesti säähakuina. Kun käyttäjä painaa nappia "search by city name" ja syöttää kaupungin tai
antaa kaupungin nimen suoraan, etenee sovelluksen kontrolli seuraavasti: 

![getWeather()]()

Telegram api reagoi käyttäjän syöttämään komentoon kutsumalla Bot-luokan metodia onUpdateReceived(update). Seuraavaksi komento ohjautuu kohtaan "default", jossa ensin kutsutaan luokan *WeatherService*
getWeather(String city, long userID)-metodia, joka ensin tarkistaa, onko kyseisestä kaupungista haettu säätietoja viimeisen 10 minuutin aikana (*WeatherDao*) ja seuraavaksi tarkistaa, 
löytyykö tietoa kyseisen käyttäjän valitsemasta lämpötilayksiköstä (*UserDao*). Jos tiedot löytyy, ne haetaan tietokannasta ja/tai tiedostosta ja palautetaan String-muodossa luokalle *Bot*. Jos 
esimerkiksi säätietoja ei löydy tietokannasta, *WeatherService* lähettää url-kyselyn openweatherapi:lle. Kun *WeatherService* on palauttanut vastauksen, *Bot* lähettää tiedot eteenpäin luokalle 
*ReplyMessage*, joka luo vastausviestin ja siihen liityvän graafisen näkymän. Tätä varten *ReplyMessage* kutsuu luokkaa *KeyboardBuilder*, joka palauttaa osaksi vastausviestiä liitettävän näppäimistön.
Lopuksi onUpdateReceived() lähettää käyttäjälle valmiin vastausviestin ja jää odottamaan käyttäjän komentoa.       

## **Ohjelman rakenteeseen jääneet heikkoudet** 
  
