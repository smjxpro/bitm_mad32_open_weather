package pro.smjx.openweather;

import pro.smjx.openweather.currentweather.*;
import pro.smjx.openweather.weatherforecast.*;


import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;


    private FusedLocationProviderClient client;
    private double latitude, longitude;

    pro.smjx.openweather.currentweather.CurrentWeather currentWeather;
    pro.smjx.openweather.weatherforecast.WeatherForecast weatherForecast;

    private static final String BASE_URL_CURRENT = "http://api.openweathermap.org/data/2.5/";


    WeatherService weatherService;
    private String tempUnit = "metric";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        //reloadFragment();

        if (getIntent()!=null){
            tempUnit = getIntent().getStringExtra("tu");
        }




        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1000);
            return;
        }


        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    return;
                }
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Toast.makeText(getApplicationContext(), "Lat" + String.valueOf(latitude) + " Lon" + String.valueOf(longitude), Toast.LENGTH_SHORT).show();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL_CURRENT)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                weatherService = retrofit.create(WeatherService.class);

                String appId = getString(R.string.current_weather_api);
                String urlCurrentWeather = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s", latitude, longitude, tempUnit, appId);
                String urlWeatherForecast = String.format("forecast?lat=%f&lon=%f&appid=%s", latitude, longitude, appId);

                Call<CurrentWeather> weatherCall = weatherService.getCurrentWeather(urlCurrentWeather);
                weatherCall.enqueue(new Callback<CurrentWeather>() {
                    @Override
                    public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                        if (response.code() == 200) {
                            currentWeather = response.body();
                           reloadFragment();
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeather> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "An error occured", Toast.LENGTH_SHORT).show();

                        reloadFragment();
                    }
                });

                Call<WeatherForecast> forecastCall = weatherService.getWeatherForecast(urlWeatherForecast);
                forecastCall.enqueue(new Callback<WeatherForecast>() {
                    @Override
                    public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                        if (response.code() == 200) {
                            weatherForecast = response.body();
                            Log.e("WF",String.valueOf(weatherForecast));
                            reloadFragment();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherForecast> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Weather Forecast error occured", Toast.LENGTH_SHORT).show();

                        reloadFragment();
                    }
                });


            }
        });



    }

    public void reloadFragment() {

        tabLayout.removeAllTabs();

        tabLayout.addTab(tabLayout.newTab().setText("Current Weather").setIcon(R.drawable.logo_icon));
        tabLayout.addTab(tabLayout.newTab().setText("Weather Forecast").setIcon(R.drawable.logo_icon));

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuC:
                tempUnit = "metric";
                startActivity(new Intent(this,MainActivity.class).putExtra("tu",tempUnit));
                Toast.makeText(this, "Celcius Chosen", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuF:
                tempUnit = "imperial";
                startActivity(new Intent(this,MainActivity.class).putExtra("tu",tempUnit));
                Toast.makeText(this, "Fahrenheit Chosen", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    private class PagerAdapter extends FragmentPagerAdapter {
        private int tabCount;

        public PagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CurrentWeatherFragment.newInstance(currentWeather);
                case 1:
                    return WeatherForecastFragment.newInstance(weatherForecast);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        return true;
    }
}

