package com.example.closet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.closet.History.History;
import com.example.closet.Home.Home;
import com.example.closet.Match.Match;
import com.example.closet.Recommend.Recommend;

import com.example.closet.User.User;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    ImageButton imageButton;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    // uid
    Context context;
    static public String UID;
    GetUID getUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getUID = new GetUID(context);
        UID = getUID.uid;

        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager()));
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        init();
    }

    public void init() {
        int[] tabIcons = new int[]{R.drawable.icon_home, R.drawable.icon_match, R.drawable.icon_history, R.drawable.icon_recommend, R.drawable.icon_mypage};

        for (int i = 0; i < tabIcons.length; i++) {

            ImageView imageView = new ImageView(this);
            imageView.setBackgroundColor(Color.TRANSPARENT);
            imageView.setImageResource(tabIcons[i]);
            //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            //imageView.setPadding(5, 5, 5, 5);
            try {
                tabLayout.getTabAt(i).setCustomView(imageView);
            } catch (Exception e) {
                Log.d("Log_d", e.toString());
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    class myPagerAdapter extends FragmentStatePagerAdapter {
        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public myPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            fragmentList.add(new Home());
            fragmentList.add(new Match());
            fragmentList.add(new History());
            fragmentList.add(new Recommend());
            fragmentList.add(new User());
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            //return null;
            return this.fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return this.fragmentList.size();
        }
    }


}