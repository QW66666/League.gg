package League.threads;

import League.GUI.GUIController;
import League.info.LeagueOfLegendsClient;
import org.json.JSONObject;

import java.util.concurrent.Callable;

public class GameStatusThread extends LeagueOfLegendsClient implements Callable<Object> {
    @Override
    public String call() throws Exception {
        String endPoint = "/lol/spectator/v4/active-games/by-summoner/";
        String url = LeagueOfLegendsClient.baseUrl + endPoint + GUIController.summonerID + "?api_key=" + LeagueOfLegendsClient.APIkey;
        String response = LeagueOfLegendsClient.makeAPICall(url);

        String status = "IN GAME ";
        try{
            JSONObject jsonObject = new JSONObject(response);
            String gameType = jsonObject.getString("gameMode");
            if(gameType.equals("CLASSIC")){
                status += "SUMMONER'S RIFT";
            }
            else{
                status += jsonObject.getString("gameMode");
            }
        }
        catch (Exception e){
            if(e.getMessage().contains("not found")){
                status = "NOT IN GAME";
            }
        }
        return status;
    }
}
