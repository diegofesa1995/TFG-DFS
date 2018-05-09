package com.example.diego.myapplication.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.example.diego.myapplication.FragmentsPedidos.FragmentPageAdapter;
import com.example.diego.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;


public class Pedidos extends Fragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_pedidos, container, false);

        ViewPager vp = (ViewPager) rootView.findViewById(R.id.viewpager);
        vp.setAdapter(new FragmentPageAdapter(getChildFragmentManager()));

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.appbartabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp);

        return rootView;
    }

    //CONTROL BACK BUTTON
    public void onBackPressed() {
       /* Intent i = new Intent(Pedidos.this, MainActivity.class);
        startActivity(i);*/
    }
}
