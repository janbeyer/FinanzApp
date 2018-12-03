package wbh.finanzapp.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import wbh.finanzapp.R;

public class MonthPicker extends Dialog {

    private static final String LOG_TAG = MonthPicker.class.getSimpleName();

    private TransactionStates transactionStates;

    public MonthPicker(@NonNull Context context, TransactionStates transactionStates) {
        super(context);
        this.transactionStates = transactionStates;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "--> onCreateDialog()");
        setContentView(R.layout.dialog_month_picker);
        Spinner spinner = findViewById(R.id.month_picker_spinner);
        Integer[] dayMonth = new Integer[31];
        for (int i = 1; i <=31; ++i) {
            dayMonth[i-1] = i;
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, dayMonth);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 0 ... 31 --> 1 ... 31
                Log.d(LOG_TAG, "--> onItemSelected()" + position + " " + id);
                transactionStates.setMonthlyDay(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = findViewById(R.id.button_month_picker);
        button.setOnClickListener(v -> dismiss());
    }
}
