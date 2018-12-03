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

public class YearPicker extends Dialog {

    private static final String LOG_TAG = YearPicker.class.getSimpleName();

    private TransactionStates transactionStates;

    public YearPicker(@NonNull Context context, TransactionStates transactionStates) {
        super(context);
        this.transactionStates = transactionStates;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "--> onCreateDialog()");
        setContentView(R.layout.dialog_year_picker);

        Spinner spinner = findViewById(R.id.year_picker_spinner_day);
        Integer[] dayMonth = new Integer[31];
        for (int i = 1; i <=31; ++i) {
            dayMonth[i-1] = i;
        }

        // add the dropdown for the day of the month
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, dayMonth);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 0 ... 31 --> 1 ... 31
                Log.d(LOG_TAG, "--> onItemSelected()" + position + " " + id);
                transactionStates.setYearlyDay(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // add the dropdown for the month
        Spinner spinner2 = findViewById(R.id.year_picker_spinner_month);
        Integer[] month = new Integer[12];
        for (int i = 1; i <=12; ++i) {
            month[i-1] = i;
        }

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, month);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 0 ... 11 --> 1 ... 12
                Log.d(LOG_TAG, "--> onItemSelected()" + position + " " + id);
                transactionStates.setYearlyMonth(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = findViewById(R.id.button_year_picker);
        button.setOnClickListener(v -> dismiss());
    }
}
