package com.sundropelectric.sundropper.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by seth.darr on 5/5/2014.
 */
public class MenuPagerAdapter extends FragmentPagerAdapter {

    public MenuPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MenuPage.newInstance(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
