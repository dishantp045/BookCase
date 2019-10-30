package edu.temple.bookcase;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    String names[];
    public ViewPagerAdapter(FragmentManager fm, String names[]){
        super(fm);
        this.names = names;
    }
    @Override
    public Fragment getItem(int i) {
        return BookDetailsFragment.newInstance(names[i]);
    }

    @Override
    public int getCount() {
        return names.length;
    }
}
