package it.polimi.ingsw.client;

/**This class stores god cards information */

public class GodCard {
    private String godName;
    private String godDescription;
    private String godImage;

    public GodCard(String godName, String godDescription, String godImage){
        this.godName = godName;
        this.godDescription = godDescription;
        this.godImage = godImage;
    }

    public String getGodName(){return godName;}
    public String getGodDescription(){return godDescription;}
    public String getGodImage(){return godImage;}
}
