package slicksoala.wheretoapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Welcome extends AppCompatActivity implements View.OnClickListener{
    final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    final String API_KEY = "AIzaSyCbOyfoowxZOrKqFpMUWfm94kUvJXLJQSk";
    final String LOC_PAR = "?location=";
    final String RAD_PAR = "&radius=";
    final String TYP_PAR = "&types=";
    final String KEY_PAR = "&key=";

    final int radiusW = 1000;
    final int radiusD = 16093;
    final int radiusT = 48280;

    final String placeTypePTG = "amusement_park,aquarium,art_gallery,natural_feature,cafe,casino,library,hindu_temple,museum,park,stadium,zoo";
    final String placeTypeTTD = "bowling_alley,bookstore,gym,shopping_mall,spa,movie_theater,movie_rental";
    final String placeTypeSTE = "bakery,bar,cafe,food,restaurant";

    private ActivityDo activitySelect = null;
    private Range rangeSelect = null;
    private String currLat, currLong;
    private String placeType = "";
    private int rad = 0;
    ArrayList placesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);
        rippleBackground.startRippleAnimation();

        ImageView goBtn = (ImageView) findViewById(R.id.centerImage);
        FloatingActionButton walkBtn = (FloatingActionButton) findViewById(R.id.walkFAM);
        FloatingActionButton driveBtn = (FloatingActionButton) findViewById(R.id.driveFAM);
        FloatingActionButton travBtn = (FloatingActionButton) findViewById(R.id.travelFAM);
        FloatingActionButton ptgBtn = (FloatingActionButton) findViewById(R.id.ptgACT);
        FloatingActionButton ttdBtn = (FloatingActionButton) findViewById(R.id.ttdACT);
        FloatingActionButton steBtn = (FloatingActionButton) findViewById(R.id.steACT);

        goBtn.setOnClickListener(this);
        walkBtn.setOnClickListener(this);
        driveBtn.setOnClickListener(this);
        travBtn.setOnClickListener(this);
        ptgBtn.setOnClickListener(this);
        ttdBtn.setOnClickListener(this);
        steBtn.setOnClickListener(this);
    }

    public void goAction() throws ExecutionException, InterruptedException {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Welcome.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currLat = Double.toString(location.getLatitude());
        currLong = Double.toString(location.getLongitude());

        if (rangeSelect == Range.WALK)
            rad = radiusW;
        else if (rangeSelect == Range.DRIVE)
            rad = radiusD;
        else
            rad = radiusT;

        if (activitySelect == ActivityDo.PLACESTOGO)
            placeType = placeTypePTG;
        else if (activitySelect == ActivityDo.THINGSTODO)
            placeType = placeTypeTTD;
        else
            placeType = placeTypeSTE;

        ReadJSONGPlacesTask task = new ReadJSONGPlacesTask();
        JSONObject retreived = task.execute().get();
        placesList = parseGoogleParse(retreived);
        GPlace placeToGo = (GPlace) placesList.get(new Random().nextInt(placesList.size()));

    }

    class ReadJSONGPlacesTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {
            String url_base = PLACES_API_BASE + LOC_PAR + currLat + "," + currLong + RAD_PAR +
                    Integer.toString(rad);
            String urlString = "";
            StringBuffer sb = null;
            String apikey_base = KEY_PAR + API_KEY;


            urlString = url_base + TYP_PAR + placeType + apikey_base;

            URL url;
            HttpURLConnection urlConnection = null;
            String resultString = "";
            JSONObject result;

            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    resultString += current;
                    data = reader.read();
                }
                result = new JSONObject(resultString);
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static ArrayList parseGoogleParse(JSONObject jsonObject) {
        ArrayList<GPlace> parseList = new ArrayList();
        try {
            if (jsonObject.has("results")) {
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    GPlace poi = new GPlace();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                        poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));
                        if (jsonArray.getJSONObject(i).has("opening_hours")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                    poi.setOpenNow("YES");
                                } else {
                                    poi.setOpenNow("NO");
                                }
                            }
                        } else {
                            poi.setOpenNow("Not Known");
                        }
                        if (jsonArray.getJSONObject(i).has("geometry"))
                        {
                            if (jsonArray.getJSONObject(i).getJSONObject("geometry").has("location"))
                            {
                                if (jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").has("lat"))
                                {
                                    poi.setLatLng(Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat")), Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng")));
                                }
                            }
                        }
                        if (jsonArray.getJSONObject(i).has("vicinity")) {
                            poi.setVicinity(jsonArray.getJSONObject(i).optString("vicinity"));
                        }
                        if (jsonArray.getJSONObject(i).has("types")) {
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");
                            for (int j = 0; j < typesArray.length(); j++) {
                                poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                            }
                        }
                    }
                    parseList.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
        return parseList;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.centerImage:
                if (activitySelect == null && rangeSelect == null) {
                    Toast.makeText(Welcome.this, "Please select an activity and range!", Toast.LENGTH_LONG).show();
                } else if (activitySelect == null) {
                    Toast.makeText(Welcome.this, "Please select an activity!", Toast.LENGTH_LONG).show();
                } else if (rangeSelect == null) {
                    Toast.makeText(Welcome.this, "Please select a range!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        goAction();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.walkFAM:
                Toast.makeText(Welcome.this, Range.WALK.toString(), Toast.LENGTH_SHORT).show();
                rangeSelect = Range.WALK;
                break;
            case R.id.driveFAM:
                Toast.makeText(Welcome.this, Range.DRIVE.toString(), Toast.LENGTH_SHORT).show();
                rangeSelect = Range.DRIVE;
                break;
            case R.id.travelFAM:
                Toast.makeText(Welcome.this, Range.TRAVEL.toString(), Toast.LENGTH_SHORT).show();
                rangeSelect = Range.TRAVEL;
                break;
            case R.id.ptgACT:
                Toast.makeText(Welcome.this, ActivityDo.PLACESTOGO.toString(), Toast.LENGTH_SHORT).show();
                activitySelect = ActivityDo.PLACESTOGO;
                break;
            case R.id.ttdACT:
                Toast.makeText(Welcome.this, ActivityDo.THINGSTODO.toString(), Toast.LENGTH_SHORT).show();
                activitySelect = ActivityDo.THINGSTODO;;
                break;
            case R.id.steACT:
                Toast.makeText(Welcome.this, ActivityDo.STUFFTOEAT.toString(), Toast.LENGTH_SHORT).show();
                activitySelect = ActivityDo.STUFFTOEAT;
                break;
        }
    }

}
