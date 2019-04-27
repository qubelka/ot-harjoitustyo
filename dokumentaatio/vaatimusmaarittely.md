# **Vaatimusmäärittely: Telegram-sääbotti**

## **Sovelluksen tarkoitus**

Sääbotin avulla käyttäjä voi hakea tämänhetkiset säätiedot yhdestä kaupungista ympäri maailmaa. Sovellus näyttää tämänhetkisen lämpötilan, kosteusprosentin, pilvisyyden ja
säätilaa havainnollistavan kuvan ![kuvan](http://openweathermap.org/img/w/13n.png).
     
## **Käyttäjät**

Ei erikoisia käyttäjäryhmiä. 

## **Käyttöliittymäluonnos**

Sovellus periaatteessa koostuu kahdesta näkymästä: perusvalikko ja "my locations" -valikko. "units"-toiminto toteutetaan InlineKeyaboardin avulla, joten sille ei ole ReplyKeyboard-tyyppistä näkymää, ja 
"search by name" -toiminto ei vaadi näppäimistöä ollenkaan.
  
![luonnos](https://github.com/qubelka/ot-harjoitustyo/blob/master/laskarit/viikko2/Telegram-bot%20cropped.jpg)

## **Perusversion tarjoama toiminnallisuus**

Säätietoja voi hakea kaupungin (tai vaihtoehtoisesti kaupungin ja maan) perusteella yhdellä kielellä - englannilla. Käyttäjä voi valita, näytetäänkö lämpötila Celsius- vai Farenheit-asteina. 
Käyttäjä voi tallentaa omat sijaintitiedot sovellukseen ja hakea säätä jatkossa tämän sijaintitiedon perusteella.
 
## **Jatkokehitysideoita**
- säätietoja voi hakea useammalla kielellä
- sovelluksen voi ajastaa lähettämään säätiedotuksen tietyin väliajoin joko käyttäjän puhelimeen (tai lisätä bottikäyttäjän keskusteluun, jolloin tiedot lähetetään kaikille keskustelun 
osallistujille)  
- sovellus näyttää useamman päivän sääennusteen 
- sovellus näyttää tarkempia tietoja, mm. tuulen suunnan, tuulen nopeuden, paineen jne. 
- "omat sijainnit" -listalta voi poistaa sijainteja
