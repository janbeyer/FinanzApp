package wbh.finanzapp.activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import wbh.finanzapp.R;
import wbh.finanzapp.access.TransactionsDataSource;
import wbh.finanzapp.util.ProfileMemory;

/**
 * This activity has the ability to create a analysis of all transactions in an given
 * time interval.
 */
public class AnalysisActivity extends AbstractActivity {

    private static final String LOG_TAG = ProfilesActivity.class.getSimpleName();

    private TransactionsDataSource transactionsDataSource;

    private Button startButton;
    private EditText textStartValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "--> Create ProfilesActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        transactionsDataSource = new TransactionsDataSource(this, ProfileMemory.getCurProfileBean().getId());
        startButton = findViewById(R.id.button_start_analysis);
        startButton.setOnClickListener(view -> {
            Toast.makeText(this, "Start the analysis", Toast.LENGTH_LONG).show();
        });

        prepareFormElements();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "--> Resume AnalysisActivity");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "--> Pause AnalysisActivity");
        super.onPause();
    }

    @Override
    protected int getHelpText() {
        return R.string.help_analysis_text;
    }

    private void prepareFormElements() {
        // StartValue.
        textStartValue = this.findViewById(R.id.analysis_startValue);
        textStartValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String startValueStr = charSequence.toString();

                if (startValueStr.isEmpty() || startValueStr.equals("-") || startValueStr.endsWith(".")) {
                    textStartValue.setError(getString(R.string.transaction_amount_validation_error));
                    startButton.setEnabled(false);
                } else {
                    textStartValue.setError(null);
                    enableStartButtonIfErrorFree(getWindow().getDecorView().getRootView());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String startValueStr = editable.toString();

                Double startValue = null;
                try { startValue = Double.parseDouble(startValueStr); } catch (NumberFormatException e) { e.printStackTrace(); }

                if (startValue != null) {
                    int decimalAmount = (int) (startValue * 100);
                    if ((startValue * 100) != ((double) decimalAmount)) {
                        String newText = "" + ((double) decimalAmount / 100);
                        textStartValue.setText(newText);
                        textStartValue.setSelection(newText.length());
                    }
                }
            }
        });
    }

    private void enableStartButtonIfErrorFree(View view) {
        if(checkIfViewIsErrorFree(view)) {
            startButton.setEnabled(true);
        }
    }

}
