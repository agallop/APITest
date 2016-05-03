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
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
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
                ArrayList<String> names = new ArrayList<String>();
                names.add("ajmgallop");
                names.add("raichinny");
                TreeMap<String, SummonerDto> results = RiotAPI.getSummonerByName("NA", new ArrayList<String>(names));
                SummonerDto result = results.get("ajmgallop");
                if (result != null) {
                    StringBuilder builder = new StringBuilder();
                    info[0].setText(result.id + "");
                    info[1].setText(result.name);
                    info[2].setText(result.profileIconId + "");

                    Date date = new Date(result.revisionDate);
                    SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yy");
                    String dateText = df2.format(date);
                    System.out.println(dateText);
                    info[3].setText(dateText);

                    info[4].setText(result.summonerLevel + "");
                }
            }
        });
    }
}
