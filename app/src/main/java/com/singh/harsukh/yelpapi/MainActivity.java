package com.singh.harsukh.yelpapi;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Finding Tacos...");
        mTextView = (TextView)findViewById(R.id.searchResults);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Yelp yelp = new Yelp(getResources().getString(R.string.yelp_consumer_key),getResources().getString(R.string.yelp_consumer_secret),getResources().getString(R.string.yelp_token),getResources().getString(R.string.yelp_token_secret));
                String businesses = yelp.search("tacos", 37.788022, -122.399797);
                try {
                    return processJson(businesses);
                } catch (JSONException e) {
                    return businesses;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                mTextView.setText(result);
                setProgressBarIndeterminateVisibility(false);
            }
        }.execute();
    }
    String processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        ArrayList<String> businessNames = new ArrayList<String>(businesses.length());
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);
            businessNames.add(business.getString("name"));
        }
        return TextUtils.join("\n", businessNames);
    }
}
