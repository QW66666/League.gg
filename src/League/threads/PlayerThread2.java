package League.threads;

import League.GUI.GUIController;
import League.info.LeagueOfLegendsClient;
import League.info.Player2;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.concurrent.Callable;

public class PlayerThread2 extends LeagueOfLegendsClient implements Callable<Object> {

    @Override
    public Player2 call() {
        String summonerID = getID(GUIController.userName);
        String endPoint = "/lol/league/v4/entries/by-summoner/" + summonerID;
        String playerUrl = baseUrl + endPoint + "?api_key=" + APIkey;
        String response = makeAPICall(playerUrl);

        JSONArray jsonArray = new JSONArray(response);
        JSONObject solo = null;
        JSONObject flex = null;
        for(int i = 0; i < jsonArray.length(); i++)
        {
            if(jsonArray.getJSONObject(i).getString("queueType").equals("RANKED_SOLO_5x5"))
            {
                solo = jsonArray.getJSONObject(i);
            }
            if(jsonArray.getJSONObject(i).getString("queueType").equals("RANKED_FLEX_SR"))
            {
                flex = jsonArray.getJSONObject(i);
            }
        }
        String soloRank = "Unranked";
        String soloTier = "Unranked";
        String flexRank = "Unranked";
        String flexTier = "Unranked";
        String soloWinLose = "";
        String soloWinRate = "";
        String flexWinLose = "";
        String flexWinRate = "";
        if(solo != null) {
            soloRank = solo.getString("tier") + " " + solo.getString("rank") + " " + solo.getInt("leaguePoints");
            soloTier = solo.getString("tier");
            soloWinLose = solo.getInt("wins") + " W  " + solo.getInt("losses") + " L";
            soloWinRate = round((double)solo.getInt("wins") / (solo.getInt("wins") + solo.getInt("losses")) * 100) + "%";
        }
        if(flex != null) {
            flexRank = flex.getString("tier") + " " + flex.getString("rank") + " " + flex.getInt("leaguePoints");
            flexTier = flex.getString("tier");
            flexWinLose = flex.getInt("wins") + " W  " + flex.getInt("losses") + " L";
            flexWinRate = round((double)flex.getInt("wins") / (flex.getInt("wins") + flex.getInt("losses")) * 100) + "%";
        }
        Player2 player2 = new Player2(soloRank,soloTier,flexRank,flexTier,soloWinLose,soloWinRate,flexWinLose,flexWinRate);
        System.out.println(player2);
        return player2;
    }
}
