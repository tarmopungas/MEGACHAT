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
* Kasutaja peab sisse logima ning tema userID küljes on kõik
  tema sõbrad ja vestlused
* Kestluseid ja kasutajate andmeid hoitakse keskserveris
* Kasutaja arvutis olev klient hoiab puhvris sisselogitud kasutaja
  kõigist vestlustest mõistliku koguse kõige uuemaid sõnumeid (nt ~100 sõnumit?)
