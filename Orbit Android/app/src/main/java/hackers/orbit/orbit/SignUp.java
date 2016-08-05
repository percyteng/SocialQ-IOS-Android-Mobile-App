package hackers.orbit.orbit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Network;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity  implements View.OnClickListener{
    private static final int PICK_IMAGE_REQUEST = 1;
    private String KEY_IMAGE = "image";
    String school = "QUEENS";
    private Bitmap bitmap = null;
    ImageButton camera;
    final int QUEENS = 0;
    final int UWO = 1;
    final int UOT = 2;
    final int MC = 3;
    final int UO = 4;
    TextView addPhoto;
    TextView skip;
    Button bSignup;
    EditText etName, etEmail, etPassword;
    Spinner etSchool;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#383858"));
        }
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        camera= (ImageButton) findViewById(R.id.camera);
        addPhoto = (TextView) findViewById(R.id.addPhoto);
        bSignup = (Button) findViewById(R.id.bSignup);
        bSignup.setOnClickListener(this);
        skip = (TextView) findViewById(R.id.skip);
        skip.setOnClickListener(this);
        etSchool = (Spinner) findViewById(R.id.etSchool);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
// Apply the adapter to the spinner
        etSchool.setAdapter(adapter);
        etSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == QUEENS)
                    school = "QUEENS";
                else if (position == UWO)
                    school = "UWO";
                else if (position == UOT)
                    school = "UOT";
                else if (position == MC)
                    school = "MC";
                else if (position == UO)
                    school = "UO";
                else if (position == 5)
                    school = "OTHER";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        addPhoto.setText("");
        camera.setBackground(null);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void postVolley() {
        final ProgressDialog proDia = ProgressDialog.show(this,"Uploading...","Please wait...");
        // Instantiate the cache
        proDia.setCancelable(true);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
// Set up the network to use HttpURLConnection as the HTTP client.
        com.android.volley.Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
// Start the queue
        mRequestQueue.start();
// Add request to queue
        String url = "http://percyteng.com/orbit/newUser.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int result = new JSONObject(response).getInt("success");
                            if (result == 1) {
                                proDia.dismiss();
                                String message = new JSONObject(response).getString("message");
                                Toast.makeText(SignUp.this, message ,Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                String message = new JSONObject(response).getString("message");
                                System.out.println(message);
                                proDia.dismiss();
                            }
                        } catch (JSONException e) {
                            proDia.dismiss();
                            System.out.println("something went wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        proDia.dismiss();
                        alert("Please upload an image, because that's more fun");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                String image;
                if (bitmap!=null)
                    image = getStringImage(bitmap);
                else
                    image = "null";
                Map<String, String> params = new HashMap<>();
                // the POST parameters:

                params.put("useremail", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());
                params.put("username", etName.getText().toString());
                params.put("school", school);
                params.put(KEY_IMAGE, image);
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
        bundle.putString("username", "Visitor");
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSignup:
                String ed_email = etEmail.getText().toString();
                String ed_Name = etName.getText().toString();
                String ed_Password = etPassword.getText().toString();
                if (ed_Name.isEmpty()) {
                    etName.setError( "Name is required!" );
                }
                else if (ed_Name.length()>20 || ed_Name.length()<3)
                    etName.setError("User name has to be between 3 - 20 chracters.");
                else if (ed_email.isEmpty()) {
                    etEmail.setError( "Email is required!" );
                }
                else if (ed_Password.length()>20)
                    etName.setError("Password has to be shorter than 30 chracters.");
                else if (ed_Password.isEmpty()) {
                    etPassword.setError("Password is required!" );
                }
                else if (!ed_email.contains("@")) {
                    etEmail.setError("Please enter a valid email address.");
                }
                else {
                    postVolley();
                }
                break;
            case R.id.skip:
                finishIt();
                break;
            case R.id.imageView:
                showFileChooser();
                break;

        }
    }
}
