package League.info;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;

import League.info.Champion;
import org.json.JSONArray;
import org.json.JSONObject;

public class LeagueOfLegendsClient {
    public static String APIkey;
    public static String baseUrl;

    public LeagueOfLegendsClient(){
        APIkey = "RGAPI-e5fe4502-ddea-4a0a-a2d6-543eb7cd7e39";
        baseUrl = "https://na1.api.riotgames.com";
    }

    public static ArrayList<Champion> getChampions(String summonerID){
        String endPoint = "/lol/champion-mastery/v4/champion-masteries/by-summoner/" + summonerID;
        String champUrl = baseUrl + endPoint + "?api_key=" + APIkey;
        String response = makeAPICall(champUrl);

        ArrayList<Champion> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for(int i = 0; i < jsonArray.length(); i++){
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
            Champion champion = new Champion(championName, pictureURL, level, points);
            list.add(champion);
        }
        return list;
    }


    public ArrayList<String> getTopPlayers() {
        String endPoint = "/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5";
        String url = baseUrl + endPoint + "?api_key=" + APIkey;
        String response = makeAPICall(url);

        ArrayList<String> topPlayers = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray players = jsonObject. getJSONArray("entries");
        JSONObject player = null;
        int highestIdx = 0;
        for (int i = 0; i < 10; i++) {
            JSONObject highest = players.getJSONObject(0);
            for (int j = 1; j < players.length(); j++) {
                player = players.getJSONObject(j);
                if (player.getInt("leaguePoints") > highest.getInt("leaguePoints")) {
                    highest = player;
                    highestIdx = j;
                }
            }
            topPlayers.add(highest.getString("summonerName"));
            players.remove(highestIdx);
        }
        return topPlayers;
    }

    public static String getID(String name){
        name = fixName(name);
        String endPoint = "/lol/summoner/v4/summoners/by-name/" + name;
        String url = baseUrl + endPoint + "?api_key=" + APIkey;
        JSONObject jsonObject = new JSONObject(makeAPICall(url));
        return jsonObject.getString("id");
    }


    public static String fixName(String name){
        String[] nameList = name.split("");
        for(int i = 0; i < nameList.length; i++){
            if(nameList[i].equals(" ")){
                nameList[i] = "%20";
            }
        }
        String result = "";
        for(String str : nameList){
            result += str;
        }
        return result;
    }

    public double round(double num){
        return Math.round(num * 100.0) / 100.0;
    }

    public static String makeAPICall(String url)
    {
        try {
            URI myUri = URI.create(url); // creates a URI object from the url string
            HttpRequest request = HttpRequest.newBuilder().uri(myUri).build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }
}