package wbh.finanzapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import wbh.finanzapp.R;

public class MenuActivity extends AbstractActivity {

    private static final String LOG_TAG = MenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "--> onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        activateButtons();
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "--> onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "--> onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "--> onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "--> onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "--> onDestroy()");
        super.onDestroy();
    }

    @SuppressWarnings("CodeBlock2Expr")
    private void activateButtons() {
        Button buttonTransaction = findViewById(R.id.menu_button_transaction);
        buttonTransaction.setOnClickListener(view -> {
            Intent myIntent = new Intent(this, TransactionsActivity.class);
            Log.d(LOG_TAG, "--> Start the groups activity.");
            startActivity(myIntent);
        });
        Button buttonAnalyze = findViewById(R.id.menu_button_analyze);
        buttonAnalyze.setOnClickListener(view -> {
            Intent myIntent = new Intent(this, AnalysisActivity.class);
            Log.d(LOG_TAG, "--> Start the analysis activity.");
            startActivity(myIntent);
        });
        Button buttonGroups = findViewById(R.id.menu_button_groups);
        buttonGroups.setOnClickListener(view -> {
            Intent myIntent = new Intent(this, GroupsActivity.class);
            Log.d(LOG_TAG, "--> Start the groups activity.");
            startActivity(myIntent);
        });
    }

    protected int getHelpText() {
        return R.string.help_menu_text;
    }
}