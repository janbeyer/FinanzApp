package wbh.finanzapp.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import wbh.finanzapp.R;
import wbh.finanzapp.activity.TransactionsActivity;

public class MonthPicker extends Dialog {

    private static final String LOG_TAG = MonthPicker.class.getSimpleName();

    private TransactionsActivity.TransactionStates transactionStates;

    public void setTransactionStates(TransactionsActivity.TransactionStates transactionStates) {
        this.transactionStates = transactionStates;
    }

    public MonthPicker(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "--> onCreateDialog()");
        setContentView(R.layout.dialog_month_picker);
        Spinner spinner = findViewById(R.id.month_picker_spinner);
        Integer[] month = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, month);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 0 ... 11 --> Jan ... Dez
                Log.d(LOG_TAG, "--> onItemSelected()" + position + " " + id);
                transactionStates.setDayOfWeek(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
