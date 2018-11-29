package wbh.finanzapp.activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import wbh.finanzapp.R;
import wbh.finanzapp.access.TransactionsDataSource;
import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.AnalysisBean;
import wbh.finanzapp.business.TransactionBean;
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
    private Date startDate = new Date(1514761200000L); // 01.01.2018.
    private Date endDate = new Date(1609369200000L); // 31.12.2020.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "--> Create ProfilesActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        transactionsDataSource = new TransactionsDataSource(this, ProfileMemory.getCurProfileBean().getId());
        startButton = findViewById(R.id.button_start_analysis);
        startButton.setOnClickListener(view -> {
            AnalysisBean analysisBean = createAnalysisBean();
            // TODO: Build tables and diagrams here with the input of the analysis bean ...
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

    private AnalysisBean createAnalysisBean() {
        AnalysisBean analysisBean = new AnalysisBean();
        List<AbstractBean> transactions = transactionsDataSource.getBeans();

        transactions.forEach(element -> {

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);

            TransactionBean transactionBean = (TransactionBean) element;

            int state = transactionBean.getState();

            if(state == 1) { // unique.
                Calendar uniqueCalendar = Calendar.getInstance();
                uniqueCalendar.setTime(new Date(transactionBean.getUniqueDate()));
                if((uniqueCalendar.equals(startCalendar) || uniqueCalendar.after(startCalendar)) && (uniqueCalendar.before(endCalendar) || uniqueCalendar.equals(endCalendar))) {
                    addTransactionsToAnalysisBean(analysisBean, transactionBean, 1);
                }
            } else if(state == 2) { // daily.
                int days = (int) TimeUnit.DAYS.convert((endDate.getTime() - startDate.getTime()), TimeUnit.MILLISECONDS);
                addTransactionsToAnalysisBean(analysisBean, transactionBean, days);
            } else if(state == 3) { // weekly.
                int dayOfWeek = transactionBean.getDayOfWeek();
                int daysOfWeek = 0;
                while(startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)) {
                    if(startCalendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                        daysOfWeek++;
                        startCalendar.add(Calendar.DATE, 7);
                    } else {
                        startCalendar.add(Calendar.DATE, 1);
                    }
                }
                addTransactionsToAnalysisBean(analysisBean, transactionBean, daysOfWeek);
            } else if(state == 4) { // monthly.
                int monthlyDay = transactionBean.getMonthlyDay();
                int startMonthlyDay = startCalendar.get(Calendar.DAY_OF_MONTH);
                int daysOfMonth = 0;
                while(startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)) {
                    if(startMonthlyDay <= monthlyDay) {
                        Calendar tmpDate = Calendar.getInstance();
                        tmpDate.setTime(startCalendar.getTime());
                        tmpDate.add(Calendar.DATE, monthlyDay - startMonthlyDay);
                        if(tmpDate.before(endCalendar) || tmpDate.equals(endCalendar)) {
                            daysOfMonth++;
                            startCalendar.add(Calendar.MONTH, 1);
                        }
                    } else if(monthlyDay < startMonthlyDay) {
                        startCalendar.add(Calendar.DATE, 1);
                    }
                }
                addTransactionsToAnalysisBean(analysisBean, transactionBean, daysOfMonth);
            } else if(state == 5) { // yearly.
                int yearlyMonth = transactionBean.getYearlyMonth();
                int yearlyDay = transactionBean.getYearlyDay();
                int monthOfYear = 0;
                while(startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)) {
                    if(startCalendar.get(Calendar.MONTH) < yearlyMonth) {
                        startCalendar.add(Calendar.MONTH, 1);
                    } else if(startCalendar.get(Calendar.DAY_OF_MONTH) < yearlyDay) {
                        startCalendar.add(Calendar.DATE, 1);
                    } else {
                        monthOfYear++;
                        startCalendar.add(Calendar.YEAR, 1);
                    }
                }
                addTransactionsToAnalysisBean(analysisBean, transactionBean, monthOfYear);
            }
        });

        return analysisBean;
    }

    private void addTransactionsToAnalysisBean(AnalysisBean analysisBean, TransactionBean transactionBean, int count) {
        long groupId = transactionBean.getGroupId();
        double amount = transactionBean.getAmount();

        AnalysisBean.CashFlow totalCF = analysisBean.getTotal();
        AnalysisBean.CashFlow groupCF = analysisBean.getGroups().get(groupId);
        if(groupCF == null) groupCF = new AnalysisBean.CashFlow();

        if(amount > 0) {
            AnalysisBean.Statistic totalStatistic = totalCF.getIncome();
            addStatisticToCashFlow(totalStatistic, count, amount);
            AnalysisBean.Statistic groupStatistic = groupCF.getIncome();
            addStatisticToCashFlow(groupStatistic, count, amount);
        } else {
            AnalysisBean.Statistic totalStatistic = totalCF.getExpenses();
            addStatisticToCashFlow(totalStatistic, count, amount * -1);
            AnalysisBean.Statistic groupStatistic = groupCF.getExpenses();
            addStatisticToCashFlow(groupStatistic, count, amount * -1);
        }

        analysisBean.getGroups().put(groupId, groupCF);
    }

    private void addStatisticToCashFlow(AnalysisBean.Statistic statistic, int count, double sum) {
        statistic.setCount(statistic.getCount() + count);
        statistic.setSum(statistic.getSum() + (count * sum));
    }
}
