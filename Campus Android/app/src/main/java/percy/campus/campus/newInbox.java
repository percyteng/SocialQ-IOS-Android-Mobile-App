package percy.campus.campus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

public class newInbox extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener{
    ViewPager viewPager;
    TabHost tabHost;
    String username;
    ImageView refreshIcon;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_inbox);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        back = (ImageView) findViewById(R.id.back);
        refreshIcon = (ImageView) findViewById(R.id.refreshIcon);
        refreshIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(newInbox.this, newInbox.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initViewPager();
        initTabHost();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#383858"));
        }
    }
    private void initTabHost() {
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        String[]  tabNames = {"Inbox", "Outbox"};
        for (int i = 0; i<tabNames.length; i++){
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new FakeContent(getApplicationContext()));
            tabHost.addTab(tabSpec);
        }
        tabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        int seletedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(seletedItem);

    }

    public class FakeContent implements TabHost.TabContentFactory{
        Context context;
        public FakeContent(Context context){
            this.context = context;
        }
        @Override
        public View createTabContent(String tag) {
            View fakeView = new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);
            return fakeView;
        }
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
    private void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.vew_pager);

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        Fragment inbox = new inbox();
        inbox.setArguments(bundle);
        Fragment outbox = new outbox();
        outbox.setArguments(bundle);

        List<Fragment> listFragments = new ArrayList<Fragment>();
        listFragments.add(inbox);
        listFragments.add(outbox);

        myFragmentPagerListener myListener = new myFragmentPagerListener(getSupportFragmentManager(), listFragments);
        viewPager.setAdapter(myListener);
        viewPager.setOnPageChangeListener(this);
    }
}