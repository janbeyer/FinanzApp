package wbh.finanzapp.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * A Date selection dialog implemented as fragment.
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = DateDialog.class.getSimpleName();

    private TransactionsActivity.TransactionStates transactionStates;

    public void setTransactionStates(TransactionsActivity.TransactionStates transactionStates) {
        this.transactionStates = transactionStates;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month= calendar.get(Calendar.MONTH);
        int day= calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + ":" + month + ":" + year;
        Log.d(LOG_TAG, "--> Date: " + date);
        transactionStates.uniqueDate = new Date(year, month, dayOfMonth).getTime();
    }
}
