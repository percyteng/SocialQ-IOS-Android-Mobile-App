package percy.SocialQ.SocialQ;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Splash extends Activity {
    private static int splashInterval = 3000;
    String username, checkAccount, checkPassword, checkSchool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        if(firstTime()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Intent i = new Intent(Splash.this, walkthrough1.class);
                    startActivity(i);
                    finish();
                }
            }, splashInterval);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    checkMemory();
                }
            }, splashInterval);
        }

    }
    public void volley(final String account, final String password) {
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
                            finishIt();

                        } catch (JSONException e) {
                            startActivity(new Intent(Splash.this, Signin.class));
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        alert("Something went wrong! Check your internet connection");

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
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
    public void finishIt(){
        Intent i = new Intent(this, NavigationHome.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("school", checkSchool);
        i.putExtras(bundle);
        finish();
        startActivity(i);
    }
    public boolean firstTime() {
        try {
            String filePath = getFilesDir().getPath().toString() + "/firstTime.txt";
            File myFile = new File(filePath);
            if (!myFile.exists()) {
                myFile.createNewFile();
                return true;
            } else {
                return false;
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }
    public void checkMemory(){
        try{
            String filePath = getFilesDir().getPath().toString() + "/localMemory.txt";
            File myFile = new File(filePath);
            if(!myFile.exists()) {
                startActivity(new Intent(this, Signin.class));
                finish();
                return;
            }

            BufferedReader in = new BufferedReader(new FileReader(filePath));
            String str;
            str = in.readLine();
            String[] arrayList=str.split("/");
            checkAccount = (arrayList[0]);
            checkPassword = (arrayList[1]);
            checkSchool = (arrayList[2]);
            volley(checkAccount, checkPassword);

            in.close();
        }
        catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        catch (IOException e) {

            e.printStackTrace();
        }
    }
    public void deleteFile(){
        String filePath = getFilesDir().getPath().toString() + "/localMemory.txt";
        File file = new File(filePath);
        if (file.exists())
            file.delete();
    }
}
