package it.polimi.ingsw.client;

import it.polimi.ingsw.JSONManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/** This class stores and shares all the text fields to be shown inside user interfaces */

public class Textfields {
    private final String initialplaying;
    private final String chooseworkerpositions;
    private final String won1;
    private final String won2;
    private final String lost1;
    private final String lost2;
    private final String playing1;
    private final String playing2;
    private final String chosen1;
    private final String chosen2;
    private final String disconnected;
    private final String moves1;
    private final String moves2;
    private final String movesopt;
    private final String builds1;
    private final String builds2;
    private final String buildsopt;
    private final String name;
    private final String numofplayers;
    private final String turnover;
    private final String worker1;
    private final String worker2;
    private final String wcanmove;
    private final String wcanbuild;
    private final String wcanboth;
    private final String wcantmove;
    private final String wcantbuild;
    private final String wcantdoactions;
    private final String canendturn;
    private final String waitgodpowers;
    private final String nocell;
    private final String err0;
    private final String err1;
    private final String err2;


    /**
     * This method reads all text fields from Textfields.json and stores them
     * @throws ParseException ParseException
     * */

    public Textfields() throws ParseException {
        JSONObject jsonObject = JSONManager.readMyJSONAsText("configurations/Textfields.json");
        this.initialplaying = (String) jsonObject.get("initialplaying");
        this.chooseworkerpositions = (String) jsonObject.get("chooseworkerpositions");
        this.won1 = (String) jsonObject.get("won1");
        this.won2 = (String) jsonObject.get("won2");
        this.lost1 = (String) jsonObject.get("lost1");
        this.lost2 = (String) jsonObject.get("lost2");
        this.playing1 = (String) jsonObject.get("playing1");
        this.playing2 = (String) jsonObject.get("playing2");
        this.chosen1 = (String) jsonObject.get("chosen1");
        this.chosen2 = (String) jsonObject.get("chosen2");
        this.disconnected = (String) jsonObject.get("disconnected");
        this.moves1 = (String) jsonObject.get("moves1");
        this.moves2 = (String) jsonObject.get("moves2");
        this.movesopt = (String) jsonObject.get("movesopt");
        this.builds1 = (String) jsonObject.get("builds1");
        this.builds2 = (String) jsonObject.get("builds2");
        this.buildsopt = (String) jsonObject.get("buildsopt");
        this.name = (String) jsonObject.get("name");
        this.numofplayers = (String) jsonObject.get("numofplayers");
        this.turnover = (String) jsonObject.get("turnover");
        this.worker1 = (String) jsonObject.get("worker1");
        this.worker2 = (String) jsonObject.get("worker2");
        this.err0 = (String) jsonObject.get("err0");
        this.err1 = (String) jsonObject.get("err1");
        this.err2 = (String) jsonObject.get("err2");
        this.wcanmove = (String) jsonObject.get("wcanmove");
        this.wcanbuild = (String) jsonObject.get("wcanbuild");
        this.wcanboth = (String) jsonObject.get("wcanmoveandbuild");
        this.wcantmove = (String) jsonObject.get("wcantmove");
        this.wcantbuild = (String) jsonObject.get("wcantbuild");
        this.wcantdoactions = (String) jsonObject.get("wcantdoanything");
        this.canendturn = (String) jsonObject.get("canendturn");
        this.nocell = (String) jsonObject.get("nocell");
        this.waitgodpowers = (String) jsonObject.get("waitgodpowers");
    }

    public String getInitialplaying() {
        return initialplaying;
    }

    public String getChooseworkerpositions(){ return chooseworkerpositions; }

    public String getWon1() {
        return won1;
    }

    public String getWon2() {
        return won2;
    }

    public String getLost1() {
        return lost1;
    }

    public String getLost2() {
        return lost2;
    }

    public String getPlaying1() {
        return playing1;
    }

    public String getPlaying2() {
        return playing2;
    }

    public String getChosen1() {
        return chosen1;
    }

    public String getChosen2() {
        return chosen2;
    }

    public String getDisconnected() {
        return disconnected;
    }

    public String getMoves1() {
        return moves1;
    }

    public String getMoves2() {
        return moves2;
    }

    public String getMovesopt() {
        return movesopt;
    }

    public String getBuilds1() {
        return builds1;
    }

    public String getBuilds2() {
        return builds2;
    }

    public String getBuildsopt() {
        return buildsopt;
    }

    public String getName() {
        return name;
    }

    public String getNumofplayers() {
        return numofplayers;
    }

    public String getTurnover() {
        return turnover;
    }

    public String getWorker1() { return worker1; }

    public String getWorker2() { return worker2; }

    public String getErr0() {
        return err0;
    }

    public String getErr1() {
        return err1;
    }

    public String getErr2() {
        return err2;
    }

    public String getWcantmove() { return wcantmove; }

    public String getWcantbuild() { return wcantbuild; }

    public String getCanendturn() { return canendturn; }

    public String getWaitgodpowers() { return waitgodpowers; }

    public String getWcanmove() {
        return wcanmove;
    }

    public String getWcanbuild() {
        return wcanbuild;
    }

    public String getWcanboth() {
        return wcanboth;
    }

    public String getWcantdoactions() {
        return wcantdoactions;
    }

    public String getNocell() { return nocell;}
}
