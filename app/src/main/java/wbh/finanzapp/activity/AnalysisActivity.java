package wbh.finanzapp.activity;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import wbh.finanzapp.R;

/**
 * This activity has the ability to create a analysis of all transactions in an given
 * time interval.
 */
public class AnalysisActivity extends AbstractActivity {

    private static final String LOG_TAG = ProfilesActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "--> Create ProfilesActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        // profileDataSource = new ProfilesDataSource(this);

        Button buttonAddProfile = findViewById(R.id.button_start_analysis);
        buttonAddProfile.setOnClickListener(view -> {
            // createDialog(R.string.profile_add_title, new ProfilesActivity.AddListener(), false);
            Toast.makeText(this, "Analysis", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    protected int getHelpText() {
        return 0;
    }

}
