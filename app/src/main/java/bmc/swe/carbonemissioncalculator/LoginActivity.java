package bmc.swe.carbonemissioncalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {
    EditText user;
    EditText pass;

    String test;


    ArrayList<String> profiles = new ArrayList<String>();
    ArrayList<String> passwords = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);


    }

    public void clickLogin(View v){
        String username = user.getText().toString();
        String password = pass.getText().toString();

        TextView tv = findViewById(R.id.msg);
        final String[] message = {"it works<"};
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000
                    // and that it has a /test endpoint that returns a JSON object with
                    // a field called "message"
                    //you are connecting to a website not a database
                    URL url = new URL("http://165.106.118.248:3000/allProfiles");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();
                    //response = response.replace('[',' ');
                    //response = response.replace(']',' ');
                    test = response;

                    JSONArray jsArr = new JSONArray(response);
                    test = "cast to json array successful";

                    for(int i = 0; i < jsArr.length(); i++){
                        JSONObject jo = jsArr.getJSONObject(i);
                        String eachUser = jo.getString("user");
                        String eachPwd = jo.getString("password");
                        profiles.add(i, eachUser);
                        passwords.add(i, eachPwd);
                    }

                    // need to set the instance variable in the Activity object
                    // because we cannot directly access the TextView from here
                    String tester = "";
                    test = "added";
                    for(int i = 0; i < profiles.size(); i++){
                        tester += profiles.get(i);
                        tester += passwords.get(i);
                    }
                    message[0] = tester;

                    message[0] = "nothing";
                    String expectedPwd = userExists(user.getText().toString());

                    if(expectedPwd.compareTo("false") == 0){
                        //the user does not exist - use toaster
                        message[0] = "user doesn't exist";
                        user.setText("");
                        pass.setText("");
                    }else{
                        if(expectedPwd.compareTo(pass.getText().toString()) == 0){
                            //redirect to main activity
                            message[0] = "success!";
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userName", user.getText().toString());
                            startActivity(intent);
                        }else{
                            //incorrect password
                            message[0] = "incorrect password";
                            user.setText("");
                            pass.setText("");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //message[0] = e.toString();
                    message[0] = e.toString();
                }
            });


            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(3, TimeUnit.SECONDS);

            // now we can set the status in the TextView
            tv.setText(message[0]);
        } catch (Exception e) {
            // uh oh
            e.printStackTrace();
            tv.setText(e.toString());
        }
    }

    public void clickSignUp(View v){
        String username = user.getText().toString();
        String password = pass.getText().toString();
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }


    // returns the password if username exists, else returns string "false"
    public String userExists(String eUserN){
        String result = "false";
        for(int i = 0; i < profiles.size(); i++){
            if (profiles.get(i).compareTo(eUserN) == 0){
                result = passwords.get(i);
            }
        }
        return result;
    }


}