package League.threads;

import League.GUI.GUIController;
import League.info.LeagueOfLegendsClient;
import org.json.JSONObject;

import java.util.concurrent.Callable;

public class IconUrlThread extends LeagueOfLegendsClient implements Callable<Object> {
    @Override
    public String call() throws Exception {
        String name = fixName(GUIController.userName);
        String endPoint = "/lol/summoner/v4/summoners/by-name/" + name;
        String url = baseUrl + endPoint + "?api_key=" + APIkey;
        JSONObject jsonObject = new JSONObject(makeAPICall(url));
        int iconId = jsonObject.getInt("profileIconId");
        String iconUrl = "http://ddragon.leagueoflegends.com/cdn/12.10.1/img/profileicon/" + iconId + ".png";
        return iconUrl;
    }
}
