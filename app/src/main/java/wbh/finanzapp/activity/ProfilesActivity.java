package wbh.finanzapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import wbh.finanzapp.R;
import wbh.finanzapp.access.ProfilesDataSource;
import wbh.finanzapp.business.ProfileBean;

public class ProfilesActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfilesActivity.class.getSimpleName();

    private ProfilesDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        dataSource = new ProfilesDataSource(this);

        activateAddButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "--> Die Datenquelle wird geöffnet.");
        dataSource.open();

        showAllListEntries();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "--> Die Datenquelle wird geschlossen.");
        dataSource.close();
    }

    private void showAllListEntries() {
        List<ProfileBean> profiles = dataSource.getAllProfiles();

        ArrayAdapter<ProfileBean> profileArrayAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            profiles);

        ListView profilesListView = (ListView) findViewById(R.id.listview_profiles);
        profilesListView.setAdapter(profileArrayAdapter);
    }

    private void activateAddButton() {
        Button buttonAddProfile = (Button) findViewById(R.id.button_add_profile);

        buttonAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog addProfileDialog = createAddProfileDialog();
                addProfileDialog.show();
            }
        });
    }

    private AlertDialog createAddProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogsView = inflater.inflate(R.layout.dialog_add_profile, null);

        final EditText editTextNewName = (EditText) dialogsView.findViewById(R.id.profile_new_name);
        editTextNewName.setText("");

        final EditText editTextNewDescription = (EditText) dialogsView.findViewById(R.id.profile_new_description);
        editTextNewDescription.setText("");

        builder.setView(dialogsView)
            .setTitle(R.string.profile_add_title)
            .setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String name = editTextNewName.getText().toString();
                    String description = editTextNewDescription.getText().toString();

                    if((TextUtils.isEmpty(name))) {
                        editTextNewName.setError(getString(R.string.profile_name_error));
                        return;
                    }

                    ProfileBean newProfile = dataSource.insertProfile(name, description);
                    Log.d(LOG_TAG, "--> Neuer Eintrag: " + newProfile.toString());

                    showAllListEntries();
                    dialog.dismiss();
                }
            })
            .setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

        return builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

}
