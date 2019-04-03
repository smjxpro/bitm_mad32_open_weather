package pro.smjx.openweather;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pro.smjx.openweather.weatherforecast.WeatherForecast;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherForecastFragment extends Fragment {
    private TextView weeklyHighTempTV, forecastTempTV;

    public WeatherForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_weather_forecast, container, false);

        weeklyHighTempTV=v.findViewById(R.id.weeklyHighTempTV);
        forecastTempTV=v.findViewById(R.id.forecastTemp);


        try {
            if (getArguments()!=null) {

                WeatherForecast weatherForecast = (WeatherForecast) getArguments().getSerializable("wf");

                double tempMax = weatherForecast.getList().get(1).getMain().getTempMax();
                Toast.makeText(getContext(), "Max Temparature"+String.valueOf(tempMax), Toast.LENGTH_SHORT).show();
                weeklyHighTempTV.setText(String.valueOf(tempMax));
                forecastTempTV.setText(String.valueOf(weatherForecast.getList().get(1).getMain().getTemp()));


            }
        }catch (NullPointerException e){
            Toast.makeText(getContext(), "Temparature error", Toast.LENGTH_SHORT).show();


        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }


    public static WeatherForecastFragment newInstance(WeatherForecast weatherForecast) {

        Bundle b = new Bundle();
        b.putSerializable("wf", weatherForecast);
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        fragment.setArguments(b);
        return fragment;
    }

}
