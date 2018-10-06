package wbh.finanzapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import wbh.finanzapp.R;
import wbh.finanzapp.access.ProfilesDataSource;
import wbh.finanzapp.business.ProfileBean;

public class MenuActivity extends AppCompatActivity {

    private static final String LOG_TAG = MenuActivity.class.getSimpleName();

    public static final String PARAM_PROFILE_ID = "profileId";

    private static Context mContext;
    public static Context getContext() {
        return mContext;
    }

    private ProfilesDataSource profileDataSource;
    private ProfileBean profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
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
        long profileId = (Long) getIntent().getExtras().get(PARAM_PROFILE_ID);
        this.profile = profileDataSource.getProfile(profileId);
        this.setTitle(this.profile.getName());
    }

    private void activateButtons() {
        Button buttonTransaction = (Button) findViewById(R.id.menu_button_transaction);
        buttonTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "TODO: Open Transaktion Activity.", Toast.LENGTH_SHORT).show();
            }
        });
        Button buttonAnalyze = (Button) findViewById(R.id.menu_button_analyze);
        buttonAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "TODO: Open Analyze Activity.", Toast.LENGTH_SHORT).show();
            }
        });
        Button buttonGroups = (Button) findViewById(R.id.menu_button_groups);
        buttonGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "TODO: Open Groups Activity.", Toast.LENGTH_SHORT).show();
            }
        });
        Button buttonHelp = (Button) findViewById(R.id.menu_button_help);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "TODO: Open Help Activity.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}