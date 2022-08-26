package League.info;

public class Player2 {
    private String soloRank;
    private String soloTier;
    private String flexRank;
    private String flexTier;
    private String soloWinLose;
    private String soloWinRate;
    private String flexWinLose;
    private String flexWinRate;

    public String getSoloRank() {
        return soloRank;
    }

    public String getSoloTier() {
        return soloTier;
    }

    public String getFlexRank() {
        return flexRank;
    }

    public String getFlexTier() {
        return flexTier;
    }

    public String getSoloWinLose() {
        return soloWinLose;
    }

    public String getSoloWinRate() {
        return soloWinRate;
    }

    public String getFlexWinLose() {
        return flexWinLose;
    }

    public String getFlexWinRate() {
        return flexWinRate;
    }


    public Player2(String soloRank, String soloTier, String flexRank, String flexTier, String soloWinLose, String soloWinRate, String flexWinLose, String flexWinRate){
        this.soloRank = soloRank;
        this.soloTier = soloTier;
        this.flexRank = flexRank;
        this.flexTier = flexTier;
        this.soloWinLose = soloWinLose;
        this.soloWinRate = soloWinRate;
        this.flexWinLose = flexWinLose;
        this.flexWinRate = flexWinRate;
    }

}
