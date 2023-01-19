package League.info;

public class Champion {
    private String name;
    private String pictureURL;
    private int points;
    private int level;

    public Champion(String name, String pictureURL, int level, int points){
        this.name = name;
        this.pictureURL = pictureURL;
        this.points = points;
        this.level = level;
    }


    public String getName(){
        return name;
    }

    public int getPoints(){
        return points;
    }

    public int getLevel(){return level;}

    public String getPictureURL(){return pictureURL;}
}
