package wbh.finanzapp.util;

import android.util.Log;

import java.util.Date;

import wbh.finanzapp.R;
import wbh.finanzapp.activity.TransactionsActivity;

/**
 * Helper class for Transaction date states.
 */
public class TransactionStates {

    private static final String LOG_TAG = TransactionsActivity.class.getSimpleName();

    // state can be: 1=unique; 2=daily; 3=weekly;  4=monthly;  5=yearly
    private int state = 0;

    // 1 = Unique
    private Long uniqueDate = null;

    // 3 = Weekly
    private Integer dayOfWeek = null;

    // 4 = Monthly
    private Integer monthlyDay = null;

    // 5 = Yearly
    private Integer yearlyMonth = null;
    private Integer yearlyDay = null;

    public void setState(int rbDateMode) {
        if (rbDateMode == R.id.rb_unique) {
            Log.d(LOG_TAG, "--> The selected rb state: unique");
            state = 1;
            dayOfWeek = null;
            monthlyDay = null;
            yearlyMonth = null;
            yearlyDay = null;
        } else if (rbDateMode == R.id.rb_daily) {
            Log.d(LOG_TAG, "--> The selected rb state: daily");
            state = 2;
            uniqueDate = null;
            dayOfWeek = null;
            monthlyDay = null;
            yearlyMonth = null;
            yearlyDay = null;
        } else if (rbDateMode == R.id.rb_weekly) {
            Log.d(LOG_TAG, "--> The selected rb state: weekly");
            state = 3;
            uniqueDate = null;
            monthlyDay = null;
            yearlyMonth = null;
            yearlyDay = null;
        } else if (rbDateMode == R.id.rb_monthly) {
            Log.d(LOG_TAG, "--> The selected rb state: monthly");
            state = 4;
            uniqueDate = null;
            dayOfWeek = null;
            yearlyMonth = null;
            yearlyDay = null;
        } else if (rbDateMode == R.id.rb_yearly) {
            Log.d(LOG_TAG, "--> The selected rb state: yearly");
            state = 5;
            uniqueDate = null;
            dayOfWeek = null;
            monthlyDay = null;
        }
    }

    public void setUniqueDate(long uniqueDate) {
        this.uniqueDate = uniqueDate;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setMonthlyDay(int monthlyDay) {
        this.monthlyDay = monthlyDay;
    }

    public void setYearlyDay(int yearlyDay) {
        this.yearlyDay = yearlyDay;
    }

    public void setYearlyMonth(int yearlyMonth) {
        this.yearlyMonth = yearlyMonth;
    }

    public int getState() {
        return state;
    }

    public Long getUniqueDate() {
        return uniqueDate;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public Integer getMonthlyDay() {
        return monthlyDay;
    }

    public Integer getYearlyMonth() {
        return yearlyMonth;
    }

    public Integer getYearlyDay() {
        return yearlyDay;
    }

    @Override
    public String toString() {
        return state + " " + uniqueDate + " " + dayOfWeek + " " + monthlyDay + " " + yearlyMonth + " " + yearlyDay;
    }
}
