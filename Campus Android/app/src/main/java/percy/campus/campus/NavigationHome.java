package percy.campus.campus;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;

public class NavigationHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView name, pageTtile;
    int originalPosition;
    String username, userSchool;
    NavigationView navView;
    final int QUEENS = 0;
    final int UWO = 1;
    final int UOT = 2;
    final int MC = 3;
    final int UO = 4;
    ImageView profileImage, refreshIcon;
    Spinner dropDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        userSchool = bundle.getString("school");

        dropDown = (Spinner) findViewById(R.id.dropDown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, R.layout.spinner_item2);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item2);
// Apply the adapter to the spinner
        dropDown.setAdapter(adapter);
        if (userSchool.equals("QUEENS"))
            originalPosition = 0;
        else if (userSchool.equals("UWO"))
            originalPosition = 1;
        if (userSchool.equals("UOT"))
            originalPosition = 2;
        if (userSchool.equals("MC"))
            originalPosition = 3;
        if (userSchool.equals("UO"))
            originalPosition = 4;
        if (userSchool.equals("OTHER"))
            originalPosition = 5;
        dropDown.setSelection(originalPosition);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == QUEENS){
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    userSchool = "QUEENS";
                    bundle.putString("school", userSchool);
                    Fragment home = new HomeClass();
                    home.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, home).addToBackStack(null).commit();
                }
                else if(position == UWO){
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    userSchool = "UWO";
                    bundle.putString("school", userSchool);
                    Fragment home = new HomeClass();
                    home.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, home).addToBackStack(null).commit();
                }
                else if(position == UOT){
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    userSchool = "UOT";
                    bundle.putString("school", userSchool);
                    Fragment home = new HomeClass();
                    home.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, home).addToBackStack(null).commit();
                }
                else if(position == MC){
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    userSchool = "MC";
                    bundle.putString("school", userSchool);
                    Fragment home = new HomeClass();
                    home.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, home).addToBackStack(null).commit();
                }
                else if(position == UO){
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    userSchool = "UO";
                    bundle.putString("school", userSchool);
                    Fragment home = new HomeClass();
                    home.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, home).addToBackStack(null).commit();
                }
                else if(position == 5){
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    userSchool = "OTHERS";
                    bundle.putString("school", userSchool);
                    Fragment home = new HomeClass();
                    home.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, home).addToBackStack(null).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        navView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navView.inflateHeaderView(R.layout.navbar);
        profileImage = (ImageView) headerView.findViewById(R.id.profileImage);
        refreshIcon = (ImageView) findViewById(R.id.refreshIcon);
        refreshIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("school", userSchool);
                Fragment home = new HomeClass();
                home.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, home).commit();
            }
        });
        name = (TextView) headerView.findViewById(R.id.profileName);
        pageTtile = (TextView) findViewById(R.id.pageTitle);
        name.setText(username);
        if (username.equals("Visitor")){
            profileImage.setBackground(getResources().getDrawable(R.drawable.visitor));
        }
        else{
            getImages();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#383858"));
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    public void getImages(){
        String url = "http://percyteng.com/orbit/pictures/" + username +".JPG";

        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        profileImage.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                profileImage.setBackground(getResources().getDrawable(R.drawable.visitor));
                error.printStackTrace();

            }
        });

        Volley.newRequestQueue(this).add(imgRequest);
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
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            pageTtile.setText("Home");
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("school", userSchool);
            Fragment home = new HomeClass();
            home.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, home).addToBackStack(null).commit();
        } else if (id == R.id.nav_profile) {
            pageTtile.setText("Profile");
            if (username.equals("Visitor")){
                startActivity(new Intent(this, Signin.class));
            }
            else {
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                Fragment profile = new profileFragment();
                profile.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, profile).addToBackStack(null).commit();
            }
        }
        else if (id == R.id.nav_boards) {
            pageTtile.setText("Boards");
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("school", userSchool);
            Fragment allBoards = new allBoards();
            allBoards.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, allBoards).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_inbox) {
            pageTtile.setText("Inbox");
            Intent i = new Intent(NavigationHome.this, newInbox.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            i.putExtras(bundle);
            startActivity(i);
        }
//        else if (id == R.id.nav_outbox) {
//            pageTtile.setText("Outbox");
//            Bundle bundle = new Bundle();
//            bundle.putString("username", username);
//            Fragment outbox = new outboxClass();
//            outbox.setArguments(bundle);
//            getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, outbox).addToBackStack(null).commit();
//        }
//        else if (id == R.id.nav_postings) {
//            if (username.equals("Visitor")){
//                startActivity(new Intent(this, Signin.class));
//            }
//            else{
//                Bundle bundle = new Bundle();
//                bundle.putString("username", username);
//                Fragment myPost = new myPostFragment();
//                myPost.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myPost).commit();
//            }
//        }
        else if (id == R.id.nav_out) {
            deleteFile();
            finish();
            startActivity(new Intent(this, Signin.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void deleteFile(){
        String filePath = getFilesDir().getPath().toString() + "/localMemory.txt";
        File file = new File(filePath);
        if (file.exists())
            file.delete();
    }
}
