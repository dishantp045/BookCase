package edu.temple.bookcase;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.Buffer;
import java.util.*;

public class MainActivity extends AppCompatActivity implements BookListFragment.OnFragmentInteractionListener {
    final ArrayList<Book> names = new ArrayList<Book>();
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
        final String searchString = "";
        Thread t = new Thread(){
            @Override
            public void run(){
                URL bookData;
                try{
                    bookData = new URL("https://kamorris.com/lab/audlib/booksearch.php?search="+searchString);
                    HttpURLConnection conn = (HttpURLConnection) bookData.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String response = "", tmpResponse;
                    tmpResponse =  br.readLine();
                    while(tmpResponse != null){
                        response= response + tmpResponse;
                        Log.d("response", tmpResponse);
                        tmpResponse = br.readLine();
                    }
                    System.out.println(response);
                    JSONObject bookObject = new JSONObject(response);
                    Message msg = Message.obtain();
                    msg.obj = bookObject;

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
        if(twoPane){
            ViewPagerFragment vp = ViewPagerFragment.newInstance(names);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container1,vp);
            ft.commit();
        } else {
            blf = BookListFragment.newInstance(names);
            bdf = BookDetailsFragment.newInstance(names.get(0));
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container1, blf);
            ft.replace(R.id.container2,bdf);
            ft.addToBackStack(null);
            ft.commit();
        }

    }

    Handler responseHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            JSONObject respObject = (JSONObject) msg.obj;

            return false;
        }
    });

    @Override
    public void onItemSelection(Book bookname) {
        bdf.updateBookName(bookname);
    }
}
