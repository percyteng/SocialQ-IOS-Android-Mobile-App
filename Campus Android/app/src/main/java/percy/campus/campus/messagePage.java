package percy.campus.campus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class messagePage extends AppCompatActivity {
    TextView messageTitle, nameText, timeText, contentText,HiText;
    ImageView profilePic, back;
    ImageButton replyButton;
    String username,subject, content, time, currentUser, cameFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#383858"));
        }
        messageTitle = (TextView) findViewById(R.id.messageTitle);
        nameText = (TextView) findViewById(R.id.nameText);
        HiText = (TextView) findViewById(R.id.HiText);
        timeText = (TextView) findViewById(R.id.timeText);
        contentText = (TextView) findViewById(R.id.contentText);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profilePic = (ImageView) findViewById(R.id.profilePic);
        replyButton = (ImageButton) findViewById(R.id.replyButton);



        Bundle bundle = getIntent().getExtras();
        if(!bundle.getString("cameFrom").equals("inbox"))
            replyButton.setVisibility(View.GONE);
        username = bundle.getString("from");
        subject = bundle.getString("subject");
        content = bundle.getString("content");
        time = bundle.getString("time");
        currentUser = bundle.getString("to");
        cameFrom = bundle.getString("tag");

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(messagePage.this, Compose.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("cameFrom", currentUser + " replied to your message. :)");
                bundle.putString("currentUser", currentUser);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        nameText.setText(subject);
        timeText.setText(time);
        contentText.setText(content);
        messageTitle.setText("From: " + username);
        HiText.setText(cameFrom);
        getImages();
    }
    public void getImages(){
        String url = "http://percyteng.com/orbit/pictures/" + username +".JPG";
        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        profilePic.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                profilePic.setBackground(getResources().getDrawable(R.drawable.visitor));
                error.printStackTrace();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(imgRequest);
    }
}
