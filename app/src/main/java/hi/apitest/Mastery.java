package hi.apitest;

import org.json.JSONObject;

/**
 * Created by Anthony on 4/30/2016.
 */
public class Mastery {
    public final int id;
    public final int rank;

    //Creats a Mastery using primitives
    public Mastery(int id, int rank){
        this.id = id;
        this.rank = rank;
    }


    // Creates a Mastery using a JSON
    public Mastery(JSONObject data){


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

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(!obj.getClass().equals(this.getClass()))
            return false;
        if(((Mastery) obj).id == this.id)
            return true;
        return false;
    }


}
