package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Descriptors for the various EditTexts
    private TextView mTextViewProductionTitle;
    private TextView mTextViewShortBreak;
    private TextView mTextViewLongBreak;
    private TextView mTextViewCycles;

    // EditTexts for the user to set the various timers
    private EditText mEditTextProductionTime;
    private EditText mEditTextShortBreak;
    private EditText mEditTextLongBreak;
    private EditText mEditTextCycles;

    // Button to set and Navigate to Timer
    private Button mButtonToTimer;

    // Information for the timer
    private String productionTimeInput;
    private String shortBreakInput;
    private String longBreakInput;
    private String cyclesInput;
    private Bundle infoBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewProductionTitle = findViewById(R.id.text_title_production_time);
        mTextViewShortBreak = findViewById(R.id.text_title_short_break);
        mTextViewLongBreak = findViewById(R.id.text_title_long_break);
        mTextViewCycles = findViewById(R.id.text_title_cycle);

        mEditTextProductionTime = findViewById(R.id.text_production_time);
        mEditTextShortBreak = findViewById(R.id.text_short_break);
        mEditTextLongBreak = findViewById(R.id.text_long_break);
        mEditTextCycles = findViewById(R.id.text_cycles);

        mButtonToTimer = findViewById(R.id.button_to_timer);
        infoBundle = new Bundle();

        mButtonToTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                getInputInformation();

                // Check that all fields are filled - Alert Dialog 1
                if (productionTimeInput.isEmpty() || shortBreakInput.isEmpty() || longBreakInput.isEmpty() || cyclesInput.isEmpty()) {
                    showAlertDialog("Please enter all values", "Empty Fields");
                }

                // Check that all minutes are between 1 and 99 - Alert Dialog 2
                else if ((Integer.parseInt(productionTimeInput) < 1 || Integer.parseInt(productionTimeInput) > 99) ||
                        (Integer.parseInt(shortBreakInput) < 1 || Integer.parseInt(shortBreakInput) > 99) ||
                        (Integer.parseInt(longBreakInput) < 1 || Integer.parseInt(longBreakInput) > 99)) {
                    showAlertDialog("Please enter between 1 and 99 minutes", "Minutes");
                }

                // Check that all cycles are between 1 and 10 - Alert Dialog 3
                else if ((Integer.parseInt(cyclesInput) < 1 || Integer.parseInt(cyclesInput) > 10)) {
                    showAlertDialog("Please enter between 1 and 10 cycles", "Cycles");
                }

                else {
                    generateBundle(infoBundle);
                    intent.putExtras(infoBundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void showAlertDialog(String message, String title) {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        dialog.show();
    }

    public void getInputInformation() {
        productionTimeInput = mEditTextProductionTime.getText().toString().trim();
        shortBreakInput = mEditTextShortBreak.getText().toString().trim();
        longBreakInput = mEditTextLongBreak.getText().toString().trim();
        cyclesInput = mEditTextCycles.getText().toString().trim();
    }

    public void generateBundle(Bundle b) {
        b.putString("productionTime", productionTimeInput);
        b.putString("shortBreak", shortBreakInput);
        b.putString("longBreak", longBreakInput);
        b.putString("cycles", cyclesInput);
    }

    // Getters and Setters

    public String getProductionTimeInput() {
        return productionTimeInput;
    }

    public String getShortBreakInput() {
        return shortBreakInput;
    }

    public String getLongBreakInput() {
        return longBreakInput;
    }

    public String getCyclesInput() {
        return cyclesInput;
    }
}
