package hi.apitest;

import org.json.JSONObject;

/**
 * Created by Anthony on 5/3/2016.
 */
public class Rune {
    final int count;
    final long runeId;

    public Rune(JSONObject data){
        //Get id from data
        int count = 0;
        try{
            count = data.getInt("count");
        } catch (Exception ex){
        } finally {
            this.count = count;
        }

        //get rank from data
        long runeId = 0;
        try {
            runeId = data.getLong("runeId");
        } catch (Exception ex){
        } finally {
            this.runeId = runeId;
        }
    }

}
