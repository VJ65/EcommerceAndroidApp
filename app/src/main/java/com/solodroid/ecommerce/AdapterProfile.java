package com.solodroid.ecommerce;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterProfile extends FragmentPagerAdapter {

    public AdapterProfile(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        Bundle bundle = new Bundle();
        int imgResId = 0;
        int tab = 0;
        int colorResId = 0;
        switch (index) {
            case 0:
            	tab = R.string.profile_tab1;
                break;
            case 1:
            	tab = R.string.profile_tab2;
                break;
        }
        bundle.putInt("image", imgResId);
        bundle.putInt("tab",tab);
        bundle.putInt("color", colorResId);
        SwipeTabFragment swipeTabFragment = new SwipeTabFragment();
        swipeTabFragment.setArguments(bundle);
        return swipeTabFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}