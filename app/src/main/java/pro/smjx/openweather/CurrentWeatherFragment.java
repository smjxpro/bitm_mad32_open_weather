package pro.smjx.openweather;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static android.provider.Settings.System.DATE_FORMAT;
import static android.provider.Settings.System.TIME_12_24;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherFragment extends Fragment {
    private TextView cwLocationTV, cwDateTimeTV, cwTempTV, cwDescTV, cwHumidityTV, cwSunsetSunriseTV;
    private ImageView cwIconIV;

   SimpleDateFormat simpleDateFormat =new SimpleDateFormat("hh:mm");

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_current_weather, container, false);


        cwLocationTV = v.findViewById(R.id.cwLocationTV);
        cwDateTimeTV = v.findViewById(R.id.cwDateTimeTV);
        cwTempTV = v.findViewById(R.id.cwTempTV);
        cwDescTV = v.findViewById(R.id.cwDescTV);
        cwHumidityTV = v.findViewById(R.id.cwHumidityTV);
        cwSunsetSunriseTV = v.findViewById(R.id.cwSunsetSunriseTV);
        cwIconIV = v.findViewById(R.id.cwIconIV);


        try {
            if (getArguments() != null) {

                pro.smjx.openweather.currentweather.CurrentWeather currentWeather = (pro.smjx.openweather.currentweather.CurrentWeather) getArguments().getSerializable("cw");



                cwLocationTV.setText(String.valueOf(currentWeather.getName()));
                cwDateTimeTV.setText(String.valueOf(currentWeather.getSys().getCountry()));
                cwTempTV.setText(String.valueOf(currentWeather.getMain().getTemp()));
                cwDescTV.setText(String.valueOf(currentWeather.getWeather().get(0).getDescription()));
                cwHumidityTV.setText("Humidity: "+String.valueOf(currentWeather.getMain().getHumidity())+"%");
                cwSunsetSunriseTV.setText("Sunrise: "+simpleDateFormat.format(currentWeather.getSys().getSunrise())+"/"+"Sunset: "+simpleDateFormat.format(currentWeather.getSys().getSunset()));

            }
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Temparature err", Toast.LENGTH_SHORT).show();


        }


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        cwDataPassListener = (CWDataPassListener) context;

    }


    public static CurrentWeatherFragment newInstance(pro.smjx.openweather.currentweather.CurrentWeather currentWeather) {

        Bundle b = new Bundle();
        b.putSerializable("cw", currentWeather);
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        fragment.setArguments(b);
        return fragment;
    }

}
