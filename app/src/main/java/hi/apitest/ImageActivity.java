package hi.apitest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created by Anthony on 5/9/2016.
 */
public class ImageActivity extends AppCompatActivity {
    ImageView spellImage;

    ImageView championImage;
    Button getButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summoner_spell_test);
        RiotAPI.setKey(getString(R.string.riot_api_key));
        spellImage = (ImageView) findViewById(R.id.spell_image);
        championImage = (ImageView) findViewById(R.id.champ_image);
        getButton = (Button) findViewById(R.id.get_image_button);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spellImage.setImageBitmap(RiotAPI.getSummonerImage((long)1));
                championImage.setImageBitmap(RiotAPI.getChampionImage((long)1));
            }
        });
    }
}
