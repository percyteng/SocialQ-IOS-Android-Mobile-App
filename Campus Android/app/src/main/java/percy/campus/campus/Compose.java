package percy.campus.campus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Compose extends Activity implements View.OnClickListener{
    String username, currentUser, cameFrom;
    EditText etSubject, etContent;
    TextView etTo;
    ImageView back;
    ImageButton composeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        currentUser = bundle.getString("currentUser");
        cameFrom = bundle.getString("cameFrom");

        etTo = (TextView) findViewById(R.id.etTo);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etContent = (EditText) findViewById(R.id.etContent);

        onFocusChange(etSubject);
        onFocusChange(etContent);

        back = (ImageView) findViewById(R.id.back);
        composeButton = (ImageButton) findViewById(R.id.composeButton);
        etTo.setText("To: " + username);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#383858"));
        }
        back.setOnClickListener(this);
        composeButton.setOnClickListener(this);
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
    public void sendMessage(final int hour, final String minute){
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
        String url = "http://percyteng.com/orbit/sendMessage.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int result = new JSONObject(response).getInt("success");
                            if (result == 1) {
                                Toast.makeText(getBaseContext(), "Message sent",
                                        Toast.LENGTH_SHORT).show();
                                proDia.dismiss();
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

                params.put("from", currentUser);
                params.put("to", username);
                params.put("subject", etSubject.getText().toString());
                params.put("content", etContent.getText().toString());
                params.put("time", hour + ":" + minute);
                params.put("cameFrom", cameFrom);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back)
            finish();
        if(v.getId() == R.id.composeButton){
            Calendar c = Calendar.getInstance();
            int minutes = c.get(Calendar.MINUTE);
            String minute;
            int hour = c.get(Calendar.HOUR_OF_DAY);

            if (minutes<10)
                minute = "0" + Integer.toString(minutes);
            else
                minute = Integer.toString(minutes);
            if (etSubject.getText().toString().length() > 45)
                etSubject.setError("Subject has to be shorter than 45 letters ");
            else if (etContent.getText().toString().length() > 500)
                etContent.setError("Message has to be shorter than 500 letters");
            else
                sendMessage(hour,minute);
        }
    }
}
