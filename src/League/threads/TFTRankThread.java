package League.threads;

import League.GUI.GUIController;
import League.info.LeagueOfLegendsClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.Callable;

public class TFTRankThread extends LeagueOfLegendsClient implements Callable<Object> {
    @Override
    public String call() throws Exception {
        String endPoint = "/tft/league/v1/entries/by-summoner/";
        String url = baseUrl + endPoint + GUIController.summonerID + "?api_key=" + APIkey;
        String response = makeAPICall(url);

        String tftRank = "Unranked";
        JSONArray arr = new JSONArray(response);
        if(arr.length() != 0){
            JSONObject info = arr.getJSONObject(0);
            tftRank = info.getString("tier") + " " + info.getString("rank") + " " + info.getInt("leaguePoints");
        }
        System.out.println(tftRank);
        return tftRank;
    }
}
