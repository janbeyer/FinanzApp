package wbh.finanzapp.activity;


import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wbh.finanzapp.R;
import wbh.finanzapp.access.TransactionsDataSource;
import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.util.AnalysisCalculation;
import wbh.finanzapp.util.DateDialog;
import wbh.finanzapp.util.ProfileMemory;
import wbh.finanzapp.util.TransactionStates;

/**
 * This activity has the ability to create a analysis of all transactions in an given
 * time interval.
 */
public class AnalysisActivity extends AbstractActivity {

    private static final String LOG_TAG = ProfilesActivity.class.getSimpleName();

    private TransactionsDataSource transactionsDataSource;

    private Button startButton;
    private EditText textStartValue;
    private Date startDate = new Date(1514761200000L); // 01.01.2018.
    private Date endDate = new Date(1609369200000L); // 31.12.2020.

    private TransactionStates transactionStates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "--> Create ProfilesActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        transactionsDataSource = new TransactionsDataSource(this, ProfileMemory.getCurProfileBean().getId());
        startButton = findViewById(R.id.button_start_analysis);
        startButton.setOnClickListener(view -> {
            List<AbstractBean> transactions = transactionsDataSource.getBeans();
            // AnalysisBean analysisBean =
            AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

            // TODO: Build tables and diagrams here with the input of the analysis bean ...

            createIncomeChart();
            createExpenseChart();
        });

        prepareFormElements();
        transactionStates = new TransactionStates();
        refreshDateEditText();
    }

    private void createExpenseChart() {
        float[] xValues =  {  1,  2,   3,   4,  5};
        float[] yValues = {100, 50, 300, 250, 70};
        List<BarEntry> entries = new ArrayList<>();
        int i = 0;
        for (Float data : xValues) {
            entries.add(new BarEntry(data, yValues[i++]));
        }
        BarChart chart = findViewById(R.id.expenses);
        BarDataSet dataSet = new BarDataSet(entries, "Expenses");
        BarData barData = new BarData(dataSet);
        chart.setData(barData);
        chart.invalidate(); // refresh chart
    }

    private void createIncomeChart() {
        float[] dataNames =  {  1, 2, 3};
        float[] dataValues = {1000, 100, 50};
        List<BarEntry> entries = new ArrayList<>();
        int i = 0;
        for (Float data : dataNames) {
            // turn your data into Entry objects
            entries.add(new BarEntry(data, dataValues[i++]));
        }
        BarChart chart = findViewById(R.id.income);
        BarDataSet dataSet = new BarDataSet(entries, "Income");
        BarData barData = new BarData(dataSet);
        chart.setData(barData);
        chart.invalidate(); // refresh chart
    }

    private void createDateDialog(DateDialog dateDialog) {
        Log.d(LOG_TAG, "--> createDateDialog()");
        dateDialog.setTransactionStates(transactionStates);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        dateDialog.show(transaction, "Date Dialog");
    }

    /**
     * Create a formatted date an return it as a string.
     */
    private String getFormattedDateAsString(Date date) {
        // TODO get local date format
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return simpleDateFormat.format(date);
    }

    public void refreshDateEditText() {
        Log.d(LOG_TAG, "--> refreshDateEditText()");
        ImageButton button = findViewById(R.id.b_start_date);
        button.setOnClickListener(v -> {
            DateDialog dateDialog = new DateDialog();
            createDateDialog(dateDialog);
            dateDialog.setListener(v1 -> {
                if (transactionStates.uniqueDate == 0)
                    return;
                Date date = new Date(transactionStates.uniqueDate);
                Log.d(LOG_TAG, "--> Refresh date: " + date);
                TextView editText = findViewById(R.id.tv_start_date);
                editText.setText(getFormattedDateAsString(date));
            });
        });


        ImageButton button2 = findViewById(R.id.b_end_date);
        button2.setOnClickListener(v -> {
            DateDialog dateDialog = new DateDialog();
            createDateDialog(dateDialog);
            dateDialog.setListener(v12 -> {
                if (transactionStates.uniqueDate == 0)
                    return;
                Date date = new Date(transactionStates.uniqueDate);
                Log.d(LOG_TAG, "--> Refresh date: " + date);
                TextView editText = findViewById(R.id.tv_end_date);
                editText.setText(getFormattedDateAsString(date));
            });
        });
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
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

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
                try {
                    startValue = Double.parseDouble(startValueStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

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
        if (checkIfViewIsErrorFree(view)) {
            startButton.setEnabled(true);
        }
    }
}
