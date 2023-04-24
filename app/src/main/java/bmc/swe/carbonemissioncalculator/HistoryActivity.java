package bmc.swe.carbonemissioncalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HistoryActivity extends AppCompatActivity {

    String user;
    String firstDate;
    String lastDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        user = getIntent().getStringExtra("userName");
        ArrayList<Answer> userAnswers = getAnswerData();

        ListView answerList = (ListView)findViewById(R.id.activity_list);
        ArrayAdapter<Answer> adapter = new ArrayAdapter<Answer>(this, android.R.layout.simple_list_item_1, userAnswers);
        answerList.setAdapter(adapter);
        adapter.addAll(userAnswers);

    }

    public ArrayList<Answer> getAnswerData(){
        ArrayList<Answer> responses = new ArrayList<>();
        try{
            ExecutorService exe = Executors.newSingleThreadExecutor();
            exe.execute( () -> {
                        try{
                            URL url = new URL("http://165.106.126.48:3000/findAnswers?user=" + user);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();
                            int responseCode = conn.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {

                                Scanner in = new Scanner(url.openStream());
                                String response = in.nextLine();

                                JSONArray jsonArray = new JSONArray(response);
                                System.out.println("\n-----------------\n_------------\n----------\n");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    // Extract values from the JSON object
                                    JSONObject jobj = (JSONObject) jsonObject.get("answer");
                                    String aQ = jobj.getString("answerQuestion");
                                    String aT = jobj.getString("answerText");
                                    String aN = jobj.getString("answerNumber");
                                    String date = jobj.getString("date");
                                    String userN = jobj.getString("user");
                                    Answer each = new Answer(aQ, aT, aN, date, userN);
                                    // Use the extracted values as needed
                                    responses.add(each);
                                }
                                System.out.println("responses size inside thread: " + responses.size());

                            } else {
                                Log.v("MainActivity", "Connection failed with response code: " + responseCode);
                            }
                        } catch(Exception e){
                            e.printStackTrace();
                            System.out.println("\n-----------------\n_------------\n----------\n");
                        }
                    }
            );
            exe.awaitTermination(3, TimeUnit.SECONDS);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("responses size outside thread: " + responses.size());
        return responses;
    }
}