package hi.apitest;

import org.json.JSONObject;

/**
 * Created by Anthony on 5/3/2016.
 */
public class Observer {
    public String encryptionKey;

    public Observer (JSONObject data){
        //Get id from data
        String encryptionKey = null;
        try{
            encryptionKey = data.getString("encryptionKey");
        } catch (Exception ex){
        } finally {
            this.encryptionKey = encryptionKey;
        }
    }
}
