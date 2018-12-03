package wbh.finanzapp.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import wbh.finanzapp.R;

public class WeekPicker extends Dialog {

    private static final String LOG_TAG = WeekPicker.class.getSimpleName();

    private TransactionStates transactionStates;

    enum DAYS {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public WeekPicker(Context context, TransactionStates transactionStates) {
        super(context);
        this.transactionStates = transactionStates;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "--> onCreateDialog()");
        setContentView(R.layout.dialog_week_picker);
        Spinner spinner = findViewById(R.id.week_picker_spinner);
        ArrayAdapter<DAYS> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, DAYS.values());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 0 ... 6 --> Monday ... Sunday
                Log.d(LOG_TAG, "--> onItemSelected()" + position + " " + id);
                transactionStates.setDayOfWeek(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = findViewById(R.id.button_week_picker);
        button.setOnClickListener(v -> dismiss());
    }

}
