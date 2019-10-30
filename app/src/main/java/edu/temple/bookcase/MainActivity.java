package edu.temple.bookcase;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements BookListFragment.OnFragmentInteractionListener {

    private boolean twoPane = false;
    BookListFragment blf;
    BookDetailsFragment bdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twoPane = findViewById(R.id.container2) == null;
        Context context = getApplicationContext();
        Resources res = context.getResources();
        final String names[] = res.getStringArray(R.array.bookNames);
        if(twoPane){
            ViewPagerFragment vp = ViewPagerFragment.newInstance(names);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container1,vp);
            ft.commit();
        } else {
            blf = BookListFragment.newInstance(names);
            bdf = BookDetailsFragment.newInstance(names[0]);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container1, blf);
            ft.replace(R.id.container2,bdf);
            ft.addToBackStack(null);
            ft.commit();
        }

    }

    @Override
    public void onItemSelection(String bookname) {
        bdf.setBookName(bookname);
    }
}
