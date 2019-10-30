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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twoPane = findViewById(R.id.container2) == null;
        Context context = getApplicationContext();
        Resources res = context.getResources();
        String names[] = res.getStringArray(R.array.bookNames);

        if(!twoPane){

        } else {
            BookListFragment blf = BookListFragment.newInstance(names);
            BookDetailsFragment bdf = BookDetailsFragment.newInstance(names[0]);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container1, blf);
            ft.add(R.id.container2,bdf);
            ft.commit();
        }

    }

    @Override
    public void onItemSelection(String bookname) {
        BookDetailsFragment bdf = BookDetailsFragment.newInstance(bookname);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container2,bdf);
    }
}
