# MEGACHAT
Terminalipõhine vestlusrakendus vestlusruumide ja privaatvestlustega

Kasutajapoolne funktsionaalsus:
* Saab teisi inimesi sõpradeks lisada
* Saab luua vestlusruume ning sinna sõpru kutsuda
* Iga sõbraga on privaatvestlus
* Igasse vestlusesse saab ka manuseid saata (pilte ja muid faile)
* Igat vestlust on võimalik alla laadida eraldi
* Saab otsida sõnumeid märksõna, saatja või aja põhjal üle kõigi sõnumite (neis vestlustes, kus sa sees oled)

Ehitus:
* Kasutaja peab sisse logima ning tema userID küljes on kõik tema sõbrad ja vestlused
* Vestluseid ja kasutajate andmeid hoitakse keskserveris
* Kasutaja arvutis olev klient hoiab puhvris sisselogitud kasutaja kõigist vestlustest mõistliku koguse kõige uuemaid sõnumeid (~100 sõnumit)

Praegune käivitamisõpetus:
* Käivita ServerMain (see jookseb pordil 1337)
* Käivita ChatClient argumentidega ip aadress ja pord (ehk default serveri settingutega java ChatClient 127.0.0.1 1337)
