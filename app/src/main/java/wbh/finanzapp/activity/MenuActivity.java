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

    private ProfileBean profileBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ProfilesDataSource profileDataSource = new ProfilesDataSource(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            Long profileId = (Long) bundle.get(PARAM_PROFILE_ID);
            if(profileId != null) {
                profileBean = (ProfileBean) profileDataSource.getProfile(profileId);
                this.setTitle(profileBean.getName());
            }
        }
        activateButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressWarnings("CodeBlock2Expr")
    private void activateButtons() {
        Button buttonTransaction = findViewById(R.id.menu_button_transaction);
        buttonTransaction.setOnClickListener(view -> {
            Toast.makeText(this, "TODO: Open Transaktion Activity.", Toast.LENGTH_SHORT).show();
        });
        Button buttonAnalyze = findViewById(R.id.menu_button_analyze);
        buttonAnalyze.setOnClickListener(view -> {
            Toast.makeText(this, "TODO: Open Analyze Activity.", Toast.LENGTH_SHORT).show();
        });
        Button buttonGroups = findViewById(R.id.menu_button_groups);
        buttonGroups.setOnClickListener(view -> {
            Intent myIntent = new Intent(this, GroupsActivity.class);
            Log.d(LOG_TAG, "--> Start the groups activity.");
            myIntent.putExtra(GroupsActivity.PARAM_PROFILE_ID, profileBean.getId());
            startActivity(myIntent);
        });
        Button buttonHelp = findViewById(R.id.menu_button_help);
        buttonHelp.setOnClickListener(view -> {
            Toast.makeText(this, "TODO: Open Help Activity.", Toast.LENGTH_SHORT).show();
        });
    }
}