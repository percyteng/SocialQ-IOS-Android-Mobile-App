package percy.campus.campus;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by percy on 2016/3/14.
 */
public class outboxClass extends Fragment {
    String username;
    LinearLayout container;
    private View myFragmentView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.outbox_fragment, container, false);
        username = getArguments().getString("username");
        getVolley();
        return myFragmentView;
    }
    public void getImages(final ImageView userPic, String to){
        String url = "http://percyteng.com/orbit/pictures/" + to +".JPG";
        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        userPic.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userPic.setBackground(getResources().getDrawable(R.drawable.visitor));
                error.printStackTrace();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(imgRequest);
    }
    public void deleteVolley(final String from, final String to, final String subject, final String content){
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
        String url = "http://percyteng.com/orbit/deleteMessage.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int result = new JSONObject(response).getInt("success");
                            if (result == 1) {
                                Toast.makeText(getActivity().getBaseContext(), "Message deleted",
                                        Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                bundle.putString("username", username);
                                Fragment outbox = new outboxClass();
                                outbox.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, outbox).commit();
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
                params.put("from", from);
                params.put("to", to);
                params.put("subject", subject);
                params.put("content", content);
                return params;
            }
        };

        Volley.newRequestQueue(getActivity()).add(postRequest);
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
        String url = "http://percyteng.com/orbit/getInbox.php";
        final JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            JSONArray jsonResponse = response.getJSONArray("all");
                            for (int i = jsonResponse.length()-1; i >= 0; i--){
                                JSONObject eachPosts = jsonResponse.getJSONObject(i);
                                String from = eachPosts.getString("fromText");
                                if (from.equals(username)) {
                                    String to = eachPosts.getString("toText");
                                    String subject = eachPosts.getString("subject");
                                    String content = eachPosts.getString("content");
                                    String time = eachPosts.getString("time");
                                    String cameFrom = eachPosts.getString("cameFrom");
                                    createLayout(from, to, subject, content, time, cameFrom);
                                }
                                proDia.dismiss();
                            }
                        } catch (JSONException e) {
//                            alert("Whoops, something went wrong.");
                            e.printStackTrace();
                            System.out.println("error");
                            proDia.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("This isn't right");
                        error.printStackTrace();
                    }
                });
        Volley.newRequestQueue(getActivity()).add(jsonRequest);
    }
    public void createLayout(final String from,final String to, final String subject, final String content, final String time, final String cameFrom){
        RelativeLayout relay = new RelativeLayout(getActivity());
        LinearLayout.LayoutParams Rp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 240);
        Rp.setMargins(10, 10, 10, 0);
        relay.setLayoutParams(Rp);
        relay.setBackgroundColor(Color.parseColor("#ffffff"));
        relay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), messagePage.class);
                Bundle bundle = new Bundle();
                bundle.putString("from", from);
                bundle.putString("subject", subject);
                bundle.putString("content", content);
                bundle.putString("time", time);
                bundle.putString("cameFrom", "outbox");
                bundle.putString("tag", cameFrom);
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        MLRoundedImageView userPic = new MLRoundedImageView(getActivity());
        RelativeLayout.LayoutParams userPicLayout = new RelativeLayout.LayoutParams(150, 150);
        userPicLayout.setMargins(40, 0, 0, 0);
        userPic.setId(R.id.logo);
        userPicLayout.addRule(RelativeLayout.CENTER_VERTICAL);
        userPic.setLayoutParams(userPicLayout);
        getImages(userPic, to);

        relay.addView(userPic);

        ImageButton heart = new ImageButton(getActivity());
        RelativeLayout.LayoutParams heartLayout = new RelativeLayout.LayoutParams(60, 60);
        heartLayout.setMargins(0, 50, 30, 0);
        heartLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        heart.setLayoutParams(heartLayout);
        heart.setBackground(getResources().getDrawable(R.drawable.ic_heart_inactive));
        heart.setId(R.id.heart);
        relay.addView(heart);
        heart.setVisibility(View.INVISIBLE);



        TextView delete = new TextView(getActivity());
        RelativeLayout.LayoutParams deleteLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        delete.setText("Delete");
        deleteLayout.setMargins(0, 50, 30, 0);
        deleteLayout.addRule(RelativeLayout.LEFT_OF, R.id.heart);
        delete.setLayoutParams(deleteLayout);
        relay.addView(delete);
        delete.setId(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVolley(from, to, subject, content);
            }
        });
        TextView timePassed = new TextView(getActivity());
        RelativeLayout.LayoutParams timeLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        timePassed.setText(time);
        timeLayout.setMargins(0, 10, 30, 0);
        timeLayout.addRule(RelativeLayout.LEFT_OF, R.id.heart);
        timeLayout.addRule(RelativeLayout.BELOW, R.id.delete);
        timePassed.setLayoutParams(timeLayout);
        relay.addView(timePassed);



        TextView title = new TextView(getActivity());
        RelativeLayout.LayoutParams titleLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        titleLayout.setMargins(60, 50, 0, 0);
        titleLayout.addRule(RelativeLayout.RIGHT_OF, R.id.logo);
        title.setLayoutParams(titleLayout);
        title.setTextSize(16);
        title.setText("To: " + to);
        title.setTextColor(Color.parseColor("#000000"));
        title.setId(R.id.title);

        relay.addView(title);

        TextView locate = new TextView(getActivity());
        RelativeLayout.LayoutParams locateLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        locateLayout.setMargins(60, 5, 0, 0);
        locateLayout.addRule(RelativeLayout.BELOW, R.id.title);
        locateLayout.addRule(RelativeLayout.RIGHT_OF, R.id.logo);
        locate.setId(R.id.location);
        if(subject.length()<20)
            locate.setText(subject);
        else
            locate.setText(subject.substring(0,20) + "...");
        locate.setLayoutParams(locateLayout);
        relay.addView(locate);

        container = (LinearLayout) myFragmentView.findViewById(R.id.container);
        container.addView(relay);


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
}
