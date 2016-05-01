package hi.apitest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Anthony on 4/30/2016.
 */
public class MasteryPagesDto {
    final Set<MasteryPageDto> pages;
    final long summonerId;

    public MasteryPagesDto(JSONObject data){

        //Getting Mastert Pages from data
        TreeSet<MasteryPageDto> pages = new TreeSet<MasteryPageDto>();
        try{
            JSONArray dataArray = data.getJSONArray("pages");
            int size = dataArray.length();
            for(int i = 0; i < size; i++){
                pages.add(new MasteryPageDto(dataArray.getJSONObject(i)));
            }
        }catch (Exception ex){
        }finally {
            this.pages = pages;
        }

        long summonerId = 0;
        try{
            summonerId = data.getLong("summonerId");
        }catch (Exception ex){
        }finally {
            this.summonerId = summonerId;
        }

    }

}
