package bmc.swe.carbonemissioncalculator;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.tvWelcome);
        TextView q1 = findViewById(R.id.Q1);
        TextView q2 = findViewById(R.id.Q2);

        String[] qs = new String[3];
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    URL url = new URL("http://10.0.2.2:3000/all");
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

        // Create an Intent to start SummaryActivity
        Intent intent = new Intent(this, SummaryActivity.class);

        // Pass the values as extras to the Intent
        intent.putExtra("ANSWER_1", answer1String);
        intent.putExtra("ANSWER_2", answer2String);

        // Start SummaryActivity
        startActivity(intent);
    }


}