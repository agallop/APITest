package hi.apitest;

import org.json.JSONObject;

/**
 * Created by Anthony on 4/30/2016.
 */
public class MasteryDto {
    public final int id;
    public final int rank;

    //Creats a MasteryDto using primitives
    public MasteryDto(int id, int rank){
        this.id = id;
        this.rank = rank;
    }


    // Creates a MasteryDto using a JSON
    public MasteryDto(JSONObject data){


        //Get id from data
        int id = 0;
        try{
            id = data.getInt("id");
        } catch (Exception ex){
        } finally {
            this.id = id;
        }

        //get rank from data
        int rank = 0;
        try {
            rank = data.getInt("rank");
        } catch (Exception ex){
        } finally {
            this.rank = rank;
        }
    }

}
