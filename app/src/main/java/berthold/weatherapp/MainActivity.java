package berthold.weatherapp;

/**
 * Waether App
 *
 * Demonstrates how to use the 'OpenWeatherMap'- API
 *
 */

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView weatherInfo;
    EditText city;
    ImageView weatherInfoIcon;
    Button refreshButton;
    ProgressBar progressBar;
    WebView wv;

    WeatherLoaderActivity wl;

    /**
     * On Create...
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Boilerplate for Activity's
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // UI
        city=(EditText) findViewById(R.id.city_name);
        weatherInfo=(TextView)findViewById(R.id.weather_info);
        weatherInfoIcon=(ImageView)findViewById(R.id.weather_icon);
        refreshButton=(Button)findViewById(R.id.refresh_button);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        wv=(WebView)findViewById(R.id.browser);

        // Buttons....
        // Refresh....
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Load weather Data
                if(wl!=null) wl.cancel(true);
                wl=new WeatherLoaderActivity(wv,weatherInfoIcon,weatherInfo,progressBar,city.getText().toString());
                wl.execute();
            }
        });
    }

    /**
     * Options Menu
     *
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
