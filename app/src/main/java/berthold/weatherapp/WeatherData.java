package berthold.weatherapp;

/**
 * Created by Berthold on 6/5/18.
 */

public class WeatherData {

    String  city;
    String  icon;
    String  description;
    float   pressure;
    float   humity;
    float   temperature;
    float   tempMax;
    float   tempMin;

    public WeatherData(String city, String icon,String description,float pressure, float humity, float temperature, float tempMax, float tempMin) {
        this.city = city;
        this.icon=icon;
        this.description=description;
        this.pressure = pressure;
        this.humity = humity;
        this.temperature = temperature;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
    }
}
