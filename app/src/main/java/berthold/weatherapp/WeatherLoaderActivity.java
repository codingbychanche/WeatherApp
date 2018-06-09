package berthold.weatherapp;

/**
 * Load Weather Data from 'OpenWeatherMap'
 *
 *  @author  Berthold Fritz 2017
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.net.URL;

public class WeatherLoaderActivity extends AsyncTask<String,Object,String> {

    private final static String myUrl="http://api.openweathermap.org/data/2.5/weather?q={0}&APPID=6af03fadcdc42ab3803ed8a361b62298&lang=de";
    private final static String myUrlHtmlResult="http://api.openweathermap.org/data/2.5/weather?q={0}&APPID=6af03fadcdc42ab3803ed8a361b62298&lang=de&mode=html";
    private final static String imageUrl="http://openweathermap.org/img/w/{0}.png";
    private String tag;

    private Context c;
    private ProgressBar p;
    private WebView wv;
    private ImageView weatherIcon;
    private Bitmap weatherIconGfx;
    private TextView weatherInfoView;

    private String city;

    /**
     * Constructor
     *
     * Creates a new filler object
     */

    WeatherLoaderActivity(WebView wv, ImageView weatherIcon,TextView weatherInfoView, ProgressBar p, String city){
        this.weatherInfoView=weatherInfoView;
        this.weatherIcon=weatherIcon;
        this.wv=wv;
        this.city=city;
        this.p=p;
    }

    /**
     * All set....
     */

    @Override
    protected void onPreExecute(){
        p.setVisibility(View.VISIBLE);
        weatherInfoView.setText("");
    }

    /**
     *  Load weather data from the server....
     *
     *  Does all the work in the background
     *  Rule! => Never change view elements of the UI- thread from here! Do it in 'onPublish'!
     */

    @Override
    protected String doInBackground(String... params){

        // Debug
        tag=WeatherLoaderActivity.class.getSimpleName();

        try {
            StringBuilder jsonWeatherData=new StringBuilder();

            URL _url =new URL(MessageFormat.format(myUrl,city));
            HttpURLConnection urlConnection=(HttpURLConnection) _url.openConnection();

            //
            // Load data as json...
            //
            InputStreamReader isr=new InputStreamReader(urlConnection.getInputStream());
            BufferedReader br=new BufferedReader(isr);

            String line;
            while((line=br.readLine())!=null){
                // This is important!
                // If you miss to do this here, the class which created
                // this object has no way to end the async task started!
                // => This means, no matter how often you call task.cancel(true)
                // the async task will not stop! You have to take
                // care here to react and run the code that cancels!
                if (isCancelled()) {
                    publishProgress("Canceled",null,null);
                    break;
                }
                jsonWeatherData.append(line);
            }
            isr.close();

            StringBuilder niceWeatherInfo=new StringBuilder();

            WeatherData wd=WeatherUtils.extractData(jsonWeatherData.toString());
            niceWeatherInfo.append(wd.city+"\n");
            niceWeatherInfo.append(wd.description+"\n");

            //
            // Load weather image
            //
            _url =new URL(MessageFormat.format(imageUrl,wd.icon));
            urlConnection=(HttpURLConnection) _url.openConnection();
            InputStream ir=urlConnection.getInputStream();
            weatherIconGfx= BitmapFactory.decodeStream(ir); // Image will be set in 'onPostExecute()'

            //
            // Load HTML- Version to display in an web view
            //
            _url =new URL(MessageFormat.format(myUrlHtmlResult,city));
            urlConnection=(HttpURLConnection) _url.openConnection();
            isr=new InputStreamReader(urlConnection.getInputStream());
            br=new BufferedReader(isr);

            StringBuilder htmlString=new StringBuilder();
            while((line=br.readLine())!=null){
                if (isCancelled()) {
                    publishProgress("Canceled",null,null);
                    break;
                }
                htmlString.append(line);
            }
            isr.close();

            // Publish result......
            Log.v(tag,htmlString.toString());
            publishProgress(niceWeatherInfo.toString(),htmlString.toString(),weatherIconGfx);


        }catch (Exception e){
            Log.v(tag,"Fehler "+ e);
            publishProgress(e.toString(),null,null);
        }
        return "Done";
    }

    /**
     * Update UI- thread
     *
     * This runs on the UI thread. Not handler's and 'post'
     * needed here
     *
     * @param s
     */

    @Override
    protected void onProgressUpdate (Object... s){
        super.onProgressUpdate();
        weatherInfoView.setText((String)s[0]);          // Set text
        wv.loadData((String)s[1],"text/html",null);     // Set web view
        weatherIcon.setImageBitmap((Bitmap)s[2]); // Show weather icon
    }

    /**
     * All done..
     *
     * @param result
     */

    @Override
    protected void onPostExecute (String result){
        p.setVisibility(View.GONE);
    }
}
