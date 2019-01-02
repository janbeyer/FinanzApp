package wbh.finanzapp.util;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import wbh.finanzapp.activity.AnalysisActivity;
import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.AnalysisBean;
import wbh.finanzapp.business.TransactionBean;

public class AnalysisCalculation {

    private static final String LOG_TAG = AnalysisCalculation.class.getSimpleName();

    /**
     * Create the analysis bean. Iterates over all transaction within an given start and end date.
     */
    public static AnalysisBean createAnalysisBean(
            Date startDate,
            Date endDate,
            List<AbstractBean> transactions) {
        Log.d(LOG_TAG, "--> createAnalysisBean() Date is between: " +
                AnalysisActivity.getFormattedDateAsString(startDate) + " to "
                + AnalysisActivity.getFormattedDateAsString(endDate));

        AnalysisBean analysisBean = new AnalysisBean(initCalendar(startDate), initCalendar(endDate));
        Log.d(LOG_TAG, "--> Iterate over each transaction count: " + transactions.size());
        // Iterate over each transaction and create the analysis bean.
        transactions.forEach(element -> {

            TransactionBean transactionBean = (TransactionBean) element;
            Log.d(LOG_TAG, "--> Iterate over: " + transactionBean);
            // set the transaction to the analyse
            analysisBean.getTransactionNames().add(transactionBean.getName());

            int state = transactionBean.getState();

            if (state == 1) { // unique.
                Log.d(LOG_TAG, "--> addUniqueTransaction.");
                addUniqueTransaction(analysisBean, transactionBean);
            } else if (state == 2) { // daily.
                Log.d(LOG_TAG, "--> addDailyTransaction.");
                addDailyTransaction(analysisBean, transactionBean, startDate, endDate);
            } else if (state == 3) { // weekly.
                Log.d(LOG_TAG, "--> addWeeklyTransaction.");
                addWeeklyTransaction(analysisBean, transactionBean);
            } else if (state == 4) { // monthly.
                Log.d(LOG_TAG, "--> addMonthlyTransaction.");
                addMonthlyTransaction(analysisBean, transactionBean);
            } else if (state == 5) { // yearly.
                Log.d(LOG_TAG, "--> addYearlyTransaction.");
                addYearlyTransaction(analysisBean, transactionBean);
            }
        });

        Log.d(LOG_TAG, "<-- analysis finished: " + analysisBean);
        return analysisBean;
    }

    /**
     * Create a new calendar object.
     */
    private static Calendar initCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Check if the unique date of the transaction bean is between the startCalendar and endCalendar.
     * If this is true, add this transaction to the analysisBean.
     */
    private static void addUniqueTransaction(
            AnalysisBean analysisBean,
            TransactionBean transactionBean) {

        Calendar startCalendar = analysisBean.getStartCalendar();
        Calendar endCalendar = analysisBean.getEndCalendar();
        Calendar uniqueCalendar = Calendar.getInstance();
        uniqueCalendar.setTime(new Date(transactionBean.getUniqueDate()));
        if ((uniqueCalendar.equals(startCalendar) ||
                uniqueCalendar.after(startCalendar)) &&
                (uniqueCalendar.before(endCalendar) || uniqueCalendar.equals(endCalendar))) {
            addTransactionsToAnalysisBean(analysisBean, transactionBean, 1);
        }
    }

    /**
     * Count the days between the startDate and the endDate.
     * Add the count of the transactions to the analysisBean.
     */
    private static void addDailyTransaction(
            AnalysisBean analysisBean,
            TransactionBean transactionBean,
            Date startDate,
            Date endDate) {
        int days = (int) TimeUnit.DAYS.convert((endDate.getTime() - startDate.getTime()), TimeUnit.MILLISECONDS) + 1;
        addTransactionsToAnalysisBean(analysisBean, transactionBean, days);
    }

    /**
     * Count the DAY_OF_WEEK between the startCalendar and the endCalendar.
     * It is important, that the DAY_OF_WEEK (e.g. Sunday = 1) of the transactionBean is equal to the CALENDAR.DAY_OF_WEEKS (see api).
     * Add the count of the transactions to the analysisBean.
     */
    private static void addWeeklyTransaction(
            AnalysisBean analysisBean,
            TransactionBean transactionBean) {

        Calendar startCalendar = analysisBean.getStartCalendar();
        Calendar endCalendar = analysisBean.getEndCalendar();
        // Mo - Su + 2 offset, Sunday + 2 = 8 --> 1 #TODO change insert order in database
        int dayOfWeek = transactionBean.getDayOfWeek() + 2;
        if (dayOfWeek == 8) {
            dayOfWeek = 1;
        }
        int daysOfWeek = 0;
        while (startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)) {
            if (startCalendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                daysOfWeek++;
                startCalendar.add(Calendar.DATE, 7);
            } else {
                startCalendar.add(Calendar.DATE, 1);
            }
        }
        addTransactionsToAnalysisBean(analysisBean, transactionBean, daysOfWeek);
    }

    /**
     * Count the dayOfMonth between the startCalendar and the endCalendar.
     * Add the count of the transactions to the analysisBean.
     */
    private static void addMonthlyTransaction(
            AnalysisBean analysisBean,
            TransactionBean transactionBean) {

        Calendar startCalendar = analysisBean.getStartCalendar();
        Calendar endCalendar = analysisBean.getEndCalendar();
        int monthlyDay = transactionBean.getMonthlyDay();
        int startMonthlyDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        // start by 1 because this is used as multiplier in addTransactionsToAnalysisBean
        int daysOfMonth = 1;
        while (startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)) {
            if (startMonthlyDay <= monthlyDay) {
                Calendar tmpDate = Calendar.getInstance();
                tmpDate.setTime(startCalendar.getTime());
                tmpDate.add(Calendar.DATE, monthlyDay - startMonthlyDay);
                if (tmpDate.before(endCalendar) || tmpDate.equals(endCalendar)) {
                    daysOfMonth++;
                    startCalendar.add(Calendar.MONTH, 1);
                } else {
                    break;
                }
            }
        }

        addTransactionsToAnalysisBean(analysisBean, transactionBean, daysOfMonth);
    }

    /**
     * Count the yearlyMonth and yearlyDate between the startCalendar and the endCalendar.
     * Add the count of the transactions to the analysisBean.
     */
    private static void addYearlyTransaction(
            AnalysisBean analysisBean,
            TransactionBean transactionBean) {
        Calendar startCalendar = analysisBean.getStartCalendar();
        Calendar endCalendar = analysisBean.getEndCalendar();
        int yearlyMonth = transactionBean.getYearlyMonth();
        int yearlyDay = transactionBean.getYearlyDay();
        // start by 1 because this is used as multiplier in addTransactionsToAnalysisBean
        int monthOfYear = 1;
        while (startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)) {
            if (startCalendar.get(Calendar.MONTH) < yearlyMonth) {
                startCalendar.add(Calendar.MONTH, 1);
            } else if (startCalendar.get(Calendar.DAY_OF_MONTH) < yearlyDay) {
                startCalendar.add(Calendar.DATE, 1);
            } else {
                monthOfYear++;
                startCalendar.add(Calendar.YEAR, 1);
            }
        }
        addTransactionsToAnalysisBean(analysisBean, transactionBean, monthOfYear);
    }

    /**
     * Add the transaction in an amount of <count> to the analysisBean.
     * Check if the transaction is an income or an expense.
     * Fill the analysisBean.
     */
    private static void addTransactionsToAnalysisBean(
            AnalysisBean analysisBean,
            TransactionBean transactionBean,
            int count) {

        if (count == 0) {
            Log.d(LOG_TAG, "--> addTransactionsToAnalysisBean Count: " + count);
            return;
        }

        long groupId = transactionBean.getGroupId();
        double amount = transactionBean.getAmount();

        Log.d(LOG_TAG, "--> addTransactionsToAnalysisBean Sum: " + count + "*" + amount);

        AnalysisBean.CashFlow totalCF = analysisBean.getTotal();
        AnalysisBean.CashFlow groupCF = analysisBean.getGroups().get(groupId);

        analysisBean.getGroupIds().add(groupId);

        if (groupCF == null) {
            groupCF = new AnalysisBean.CashFlow();
        }

        if (amount > 0) {
            AnalysisBean.Statistic totalStatistic = totalCF.getIncome();
            addStatisticToCashFlow(totalStatistic, count, amount);

            AnalysisBean.Statistic groupStatistic = groupCF.getIncome();
            addStatisticToCashFlow(groupStatistic, count, amount);
        } else {
            AnalysisBean.Statistic totalStatistic = totalCF.getExpenses();
            addStatisticToCashFlow(totalStatistic, count, amount);
            AnalysisBean.Statistic groupStatistic = groupCF.getExpenses();
            addStatisticToCashFlow(groupStatistic, count, amount);
        }

        analysisBean.getGroups().put(groupId, groupCF);
    }

    /**
     * Add the count and the amount of <count>*<amount> into the statistic object.
     */
    private static void addStatisticToCashFlow(AnalysisBean.Statistic statistic, int count, double sum) {
        statistic.setCount(statistic.getCount() + count);
        double a = count * sum;
        double b = statistic.getSum();
        statistic.setSum(a + b);
        Log.d(LOG_TAG, "--> addStatisticToCashFlow: " + b + " + " + a);
    }
}
