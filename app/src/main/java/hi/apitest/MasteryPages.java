package hi.apitest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Anthony on 4/30/2016.
 */
public class MasteryPages {
    final Set<MasteryPage> pages;
    final long summonerId;

    public MasteryPages(JSONObject data) {

        //Getting Mastert Pages from data
        TreeSet<MasteryPage> pages = new TreeSet<MasteryPage>();
        try {
            JSONArray dataArray = data.getJSONArray("pages");
            int size = dataArray.length();
            for (int i = 0; i < size; i++) {
                pages.add(new MasteryPage(dataArray.getJSONObject(i)));
            }
        } catch (Exception ex) {
        } finally {
            this.pages = pages;
        }

        long summonerId = 0;
        try {
            summonerId = data.getLong("summonerId");
        } catch (Exception ex) {
        } finally {
            this.summonerId = summonerId;
        }

    }

    public int count() {
        return pages.size();
    }

    public Iterator<MasteryPage> iterator(){
        return pages.iterator();
    }

}


