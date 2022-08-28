package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    // Countdown Text
    private TextView mTextViewCountDown;
    private TextView mTextViewTimerType;

    // Buttons
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button mButtonFullReset;

    // CountDown Timer object
    private CountDownTimer mCountDownTimer;

    // Flag to check whether or not the timer is running
    private boolean mTimerRunning;

    // Flags to check what type of timer is running
    private boolean productionRunning;
    private boolean shortBreakRunning;
    private boolean longBreakRunning;

    private long mProductionTime;
    private long mShortBreakTime;
    private long mLongBreakTime;
    private long mResetTime;

    // Denotes the Cycles which are production-shortBreak pairs before a long break
    private int startCycles;
    private int remainingCycles;

    // Time left in the timer
    private long mTimeLeftInMillis;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (getIntent().getExtras() != null) {
            Bundle timerInfo = getIntent().getExtras();
            getTimerInfo(timerInfo);
        }
        initTimerInfo();

        mTextViewTimerType = findViewById(R.id.text_view_timer_type);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        mButtonFullReset = findViewById(R.id.button_full_reset);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        mButtonFullReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullResetTimer();
            }
        });

        updateCountDownText();
    }

    // Gets timer information from the previous activity
    private void getTimerInfo(Bundle timerInfo) {
        if (timerInfo != null) {
            mProductionTime = HelperMethods.StringToMillis(timerInfo.getString("productionTime"));
            mShortBreakTime = HelperMethods.StringToMillis(timerInfo.getString("shortBreak"));
            mLongBreakTime = HelperMethods.StringToMillis(timerInfo.getString("longBreak"));
            startCycles = Integer.parseInt(timerInfo.getString("cycles"));
        }
    }

    // Sets up the initial timer parameters
    private void initTimerInfo() {
        if (!productionRunning && !shortBreakRunning && !longBreakRunning && !mTimerRunning) {

            productionRunning = true;
            mTimeLeftInMillis = mProductionTime;
            mResetTime = mProductionTime;

            remainingCycles = startCycles;
        }
    }

    // Starts the timer <tested>
    private void startTimer() {

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                changeTimerType();
                updateCountDownText();
                mButtonStartPause.setText("Start");
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("Pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    /* Changes type of timer to the next type of timer
    *  Production --> Short Break
    *  Short Break --> Production
    *  Short Break --> Long Break
    *  Long Break --> Finished
    *  */
    private void changeTimerType() {
        // Case 1: Production has finished. Changes timer type to Short Break
        if (productionRunning == true) {
            productionRunning = false;
            shortBreakRunning = true;

            mTextViewTimerType.setText("Short Break");
            mTimeLeftInMillis = mShortBreakTime;
            mResetTime = mShortBreakTime;

            remainingCycles--;
        }

        // Case 2: Short break has finished and there are still cycles remaining. Changes timer type to Production
        else if (shortBreakRunning == true && remainingCycles != 0) {
            shortBreakRunning = false;
            productionRunning = true;

            mTextViewTimerType.setText("Be Productive");
            mTimeLeftInMillis = mProductionTime;
            mResetTime = mProductionTime;
        }

        // Case 3: Short Break has finished and there are no cycles remaining. Changes timer type to Long Break
        else if (shortBreakRunning == true && remainingCycles == 0) {
            shortBreakRunning = false;
            longBreakRunning = true;

            mTextViewTimerType.setText("Long Break");
            mTimeLeftInMillis = mLongBreakTime;
            mResetTime = mLongBreakTime;
        }

        // Case 4: Long Break has finished and there are no cycles remaining.
        else if (longBreakRunning == true && remainingCycles == 0) {
            longBreakRunning = false;
            mTextViewTimerType.setText("Good Job!");
            mButtonStartPause.setVisibility(View.INVISIBLE);
            mButtonFullReset.setVisibility(View.VISIBLE);
        }

    }

    // Stops the timer and changes the button text to Start <tested>
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;

        // change button test to start
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    // Resets the timer back to the original start time. <tested>
    private void resetTimer() {
        mTimeLeftInMillis = mResetTime;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
    }

    // Resets the timer back to the initial productive period
    private void fullResetTimer() {
        initTimerInfo();
        mButtonFullReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        mTextViewTimerType.setText("Be Productive");
        updateCountDownText();
    }

    // Updates the timer countdown
    private void updateCountDownText() {

        String timeLeftFormatted = HelperMethods.MillisToString(mTimeLeftInMillis);
        mTextViewCountDown.setText((timeLeftFormatted));

    }



    // Getters and Setters for testing

    public boolean ismTimerRunning() {
        return mTimerRunning;
    }

    public boolean isProductionRunning() {
        return productionRunning;
    }

    public boolean isShortBreakRunning() {
        return shortBreakRunning;
    }

    public boolean isLongBreakRunning() {
        return longBreakRunning;
    }

    public long getmProductionTime() {
        return mProductionTime;
    }

    public void setmProductionTime(long mProductionTime) {
        this.mProductionTime = mProductionTime;
    }

    public long getmShortBreakTime() {
        return mShortBreakTime;
    }

    public void setmShortBreakTime(long mShortBreakTime) {
        this.mShortBreakTime = mShortBreakTime;
    }

    public long getmLongBreakTime() {
        return mLongBreakTime;
    }

    public void setmLongBreakTime(long mLongBreakTime) {
        this.mLongBreakTime = mLongBreakTime;
    }

    public int getStartCycles() {
        return startCycles;
    }

    public void setStartCycles(int initCycles) {
        this.startCycles = initCycles;
    }

    public int getRemainingCycles() {
        return remainingCycles;
    }

    public void setRemainingCycles(int remainingCycles) {
        this.remainingCycles = remainingCycles;
    }

    public long getmTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    public void setmTimeLeftInMillis(long mTimeLeftInMillis) {
        this.mTimeLeftInMillis = mTimeLeftInMillis;
    }

    public long getmResetTime() {
        return mResetTime;
    }

    public void setmResetTime(long mResetTime) {
        this.mResetTime = mResetTime;
    }

}