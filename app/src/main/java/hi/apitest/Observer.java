package hi.apitest;

import org.json.JSONObject;

/**
 * Created by Anthony on 5/3/2016.
 */
public class Observer extends LeagueData{
    public String encryptionKey;

    public Observer (JSONObject data){
        //Get encryptionKey from data
        encryptionKey = getString(data, "encryptionKey");
    }
}
