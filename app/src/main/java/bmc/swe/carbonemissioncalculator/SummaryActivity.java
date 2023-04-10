package bmc.swe.carbonemissioncalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SummaryActivity extends AppCompatActivity {


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Retrieve the values from the Intent
        String answer1 = getIntent().getStringExtra("ANSWER_1");
        String answer2 = getIntent().getStringExtra("ANSWER_2");
        String answer3 = getIntent().getStringExtra("ANSWER_3");
        String answer4 = getIntent().getStringExtra("ANSWER_4");
        String answer5 = getIntent().getStringExtra("ANSWER_5");



        String q1 = getIntent().getStringExtra("QUESTION_1");
        String q2 = getIntent().getStringExtra("QUESTION_2");
        String q3 = getIntent().getStringExtra("QUESTION_3");
        String q4 = getIntent().getStringExtra("QUESTION_4");
        String q5 = getIntent().getStringExtra("QUESTION_5");




        TextView activ1 = findViewById(R.id.tvActivity1);
        TextView activ2 = findViewById(R.id.tvActivity2);
        TextView activ3 = findViewById(R.id.tvActivity3);
        TextView activ4 = findViewById(R.id.tvActivity4);
        TextView activ5 = findViewById(R.id.tvActivity5);




        activ1.setText("Question 1: "+ q1 + "Answer : " + answer1);
        activ2.setText("Question 2: " +q2 + "Answer : " + answer2);
        activ3.setText("Question 2: " +q3 + "Answer : " + answer3);
        activ4.setText("Question 2: " +q4 + "Answer : " + answer4);
        activ5.setText("Question 2: " +q5 + "Answer : " + answer5);

        String[] questionList = {q1, q2, q3, q4, q5  };

        String[] answerList = {answer1, answer2, answer3, answer4, answer5  };
/*    this is the stuff to add to databse but were having issues with duplicates
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    //for(int i =0; i < questionList.length; i++ ) {
                         String create = "answerQuestion=" + q2 + "&answerText=" + answer2 + "&answerNumber=" + "100";
                      // String create = "answerQuestion=" + questionList[i] + "&answerText=" + answerList[i] + "&answerNumber=" + "100";
                        URLConnection postUrl = new URL("http://165.106.118.248:3000/AddAnswer" + "?" + create).openConnection();
                        postUrl.setRequestProperty("Accept-Charset", create);
                        InputStream doit = postUrl.getInputStream();

                    Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
                    startActivity(intent);
                    //}
                    } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(3, TimeUnit.SECONDS);

        } catch (Exception e) {
            // uh oh
            e.printStackTrace();
        }

 */




        TextView[] textViews = {activ1, activ2, activ3, activ4, activ5};
        int m = 0;
        for(int i = 0; i < textViews.length; i++){
            TextView hold = textViews[i];
            if(hold.getText().toString().contains("y")){
                m++;
            }
        }
        int n = 0;
        if (answer1.contains("y") && answer2.contains("y") && answer3.contains("y") && answer4.contains("y") && answer5.contains("y") ) {
            n = 2;
        } else if (answer1.contains("y") || answer2.contains("y") || answer3.contains("y") || answer4.contains("y") || answer5.contains("y")) {
            n = 1;
        } else {
            n = 0;
        }
        int co2 = calculateCarbonEmis(m);

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
                EditText newAnswer3 = new EditText(SummaryActivity.this);
                newAnswer3.setHint("Enter Answer 3 (y/n)");
                EditText newAnswer4 = new EditText(SummaryActivity.this);
                newAnswer4.setHint("Enter Answer 4 (y/n)");
                EditText newAnswer5 = new EditText(SummaryActivity.this);
                newAnswer5.setHint("Enter Answer 1 (y/n)");

                LinearLayout layout = new LinearLayout(SummaryActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(newAnswer1);
                layout.addView(newAnswer2);
                layout.addView(newAnswer3);
                layout.addView(newAnswer4);
                layout.addView(newAnswer5);


                builder.setView(layout);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get new answers from EditText
                        String newAnswer1String = newAnswer1.getText().toString();
                        String newAnswer2String = newAnswer2.getText().toString();
                        String newAnswer3String = newAnswer3.getText().toString();
                        String newAnswer4String = newAnswer4.getText().toString();
                        String newAnswer5String = newAnswer5.getText().toString();


                        /* i also tried doind the list from delete here but it looks kinda weird  */


                        // Update the TextViews with new answers
                        activ1.setText(q1 + newAnswer1String);
                        activ2.setText(q2 +newAnswer2String);
                        activ3.setText(q3 +newAnswer3String);
                        activ4.setText(q4 +newAnswer4String);
                        activ5.setText(q5 +newAnswer5String);

                        // this Recalculates carbon footprint
                        TextView[] textViews = {activ1, activ2, activ3, activ4, activ5};
                        int m = 0;
                        for(int i = 0; i < textViews.length; i++){
                            TextView hold = textViews[i];
                            if(hold.getText().toString().contains("y")){
                                m++;
                            }
                        }
                        int n = 0;
                        if (newAnswer1String.contains("y") && newAnswer2String.contains("y") && newAnswer3String.contains("y") && newAnswer4String.contains("y") && newAnswer5String.contains("y") ) {
                            n = 2;
                        } else if (newAnswer1String.contains("y") || newAnswer2String.contains("y") || newAnswer3String.contains("y") || newAnswer4String.contains("y") || newAnswer5String.contains("y")) {
                            n = 1;
                        } else {
                            n = 0;
                        }
                        int co2 = calculateCarbonEmis(m);
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
                String[] activities = {"Activity 1", "Activity 2", "Activity 3", "Activity 4", "Activity 5"};
                builder.setItems(activities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // so it selects the activity to delete
                        TextView[] textViews = {activ1, activ2, activ3, activ4, activ5};

                        // Update the selected TextView
                        for (int i = 0; i < textViews.length; i++) {
                            if (which == i) {
                                textViews[i].setText("");
                            }
                        }

                        // this Recalculates carbon footprint
                        String newAnswer1String = activ1.getText().toString();
                        String newAnswer2String = activ2.getText().toString();
                        String newAnswer3String = activ3.getText().toString();
                        String newAnswer4String = activ4.getText().toString();
                        String newAnswer5String = activ5.getText().toString();


                        int m = 0;
                        for(int i = 0; i < textViews.length; i++){
                            TextView hold = textViews[i];
                            if(hold.getText().toString().contains("y")){
                                m++;
                            }
                        }
                        int n = 0;
                        if (newAnswer1String.contains("y") && newAnswer2String.contains("y") && newAnswer3String.contains("y")&& newAnswer4String.contains("y") && newAnswer5String.contains("y")) {
                            n = 2;
                        } else if (newAnswer1String.contains("y") || newAnswer2String.contains("y") || newAnswer3String.contains("y") || newAnswer4String.contains("y") || newAnswer5String.contains("y")) {
                            n = 1;
                        } else {
                            n = 0;
                        }
                        int co2 = calculateCarbonEmis(m);
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
        totalEmis = totalEmis * (n );
        return totalEmis;
    }

}