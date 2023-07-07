# Wekerlei Térvers

Mobil- és webes szoftverek c. tárgy házi feladatára készült az alábbi Android alkalmazás 2021-ben.

## Összefoglaló

Wekerletelepen, önkéntesek által készített „Térvers – Jelfék az úton” projekt kiegészítésre
készül az alkalmazás. A több helyszínen felállított installációs állomások művészeti 
kiegészítését teszi lehetővé (hanganyagok lejátszása, információk megjelenítése az adott 
stációkról.) Ezek mellett az állomások közötti navigálásban fog segíteni. Tehát egy turisztikai 
séta digitális élmény kiegészítéséhez készül.

## Főbb funkciók
- Az összes állomás megtekintése egy lista nézetben.
  - Az egyes elemekre kattintva rövid leírás az állomásról.
- Séta elindítása, navigálás az állomások között. (Összesen 10 állomás van.)
  - Egy térkép segítségével nézhetjük meg, hogy hol találhatóak az egyes 
állomások (és hol található maga a készülék, a felhasználó pozíciója).
- Az egyes állomásokhoz érve (GPS koordináció alapján) különböző hanganyag 
lejátszásának a lehetősége. Illetve részletes leírás az adott állomásról. A hanganyag 
a házi feladatban rövid zenei részlet bejátszásával lesz illusztrálva.
- Információs almenü pont
  - elérhetőség a projekt tervezőihez, és a weboldalhoz.
- További információk, érdekességek (például a projekt elkészüléséről).
  - Projekt készítése céljának leírása
  - rövid YouTube videó bejátszása a projektről.

 ## Alkalmazás továbbfejlesztése

 Az Szakdolgozatom készítésének keretetin belül az alkalmazást alapvető debuggolásokon és apróbb változtatásokon kívül az alábbi funkciókkal bővítettem ki:
 - Felhasználókezelés
   -  Az alkalmazásba be lehet jelentkezni, a bejelentkezett felhasználók látják, hogy melyik állomást látogatták már meg és melyiket még nem.
- Backend
  - Firebase szolgáltatások igénybevételével hoztam létre a fentebb említett felhasználókezelést.
  - A hanganyagok és egyéb tartalmak átkerültek szerveroldalra.
- Flutter
  - Az alkalmazás továbbfejlesztése Flutter szoftverfejlesztő készlet használatával történt.

A továbbfejlesztett alkalmazásról készült Szakdolgozatomat az alábbi GitHub Repository linken tudod elérni: https://github.com/IBarnabas/szakdoga
