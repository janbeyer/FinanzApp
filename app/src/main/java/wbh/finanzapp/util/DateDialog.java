package wbh.finanzapp.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * A Date selection dialog implemented as fragment.
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = DateDialog.class.getSimpleName();

    private TransactionStates transactionStates;

    public void setTransactionStates(TransactionStates transactionStates) {
        this.transactionStates = transactionStates;
    }

    View.OnClickListener listener;

   public void setListener(View.OnClickListener listener) {
       this.listener = listener;
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
        String dateStr = dayOfMonth + ":" + month + ":" + year;
        Log.d(LOG_TAG, "--> Date: " + dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date date = calendar.getTime();
        Log.d(LOG_TAG, "--> Date: " + date);
        transactionStates.setUniqueDate(date.getTime());
        if(listener != null) {
            listener.onClick(view);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


}
