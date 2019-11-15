package edu.temple.bookcase;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.*;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Book> names;
    public ViewPagerAdapter(FragmentManager fm, ArrayList<Book> names){
        super(fm);
        this.names = names;
    }
    @Override
    public Fragment getItem(int i) {
        return BookDetailsFragment.newInstance(names.get(i));
    }

    @Override
    public int getCount() {
        return names.size();
    }
}
