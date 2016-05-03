package hi.apitest;

import org.json.JSONObject;

/**
 * Created by Anthony on 4/26/2016.
 */
public class Summoner {
    public final long id;
    public final String name;
    public final int  profileIconId;
    public final long revisionDate;
    public final long summonerLevel;

    //Makes a Summoner using primitives
    public Summoner(long id, String name, int profileIconId, long revisionDate, long summonerLevel){
        this.id = id;
        this.name = name;
        this.profileIconId = profileIconId;
        this.revisionDate = revisionDate;
        this.summonerLevel = summonerLevel;
    }

    // Makes a Summoner given a JSON object
    public Summoner(JSONObject data) {

        // Parsing id from Json
        long id = 0;
        try {
            id = data.getLong("id");
        } catch (Exception ex){
        } finally {
            this.id = id;
        }

        // Parsing name from Json
        String name = null;
        try {
            name = data.getString("name");
        } catch (Exception ex){
        } finally {
            this.name = name;
        }

        // Parsing profileIconId from Json
        int profileIconId = 0;
        try {
            profileIconId = data.getInt("profileIconId");
        } catch (Exception ex){
        } finally {
            this.profileIconId = profileIconId;
        }

        // Parsing revisionDate from Json
        long revisionDate = 0;
        try {
            revisionDate = data.getLong("revisionDate");
        } catch (Exception ex){
        } finally {
            this.revisionDate = revisionDate;
        }

        //Parsing summonerLevel from Json
        long summonerLevel = 0;
        try {
            summonerLevel = data.getLong("summonerLevel");
        } catch (Exception ex){
        } finally {
            this.summonerLevel = summonerLevel;
        }
    }
}
