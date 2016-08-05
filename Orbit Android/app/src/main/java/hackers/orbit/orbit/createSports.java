package hackers.orbit.orbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class createSports extends Activity implements View.OnClickListener{
    ImageButton attachImage, back;
    ImageView profile;
    String username, userSchool;
    final int QUEENS = 0;
    final int UWO = 1;
    final int UOT = 2;
    final int MC = 3;
    final int UO = 4;
    Button submitButton;
    EditText locationText, typeText, notesText;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sports);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#383858"));
        }
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(this);
        profile = (ImageView) findViewById(R.id.profile);
        name = (TextView) findViewById(R.id.username);
        locationText = (EditText) findViewById(R.id.locationText);
        typeText = (EditText) findViewById(R.id.typeText);
        notesText = (EditText) findViewById(R.id.notesText);
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        userSchool = bundle.getString("school");
        name.setText(username);
        getImages();
    }
    public void getImages(){
        String url = "http://percyteng.com/orbit/pictures/" + username +".JPG";
        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        profile.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                profile.setBackground(getResources().getDrawable(R.drawable.visitor));
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(this).add(imgRequest);
    }

    public void postVolley(){
        // Instantiate the cache
        final ProgressDialog proDia = new ProgressDialog(this);
        proDia.setTitle("Processing");
        proDia.setMessage("Please wait...");
        proDia.show();
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
// Start the queue
        mRequestQueue.start();
// Add request to queue
        String url = "http://percyteng.com/orbit/createSports.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int result = new JSONObject(response).getInt("success");
                            if (result == 1) {
                                Toast.makeText(getBaseContext(), "Post successful created",
                                        Toast.LENGTH_SHORT).show();
                                proDia.dismiss();
                                Intent i = new Intent(createSports.this, sportBoard.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("username", username);
                                bundle.putString("school", userSchool);
                                i.putExtras(bundle);
                                startActivity(i);
                                finish();

                            }
                            else {
                                String message = new JSONObject(response).getString("message");
                                alert(message);
                                proDia.dismiss();
                            }
                        } catch (JSONException e) {
                            alert("Something went wrong, pfff");
                            e.printStackTrace();
                            proDia.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        proDia.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("username", username);
                params.put("location", locationText.getText().toString());
                params.put("type", typeText.getText().toString());
                params.put("school", userSchool);
                params.put("notes", notesText.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
    public void alert(String message){
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
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            finish();
            Intent i = new Intent(createSports.this, carPoolBoard.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("school", userSchool);
            i.putExtras(bundle);
            startActivity(i);
        }
        else if(v.getId() == R.id.submitButton){
            if (typeText.getText().toString().isEmpty() || typeText.getText().toString().length() == 0 || typeText.getText().toString() == null)
                typeText.setError("Type is required!");
            else if (typeText.getText().toString().length()>50)
                typeText.setError("Category has to be shorter than 50 chracters.");
            else if (locationText.getText().toString().length()>50)
                locationText.setError("Location has to be shorter than 50 chracters.");
            else if (notesText.getText().toString().length()>500)
                notesText.setError("Notes has to be shorter than 500 chracters.");
            else {
                postVolley();
            }
        }
    }
}
