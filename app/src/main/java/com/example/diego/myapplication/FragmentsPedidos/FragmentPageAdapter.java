package com.example.diego.myapplication.FragmentsPedidos;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;



/**
 * Created by diego on 26/02/2018.
 */

public class FragmentPageAdapter extends FragmentPagerAdapter {

    private String titulos[] = new String[] {"Pendientes","Entregados","Cobrados"};

    public FragmentPageAdapter(FragmentManager fm){
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new pedidos_pendientes();
            case 1: return new pedidos_entregados();
            case 2: return new pedidos_cobrados();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        return titulos[position];
    }

}
