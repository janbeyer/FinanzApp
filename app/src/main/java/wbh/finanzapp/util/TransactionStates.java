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
    public int state = 0;

    // 1 = Unique --> DatePicker    --> Default current date
    public long uniqueDate = new Date().getTime();

    // 2 = Daily

    // 3 = Weekly --> Dropdown box (1 ...  7) --> Default is Monday
    public int dayOfWeek = 1;

    // 4 = Monthly--> Dropdown box (1 ... 31) --> Default is 1
    public int monthlyDay = 1;

    // 5 = Yearly --> Dropdown box (1 ... 12) --> Default is 1
    //            --> Dropdown box (1 ... 31) --> Default is 1
    public int yearlyMonth = 1;
    public int yearlyDay = 1;

    public void checkStates(int rbDateMode) {
        if (rbDateMode == R.id.rb_unique) {
            Log.d(LOG_TAG, "--> The selected rb state: unique");
            state = 1;
        } else if (rbDateMode == R.id.rb_daily) {
            Log.d(LOG_TAG, "--> The selected rb state: daily");
            state = 2;
        } else if (rbDateMode == R.id.rb_weekly) {
            Log.d(LOG_TAG, "--> The selected rb state: weekly");
            state = 3;
        } else if (rbDateMode == R.id.rb_monthly) {
            Log.d(LOG_TAG, "--> The selected rb state: monthly");
            state = 4;
        } else if (rbDateMode == R.id.rb_yearly) {
            Log.d(LOG_TAG, "--> The selected rb state: yearly");
            state = 5;
        }
    }

    void setUniqueDate(long uniqueDate) {
        this.uniqueDate = uniqueDate;
    }

    void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    void setMonthlyDay(int monthlyDay) {
        this.monthlyDay = monthlyDay;
    }

    void setYearlyDay(int yearlyDay) {
        this.yearlyDay = yearlyDay;
    }

    void setYearlyMonth(int yearlyMonth) {
        this.yearlyMonth = yearlyMonth;
    }

    @Override
    public String toString() {
        return state + " " + uniqueDate + " " + dayOfWeek + " " + monthlyDay + " " + yearlyMonth + " " + yearlyDay;
    }
}
