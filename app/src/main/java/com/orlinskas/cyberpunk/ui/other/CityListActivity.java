package com.orlinskas.cyberpunk.ui.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.repository.CityListPresenter;

import java.util.ArrayList;
import java.util.Locale;

public class CityListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<City> cities;
    private EditText searchCityField;
    private TextView headText;
    private ProgressBar progressBar;
    private Country country;
    private LoadTask loadTask = new LoadTask();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        listView = findViewById(R.id.activity_city_list_lv);
        searchCityField = findViewById(R.id.activity_city_list_et);
        progressBar = findViewById(R.id.activity_city_list_pb);
        headText = findViewById(R.id.activity_city_list_tv);

        country = (Country) getIntent().getSerializableExtra("country");
    }

    @Override
    protected void onStart() {
        super.onStart();
        searchCityField.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    scrollListTo(searchCityField.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(getResources().getColor(R.color.colorBackgroundListActivity));
                City city = cities.get(position);
                resumeMainWidgetCreator(city);
            }
        });
        loadTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingAndHideUI();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            findCities();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayAdapter<City> adapter = new CityListAdapter(getApplicationContext(), R.layout.city_row, cities);
            listView.setAdapter(adapter);
            hideLoadingAndShowUI();
        }
    }

    private void showLoadingAndHideUI() {
        String message = String.format("%s %s", getString(R.string.load_cities_for), country.getName());
        headText.setText(message);
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        searchCityField.setVisibility(View.INVISIBLE);
    }

    private void findCities() {
        CityListPresenter presenter = new CityListPresenter(getApplicationContext());
        cities = presenter.present(country);
    }

    private void hideLoadingAndShowUI() {
        headText.setText(getString(R.string.city_2));
        progressBar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
        searchCityField.setVisibility(View.VISIBLE);
        ToastBuilder.create(this, String.format(Locale.US, "%s %d %s", getString(R.string.have), cities.size(), getString(R.string.cities)));
    }

    private class CityListAdapter extends ArrayAdapter<City> {
        ArrayList<City> cities;

        CityListAdapter(Context context, int textViewResourceId, ArrayList<City> cities) {
            super(context, textViewResourceId, cities);
            this.cities = cities;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("ViewHolder")
            View row = inflater.inflate(R.layout.city_row, parent, false);

            TextView cityName = row.findViewById(R.id.city_row_tv_name);
            String name = cities.get(position).getName();
            cityName.setText(name);

            if (position%2 == 0) {
                row.setBackgroundColor(getResources().getColor(R.color.colorRowHigh));
            }
            else {
                row.setBackgroundColor(getResources().getColor(R.color.colorRowLight));
            }

            return row;
        }
    }

    private void scrollListTo(String desiredCityNamePart) {
        int position = 0;

        for(City city : cities) {

            String currentCityNamePart = null;
            try {
                currentCityNamePart = city.getName().substring(0, desiredCityNamePart.length());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (currentCityNamePart != null ) {
                if(currentCityNamePart.equals(desiredCityNamePart)) {
                    try {
                        listView.setSelection(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            position++;
        }

    }

    private void resumeMainWidgetCreator(City city) {
        Intent intent = new Intent();
        intent.putExtra("city", city);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
