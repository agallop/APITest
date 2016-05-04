package hi.apitest;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Anthony on 5/4/2016.
 */
public class CurrentGameInfo extends LeagueData{
    public final List<BannedChampion> bannedChampions;
    public final long gameId;
    public final long gameLength;
    public final String gameMode;
    public final long gameQueueConfigId;
    public final long gameStartTime;
    public final String gameType;
    public final long mapId;
    public final Observer observers;
    public final List<CurrentGameParticipant> participants;
    public final String platformId;

    public CurrentGameInfo(JSONObject data){

        //Getting members from JsonObject
        bannedChampions = BannedChampion.getBannedChampions(data);
        gameId = getLong(data, "gameId");
        gameLength = getLong(data, "gameLength");
        gameMode = getString(data, "gameMode");
        gameQueueConfigId = getLong(data, "gameQueueConfigId");
        gameStartTime = getLong(data, "gameStartTime");
        gameType = getString(data, "gameType");
        mapId = getLong(data, "mapId");
        observers = new Observer(data);
        participants = CurrentGameParticipant.getParticipants(data);
        platformId = getString(data, "platformId");
    }
}
