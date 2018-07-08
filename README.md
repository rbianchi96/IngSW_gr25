Progetto di ingegneria del software 2017-2018, gruppo 25

# Componenti del gruppo

* **Bianchi** Roberto 10503557
* **Caronni** Diego 10496014
* **Cecchetti** Francesco 10497762

# Descrizione del progetto

## Stato attuale

**Progetto ultimato.**

**Requisiti soddisfatti**: Regole Complete + GUI + RMI + Socket + 1 F.A. (Carte schema dinamiche).

Il gioco prevede dai 2 ai 4 giocatori, ognuno collegato mediante il proprio client a un server comune.

## Avvio dell'applicativo

### Server

Per avviare il server spostarsi nella cartella `Deliverables/jars/server/` e avviare il server con il comando `java -jar Server.jar [resourcesFolder]` (`resourcesFolder` è il parametro opzionale che permette di caricare le risorse da una cartella specifica, per maggiorni informazioni vedere [File risorse](#file-risorse)).
Il server fornisce solo informazioni in output sul terminale.

### Client
Per avviare il server spostarsi nella cartella `Deliverables/jars/client/` e avviare il client con il comando `java -jar Client.jar [resourcesFolder]` (`resourcesFolder` è il parametro opzionale che permette di caricare le risorse da una cartella specifica, per maggiorni informazioni vedere [File risorse](#file-risorse)).
Il client è composto da un'interfaccia grafica basata su JavaFX, eventuali errori fatali verranno mostrati sul terminale.

### File risorse

Le risorse vengono fornite già all'interno del file JAR, è comunque possibile caricarle da una cartella arbitraria, passata come parametro all'avvio dell'applciazione.
I file personalizzabili sono:
- `netParams.json`: contiene i parametri di rete.
- `gameParams.json`: contiene i paramtetri di gioco (**caricato solo dal server**).
- `windowPatterns.json`: contiene le definizioni delle carte schema (**caricato solo dal server**).
- `toolCards.json`: contiene le definizioni delle carte strumento (**caricato solo dal server**).
- `privateObjectiveCards.json`: contiene le definizioni delle carte obbiettivo private (**caricato solo dal server**).
- `publicObjectiveCards.json`: contiene le definizioni delle carte obbiettivo pubbliche (**caricato solo dal server**).

Nel caso in cui venga definita una cartella per il caricamento delle risorse e non fossero presenti al suo interno alcuni file, questi saranno sostituiti dai file predefiniti presenti all'interno del JAR.
