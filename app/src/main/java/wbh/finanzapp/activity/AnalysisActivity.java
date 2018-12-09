package wbh.finanzapp.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    private EditText textStartValue;

    // current day
    private Calendar calendar = Calendar.getInstance();

    // 01.01.2018.
    private Date startDate;

    // current date.
    private Date endDate   = calendar.getTime();

    private TransactionStates transactionStates;

    private TextView textViewStartDate;
    private TextView textViewEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "--> Create ProfilesActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        calendar.set(2018, 0, 1);
        startDate = calendar.getTime();

        transactionsDataSource = new TransactionsDataSource(
                this, ProfileMemory.getCurProfileBean().getId());

        textStartValue = this.findViewById(R.id.start_value_edit_text);

        textViewStartDate = findViewById(R.id.text_view_start_date);
        textViewStartDate.setText(getFormattedDateAsString(startDate));


        textViewEndDate = findViewById(R.id.text_view_end_date);
        textViewEndDate.setText(getFormattedDateAsString(endDate));

        Button startButton = findViewById(R.id.button_start_analysis);
        startButton.setOnClickListener(view -> {
            hideKeyboard(this);

            List<AbstractBean> transactions = transactionsDataSource.getBeans();
            AnalysisBean analysisBean =
            AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

            // Build tables and diagrams here with the input of the analysis bean ...
            createPieChart(analysisBean);

            Log.d(LOG_TAG, "--> TransactionNames: ");

            List<String> transactionList = new ArrayList<>();

            float[] values = createListDate(analysisBean, transactionList);

            // The list view represent all transactions with there count and amount within the given period.
            createListView(transactionList, R.id.list_view_analysis);

            createIncomeExpenseChart(values);
        });

        prepareFormElements();
        transactionStates = new TransactionStates();
        refreshDateEditText();
    }

    /**
     * Create the list date and return it as a float array.
     */
    private float[] createListDate(AnalysisBean analysisBean, List<String> transactionList) {
        List<Long>   groupIds = analysisBean.getGroupIds();
        Map<Long, AnalysisBean.CashFlow> map = analysisBean.getGroups();
        int i = 1;
        int j = 0;
        float[] values = new float[map.size()];
        for (String item: analysisBean.getTransactionNames()) {
            Long groupId;
            if(j < map.size()) {
                groupId = groupIds.get(j);
                AnalysisBean.CashFlow cashFlow = map.get(groupId);
                AnalysisBean.Statistic statistic = cashFlow.getIncome();
                double sum = statistic.getSum();
                if(sum == 0) {
                    statistic = cashFlow.getExpenses();
                }
                String str = (i++) + ". " +  item + " = " + statistic;
                Log.d(LOG_TAG, "--> " + str);
                transactionList.add(str);
                values[j] = (float) statistic.getSum();
                j++;
            }
        }
        return values;
    }

    /**
     * Create the PieChart and show the income and the expenses.
     */
    private void createPieChart(AnalysisBean analysisBean) {
        AnalysisBean.CashFlow cashFlow = analysisBean.getTotal();
        Log.d(LOG_TAG, "--> CashFlow: " + cashFlow);
        Log.d(LOG_TAG, "--> Income  : " + cashFlow.getIncome());
        Log.d(LOG_TAG, "--> Expenses: " + cashFlow.getExpenses());
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) cashFlow.getIncome().getSum()));
        entries.add(new PieEntry((float)cashFlow.getExpenses().getSum() * -1));
        PieDataSet pieDataSet = new PieDataSet(entries, "Income/Expenses");
        pieDataSet.setColors(Color.GREEN, Color.RED);
        pieDataSet.setValueTextSize(18);
        PieData pieData = new PieData(pieDataSet);
        PieChart pieChart = findViewById(R.id.pie_chart);
        pieChart.setData(pieData);
        pieChart.setDescription(null);
        Legend legend = pieChart.getLegend();
        legend.setTextSize(14);
        pieChart.invalidate();
    }

    /**
     * Create an BarChar and show the income and expenses groupd by there category.
     */
    private void createIncomeExpenseChart(float[] values) {
        int  [] colors = new int[values.length];
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i < values.length; ++i) {
            entries.add(new BarEntry(i, values[i-1]));
            if(values[i-1] >= 0) {
                colors[i-1] = Color.GREEN;
            } else {
                colors[i-1] = Color.RED;
            }
        }
        BarDataSet dataSet = new BarDataSet(entries, "Income/Expenses");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(16);
        BarData barData = new BarData(dataSet);
        BarChart chart = findViewById(R.id.income_expenses_chart);
        chart.setData(barData);
        chart.setDescription(null);
        
        Legend legend = chart.getLegend();
        legend.setTextSize(14);

        chart.invalidate(); // refresh chart
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
    public static String getFormattedDateAsString(Date date) {
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
                startDate = date;
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
                endDate = date;
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
        textStartValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
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
}
