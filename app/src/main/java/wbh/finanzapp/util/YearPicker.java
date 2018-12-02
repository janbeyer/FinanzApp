package wbh.finanzapp.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import wbh.finanzapp.R;
import wbh.finanzapp.activity.TransactionsActivity;

public class YearPicker extends Dialog {

    private static final String LOG_TAG = YearPicker.class.getSimpleName();

    private TransactionsActivity.TransactionStates transactionStates;

    public void setTransactionStates(TransactionsActivity.TransactionStates transactionStates) {
        this.transactionStates = transactionStates;
    }

    public YearPicker(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "--> onCreateDialog()");
        setContentView(R.layout.dialog_year_picker);
    }
}
