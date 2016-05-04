package hi.apitest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 5/3/2016.
 */
public class CurrentGameParticipant extends LeagueData{

    public final boolean bot;
    public final long championId;
    public final List<Mastery> masteries;
    public final long profileIconId;
    public final List<Rune> runes;
    public final long spell1Id;
    public final long spell2Id;
    public final long summonerId;
    public final String summonerName;
    public final long teamId;

    public static List<CurrentGameParticipant> getParticipants(JSONObject data){
        List<CurrentGameParticipant> participants = new ArrayList<CurrentGameParticipant>();
        try{
            JSONArray dataArray = data.getJSONArray("participants");
            int size = dataArray.length();
            for(int i = 0; i < size; i++){
                participants.add(new CurrentGameParticipant(dataArray.getJSONObject(i)));
            }
        } catch (Exception ex){
        } finally {
            return participants;
        }
    }


    public CurrentGameParticipant(JSONObject data){
        //Getting members from data
        bot = getBoolean(data, "bot");
        championId = getLong(data, "championId");
        masteries = Mastery.getMasteries(data);
        profileIconId = getLong(data, "profileIconId");
        runes = Rune.getRunes(data);
        spell1Id = getLong(data, "spell1Id");
        spell2Id = getLong(data, "spell2Id");
        summonerId = getLong(data, "summonerId");
        summonerName = getString(data, "summonerName");
        teamId = getLong(data, "teamId");
    }

}
