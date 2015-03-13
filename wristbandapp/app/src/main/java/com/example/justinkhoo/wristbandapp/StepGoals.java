package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class StepGoals extends Activity {

    TextView stepsNeededTextView;
    EditText currentWeightEditText;
    EditText caloriesToLoseEditText;
    Button calculateStepsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_goals);

        stepsNeededTextView = (TextView) findViewById(R.id.stepsNeededTextView);
        currentWeightEditText = (EditText) findViewById(R.id.currentWeightEditText);
        caloriesToLoseEditText = (EditText) findViewById(R.id.caloriesToLoseEditText);
        calculateStepsButton = (Button) findViewById(R.id.calculateStepsButton);
        stepsNeededTextView.setVisibility(View.GONE);
    }

    public void calculateStepsNeeded(View view) {
        if(currentWeightEditText.getText().toString().equals("") || caloriesToLoseEditText.getText().toString().equals("")) {
            return;
        }
        else {
            double caloriesPerMile = Integer.parseInt(currentWeightEditText.getText().toString()) * 0.57;
            int stepsPerMile = 2200;
            double caloriesPerStep = caloriesPerMile / stepsPerMile;
            double totalStepsNeeded = Math.ceil(Integer.parseInt(caloriesToLoseEditText.getText().toString()) / caloriesPerStep);
            stepsNeededTextView.setText("You need to walk " + totalStepsNeeded + " steps to burn " + caloriesToLoseEditText.getText().toString() + " calories.");
            stepsNeededTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.step_goals, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
