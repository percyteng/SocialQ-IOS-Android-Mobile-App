package hackers.orbit.orbit;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class otheruserprofile extends AppCompatActivity implements  View.OnClickListener{
    String username, currentUser, userSchool;
    ImageView back, profile;
    LinearLayout container;
    Button messageButton;
    TextView etDescription, name;
    final int QUEENS = 0;
    final int UWO = 1;
    final int UOT = 2;
    final int MC = 3;
    final int UO = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otheruserprofile);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        currentUser = bundle.getString("currentUser");

        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        messageButton = (Button) findViewById(R.id.messageButton);
        messageButton.setOnClickListener(this);
        if (username.equals(currentUser))
            messageButton.setVisibility(View.GONE);
        name.setText(username);
        getSchool();
        profile = (ImageView) findViewById(R.id.profile_picture);
        etDescription = (TextView) findViewById(R.id.etDescription);
        back.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#383858"));
        }

        getVolley();
        volleyIntro();
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
    public void volleyIntro(){
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
// Start the queue
        mRequestQueue.start();
// Add request to queue
        String url = "http://percyteng.com/orbit/getDescription.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String userintro = new JSONObject(response).getString("user");
                            System.out.println("the result is " + userintro);
                            if(userintro.length()>=1&&!userintro.equals("null")&&userintro != null) {
                                etDescription.setText(userintro);
                            }
                            else{
                                etDescription.setText("");
                            }
//                            JSONArray jsonResponse = new JSONObject(response).getJSONArray("user");
//                            for (int i = 0; i< jsonResponse.length(); i++){
//                                JSONObject eachuser = jsonResponse.getJSONObject(i);
//                                String useremail = eachuser.getString("useremail");
//                                String password = eachuser.getString("password");
//                                alert(useremail + "," + password);
//                            }
//                            username = jsonResponse.getString("username");
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
    public void getSchool(){
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
                            System.out.println("the result is " + userSchool);
                            if(userSchool.equals("UWO"))
                                userSchool=("UW");
                            else if(userSchool.equals("MC"))
                                userSchool=("McMaster");
                            else if (userSchool.equals("uOttawa"))
                                userSchool=("University of Ottawa");
                            else if (userSchool.equals("OTHERS"))
                                userSchool=("Other Universities");
                            if(userSchool.length()==0||userSchool.equals("null")||userSchool == null) {
                                userSchool = "";
                            }
                            name.setText(username + " | " + userSchool);
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
    public void getVolley(){
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
        String url = "http://percyteng.com/orbit/getPosts.php";
        final JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            JSONArray jsonResponse = response.getJSONArray("all");
                            for (int i = jsonResponse.length()-1; i >= 0; i--){
                                JSONObject eachPosts = jsonResponse.getJSONObject(i);
                                String name = eachPosts.getString("username");
                                if (name.equals(username)) {
                                    JSONObject eachActivity = jsonResponse.getJSONObject(i);
                                    String title = null;
                                    String category = eachActivity.getString("category");
                                    if (category.equals("Events") || category.equals("Tutors") || category.equals("Services"))
                                        title = eachActivity.getString("title");
                                    String item = eachActivity.getString("item");
                                    String school = eachActivity.getString("school");
                                    String username = eachActivity.getString("username");
                                    String location = eachActivity.getString("location");
                                    String price = eachActivity.getString("price");
                                    String notes = eachActivity.getString("notes");
                                    String comments = eachActivity.getString("comments");
                                    createLayout(username,category,location,price,notes,comments,item,title, school);
                                }
                                proDia.dismiss();
                            }
                        } catch (JSONException e) {
//                            alert("Whoops, something went wrong.");
                            e.printStackTrace();
                            proDia.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        Volley.newRequestQueue(this).add(jsonRequest);
    }
    public void createLayout(final String name,final String category, final String location, final String price, final String notes, final String comments, final String item, final String nameEvent, final String school){

        RelativeLayout relay = new RelativeLayout(this);
        LinearLayout.LayoutParams Rp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 240);
        Rp.setMargins(10, 10, 10, 0);
        relay.setLayoutParams(Rp);
        relay.setElevation(4);
        relay.setBackgroundColor(Color.parseColor("#ffffff"));
        relay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(otheruserprofile.this, Comments.class);
                Bundle bundle = new Bundle();
                bundle.putString("category", category);
                bundle.putString("name", name);
                bundle.putString("location", location);
                bundle.putString("price", price);
                bundle.putString("titles", nameEvent);
                bundle.putString("notes", notes);
                bundle.putString("school", school);
                bundle.putString("comments", comments);
                bundle.putString("item", item);
                bundle.putString("currentUser", username);
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        ImageButton logo = new ImageButton(this);
        RelativeLayout.LayoutParams logoLayout = new RelativeLayout.LayoutParams(130, 130);
        logoLayout.setMargins(40, 0, 0, 0);
        logo.setId(R.id.logo);
        logoLayout.addRule(RelativeLayout.CENTER_VERTICAL);
        logo.setLayoutParams(logoLayout);
        if (category.equals("Sublet")){
            logo.setBackground(getResources().getDrawable(R.drawable.sublet));
        }
        else if (category.equals("Rideshare")){
            logo.setBackground(getResources().getDrawable(R.drawable.rideshare));
        }
        else if (category.equals("exchange")){
            logo.setBackground(getResources().getDrawable(R.drawable.exchange));
        }
        else if (category.equals("Sports")){
            logo.setBackground(getResources().getDrawable(R.drawable.sports));
        }
        else if (category.equals("Events")){
            logo.setBackground(getResources().getDrawable(R.drawable.events));
        }
        else if (category.equals("Services")){
            logo.setBackground(getResources().getDrawable(R.drawable.services));
        }
        else{
            logo.setBackground(getResources().getDrawable(R.drawable.tutoring));
        }


        relay.addView(logo);

        ImageButton heart = new ImageButton(this);
        RelativeLayout.LayoutParams heartLayout = new RelativeLayout.LayoutParams(40,40);
        heartLayout.setMargins(0, 50, 30, 0);
        heartLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        heart.setLayoutParams(heartLayout);
        heart.setBackground(getResources().getDrawable(R.drawable.ic_heart_inactive));
        heart.setId(R.id.heart);
        heart.setVisibility(View.INVISIBLE);
        relay.addView(heart);

        TextView title = new TextView(this);
        RelativeLayout.LayoutParams titleLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (category.equals("Sublet")) {
            title.setText("SUBLET & LEASES");
        }
        else if (category.equals("Rideshare")){
            title.setText("RIDESHARE");
        }
        else if  (category.equals("exchange")){
            title.setText("SELL & BUY");
        }
        else if (category.equals("Sports"))
            title.setText("SPORTS");
        else if (category.equals("Events"))
            title.setText("EVEMTS");
        else if (category.equals("Services"))
            title.setText("SERVICES");
        else
            title.setText("TUTORS");
        titleLayout.setMargins(60, 50, 0, 0);
        titleLayout.addRule(RelativeLayout.RIGHT_OF, R.id.logo);
        title.setLayoutParams(titleLayout);
        title.setTextSize(16);
        title.setTextColor(Color.parseColor("#000000"));
        title.setId(R.id.title);

        relay.addView(title);

        TextView locate = new TextView(this);
        RelativeLayout.LayoutParams locateLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        locateLayout.setMargins(60, 5, 0, 0);
        locateLayout.addRule(RelativeLayout.BELOW, R.id.title);
        locateLayout.addRule(RelativeLayout.RIGHT_OF, R.id.logo);
        if (category.equals("exchange")){
            locate.setText(item);
        }
        else if(category.equals("Services")){
            locate.setText(nameEvent);
        }
        else {
            locate.setText(location);
        }
        locate.setId(R.id.location);

        if (category.equals("Events") || category.equals("Tutors")){
            TextView nameEvents = new TextView(this);
            RelativeLayout.LayoutParams nameEventsLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            nameEventsLayout.setMargins(60, 5, 0, 0);
            nameEventsLayout.addRule(RelativeLayout.BELOW, R.id.title);
            nameEventsLayout.addRule(RelativeLayout.RIGHT_OF, R.id.logo);
            nameEvents.setLayoutParams(nameEventsLayout);
            nameEvents.setText(nameEvent);
            nameEvents.setId(R.id.nameEvents);

            relay.addView(nameEvents);

            locateLayout.addRule(RelativeLayout.RIGHT_OF, R.id.nameEvents);

        }
        else{
            locateLayout.addRule(RelativeLayout.RIGHT_OF, R.id.logo);
        }

        locate.setLayoutParams(locateLayout);
        relay.addView(locate);

        TextView cost = new TextView(this);
        RelativeLayout.LayoutParams costLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        costLayout.setMargins(0,5,100,0);
        costLayout.addRule(RelativeLayout.BELOW, R.id.title);
        costLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cost.setLayoutParams(costLayout);
        cost.setText(price);
        cost.setTextColor(Color.parseColor("#c4d964"));

        relay.addView(cost);
        container = (LinearLayout) findViewById(R.id.container);
        container.addView(relay);


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
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back){
            finish();
        }
        if (v.getId() == R.id.messageButton){
            Intent i = new Intent(otheruserprofile.this, Compose.class);
            Bundle bundle = new Bundle();
            bundle.putString("cameFrom", currentUser + " checked out your profile. :)");
            bundle.putString("username", username);
            bundle.putString("currentUser", currentUser);
            i.putExtras(bundle);
            startActivity(i);
        }
    }
}
