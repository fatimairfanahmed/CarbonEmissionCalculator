package bmc.swe.carbonemissioncalculator;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class GraphActivity extends AppCompatActivity {
    String user;
    GraphView graphView;

    String firstDate;
    String lastDate;


    @SuppressLint("SetTextI18n")
    @Override
    //get all the answers with given username
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        user = getIntent().getStringExtra("userName");
        ArrayList<Answer> userAnswers = getAnswerData();
        graphView = findViewById(R.id.graph);
        System.out.println("AL's size outside of function: " + userAnswers.size());
        graphView.addSeries(createGraph(userAnswers));
        TextView x_label = findViewById(R.id.qwerty);
        x_label.setText("emission between " + firstDate + " and " + lastDate + ". " );
    }

    public ArrayList<Answer> getAnswerData(){
        ArrayList<Answer> responses = new ArrayList<>();
        try{
            ExecutorService exe = Executors.newSingleThreadExecutor();
            exe.execute( () -> {
                        try{
                            TextView tv = findViewById(R.id.qwerty);
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
                        }catch(Exception e){
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

    public LineGraphSeries<DataPoint> createGraph(ArrayList<Answer> aL){
        //get a sorted list of unique dates and the sum of all answer scores for those dates
        Map<String, Integer> uniqueDates = new TreeMap<>();
        System.out.println("Al's size in function: " + aL.size());
        firstDate = aL.get(0).getDate();
        lastDate = aL.get(aL.size()-1).getDate();
        for(int i = 0; i < aL.size(); i++){
            Answer each = aL.get(i);
            if(uniqueDates.containsKey(each.getDate())){
                int hold = uniqueDates.get(each.getDate());
                hold += Integer.parseInt(each.getAnswerNumber());
                uniqueDates.put(each.getDate(), hold);
            }else{
                uniqueDates.put(each.getDate(), Integer.parseInt(each.getAnswerNumber()));
            }
        }
        DataPoint[] pts = new DataPoint[uniqueDates.size()];
        System.out.println("unique dates size: " + uniqueDates.size());
        int j = 0;
        for(Map.Entry<String, Integer> entry: uniqueDates.entrySet()){
            pts[j] = new DataPoint(j, entry.getValue());
            j++;
        }
        LineGraphSeries<DataPoint> lineGraph = new LineGraphSeries<DataPoint>(pts);

        return lineGraph;
    }

}
