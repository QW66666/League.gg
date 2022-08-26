import jdk.swing.interop.SwingInterOpUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.spi.CharsetProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

public class ChampionListThread extends LeagueOfLegendsClient implements Callable<ArrayList<Champion>> {
    @Override
    public ArrayList<Champion> call() throws Exception {
        String endPoint = "/lol/champion-mastery/v4/champion-masteries/by-summoner/" + GUIController.summonerID;
        String champUrl = baseUrl + endPoint + "?api_key=" + APIkey;
        String response = makeAPICall(champUrl);

        ArrayList<Champion> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for(int i = 0; i < 3; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("championId");
            int level = jsonObject.getInt("championLevel");
            int points = jsonObject.getInt("championPoints");
            String championName = "";
            String pictureURL = "";

            JSONObject jsonChamp = new JSONObject(makeAPICall("http://ddragon.leagueoflegends.com/cdn/12.9.1/data/en_US/champion.json"));
            JSONObject data = jsonChamp.getJSONObject("data");
            Iterator<String> keys = data.keys();

            while(keys.hasNext()) {
                String key = keys.next();
                if (data.get(key) instanceof JSONObject) {
                    JSONObject character = data.getJSONObject(key);
                    int idInList = character.getInt("key");
                    if(idInList == id){
                        championName = character.getString("id");
                        pictureURL = "http://ddragon.leagueoflegends.com/cdn/12.9.1/img/champion/" + championName + ".png";
                    }
                }
            }
            System.out.println(i);
            Champion champion = new Champion(championName, pictureURL, level, points);
            list.add(champion);
        }
        System.out.println(list);
        return list;
    }
}
