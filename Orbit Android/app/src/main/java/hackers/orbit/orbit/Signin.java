package hackers.orbit.orbit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Signin extends AppCompatActivity implements View.OnClickListener{
    Button bLogin;
    TextView signUp;
    String checkAccount, checkPassword;
    EditText etUsername, etPassword;
    String username;
    String userSchool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        bLogin = (Button) findViewById(R.id.blogin);
        signUp = (TextView) findViewById(R.id.signUp);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        signUp.setOnClickListener(this);
        bLogin.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#383858"));
        }
    }
    public void volleySchool(){
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
// Start the queue
        mRequestQueue.start();
// Add request to queue
        String url = "http://percyteng.com/orbit/getUserSchool.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            userSchool = new JSONObject(response).getString("user");
                            Intent i = new Intent(Signin.this, NavigationHome.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("username", username);
                            bundle.putString("school", userSchool);
                            i.putExtras(bundle);
                            System.out.println(userSchool);
                            createMemory();
                            finish();
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("username", username);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
    public void volley(final String account, final String password) {
        final ProgressDialog proDia = new ProgressDialog(this);
        proDia.setTitle("Processing");
        proDia.setMessage("Please wait...");
        proDia.show();
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
// Start the queue
        mRequestQueue.start();
// Add request to queue
        String url = "http://percyteng.com/orbit/login.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            username = new JSONObject(response).getString("user");
                            volleySchool();
                            proDia.dismiss();
                            Toast.makeText(Signin.this, "login successfully", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            alert("Incorrect user information");
                            proDia.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        alert("Something went wrong! Check your internet connection");
                        proDia.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("useremail", account);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(postRequest);
    }
    public void alert( String message){
        AlertDialog alert = new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alert.show();
    }
    public void createMemory(){
        BufferedWriter bw;
        try {
            String mycontent = etUsername.getText().toString() + "/" + etPassword.getText().toString()+ "/" + userSchool;
            String filePath = getFilesDir().getPath().toString() + "/localMemory.txt";
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, false);
            bw = new BufferedWriter(fw);
            bw.write(mycontent);
            bw.newLine();
            bw.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public void saveInfo(View view){
        SharedPreferences sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", etUsername.getText().toString());
        editor.putString("password", etPassword.getText().toString());
        editor.apply();
    }
    public void getInfo(View view){
        SharedPreferences sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        String password = sharedPref.getString("password", "");
        if (!email.isEmpty() && !password.isEmpty()){
//            Intent i = new Intent(this, Profile.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("username", username);
//            i.putExtras(bundle);
//            startActivity(i);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blogin:
                if (etUsername.getText().toString().isEmpty()){
                    etUsername.setError("Please enter your email!");
                }
                else if(etPassword.getText().toString().isEmpty()){
                    etPassword.setError( "Please enter your password!" );
                }
                else{
                    volley(etUsername.getText().toString(), etPassword.getText().toString());
                }
                break;
            case R.id.signUp:
                startActivity(new Intent(this, SignUp.class));
                break;

        }
    }
}
