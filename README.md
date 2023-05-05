<div align="center">
  <h1 align="center">Smartmat backend</h1>
  <p align="center">
    Backend til smartmat laget av Callum Gran, Nicolai Brand, Simen Grønnslett, Thomas Svendal, Tobias Orø, Carl Johan Gützkow (Scrum Master) i faget Systemutvikling 2 (IDATT2106) vår 2023.
  </p>
</div>

## Info

Oppgaven gikk ut på å utvikle en webapplikasjon hvor målet er å hjelpe husholdninger å redusere matsvinn.

Dette er backend repositoriet til scrum-prosjektet i faget Systemutvkling 2. Prosjektet har også en frontend webapplikasjon kan finnes [her](https://gitlab.stud.idi.ntnu.no/scrum-team-4/scrum-frontend).

Resten av dokumentasjonen til applikasjonen er skrevet som del av sluttrapporten.

## Kjør backend lokalt

### Forutsetninger

For å kjøre programmet trenger du:

- JDK 17
- Maven
- Make
- Docker
- Docker-compose

### Installasjon og kjøre opp lokalt fra terminalen

1. Klon repositoriet
   ```sh
   git clone git@gitlab.stud.idi.ntnu.no:scrum-team-4/scrum-backend.git
   ```
2. Bytt mappe til det nedlastede repositoriet
   ```sh
   cd scrum-backend
   ```
3. Legg eksempel miljøvariabler i en .env fil
   ```sh
   cp .env.example .env
   ```
   VIKTIG: Eksempel env filen `.env.example` inneholder en allerede generert og fungerende (05.05.2023) api-nøkkel til [kasall.app](https://kassal.app/). Hvis denne nøkkelen ikke fungerer lenger kan det lages en ny [her](https://kassal.app/api). Deretter må den nye nøkkelen byttes ut med den ikke-fungerende nøkkelen i `.env` filen. Linjen som inneholder kasall.app api-nøkkelen skal se slik ut:
   `KASSAL_API_KEY="din_nøkkel"`.


4. Start databasen (obs: docker må være kjørende)
   ```sh
   make database
   ```
5. Kjør applikasjonen (obs: hvis dette er første gang du kjører applikasjonen kan det hende du må vente litt før databasen er klar)
   ```sh
   make
   ```


### Andre kommandoer

- `make database-down` - Sletter databasen
- `make test`- Kjører alle testene
- `make compile` - Kompilerer kildekoden
- `make prettier` - Formatterer kildekoden
- `make clean` - Sletter `target` mappen

## Kontakt

Carl J. Gützkow - carljgu@stud.ntnu.no (scrum master)

Nicolai H. Brand - nicolahb@stud.ntnu.no

Callum Gran - callumg@stud.ntnu.no

Thomas H. Svendal - thosve@stud.ntnu.no

Simen J. Grønnslett - simenjgr@stud.ntnu.no

Tobias Olaussen Orø - tobiasoo@stud.ntnu.no