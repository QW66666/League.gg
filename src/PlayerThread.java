/*
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.Callable;

public class PlayerThread extends LeagueOfLegendsClient implements Callable<Object> {

    @Override
    public Player call() throws Exception {
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
        String tftRank = getTFTRank(summonerID);
        String tftTier = "Unranked";
        if(!tftRank.equals("Unranked")){
            tftTier = tftRank.substring(0, tftRank.indexOf(" "));
        }
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
        Player player = new Player(soloRank,soloTier, flexRank, flexTier, tftRank, tftTier, soloWinLose,soloWinRate,flexWinLose,flexWinRate,getChampions(summonerID), getIconUrl(GUIController.userName), getGameStatus(summonerID));
        return player;
    }
}
*/
