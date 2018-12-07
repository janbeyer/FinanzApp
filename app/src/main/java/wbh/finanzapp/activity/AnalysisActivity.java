package wbh.finanzapp.activity;


import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wbh.finanzapp.R;
import wbh.finanzapp.access.TransactionsDataSource;
import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.AnalysisBean;
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

    private TextView textViewStartDate;
    private TextView textViewEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "--> Create ProfilesActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        transactionsDataSource = new TransactionsDataSource(
                this, ProfileMemory.getCurProfileBean().getId());

        textStartValue = this.findViewById(R.id.analysis_startValue);

        textViewStartDate = findViewById(R.id.text_view_start_date);
        textViewStartDate.setText(getFormattedDateAsString(startDate));


        textViewEndDate = findViewById(R.id.text_view_end_date);
        textViewEndDate.setText(getFormattedDateAsString(endDate));

        startButton = findViewById(R.id.button_start_analysis);
        startButton.setOnClickListener(view -> {
            List<AbstractBean> transactions = transactionsDataSource.getBeans();
            AnalysisBean analysisBean =
            AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

            // Build tables and diagrams here with the input of the analysis bean ...
            createPieChart(analysisBean);
            createIncomeChart(analysisBean);
            createExpenseChart(analysisBean);
        });

        prepareFormElements();
        transactionStates = new TransactionStates();
        refreshDateEditText();
    }

    private void createPieChart(AnalysisBean analysisBean) {
        AnalysisBean.CashFlow cashFlow = analysisBean.getTotal();
        Log.d(LOG_TAG, "--> CashFlow: " + cashFlow);
        Log.d(LOG_TAG, "--> Income  : " + cashFlow.getIncome());
        Log.d(LOG_TAG, "--> Expenses: " + cashFlow.getExpenses());
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(1000));
        entries.add(new PieEntry(950));
        PieDataSet pieDataSet = new PieDataSet(entries, "Income/Expenses");
        pieDataSet.setColors(Color.GREEN, Color.BLUE);
        pieDataSet.setValueTextSize(18);
        PieData pieData = new PieData(pieDataSet);

        PieChart pieChart = findViewById(R.id.pie_chart);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void createExpenseChart(AnalysisBean analysisBean) {
        float[] xValues =  {  1,  2,   3,   4,  5};
        float[] yValues = {100, 50, 300, 250, 70};
        List<BarEntry> entries = new ArrayList<>();
        int i = 0;
        for (Float data : xValues) {
            entries.add(new BarEntry(data, yValues[i++]));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Expenses");
        BarData barData = new BarData(dataSet);

        BarChart chart = findViewById(R.id.expenses_chart);
        chart.setData(barData);
        chart.invalidate(); // refresh chart
    }

    private void createIncomeChart(AnalysisBean analysisBean) {
        float[] dataNames =  {  1, 2, 3};
        float[] dataValues = {1000, 100, 50};
        List<BarEntry> entries = new ArrayList<>();
        int i = 0;
        for (Float data : dataNames) {
            // turn your data into Entry objects
            entries.add(new BarEntry(data, dataValues[i++]));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Income");
        BarData barData = new BarData(dataSet);

        BarChart chart = findViewById(R.id.income_chart);
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
                textViewStartDate.setText(getFormattedDateAsString(date));
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
                textViewEndDate.setText(getFormattedDateAsString(date));
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
        // set error for the beginning
        textStartValue.setError(
                getString(R.string.transaction_amount_validation_error));
        startButton.setEnabled(false);
        // StartValue.
        textStartValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence,
                                          int start, int count, int after) {
                String startValueStr = charSequence.toString();
                if (startValueStr.isEmpty() || startValueStr.equals("0") ||
                        startValueStr.equals("-") || startValueStr.endsWith(".")) {
                    textStartValue.setError(
                            getString(R.string.transaction_amount_validation_error));
                    startButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence,
                                      int start, int before, int count) {
                String startValueStr = charSequence.toString();
                if (startValueStr.isEmpty() || startValueStr.equals("0") ||
                        startValueStr.equals("-") || startValueStr.endsWith(".")) {
                    textStartValue.setError(
                            getString(R.string.transaction_amount_validation_error));
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
