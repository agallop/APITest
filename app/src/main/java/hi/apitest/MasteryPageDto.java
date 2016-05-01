package hi.apitest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 4/30/2016.
 */
public class MasteryPageDto {
    public final boolean current;
    public final long id;
    public final List<MasteryDto> masteries;
    public final String name;

    public MasteryPageDto(JSONObject data){

        //Getting current from data
        boolean current = false;
        try{
            current = data.getBoolean("current");
        } catch (Exception ex){
        } finally {
            this.current = current;
        }

        //Getting current from data
        long id = 0;
        try{
            id = data.getLong("id");
        } catch (Exception ex){
        } finally {
            this.id = id;
        }

        //Getting current from data
        List<MasteryDto> masteries = new ArrayList<MasteryDto>() {
        };
        try{
            JSONArray dataArray = data.getJSONArray("masteries");
            int size = dataArray.length();
            for(int i = 0; i < size; i++){
                masteries.add(new MasteryDto(dataArray.getJSONObject(i)));
            }
        } catch (Exception ex){
        } finally {
            this.masteries = masteries;
        }

        //Getting current from data
        String name = "";
        try{
            name = data.getString("name");
        } catch (Exception ex){
        } finally {
            this.name = name;
        }

    }
}
