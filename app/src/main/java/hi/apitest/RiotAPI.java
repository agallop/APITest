package hi.apitest;

import android.util.Log;
import android.util.Pair;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * Created by Anthony on 4/26/2016.
 */
public class RiotAPI {
    public static String KEY;
    private static final Semaphore resultSemaphore = new Semaphore(1);
    private static Object callResult;

    /* Sets the API key
    * Required before calling any other method */
    public static void setKey(String key) {
        KEY = key;
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
        Log.d("API", "Starting Thread");
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

}
