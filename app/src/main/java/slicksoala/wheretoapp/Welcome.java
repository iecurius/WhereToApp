package slicksoala.wheretoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Welcome extends AppCompatActivity implements View.OnClickListener{

    final int radiusW = 1000;
    final int radiusD = 16093;
    final int radiusT = 48280;

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
        /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Welcome.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }*/
        /*LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currLat = Double.toString(location.getLatitude());
        currLong = Double.toString(location.getLongitude());*/
        currLat = "33.78508547";
        currLong = "-84.3879824";

        if (rangeSelect == Range.WALK)
            rad = radiusW;
        else if (rangeSelect == Range.DRIVE)
            rad = radiusD;
        else
            rad = radiusT;

        GooglePlacesListTask task = new GooglePlacesListTask();
        placesList = task.execute(currLat, currLong, Integer.toString(rad), activitySelect.toString()).get();
        Place placeToGo = (Place) placesList.get(new Random().nextInt(placesList.size()));
        System.out.println("PLACE NAME:" + placeToGo.getName());
        

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
