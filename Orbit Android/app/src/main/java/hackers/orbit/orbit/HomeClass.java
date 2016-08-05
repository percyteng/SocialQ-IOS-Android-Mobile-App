package hackers.orbit.orbit;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by percy on 2016/3/5.
 */
public class HomeClass extends Fragment implements  View.OnClickListener{
    private View myFragmentView;
    String username;
    String userSchool;
    final int QUEENS = 0;
    final int UWO = 1;
    final int UOT = 2;
    final int MC = 3;
    final int UO = 4;
    LinearLayout container;
    ImageView carpool, sport, tutoring, events,exchange, sublet, service;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.content_navigation_home, container, false);
        username = getArguments().getString("username");
        userSchool = getArguments().getString("school");

        service = (ImageView) myFragmentView.findViewById(R.id.service);
        sublet = (ImageView) myFragmentView.findViewById(R.id.sublet);
        carpool = (ImageView) myFragmentView.findViewById(R.id.carpool);
        sport = (ImageView) myFragmentView.findViewById(R.id.sport);
        tutoring = (ImageView) myFragmentView.findViewById(R.id.tutoring);
        events = (ImageView) myFragmentView.findViewById(R.id.events);
        exchange = (ImageView) myFragmentView.findViewById(R.id.exchange);

        sublet.setOnClickListener(this);
        service.setOnClickListener(this);
        carpool.setOnClickListener(this);
        sport.setOnClickListener(this);
        tutoring.setOnClickListener(this);
        events.setOnClickListener(this);
        exchange.setOnClickListener(this);
        getVolley();
        return myFragmentView;

    }

    public void getVolley(){
        final ProgressDialog proDia = new ProgressDialog(getActivity());
        proDia.setTitle("Processing");
        proDia.setMessage("Please wait...");
        proDia.show();
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
// Start the queue
        mRequestQueue.start();
// Add request to queue
        String url = "http://percyteng.com/orbit/getAllposts.php";
        final JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            JSONArray jsonResponse = response.getJSONArray("all");
                            for (int i = jsonResponse.length()-1; i >= jsonResponse.length()-21; i--){
                                JSONObject eachActivity = jsonResponse.getJSONObject(i);
                                String title = null;
                                String school = eachActivity.getString("school");
                                if (school.equals(userSchool)) {
                                    String category = eachActivity.getString("category");
                                    if (category.equals("Events") || category.equals("Tutors") || category.equals("Services"))
                                        title = eachActivity.getString("title");
                                    String item = eachActivity.getString("item");
                                    String username = eachActivity.getString("username");
                                    String location = eachActivity.getString("location");
                                    String price = eachActivity.getString("price");
                                    String notes = eachActivity.getString("notes");
                                    String comments = eachActivity.getString("comments");

                                    createLayout(category, username, location, price, notes, comments, item, title, school);
                                }
                                proDia.dismiss();
                            }
//                            alert(jsonResponse.toString());
//                            response = response.getJSONObject("args");
//                            String site = response.getString("site"),
//                                    network = response.getString("network");
//                            System.out.println("Site: "+site+"\nNetwork: "+network);
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
        Volley.newRequestQueue(getActivity()).add(jsonRequest);
    }
    public void createLayout(final String category, final String name, final String location, final String price, final String notes, final String comments, final String item, final String nameEvent, final String school){
        RelativeLayout relay = new RelativeLayout(getActivity());
        LinearLayout.LayoutParams Rp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 240);
        Rp.setMargins(10, 10, 10, 0);
        relay.setLayoutParams(Rp);
        relay.setElevation(4);
        relay.setBackgroundColor(Color.parseColor("#ffffff"));
        relay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Comments.class);
                Bundle bundle = new Bundle();
                bundle.putString("category", category);
                bundle.putString("name", name);
                bundle.putString("location", location);
                bundle.putString("price", price);
                bundle.putString("titles", nameEvent);
                bundle.putString("notes", notes);
                bundle.putString("comments", comments);
                bundle.putString("item", item);
                bundle.putString("currentUser", username);
                bundle.putString("school", school);
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        ImageButton logo = new ImageButton(getActivity());
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


        if (name.equals(username) || username.equals("admin")){
            TextView delete = new TextView(getActivity());
            RelativeLayout.LayoutParams deleteLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            delete.setText("Delete");
            deleteLayout.setMargins(0, 50, 80, 0);
            deleteLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            delete.setLayoutParams(deleteLayout);
            relay.addView(delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(category.equals("exchange"))
                        deleteVolley(name, price, item, notes, category, school);
                    else
                        deleteVolley(name, price, location, notes, category, school);
                }
            });
        }

        TextView title = new TextView(getActivity());
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

        TextView locate = new TextView(getActivity());
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
            TextView nameEvents = new TextView(getActivity());
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

        TextView cost = new TextView(getActivity());
        RelativeLayout.LayoutParams costLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        costLayout.setMargins(0,5,100,0);
        costLayout.addRule(RelativeLayout.BELOW, R.id.title);
        costLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cost.setLayoutParams(costLayout);
        cost.setText(price);
        cost.setTextColor(Color.parseColor("#c4d964"));

        relay.addView(cost);
        container = (LinearLayout) myFragmentView.findViewById(R.id.container);
        container.addView(relay);

    }
    public void deleteVolley(final String name, final String price, final String location, final String notes, final String category, final String school){
        final ProgressDialog proDia = new ProgressDialog(getActivity());
        proDia.setTitle("Processing");
        proDia.setMessage("Please wait...");
        proDia.show();
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
// Start the queue
        mRequestQueue.start();
// Add request to queue
        String url = "http://percyteng.com/orbit/deletePost.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int result = new JSONObject(response).getInt("success");
                            if (result == 1) {
                                Toast.makeText(getActivity().getBaseContext(), "Post successful deleted",
                                        Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                bundle.putString("username", username);
                                bundle.putString("school", school);
                                Fragment home = new HomeClass();
                                home.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, home).commit();
                                proDia.dismiss();
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
                        alert("An error occured");
                        proDia.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("name", name);
                params.put("category", category);
                params.put("location", location);
                params.put("price", price);
                params.put("school", school);
                params.put("notes", notes);
                return params;
            }
        };

        Volley.newRequestQueue(getActivity()).add(postRequest);
    }
    public void alert(String message){
        new AlertDialog.Builder(getActivity())
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
        switch (v.getId()) {
            case R.id.sublet:
                Intent intent = new Intent(getActivity().getBaseContext(),
                        subletBoard.class);
                intent.putExtra("username", username);
                intent.putExtra("school", userSchool);
                getActivity().startActivity(intent);
                break;
            case R.id.carpool:
                Intent intent1 = new Intent(getActivity().getBaseContext(),
                        carPoolBoard.class);
                intent1.putExtra("username", username);
                intent1.putExtra("school", userSchool);
                getActivity().startActivity(intent1);
                break;
            case R.id.sport:
                Intent intent3 = new Intent(getActivity().getBaseContext(),
                        sportBoard.class);
                intent3.putExtra("username", username);
                intent3.putExtra("school", userSchool);
                getActivity().startActivity(intent3);
                break;
            case R.id.tutoring:
                Intent intent2 = new Intent(getActivity().getBaseContext(),
                        tutorBoard.class);
                intent2.putExtra("username", username);
                intent2.putExtra("school", userSchool);
                getActivity().startActivity(intent2);
                break;
            case R.id.events:
                Intent intent4 = new Intent(getActivity().getBaseContext(),
                        eventBoard.class);
                intent4.putExtra("username", username);
                intent4.putExtra("school", userSchool);
                getActivity().startActivity(intent4);
                break;
            case R.id.exchange:
                Intent intent6 = new Intent(getActivity().getBaseContext(),
                        exchangeBoard.class);
                intent6.putExtra("username", username);
                intent6.putExtra("school", userSchool);
                getActivity().startActivity(intent6);
                break;
            case R.id.service:
                Intent intent5 = new Intent(getActivity().getBaseContext(),
                        serviceBoard.class);
                intent5.putExtra("username", username);
                intent5.putExtra("school", userSchool);
                getActivity().startActivity(intent5);
                break;
        }
    }
}
