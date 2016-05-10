package hi.apitest;

import android.provider.ContactsContract;

import org.json.JSONObject;

/**
 * Created by Anthony on 5/8/2016.
 */
public class SummonerSpell extends LeagueData{
    public final Image image;

    public SummonerSpell(JSONObject data){
        image = Image.getImage(data);
    }
}
