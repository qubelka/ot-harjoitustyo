# **Arkkitehtuurikuvaus**

## **Rakenne**

Koodin pakkausrakenne on seuraava: 

![pakkauskaavio](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko7/pakkauskaavio_updated.jpg)

Pakkaus *configuration* sisältää botin ja tietokannan asetukset, *ui* sisältää tekstikäyttöliittymän, *domain* sovelluslogiikan ja *dao* tietojen pysyväistallennuksesta vastaavan koodin. Luokka *BotApp* käynnistää sovelluksen kutsumalla käyttöliittymästä vastaavan luokan *BotUi*.

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
 
  * getMainMenuKeyboard()
  * getUnitsKeyboard()
  * sendDefaultReply(Message received, String reply)
  * sendUnitsReply(Message received, String reply)  
  * getWeather(String city, long userID)

*WeatherService* hakee säätietoja openweathermap-apista, lisäksi luokka pääsee käsiksi käyttäjiin ja säätietoihin tietojen tallennuksesta vastaavassa pakkauksessa *dao* sijaitsevien rajapinnan *dao* toteuttavien luokkien *WeatherDao*, *UserDao* ja *LocationDao* kautta. Luokkien toteutukset injektoidaan sovelluslogiikalle konstruktorikutsun yhteydessä.

## **Tietojen pysyväistallennus**

Pakkauksen *dao* luokat *WeatherDao*, *UserDao* ja *LocationDao* huolehtivat tietojen tallettamisesta h2-tietokantaan. Säätiedot talletetaan tietokantaan 10 minuutiksi. Tietoja poistetaan tietokannasta aikaleiman perusteella. Käyttäjistä talletetaan käyttäjätunnus ja käyttäjän valitsema lämpötila-asteikko. Käyttäjä voi myös luoda listan sijainneistaan. Luokat noudattavat Data Access Object-suunnittelumallia ja ne on tarvittaessa mahdollista korvata uusilla toteutuksilla, jos sovelluksen datan talletustapaa päätetään vaihtaa. Luokat on eristetty rajapinnan *dao* taakse. Dao-toteutuksen ansiosta sovelluslogiikan ei tarvitse käsitellä käyttäjien tietoja tai säähän liittyvää tietoa suoraan, eikä tietää tiedon tallennuksen käytännön toteutuksesta.

Alla on botin toimintaa kuvaava luokkakaavio: 

![luokkakaavio](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko7/taulut.jpg)

Weather-taulu ei ole sidottu käyttäjään tai sijainteihin. Jos käyttäjä on tallentanut sijainteja tietokantaan, niin sää selviää kahdella tietokantakyselyllä: ensin haetaan sijaintitiedot ja sitten säätiedot, jos kyseisestä kaupungista on tehty hakuja viimeisen 10 minuutin aikana. Relaatiotietokannan sijasta tähän tarkoitukseen sopisi paremmin yksinkertaisempi NoSQL-tietokanta, mutta uuden tietokannan käyttöönotto vaikeuttaisi työn tarkistamista, joten tässä käytetään h2:ta, joka ei vaadi asennusta. 

### **Tiedostot**

Alun perin sovellus tallensi käyttäjien tietoja tiedostoon users.txt. Tiedostoon tuli tieto käyttäjän tunnuksesta sekä valitusta lämpötilayksiköstä. Tiedot tallennettiin seuraavassa formaatissa

```
90908789,1
23567891,2
```

Pitempi long-tyyppinen numero viittasi käyttäjän tunnukseen ja integer lämpötilayksikköön (1 = °C, 2 = °F). Tunnuksen ja lämpötilayksikön väliin tuli pilkku. 

Kun sovellukseen lisättiin toiminto, jonka avulla käyttäjät pystyivät tallentamaan omia sijainteja, käyttöön tuli myös *LocationDao*, joka tallentaa nämä sijainnit. Koska sijainnit ovat yhteydessä käyttäjiin, sijaintitietoja oli kätevä hakea relaatiotietokannasta käyttäjätunnuksen perusteella. Tämän takia käyttäjille tehtiin oma *UserDao*. 


## **Päätoiminnallisuudet**

Kuvataan seuraavaksi sovelluksen toimintalogiikka muutaman päätoiminnallisuuden osalta sekvenssikaaviona.

**säätietojen haku**

Säätietoja voi hakea joko komennolla "search by city name" tai suoraan kirjoittamalla kaupungin nimen tekstikenttään. Eli sovelluksen default-toiminto on säätietojen haku, joten kaikkia käyttäjän
syötteietä (lukuun ottamatta joukkoa sovellukseen määriteltyjä komentoja kuten /help) tulkitaan oletusarvoisesti säähakuina. Kun käyttäjä painaa nappia "search by city name" ja syöttää kaupungin tai
antaa kaupungin nimen suoraan, etenee sovelluksen kontrolli seuraavasti: 

![getWeather()](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko5/getWeather().png?raw=true)

Telegram api reagoi käyttäjän syöttämään komentoon kutsumalla Bot-luokan metodia onUpdateReceived(update). Seuraavaksi komento ohjautuu kohtaan "default", jossa ensin kutsutaan luokan *WeatherService*
getWeather(String city, long userID)-metodia, joka ensin tarkistaa, onko kyseisestä kaupungista haettu säätietoja viimeisen 10 minuutin aikana (*WeatherDao*) ja seuraavaksi tarkistaa, 
löytyykö tietoa kyseisen käyttäjän valitsemasta lämpötilayksiköstä (*UserDao*). Jos tiedot löytyy, ne haetaan tietokannasta ja/tai tiedostosta ja palautetaan String-muodossa luokalle *Bot*. Jos 
esimerkiksi säätietoja ei löydy tietokannasta, *WeatherService* lähettää url-kyselyn openweatherapi:lle. Kun *WeatherService* on palauttanut vastauksen, *Bot* lähettää tiedot eteenpäin luokalle 
*ReplyMessage*, joka luo vastausviestin ja siihen liityvän graafisen näkymän. Tätä varten *ReplyMessage* kutsuu luokkaa *KeyboardBuilder*, joka palauttaa osaksi vastausviestiä liitettävän näppäimistön.
Lopuksi onUpdateReceived() lähettää käyttäjälle valmiin vastausviestin ja jää odottamaan käyttäjän komentoa.

**uuden sijainnin lisääminen**

Uuden sijainnin lisääminen onnistuu "my locations" -toiminnon avulla. "My locations" -valikossa käyttäjä voi selata aikaisemmin tallennettuja sijainteja tai luoda uusia. Uuden sijainnin pääsee lisäämään painamalla nappia "add new location", joka palauttaa vastauksena ForcedReply -tyyppisen näppäimistön. Käyttäjää pyydetään syöttämään lisättävän kaupungin nimen, jolloin onUpdateReceived() -metodi saa tiedon, että viestiin on vastattu ja käsittelee vastausta kaavion esittämällä tavalla: 

![addLocation()](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko7/new_location.png)

## **Ohjelman rakenteeseen jääneet heikkoudet** 
  
### **käyttöliittymä**

Desktop-sovelluksen tekstikäyttöliittymä on jäänyt suppeaksi, koska sitä korvaavat telegram-apin valmiit metodit. Bot reagoi käyttäjän komentoihin onUpdateReceived()-metodin avulla, ja
kaikki viestintä tapahtuu telegrammin puolella.

### **koodin rakenne**  

Metodi onUpdateReceived() ylittää checkstylen määrittämän metodin maksimipituuden, mutta sen pilkkominen osiksi olisi epäkäytännöllistä, koska se on switch-tyyppinen luokka, 
ja se ohjaa sovelluksen toimintaa kutsumalla joka päivitykselle oman metodin. 

Luokassa ReplyMessage on jonkin verran toisteista koodia, mutta esimerkiksi jos SendMessagen luominen eriytettäisiin omaan luokkaan, niin tämä ei vaikuttaisi asiaan paljon,
koska SendMessage-objekti vaatii replyMarkupin tyypin, joten se olisi annettava metodille parametrina, mutta samaa replyKeyboardMarkuppia käytetään 
vain kahdessa sendReply-metodissa, joten muut sendReply-metodit eivät pääsisi hyödyntämään tätä uutta metodia.   

### **toiminnot**

Ohjelman toiminnassa ja toiminnoissa on joitakin puutteita: esimerkiksi haut tapahtuu vain yhdellä kielellä, "my locations" -listalta ei pääse poistamaan sijainteja, sääkuvio näkyy vain puhelinsovelluksessa. 
Koodikatselmoinnin yhteydessä myös kävi ilmi, että botti saattaa lähettää duplikoituja viestejä, ja ilmeisesti taustalla on reititysongelma, jota en pysty ratkaisemaan tämän kurssin puitteissa. 


