package bmc.swe.carbonemissioncalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Retrieve the values from the Intent
        String answer1 = getIntent().getStringExtra("ANSWER_1");
        String answer2 = getIntent().getStringExtra("ANSWER_2");
        String q1 = getIntent().getStringExtra("QUESTION_1");
        String q2 = getIntent().getStringExtra("QUESTION_2");



        TextView activ1 = findViewById(R.id.tvActivity1);
        TextView activ2 = findViewById(R.id.tvActivity2);

        activ1.setText("Question 1: "+ q1 + "Answer : " + answer1);
        activ2.setText("Question 2: " +q2 + "Answer : " + answer2);

        int n = 0;
        if (answer1.contains("y") && answer2.contains("y")) {
            n = 2;
        } else if (answer1.contains("y") || answer2.contains("y")) {
            n = 1;
        } else {
            n = 0;
        }
        int co2 = calculateCarbonEmis(n);

        TextView displayEmis = findViewById(R.id.tvC02);
        displayEmis.setText(" " + co2 + " ");


        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implements the Edit button
                AlertDialog.Builder builder = new AlertDialog.Builder(SummaryActivity.this);
                builder.setTitle("Edit Answers");

                // Add EditText for new answers
                EditText newAnswer1 = new EditText(SummaryActivity.this);
                newAnswer1.setHint("Enter Answer 1 (y/n)");
                EditText newAnswer2 = new EditText(SummaryActivity.this);
                newAnswer2.setHint("Enter Answer 2 (y/n)");

                LinearLayout layout = new LinearLayout(SummaryActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(newAnswer1);
                layout.addView(newAnswer2);
                builder.setView(layout);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get new answers from EditText
                        String newAnswer1String = newAnswer1.getText().toString();
                        String newAnswer2String = newAnswer2.getText().toString();



                        /* i also tried doind the list from delete here but it looks kinda weird  */


                        // Update the TextViews with new answers
                        activ1.setText(q1 + newAnswer1String);
                        activ2.setText(q2 +newAnswer2String);

                        // this Recalculates carbon footprint
                        int n = 0;
                        if (newAnswer1String.contains("y") && newAnswer2String.contains("y")) {
                            n = 2;
                        } else if (newAnswer1String.contains("y") || newAnswer2String.contains("y")) {
                            n = 1;
                        } else {
                            n = 0;
                        }
                        int co2 = calculateCarbonEmis(n);
                        displayEmis.setText(" " + co2 + " ");
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel the edit
                        dialog.dismiss();
                    }
                });

                builder.show();

            }
        });

        Button deleteButton = findViewById(R.id.button2);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the delete button
                AlertDialog.Builder builder = new AlertDialog.Builder(SummaryActivity.this);
                builder.setTitle("Delete Activity");

                // Create a list of items for the dialog? idk how else to do it,
                String[] activities = {"Activity 1", "Activity 2"};
                builder.setItems(activities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // so it selects the activity to delete
                        TextView[] textViews = {activ1, activ2};

                        // Update the selected TextView
                        for (int i = 0; i < textViews.length; i++) {
                            if (which == i) {
                                textViews[i].setText("");
                            }
                        }

                        // this Recalculates carbon footprint
                        String newAnswer1String = activ1.getText().toString();
                        String newAnswer2String = activ2.getText().toString();
                        int n = 0;
                        if (newAnswer1String.contains("y") && newAnswer2String.contains("y")) {
                            n = 2;
                        } else if (newAnswer1String.contains("y") || newAnswer2String.contains("y")) {
                            n = 1;
                        } else {
                            n = 0;
                        }
                        int co2 = calculateCarbonEmis(n);
                        displayEmis.setText(" " + co2 + " ");
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();

            }
        });
    }


    public int calculateCarbonEmis(int n) {
        int totalEmis = 100;
        totalEmis = totalEmis * (n +1);
        return totalEmis;
    }

}