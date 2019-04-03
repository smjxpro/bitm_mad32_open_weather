package pro.smjx.openweather;


import pro.smjx.openweather.currentweather.CurrentWeather;
import pro.smjx.openweather.weatherforecast.WeatherForecast;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface WeatherService {
    @GET()
    Call<CurrentWeather> getCurrentWeather(@Url String urlString);

    @ GET()
    Call<WeatherForecast> getWeatherForecast(@Url String urlString);
}
