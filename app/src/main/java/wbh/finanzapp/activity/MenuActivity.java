package wbh.finanzapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import wbh.finanzapp.R;
import wbh.finanzapp.access.ProfilesDataSource;
import wbh.finanzapp.business.ProfileBean;

public class MenuActivity extends AppCompatActivity {

    private static final String LOG_TAG = MenuActivity.class.getSimpleName();

    public static final String PARAM_PROFILE_ID = "profileId";

    private ProfilesDataSource profileDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        profileDataSource = new ProfilesDataSource(this);
        init();
        activateButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "--> Open the data source.");
        profileDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "--> Close the data source.");
        profileDataSource.close();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            // TODO use Shared preferences
            long profileId = (Long) bundle.get(PARAM_PROFILE_ID);
            ProfileBean profile = profileDataSource.getProfile(profileId);
            this.setTitle(profile.getName());
        }
    }

    private void activateButtons() {
        Button buttonTransaction = findViewById(R.id.menu_button_transaction);
        //noinspection CodeBlock2Expr
        buttonTransaction.setOnClickListener(view -> {
            Toast.makeText(this, "TODO: Open Transaktion Activity.", Toast.LENGTH_SHORT).show();
        });
        Button buttonAnalyze = findViewById(R.id.menu_button_analyze);
        //noinspection CodeBlock2Expr
        buttonAnalyze.setOnClickListener(view -> {
            Toast.makeText(this, "TODO: Open Analyze Activity.", Toast.LENGTH_SHORT).show();
        });
        Button buttonGroups = findViewById(R.id.menu_button_groups);
        buttonGroups.setOnClickListener(view -> {
            Intent myIntent = new Intent(MenuActivity.this, GroupsActivity.class);
            Log.d(LOG_TAG, "--> Start the groups activity.");
            MenuActivity.this.startActivity(myIntent);
        });
        Button buttonHelp = findViewById(R.id.menu_button_help);
        //noinspection CodeBlock2Expr
        buttonHelp.setOnClickListener(view -> {
            Toast.makeText(this, "TODO: Open Help Activity.", Toast.LENGTH_SHORT).show();
        });
    }
}