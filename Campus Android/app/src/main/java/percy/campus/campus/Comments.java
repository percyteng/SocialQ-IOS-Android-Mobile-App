package percy.campus.campus;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

public class Comments extends AppCompatActivity implements View.OnClickListener {
    String name, location, price, notes, category, currentUser, school;
    String titles = null;
    String item = null;
    final int QUEENS = 0;
    final int UWO = 1;
    final int UOT = 2;
    final int MC = 3;
    final int UO = 4;
    EditText edComments;
    ImageButton imageIcon, back;
    ImageView profile, submitComment, icon,additionalImage, firstIcon;
    TextView title, username, locationText, costText, notesText, commentText, additionalText, schoolText, messageText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#383858"));
        }
        additionalImage = (ImageView) findViewById(R.id.additionalImage);
        additionalText = (TextView) findViewById(R.id.additionalText);
        submitComment = (ImageView) findViewById(R.id.submitComment);
        messageText = (TextView) findViewById(R.id.messageText);
        messageText.setOnClickListener(this);
        submitComment.setOnClickListener(this);
        firstIcon = (ImageView) findViewById(R.id.firstIcon);
        icon = (ImageView) findViewById(R.id.icon);
        profile = (ImageView) findViewById(R.id.profile);
        profile.setOnClickListener(this);
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(this);
        commentText = (TextView) findViewById(R.id.comments);
        title = (TextView) findViewById(R.id.title);
        username = (TextView) findViewById(R.id.username);
        locationText = (TextView) findViewById(R.id.locationText);
        costText = (TextView) findViewById(R.id.costText);
        notesText = (TextView) findViewById(R.id.notesText);
        imageIcon = (ImageButton) findViewById(R.id.imageIcon);
        edComments = (EditText) findViewById(R.id.edComments);
        onFocusChange(edComments);
        schoolText = (TextView) findViewById(R.id.schoolText);
        Bundle bundle = getIntent().getExtras();

        name = bundle.getString("name");
        category = bundle.getString("category");
        if (category.equals("exchange")){
            item = bundle.getString("item");
            locationText.setText("Item: " + item);
        }
        else if(category.equals("Services")){
            titles = bundle.getString("titles");
            locationText.setText("Service: " + titles);
            firstIcon.setBackground(getResources().getDrawable(R.drawable.serviceicon));
        }
        else {
            location = bundle.getString("location");
            locationText.setText("Location: " + location);
        }

        price = bundle.getString("price");
        notes = bundle.getString("notes");
        school = bundle.getString("school");
        if (school.equals("QUEENS"))
            schoolText.setText("Queen's University");
        else if(school.equals("UWO"))
            schoolText.setText("University of Waterloo");
        else if (school.equals("UOT"))
            schoolText.setText("University of Toronto");
        else if(school.equals("MC"))
            schoolText.setText("McMaster University");
        else if (school.equals("UO"))
            schoolText.setText("University of Ottawa");
        else if (school.equals("OTHERS"))
            schoolText.setText("Other Universities");
        currentUser = bundle.getString("currentUser");
        if (category.equals("Sports")){
            costText.setText("Type: " + price);
            icon.setBackground(getResources().getDrawable(R.drawable.ball));
        }
        else {
            costText.setText("Price: " + price);
        }
        username.setText(name);
        title.setText(category);
        if (category.equals("Rideshare"))
            imageIcon.setBackground(getResources().getDrawable(R.drawable.rideshare));
        else if (category.equals("exchange"))
            imageIcon.setBackground(getResources().getDrawable(R.drawable.exchange));
        else if (category.equals("Sublet"))
            imageIcon.setBackground(getResources().getDrawable(R.drawable.sublet));
        else if (category.equals("Sports"))
            imageIcon.setBackground(getResources().getDrawable(R.drawable.sports));
        else if (category.equals("Tutors"))
            imageIcon.setBackground(getResources().getDrawable(R.drawable.tutoring));
        else if (category.equals("Events"))
            imageIcon.setBackground(getResources().getDrawable(R.drawable.events));
        else
            imageIcon.setBackground(getResources().getDrawable(R.drawable.services));
        notesText.setText("Notes: " + notes);

        if (category.equals("Events")){
            additionalImage.setBackground(getResources().getDrawable(R.drawable.event));
            titles = bundle.getString("titles");
            additionalText.setText("Event Name: " + titles);
        }
        else if (category.equals("Tutors")){
            additionalImage.setBackground(getResources().getDrawable(R.drawable.book));
            String subject = bundle.getString("titles");
            additionalText.setText("Subject: " + subject);
        }
        if (currentUser.equals(name))
            messageText.setVisibility(View.GONE);

        getComments();
        getImages();
    }
    public void onFocusChange(EditText editChange) {
        editChange.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
    public void getImages(){
        String url = "http://percyteng.com/orbit/pictures/" + name +".JPG";
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
    public void updateComment(){
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
        String url = null;
        if(category.equals("Rideshare"))
            url = "http://percyteng.com/orbit/updateRideshare.php";
        else if(category.equals("Sublet"))
            url = "http://percyteng.com/orbit/updateSublet.php";
        else if(category.equals("exchange"))
            url = "http://percyteng.com/orbit/updateExchange.php";
        else if(category.equals("Services"))
            url = "http://percyteng.com/orbit/updateServices.php";
        else if(category.equals("Tutors"))
            url = "http://percyteng.com/orbit/updateTutors.php";
        else if(category.equals("Sports"))
            url = "http://percyteng.com/orbit/updateSports.php";
        else
            url = "http://percyteng.com/orbit/updateEvents.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int success = new JSONObject(response).getInt("success");
                            if (success == 1) {
                                Toast.makeText(getBaseContext(), "Successfully commented",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(Comments.this, Comments.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("category", category);
                                bundle.putString("name", name);
                                bundle.putString("school", school);
                                bundle.putString("location", location);
                                bundle.putString("item", item);
                                bundle.putString("titles", titles);
                                bundle.putString("price", price);
                                bundle.putString("notes", notes);
                                bundle.putString("currentUser", currentUser);
                                i.putExtras(bundle);
                                startActivity(i);
                                proDia.dismiss();
                            }
                            else {
                                alert("Something went wrong.");
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
                        alert("An error occured");
                    }
                }
        ) {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("username", name);
                if(!category.equals("Sports"))
                    params.put("cost", price);
                params.put("notes", notes);
                params.put("newComment", "\n" + currentUser + ": " + edComments.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
    public void getComments(){
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
// Start the queue
        mRequestQueue.start();
// Add request to queue
        String url = "http://percyteng.com/orbit/getComments.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String commentResponse = new JSONObject(response).getString("result");
                            commentText.setText(commentResponse);
                        }
                        catch (JSONException e) {
                            alert("Something went wrong, pffff.");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        alert("something went wrong");
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("username", name);
                params.put("cost", price);
                params.put("notes", notes);
                params.put("category", category);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(postRequest);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submitComment)
            updateComment();
        else if (v.getId() == R.id.back)
            finish();
        else  if (v.getId() == R.id.profile){
            Intent i = new Intent(this, otheruserprofile.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", name);
            bundle.putString("currentUser", currentUser);
            i.putExtras(bundle);
            startActivity(i);
            finish();
        }
        else if (v.getId() == R.id.messageText){
            Intent i = new Intent(Comments.this, Compose.class);
            Bundle bundle = new Bundle();
            bundle.putString("cameFrom", currentUser + " messaged you through " + category + " board. :)");
            bundle.putString("username", name);
            bundle.putString("currentUser", currentUser);
            i.putExtras(bundle);
            startActivity(i);
        }
    }
}
