package hi.apitest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

/**
 * Created by Anthony on 4/26/2016.
 */
public class RiotAPI {
    public static String KEY;
    private static final Semaphore resultSemaphore = new Semaphore(1);
    private static Object callResult;
    private static Map<Long, Bitmap> summonersCache = new TreeMap<Long, Bitmap>();
    private static Map<Long, Bitmap> championCache = new TreeMap<Long, Bitmap>();
    private static String currentVersion;

    /* Sets the API key
    * Required before calling any other method */
    public static void setKey(String key) {
        KEY = key;
        currentVersion = getCurrentVersion();
    }

    public static TreeMap<String, Summoner> getSummonerByName(String region, final List<String> summonerNames) {

        //Building string for API call
        StringBuilder builder = new StringBuilder();
        builder.append("https://na.api.pvp.net/api/lol/");
        builder.append(region);
        builder.append("/v1.4/summoner/by-name/");
        int i;
        for(i = 0; i < summonerNames.size() - 1; i++){
            builder.append(summonerNames.get(i));
            builder.append(",%20");
        }
        builder.append(summonerNames.get(i));
        builder.append("?api_key=");
        builder.append(KEY);
        final CountDownLatch latch = new CountDownLatch(1);
        final String query = builder.toString();
        Log.d("API", "Starting Thread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Pair<Integer, String> response = httpGet(query);
                        // Pasrsing Json into Pair
                        if(response.first == 200) {
                            TreeMap<String, Summoner> result = new TreeMap<String, Summoner>();
                            // Pasrsing Json into Pair
                            JSONObject jsonResult = new JSONObject(response.second);
                            Iterator<String> i = summonerNames.iterator();
                            while (i.hasNext()) {
                                String name = i.next();
                                result.put(name, new Summoner(jsonResult.getJSONObject(name)));
                            }
                            resultSemaphore.acquire();
                            callResult = result;
                        } else {
                            callResult = null;
                        }
                        break;
                    } catch (Exception ex) {

                    }
                }
                latch.countDown();
            }
        }).start();
        TreeMap<String, Summoner> result;
        while (true) {
            try {
                latch.await();
                result = (TreeMap<String, Summoner>) callResult;
                break;
            } catch (Exception ex) {

            }
        }
        resultSemaphore.release();
        return result;
    }

    public static TreeMap<String, MasteryPages> getMasteries(final List<String> summonerIds, String region) {
        if(summonerIds == null)
            throw new IllegalArgumentException();
        if(summonerIds.size() == 0)
            throw new IllegalArgumentException();

        StringBuilder builder = new StringBuilder();
        builder.append("https://na.api.pvp.net/api/lol/");
        builder.append(region);
        builder.append("/v1.4/summoner/");
        int i;
        for(i = 0; i < summonerIds.size() - 1; i++){
            builder.append(summonerIds);
            builder.append(",%20");
        }
        builder.append(summonerIds.get(i));
        builder.append("/masteries?api_key=");
        builder.append(KEY);

        final CountDownLatch latch = new CountDownLatch(1);
        final String query = builder.toString();
        Log.d("API", "Starting Thread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Pair<Integer, String> response = httpGet(query);
                        // Pasrsing Json into Pair
                        if(response.first == 200) {
                            TreeMap<String, MasteryPages> result = new TreeMap<String, MasteryPages>();

                            JSONObject jsonResult = new JSONObject(response.second);
                            Iterator<String> i = summonerIds.iterator();
                            while (i.hasNext()) {
                                String id = i.next();
                                JSONObject masteryPagesDto = jsonResult.getJSONObject(id);
                                result.put(id, new MasteryPages(masteryPagesDto));
                            }

                            resultSemaphore.acquire();
                            callResult = result;
                        }else {
                            callResult = null;
                        }
                        break;
                    } catch (Exception ex) {

                    }
                }
                latch.countDown();
            }
        }).start();
        TreeMap<String, MasteryPages> result;
        while (true) {
            try {
                latch.await();
                result = (TreeMap<String, MasteryPages>) callResult;
                break;
            } catch (Exception ex) {

            }
        }
        resultSemaphore.release();
        return result;

    }

    public CurrentGameInfo getCurrentGame(long summonerId, String region){

        StringBuilder builder = new StringBuilder();
        builder.append("https://na.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/");
        builder.append(region);
        builder.append("/");
        builder.append(summonerId);
        builder.append("?api_key=");
        builder.append(KEY);

        final CountDownLatch latch = new CountDownLatch(1);
        final String query = builder.toString();
        Log.d("API", "Starting Thread getCurrentGame");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Pair<Integer, String> response = httpGet(query);
                        // Pasrsing Json into Pair
                        if(response.first == 200) {
                            JSONObject jsonResult = new JSONObject(response.second);
                            resultSemaphore.acquire();
                            callResult = new CurrentGameParticipant(jsonResult);
                        }else {
                            callResult = null;
                        }
                        break;
                    } catch (Exception ex) {

                    }
                }
                latch.countDown();
            }
        }).start();
        CurrentGameInfo result;
        while (true) {
            try {
                latch.await();
                result = (CurrentGameInfo) callResult;
                break;
            } catch (Exception ex) {

            }
        }
        resultSemaphore.release();
        return result;
    }

    public static String getCurrentVersion(){


        StringBuilder builder = new StringBuilder();
        builder.append("https://global.api.pvp.net/api/lol/static-data/na/v1.2/versions?api_key=");
        builder.append(KEY);

        final CountDownLatch latch = new CountDownLatch(1);
        final String query = builder.toString();
        Log.d("API", "Starting Thread, getCurrentVersion");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Pair<Integer, String> response = httpGet(query);
                        if(response.first == 200) {
                            JSONArray jsonResult = new JSONArray(response.second);
                            resultSemaphore.acquire();
                            callResult = jsonResult.get(0);
                        }else {
                            callResult = null;
                        }
                        break;
                    } catch (Exception ex) {

                    }
                }
                latch.countDown();
            }
        }).start();
        String result;
        while (true) {
            try {
                latch.await();
                result = (String) callResult;
                break;
            } catch (Exception ex) {

            }
        }
        resultSemaphore.release();
        return result;
    }



    public static Pair<Integer, String> httpGet(String urlStr) throws IOException {
        Log.d("MAIN", "httpstart");
        URL url = new URL(urlStr);
        URLConnection con = url.openConnection();
        HttpURLConnection conn = (HttpURLConnection) con;

        //Error
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            Log.d("MAIN", "response != 200");
            return new Pair<Integer, String>(responseCode , conn.getResponseMessage());
        }

        // Buffer the result into a string
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            Log.d("MAIN", "line append");
            sb.append(line);
        }
        rd.close();
        Log.d("MAIN", "finish append");
        conn.disconnect();
        return new Pair<Integer, String>(responseCode, sb.toString());
    }

    public static Bitmap getSummonerImage(final Long id){
        if(!summonersCache.containsKey(id)){
            SummonerSpell spell = getSummonerSpell(id);
            StringBuilder builder = new StringBuilder();
            builder.append("http://ddragon.leagueoflegends.com/cdn/");
            builder.append(currentVersion);
            builder.append("/img/spell/");
            builder.append(spell.image.full);
            final String query = builder.toString();
            final CountDownLatch latch = new CountDownLatch(1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        summonersCache.put(id, bitmapFromUrl(query));
                    } catch (Exception ex){
                        Log.d("getSummonerImage", ex.getLocalizedMessage());
                    }
                    latch.countDown();
                }
            }).start();
            try {
                latch.await();
            }catch (Exception ex){

            }
        }
        return summonersCache.get(id);
    }

    public static Bitmap getChampionImage(final Long id){
        if(!championCache.containsKey(id)){
            Champion champion = getChampion(id);
            StringBuilder builder = new StringBuilder();
            builder.append("http://ddragon.leagueoflegends.com/cdn/");
            builder.append(currentVersion);
            builder.append("/img/champion/");
            builder.append(champion.image.full);
            final String query = builder.toString();
            final CountDownLatch latch = new CountDownLatch(1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                    championCache.put(id, bitmapFromUrl(query));
                        Log.d("getChampionImage", "champ" + id + "added");
                    } catch (Exception ex){
                        Log.d("getSummonerImage", ex.getLocalizedMessage());
                    }
                    latch.countDown();
                }
            }).start();
            try {
                latch.await();
            }catch (Exception ex){

            }
        }
        return championCache.get(id);
    }

    private static SummonerSpell getSummonerSpell(Long id){
        StringBuilder builder = new StringBuilder();
        builder.append("https://global.api.pvp.net/api/lol/static-data/na/v1.2/summoner-spell/");
        builder.append(id);
        builder.append("?spellData=all&api_key=");
        builder.append(KEY);

        final CountDownLatch latch = new CountDownLatch(1);
        final String query = builder.toString();
        Log.d("API", "Starting getSummonerSpell");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Pair<Integer, String> response = httpGet(query);
                        if(response.first == 200) {
                            JSONObject jsonResult = new JSONObject(response.second);
                            resultSemaphore.acquire();
                            callResult = new SummonerSpell(jsonResult);
                        }else {
                            callResult = null;
                        }
                        break;
                    } catch (Exception ex) {

                    }
                }
                latch.countDown();
            }
        }).start();
        SummonerSpell result;
        while (true) {
            try {
                latch.await();
                result = (SummonerSpell) callResult;
                break;
            } catch (Exception ex) {

            }
        }
        resultSemaphore.release();
        return result;
    }

    private static Champion getChampion(Long id){
        StringBuilder builder = new StringBuilder();
        builder.append("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/");
        builder.append(id);
        builder.append("?champData=all&api_key=");
        builder.append(KEY);

        final CountDownLatch latch = new CountDownLatch(1);
        final String query = builder.toString();
        Log.d("API", "Starting getSummonerSpell");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Pair<Integer, String> response = httpGet(query);
                        if(response.first == 200) {
                            JSONObject jsonResult = new JSONObject(response.second);
                            resultSemaphore.acquire();
                            Log.d("getChampion", "got champion");
                            callResult = new Champion(jsonResult);
                        }else {
                            callResult = null;
                        }
                        break;
                    } catch (Exception ex) {

                    }
                }
                latch.countDown();
            }
        }).start();
        Champion result;
        while (true) {
            try {
                latch.await();
                result = (Champion) callResult;
                break;
            } catch (Exception ex) {

            }
        }
        resultSemaphore.release();
        return result;
    }


    private static Bitmap bitmapFromUrl(String url) throws IOException {
        Bitmap x;
        Log.d("RiotAPI", "getBitmapFromURL: " + url);

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);

        Log.d("RiotAPI", "getBitmapFromURL: " + url);
        return x;
    }

}
