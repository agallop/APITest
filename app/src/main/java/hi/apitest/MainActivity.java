package hi.apitest;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity {
    TextView [] info = new TextView[5];
    Button getButton;
    String [] results = new String[5];

    String [] urls = new String[]{
        "https://www.cs.utexas.edu/~agallop/BusinessCard/Name",
            "https://www.cs.utexas.edu/~agallop/BusinessCard/Phone",
            "https://www.cs.utexas.edu/~agallop/BusinessCard/E-mail",
            "https://www.cs.utexas.edu/~agallop/BusinessCard/Linkedin",
            "https://www.cs.utexas.edu/~agallop/BusinessCard/Github"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RiotAPI.KEY = getString(R.string.riot_api_key);
        info[0] = (TextView) findViewById(R.id.name);
        info[1] = (TextView) findViewById(R.id.phone);
        info[2] = (TextView) findViewById(R.id.e_mail);
        info[3] = (TextView) findViewById(R.id.linkedin);
        info[4] = (TextView) findViewById(R.id.github);

        getButton = (Button) findViewById(R.id.get_button);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<String, SummonerDto> result = RiotAPI.getSummonerByName("NA", "ajmgallop");
                if (result != null) {
                    StringBuilder builder = new StringBuilder();
                    info[0].setText(result.second.id + "");
                    info[1].setText(result.second.name);
                    info[2].setText(result.second.profileIconId + "");

                    Date date = new Date(result.second.revisionDate);
                    SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yy");
                    String dateText = df2.format(date);
                    System.out.println(dateText);
                    info[3].setText(dateText);

                    info[4].setText(result.second.summonerLevel + "");
                }
            }
        });
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
