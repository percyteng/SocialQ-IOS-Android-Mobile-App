package percy.campus.campus.Boards;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percy.campus.campus.Comments;
import percy.campus.campus.R;
import percy.campus.campus.Signin;
import percy.campus.campus.createPosts.createRide;
import percy.campus.campus.posts.Posts;


public class carPoolBoard extends AppCompatActivity implements View.OnClickListener {
    Posts[] arrItems;
    ArrayList<Posts> data;
    ArrayList<Posts> fullList = new ArrayList<Posts>();
    SearchView searchView;
    MyListAdaper adapter;
    ListView lv;
    String username, userSchool;
    LinearLayout container;
    final int QUEENS = 0;
    final int UWO = 1;
    final int UOT = 2;
    final int MC = 3;
    final int UO = 4;
    ImageButton createPost, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_pool_board);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#383858"));
        }
        lv = (ListView) findViewById(R.id.listview);
        searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.toString().equals("")) {
                    // reset listview
                    generateListContent();
                } else {
                    // perform search
                    searchItem(newText);
                }
                return true;
            }
        });
        container = (LinearLayout) findViewById(R.id.container);
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(this);
        createPost = (ImageButton) findViewById(R.id.createPost);
        createPost.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        userSchool = bundle.getString("school");
        getVolley();
    }
    public void searchItem(String textToSearch){
        data=new ArrayList<Posts>(Arrays.asList(arrItems));
        adapter = new MyListAdaper(this, R.layout.posts, data);
        lv.setAdapter(adapter);
        for (int i = 0; i < fullList.size(); i++){
            if(!fullList.get(i).type.contains(textToSearch)){
                if(!fullList.get(i).location.contains(textToSearch)) {
                    data.remove(fullList.get(i));
                }
            }
        }
//        for(String item:items){
//            if(!item.contains(textToSearch)){
//                data.remove(item);
//                System.out.println(textToSearch);
//            }
//        }
        adapter.notifyDataSetChanged();
    }
    private void generateListContent() {

        data=new ArrayList<Posts>(Arrays.asList(arrItems));
        adapter = new MyListAdaper(this, R.layout.posts, data);
        lv.setAdapter(adapter);

//        adapter.notifyDataSetChanged();
//        for(int i = 0; i < 55; i++) {
//            data.add("This is row number " + i);
//        }
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
        String url = "http://percyteng.com/orbit/getRide.php";
        final JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            JSONArray jsonResponse = response.getJSONArray("rides");
                            for (int i = jsonResponse.length()-1; i >= 0; i--){
                                JSONObject eachActivity = jsonResponse.getJSONObject(i);
                                String school = eachActivity.getString("school");
                                if(school.equals(userSchool)) {
                                    String username = eachActivity.getString("username");
                                    String location = eachActivity.getString("location");
                                    String price = eachActivity.getString("price");
                                    String notes = eachActivity.getString("notes");
//                                    createLayout(username, location, price, notes, school);
                                    fullList.add(new Posts(price, location, username, notes, school));
                                }
                                proDia.dismiss();
                            }
                            arrItems = new Posts[fullList.size()];
                            for (int i = 0; i < fullList.size(); i++){
                                arrItems[i] = fullList.get(i);
                            }
                            generateListContent();
                            adapter = new MyListAdaper(carPoolBoard.this, R.layout.posts, data);
                            lv.setAdapter(adapter);
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
//    public void createLayout(final String name, final String location, final String price, final String notes, final String school){
//        RelativeLayout relay = new RelativeLayout(this);
//        LinearLayout.LayoutParams Rp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 240);
//        Rp.setMargins(10, 10, 10, 0);
//        relay.setLayoutParams(Rp);
////        relay.setElevation(4);
//        relay.setBackgroundColor(Color.parseColor("#ffffff"));
//        relay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(carPoolBoard.this, Comments.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("category", "Rideshare");
//                bundle.putString("name", name);
//                bundle.putString("location", location);
//                bundle.putString("price", price);
//                bundle.putString("school", school);
//                bundle.putString("notes", notes);
//                bundle.putString("currentUser", username);
//                i.putExtras(bundle);
//                startActivity(i);
//            }
//        });
//
//        ImageButton logo = new ImageButton(this);
//        RelativeLayout.LayoutParams logoLayout = new RelativeLayout.LayoutParams(130, 130);
//        logoLayout.setMargins(40, 0, 0, 0);
//        logo.setId(R.id.logo);
//        logoLayout.addRule(RelativeLayout.CENTER_VERTICAL);
//        logo.setLayoutParams(logoLayout);
//        logo.setBackground(getResources().getDrawable(R.drawable.rideshare));
//
//        relay.addView(logo);
//
//
//
//        if (name.equals(username) || username.equals("admin")){
//            TextView delete = new TextView(this);
//            RelativeLayout.LayoutParams deleteLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            delete.setText("Delete");
//            deleteLayout.setMargins(0, 50, 100, 0);
//            deleteLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            delete.setLayoutParams(deleteLayout);
//            relay.addView(delete);
//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    deleteVolley(name, price, location, notes, "Rideshare", school);
//
//                }
//            });
//        }
//        TextView title = new TextView(this);
//        RelativeLayout.LayoutParams titleLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        title.setText("RIDESHARE");
//        titleLayout.setMargins(60, 50, 0, 0);
//        titleLayout.addRule(RelativeLayout.RIGHT_OF, R.id.logo);
//        title.setLayoutParams(titleLayout);
//        title.setTextSize(16);
//        title.setTextColor(Color.parseColor("#000000"));
//        title.setId(R.id.title);
//
//        relay.addView(title);
//
//        TextView locate = new TextView(this);
//        RelativeLayout.LayoutParams locateLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        locateLayout.setMargins(60, 5, 0, 0);
//        locateLayout.addRule(RelativeLayout.BELOW, R.id.title);
//        locateLayout.addRule(RelativeLayout.RIGHT_OF, R.id.logo);
//        locate.setLayoutParams(locateLayout);
//        if(location.length()<15)
//            locate.setText(location);
//        else
//            locate.setText(location.substring(0,13) + "...");
//        locate.setId(R.id.location);
//
//        relay.addView(locate);
//
//        TextView cost = new TextView(this);
//        RelativeLayout.LayoutParams costLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        costLayout.setMargins(0,5,100,0);
//        costLayout.addRule(RelativeLayout.BELOW, R.id.title);
//        costLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        cost.setLayoutParams(costLayout);
//        if (price.length()<10)
//            cost.setText(price);
//        else
//            cost.setText(price.substring(0,7) + "...");
//        cost.setTextColor(Color.parseColor("#c4d964"));
//
//        relay.addView(cost);
//
//        container.addView(relay);
//
//    }
    public void alertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Login is required");

        // set dialog message
        alertDialogBuilder
                .setMessage("You have to be logged in to create posts!")
                .setCancelable(false)
                .setPositiveButton("Login",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        startActivity(new Intent(carPoolBoard.this, Signin.class));
                        finish();
                    }
                })
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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
    public void deleteVolley(final String name, final String price, final String location, final String notes, final String category, final String school){
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
        String url = "http://percyteng.com/orbit/deletePost.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int result = new JSONObject(response).getInt("success");
                            if (result == 1) {
                                Toast.makeText(getBaseContext(), "Post successful deleted",
                                        Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(carPoolBoard.this, carPoolBoard.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("username", username);
                                bundle.putString("school", userSchool);
                                i.putExtras(bundle);
                                startActivity(i);
                                finish();
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
                params.put("notes", notes);
                params.put("school", school);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(postRequest);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.createPost) {
            if (username.equals("Visitor")) {
                alertDialog();
            } else {
                Intent i = new Intent(this, createRide.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("school", userSchool);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        }
        else if (v.getId() == R.id.back){
            finish();
        }
    }
    public class MyListAdaper extends ArrayAdapter<Posts> {
        private int layout;
        private List<Posts> mObjects;
        private MyListAdaper(Context context, int resource, List<Posts> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.logo);
                viewHolder.eventName = (TextView) convertView.findViewById(R.id.eventName);
                viewHolder.price = (TextView) convertView.findViewById(R.id.price);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.delete = (TextView) convertView.findViewById(R.id.delete);
                viewHolder.location = (TextView) convertView.findViewById(R.id.location);
                viewHolder.relay = (RelativeLayout)convertView.findViewById(R.id.relay);

                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
//            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT).show();
//                }
//            });
            mainViewholder.location.setVisibility(View.GONE);
            mainViewholder.thumbnail.setBackground(getResources().getDrawable(R.drawable.rideshare));
            mainViewholder.title.setText("RIDESHARE");
            if (getItem(position).location.length() < 15)
                mainViewholder.eventName.setText(getItem(position).location);
            else
                mainViewholder.eventName.setText(getItem(position).location.substring(0,10) + "...");
            if(getItem(position).type.length()<10)
                mainViewholder.price.setText(getItem(position).type);
            else
                mainViewholder.price.setText(getItem(position).type.substring(0,8) + "...");

            mainViewholder.relay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(carPoolBoard.this, Comments.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("category", "Rideshare");
                    bundle.putString("name", getItem(position).username);
                    bundle.putString("location", getItem(position).location);
                    bundle.putString("price", getItem(position).type);
                    bundle.putString("school", getItem(position).school);
                    bundle.putString("notes", getItem(position).notes);
                    bundle.putString("currentUser", username);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
            if (!getItem(position).username.equals(username) && !username.equals("admin")) {
                mainViewholder.delete.setVisibility(View.GONE);
            }
            mainViewholder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteVolley(getItem(position).username, getItem(position).type, getItem(position).location, getItem(position).notes, "Rideshare", getItem(position).school);

                }
            });
            return convertView;
        }
    }
    class ViewHolder {
        TextView delete;
        RelativeLayout relay;
        TextView eventName;
        TextView price;
        TextView location;
        ImageView thumbnail;
        TextView title;
    }
}
