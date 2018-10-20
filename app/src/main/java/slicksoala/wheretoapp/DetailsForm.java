package slicksoala.wheretoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class DetailsForm extends AppCompatActivity {
    private EditText returnTime;
    private Spinner radiusSpinner;
    private Spinner activitySpinner;
    private Spinner travelSpinner;
    private Spinner paceSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_form);
        returnTime = (EditText) findViewById(R.id.returnTime);
        radiusSpinner = findViewById(R.id.radiusSpinner);
        activitySpinner = findViewById(R.id.activitySpinner);
        travelSpinner = findViewById(R.id.travelSpinner);
        paceSpinner = findViewById(R.id.paceSpinner);

        ArrayAdapter<String> radiusAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Radius.values());
        radiusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radiusSpinner.setAdapter(radiusAdapter);

        ArrayAdapter<String> activityAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, ActivityDo.values());
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);

        ArrayAdapter<String> travelAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Range.values());
        travelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        travelSpinner.setAdapter(travelAdapter);

        ArrayAdapter<String> paceAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Pace.values());
        paceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paceSpinner.setAdapter(paceAdapter);
    }
}
