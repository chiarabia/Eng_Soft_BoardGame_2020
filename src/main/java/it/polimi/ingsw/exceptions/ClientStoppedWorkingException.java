package it.polimi.ingsw.exceptions;

public class ClientStoppedWorkingException extends Exception {
    private boolean wasItTimeOut;

    public boolean isWasItTimeOut() {
        return wasItTimeOut;
    }

    public ClientStoppedWorkingException(boolean wasItTimeOut) {
        this.wasItTimeOut = wasItTimeOut;
    }
}
