package it.polimi.ingsw.server;

// Un EventGenerator è un thread parallelo che rilascia eventi sotto forma di oggetti
// tramite il metodo getNewEvent(). Può essere chiamato in qualunque momento e
// restituisce un nuovo oggetto-evento, se presente, o in caso contrario null.
// ServerAsyncReceiver è ovviamente un EventGenerator, ma possono esserlo anche
// oggetti che non operano sulla connessione, per esempio un timer.

public abstract class EventGenerator extends Thread {
    public abstract Object getNewEvent();
}
