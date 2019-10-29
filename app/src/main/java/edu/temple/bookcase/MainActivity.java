package edu.temple.bookcase;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements BookListFragment.OnFragmentInteractionListener {

    private boolean twoPane = false;
    private FragmentManager fm;
    private FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twoPane = findViewById(R.id.container2) == null;
        Context context = getApplicationContext();
        Resources res = context.getResources();
        final String names[] = res.getStringArray(R.array.bookNames);

        if(!twoPane){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            FrameLayout container1 = findViewById(R.id.container1);


        }

    }

    @Override
    public void onItemSelection(String bookname) {

    }
}
