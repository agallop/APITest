package hi.apitest;

import android.content.res.Resources;
import android.util.Log;
import android.util.Pair;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
    public static void setKey(String key){
        KEY = key;
    }

    public static Pair<String, SummonerDto> getSummonerByName(String region, String summonerName) {

        //Building string for API call
        StringBuilder builder = new StringBuilder();
        builder.append("https://na.api.pvp.net/api/lol/");
        builder.append(region);
        builder.append("/v1.4/summoner/by-name/");
        builder.append(summonerName);
        builder.append("?api_key=");
        builder.append(KEY);
        final CountDownLatch latch = new CountDownLatch(1);
        final String name = summonerName;
        final String query = builder.toString();
        Log.d("API", "Starting Thread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        // Pasrsing Json into Pair
                        JSONObject jsonResult = new JSONObject(httpGet(query));
                        JSONObject summonerDto = jsonResult.getJSONObject(name);
                        Pair<String, SummonerDto> result = new Pair<String, SummonerDto>(
                                name, new SummonerDto(summonerDto));
                        resultSemaphore.acquire();
                        callResult = result;
                        break;
                    } catch (Exception ex) {

                    }
                }
                latch.countDown();
            }
        }).start();
        Pair<String, SummonerDto> result;
        while(true) {
            try {
                latch.await();
                result = (Pair<String, SummonerDto>) callResult;
                break;
            } catch (Exception ex) {

            }
        }
        resultSemaphore.release();
        return result;
    }



    public static String httpGet(String urlStr) throws IOException {
        Log.d("MAIN", "httpstart");
        URL url = new URL(urlStr);
        URLConnection con = url.openConnection();
        HttpURLConnection conn = (HttpURLConnection) con;
        //conn.setConnectTimeout(10000);
        //conn.setReadTimeout(10000);
        // conn.setRequestMethod("GET");

        if(conn.getResponseCode() == -1){
            return "failed to connect";
        }


        if (conn.getResponseCode() != 200) {
            Log.d("MAIN", "response != 200");
            return conn.getResponseMessage();
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
        return sb.toString();
    }

}
