# Pokémon Red/Blue Monotype Tracker

## Projektbeschreibung
- Das Projekt ist eine Management-App für Monotype Pokémon Challenge Runs. Nutzer sollen mit der Anwendung ihre laufenden und fertiggestellten Runs verwalten und planen können.
- Die Anwendung richtet sich an Pokémon-Spieler, die sich einer extra Herausforderung in Pokémon rote Edition bzw. Pokémon blaue Edition stellen wollen. Sie soll das Verwalten von diesen Herausforderungen vereinfachen und die Planung erleichtern.
- Motivation für die Nutzung

## Geplante Funktionalitäten
- Must have
    - Anlegen und Speichern von Nutzerprofilen
        - Übersicht über alle aktiven und fertigen Runs
        - Übersicht über verwendete und geplante Teams
    - Datenbank mit statischen Daten der ersten Pokémon-Generation
        - Typen
        - Basiswerte der Pokémon
        - Attacken
        - Fundorte
    - GUI zur Organisation der Runs
- Should have
    - nebenläufige Team-Bewertung
        - Simulation der Teamaufstellung gegen die Arenaleiter und Top-4 Mitglieder
        - Ausgabe einer Gewinnwahrscheinlichkeit
    - Seite mit Spieler-Statistiken
        - Statistiken bezüglich der Pokémon und deren Attacken, die von allen Nutzern in fertiggestellten Runs genutzt wurden
- Nice to have
    - Nebenläufige Kämpfe in Echtzeit
        - Spieler sollen ihre Teams gegeneinander ausprobieren können
        - Spieler bekommen einen globalen Rang auf einer Rangliste

## Bereits implementierte Funktionalitäten
  - CRUD Operationen für Move, Pokémon und UserInfo Klassen
  - Verbindung zwischen Client und Server kann hergestellt werden
  - Commands
    - Neuen User registrieren
    - Als User einloggen
    - Neuen Run hinzufügen
    - Alle Pokémon eines bestimmten Typen ausgeben lassen
  - Server verarbeitet mehrere Verbindungen parallel
  - GUI
    - Mehrere unterschiedliche Szenen werden dem Nutzer angezeigt, je nachdem welche Funktion gebraucht wird
    - Neue Nutzer können registriert werden
    - Bestehende Nutzer können angemeldet werden
    - Neue Runs können hinzugefügt werden
    - Bestehende Runs können bearbeitet oder gelöscht werden
    - Beim Editieren des Runs können Nutzer über einen Klick auf den Names des Pokémon eine Detailansicht über das Pokémon bekommen, welche die Typen, Statuswerte und lernbaren Attacken anzeigt
    - Wenn das Clientfenster geschlossen wird, wird der 'Bye' Command automatisch an den Server gesendet

## Datenbank
In der Datenbank werden folgende Tabellen gespeichert
  - Moves
    - Name (Text, Primary Key)
    - MaxPP (Integer) 
    - Accuracy (Integer)
    - MoveType (Integer)
    - Damage (Integer)
    - Duration (Integer)
    - TurnsActive (Integer)
    - DrainPercentage (Real)
    - AffectedStat (Integer)
    - EffectStrength (Integer)
  - Pokemon
    - PokedexID (Integer, Primary Key)
    - Name (Text)
    - TypeA (Integer)
    - TypeB (Integer)
    - HP (Integer)
    - Attack (Integer)
    - Defense (Integer)
    - Special (Integer)
    - Speed (Integer)
    - KnownMoves (Text)
  - Users
    - UserID (Integer, Primary Key)
    - Username (Text)
    - PasswordHash (Blob)
  - Runs
    - RunID (Integer, Primary Key)
    - Type (Integer)
    - Pokemon1 (Integer, Foreign Key references Pokemon(PokedexID))
    - Pokemon2 (Integer, Foreign Key references Pokemon(PokedexID))
    - Pokemon3 (Integer, Foreign Key references Pokemon(PokedexID))
    - Pokemon4 (Integer, Foreign Key references Pokemon(PokedexID))
    - Pokemon5 (Integer, Foreign Key references Pokemon(PokedexID))
    - Pokemon6 (Integer, Foreign Key references Pokemon(PokedexID))
    - Beaten (Integer)
    - UserID (Foreign Key references Users(UserID))

User zu Runs haben eine 1:n Beziehung

Runs zu Pokemon haben eine 1:6 Beziehung

## Socket Wahl
In diesem Projekt wird das Socket Objekt genutzt. Die Wahl viel auf dieses Objekt, da in diesem Projekt TCP Verbindungen benötigt wird. Die Anwendung ist nicht echtzeitkritisch und Anfragen passieren grundsätzlich nur selten. Darüber hinaus hendelt es sich bei den Anfragen immer um Daten, bei denen es zu Problemen führen würde, wenn Pakete verloren gingen.

## Geschützte Ressourcen
Die Datenbankzugriffe sind die einzigen Ressourcen, die in diesem Projekt geschützt sind. Sie müssen geschützt werden, da sie auf die Datenbank zugreifen und dort lesen und schreiben können.

## Race-Conditions
Es wurde mit 2 verschiedenen Clients gleichzeitig 2 verschiedene Runs für denselben User hinzugefügt. Mit Synchronisierung wurden beide Runs in die entsprechende SQL Tabelle hinzugefügt.

## Änderungen seit der letzten Abgabe
- Neue Klassen
  - Client.GUI.Controller
    - DetailedPokemonInfoController
    - FilterController
    - LoginController
    - RunManagerController
    - StartHandler
    - Detailed
  - Client.GUI.Model
    - DetailedPokemonInfoModel
    - FilerScreenModel
    - RunManagerModel
  - Client.GUI.View
    - DetailedPokemonInfoScreen
    - FilterView
    - LoginScreen
    - RunManagerView
    - SceneManger
    - SpriteLoader
    - StartUpScreen

- Wichtige neue Methoden (Hilfsmethoden können ausgelassen werden)
  - Client
    - getKnownMoves
    - updateRun
    - deleteRun
    - requestRuns
    - getPokemon
  - ClientHandler
    - updateRun
    - deleteRun
    - getRuns
    - getPokemonByID
    - getPokemonMoves
- Überarbeitungen
  - Alle ClientHandler bekommen nun vom Server die selben Instanzen der DAO Klassen übergeben, damit die Synchronisation korrekt funktioniert
  - RunDAO createEntry
    - Syntaxfehler behoben, der verhinderte, dass bestehende Runs in der Datenbank aktualisiert werden konnten 
