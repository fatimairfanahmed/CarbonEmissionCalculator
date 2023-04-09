package bmc.swe.carbonemissioncalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Retrieve the values from the Intent
        String answer1 = getIntent().getStringExtra("ANSWER_1");
        String answer2 = getIntent().getStringExtra("ANSWER_2");

        TextView activ1 = findViewById(R.id.tvActivity1);
        TextView activ2 = findViewById(R.id.tvActivity2);

        activ1.setText(activ1.getText().toString());
        activ2.setText(activ2.getText().toString());

        int n = 0;
        if (answer1.contains("y") && answer2.contains("y")) {
            n = 2;
        }
        else if (answer1.contains("y") || answer2.contains("y")) {
            n = 1;
        } else {
            n = 0;
        }
        int co2 = calculateCarbonEmis(n);

        TextView displayEmis = findViewById(R.id.tvC02);
        displayEmis.setText(" " + co2 + " ");

    }

    public int calculateCarbonEmis(int n) {
        int totalEmis = 100;
        totalEmis = totalEmis * 2;
        return totalEmis;
    }
}