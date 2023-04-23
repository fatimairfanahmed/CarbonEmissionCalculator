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
    String user;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Retrieve the values from the Intent
        final String[] answer1 = {getIntent().getStringExtra("ANSWER_1")};
        final String[] answer2 = {getIntent().getStringExtra("ANSWER_2")};
        final String[] answer3 = {getIntent().getStringExtra("ANSWER_3")};
        final String[] answer4 = {getIntent().getStringExtra("ANSWER_4")};
        final String[] answer5 = {getIntent().getStringExtra("ANSWER_5")};



        String q1 = getIntent().getStringExtra("QUESTION_1");
        String q2 = getIntent().getStringExtra("QUESTION_2");
        String q3 = getIntent().getStringExtra("QUESTION_3");
        String q4 = getIntent().getStringExtra("QUESTION_4");
        String q5 = getIntent().getStringExtra("QUESTION_5");
        user = getIntent().getStringExtra("userName");




        TextView activ1 = findViewById(R.id.tvActivity1);
        TextView activ2 = findViewById(R.id.tvActivity2);
        TextView activ3 = findViewById(R.id.tvActivity3);
        TextView activ4 = findViewById(R.id.tvActivity4);
        TextView activ5 = findViewById(R.id.tvActivity5);




        activ1.setText("Question 1: "+ q1 + "Answer : " + answer1[0]);
        activ2.setText("Question 2: " +q2 + "Answer : " + answer2[0]);
        activ3.setText("Question 3: " +q3 + "Answer : " + answer3[0]);
        activ4.setText("Question 4: " +q4 + "Answer : " + answer4[0]);
        activ5.setText("Question 5: " +q5 + "Answer : " + answer5[0]);

        String[] questionList = {q1, q2, q3, q4, q5  };

        String[] answerList = {answer1[0], answer2[0], answer3[0], answer4[0], answer5[0]  };
   // this is the stuff to add to database but were having issues with duplicates
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    for(int i =0; i < questionList.length; i++ ) {
                         //String create = "answerQuestion=" + q2 + "&answerText=" + answer2 + "&answerNumber=" + "100";
                      String create = "answerQuestion=" + questionList[i] + "&answerText=" + answerList[i] + "&answerNumber=" + "300" +"&user=" + user + "&date=" + "19990825";
                        URLConnection postUrl = new URL("http://165.106.118.248:3000/AddAnswer" + "?" + create).openConnection();
                        postUrl.setRequestProperty("Accept-Charset", create);
                        InputStream doit = postUrl.getInputStream();


                         }
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

        //Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
        //startActivity(intent);





        TextView[] textViews = {activ1, activ2, activ3, activ4, activ5};
        int m = 0;
        for(int i = 0; i < textViews.length; i++){
            TextView hold = textViews[i];
            if(hold.getText().toString().contains("y")){
                m++;
            }
        }
        int co2 = calculateCarbonEmis(m);

        TextView displayEmis = findViewById(R.id.tvC02);
        displayEmis.setText(" " + co2 + " ");


        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implements the Edit button
                AlertDialog.Builder builder = new AlertDialog.Builder(SummaryActivity.this);
                builder.setTitle("Edit Answers");

                // Add EditText for new answers
                EditText newAnswer1 = new EditText(SummaryActivity.this);
                newAnswer1.setHint("Enter Answer 1 (y/n)");
                newAnswer1.setText(answer1[0]);

                EditText newAnswer2 = new EditText(SummaryActivity.this);
                newAnswer2.setHint("Enter Answer 2 (y/n)");
                newAnswer2.setText(answer2[0]);

                EditText newAnswer3 = new EditText(SummaryActivity.this);
                newAnswer3.setHint("Enter Answer 3 (y/n)");
                newAnswer3.setText(answer3[0]);

                EditText newAnswer4 = new EditText(SummaryActivity.this);
                newAnswer4.setHint("Enter Answer 4 (y/n)");
                newAnswer4.setText(answer4[0]);

                EditText newAnswer5 = new EditText(SummaryActivity.this);
                newAnswer5.setHint("Enter Answer 5 (y/n)");
                newAnswer5.setText(answer5[0]);

                LinearLayout layout = new LinearLayout(SummaryActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(newAnswer1);
                layout.addView(newAnswer2);
                layout.addView(newAnswer3);
                layout.addView(newAnswer4);
                layout.addView(newAnswer5);

                builder.setView(layout);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Save the new answers
                        String updatedAnswer1 = newAnswer1.getText().toString().trim();
                        String updatedAnswer2 = newAnswer2.getText().toString().trim();
                        String updatedAnswer3 = newAnswer3.getText().toString().trim();
                        String updatedAnswer4 = newAnswer4.getText().toString().trim();
                        String updatedAnswer5 = newAnswer5.getText().toString().trim();

                        // Update the text views with the new answers
                        activ1.setText("Question 1: " + q1 + "Answer : " + updatedAnswer1);
                        activ2.setText("Question 2: " + q2 + "Answer : " + updatedAnswer2);
                        activ3.setText("Question 3: " + q3 + "Answer : " + updatedAnswer3);
                        activ4.setText("Question 4: " + q4 + "Answer : " + updatedAnswer4);
                        activ5.setText("Question 5: " + q5 + "Answer : " + updatedAnswer5);

                        // Recalculate
                        String[] answerList = {updatedAnswer1, updatedAnswer2, updatedAnswer3, updatedAnswer4, updatedAnswer5};

                        // Calculate new Carbon emission value
                        int m = 0;
                        for(int i = 0; i < answerList.length; i++){
                            if(answerList[i].equals("y")){
                                m++;
                            }
                        }
                        int co2 = calculateCarbonEmis(m);

                        TextView displayEmis = findViewById(R.id.tvC02);
                        displayEmis.setText(" " + co2 + " ");

                        answer1[0] = updatedAnswer1;
                        answer2[0] = updatedAnswer2;
                        answer3[0] = updatedAnswer3;
                        answer4[0] = updatedAnswer4;
                        answer5[0] = updatedAnswer5;

                        try {
                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            executor.execute(() -> {
                                try {
                                    for (int i = 0; i < answerList.length; i++) {
                                        String update = "answerQuestion=" + questionList[i] + "&answerText=" + answerList[i] + "&answerNumber=" + "300" + "&user=" + user + "&date=" + "19990825";
                                        URLConnection postUrl = new URL("http://165.106.118.248:3000/editAnswer" + "?" + update).openConnection();
                                        postUrl.setRequestProperty("Accept-Charset", update);
                                        InputStream doit = postUrl.getInputStream();
                                    }
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            executor.awaitTermination(3, TimeUnit.SECONDS);

                        } catch (Exception e) {
                            // uh oh
                            e.printStackTrace();
                        }

                    }

                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing
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

    public void clickHistory(View v){
        Intent graph = new Intent(SummaryActivity.this, GraphActivity.class);
        graph.putExtra("userName", user);
        startActivity(graph);
    }

}