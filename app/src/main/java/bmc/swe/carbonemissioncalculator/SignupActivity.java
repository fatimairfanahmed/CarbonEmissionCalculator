package bmc.swe.carbonemissioncalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import bmc.swe.carbonemissioncalculator.R;

public class SignupActivity extends AppCompatActivity {
    EditText user2;
    EditText pass2;
    EditText email;



    ArrayList<String> profiles = new ArrayList<>();
    ArrayList<String> passwords = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        user2 = findViewById(R.id.user2);
        pass2 = findViewById(R.id.pass);
        email = findViewById(R.id.email);


    }

    public void clickCreate(View v){
        TextView tv = findViewById(R.id.box);
        String entUsr = user2.getText().toString();
        String entPwd = pass2.getText().toString();
        String entEml = email.getText().toString();
        final String[] test = {""};
        final String[] message = {""};

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {

                    if(userExists(entUsr).compareTo("false") == 0){
                        //create a new account

                        //?userName=dsf&email=Emaildfsfd&password=sdf
                        String create = "userName=" + entUsr + "&email=" + entEml + "&password=" + entPwd;
                        URLConnection postUrl = new URL("http://165.106.126.48:3000/createprof" + "?" + create).openConnection();
                        postUrl.setRequestProperty("Accept-Charset", create);
                        InputStream doit = postUrl.getInputStream();

                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        intent.putExtra("userName", user2.getText().toString());
                        startActivity(intent);
                    }else{
                        //Username taken
                        tv.setText("Username taken");
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