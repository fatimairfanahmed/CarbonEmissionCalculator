package bmc.swe.carbonemissioncalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView q1;
    TextView q2;

    TextView q3;
    TextView q4;
    TextView q5;

    String user;


    String []qs;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        user = intent.getStringExtra("userName");

        TextView tv = findViewById(R.id.tvWelcome);
        q1 = findViewById(R.id.Q1);
        q2 = findViewById(R.id.Q2);
        q3 = findViewById(R.id.Q3);
        q4 = findViewById(R.id.Q4);
        q5 = findViewById(R.id.Q5);



        qs = new String[10];
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    URL url = new URL("http://165.106.126.48:3000/all");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        Scanner in = new Scanner(url.openStream());
                        String response = in.nextLine();

                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            // Extract values from the JSON object
                            String question = jsonObject.getString("question");
                            // Use the extracted values as needed
                            qs[i] = question;
                        }

                        q1.setText(qs[0]);
                        q2.setText(qs[1]);
                        q3.setText(qs[2]);
                        q4.setText(qs[3]);
                        q5.setText(qs[4]);





                    } else {
                        Log.v("MainActivity", "Connection failed with response code: " + responseCode);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSubmitBtnClick(View v) {

        TextView answer1 = findViewById(R.id.a1);
        String answer1String = answer1.getText().toString();

        TextView answer2 = findViewById(R.id.a2);
        String answer2String = answer2.getText().toString();

        TextView answer3 = findViewById(R.id.a3);
        String answer3String = answer2.getText().toString();

        TextView answer4 = findViewById(R.id.a4);
        String answer4String = answer2.getText().toString();

        TextView answer5 = findViewById(R.id.a5);
        String answer5String = answer2.getText().toString();



        // Create an Intent to start SummaryActivity
        Intent intent = new Intent(this, SummaryActivity.class);

        // Pass the values as extras to the Intent
        intent.putExtra("ANSWER_1", answer1String);
        intent.putExtra("ANSWER_2", answer2String);
        intent.putExtra("ANSWER_3", answer3String);
        intent.putExtra("ANSWER_4", answer4String);
        intent.putExtra("ANSWER_5", answer5String);


        intent.putExtra("QUESTION_1", qs[0]);
        intent.putExtra("QUESTION_2", qs[1]);
        intent.putExtra("QUESTION_3", qs[2]);
        intent.putExtra("QUESTION_4", qs[3]);
        intent.putExtra("QUESTION_5", qs[4]);

        intent.putExtra("userName", user);



        // Start SummaryActivity
        startActivity(intent);
    }


}