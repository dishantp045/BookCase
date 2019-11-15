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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.Buffer;
import java.util.*;

public class MainActivity extends AppCompatActivity implements BookListFragment.OnFragmentInteractionListener {
    ArrayList<Book> names;
    private boolean twoPane = false;
    BookListFragment blf;
    BookDetailsFragment bdf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twoPane = findViewById(R.id.container2) == null;
        names = new ArrayList<Book>();
        final String searchString = "";
        Thread t = new Thread(){
            @Override
            public void run() {
                URL bookURL;
                try {
                    bookURL = new URL("https://kamorris.com/lab/audlib/booksearch.php?search="+searchString);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(bookURL.openStream()));
                    String response = "",tmpResponse;
                    tmpResponse = reader.readLine();
                    while(tmpResponse != null){
                        response = response + tmpResponse;
                        tmpResponse = reader.readLine();
                    }
                    JSONArray bookOBJ= new JSONArray(response);
                    Message msg = Message.obtain();
                    msg.obj = bookOBJ;
                    bookHandler.handleMessage(msg);
                } catch (Exception e) {
                    Log.d("Fail", e.toString());
                }

            }
        };
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

    Handler bookHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            JSONArray tmp = (JSONArray) msg.obj;
            try{
                for(int i = 0; i < tmp.length(); i++){
                   JSONObject a = tmp.getJSONObject(i);
                   int id = a.getInt("book_id");
                   String title = a.getString("title");
                   String author = a.getString("author");
                   int published =  a.getInt("published");
                   String coverUrl = a.getString("cover_url");
                   Book book = new Book(id,title,author,published,coverUrl);
                   names.add(book);
                }

            } catch (Exception e){
                Log.d("FAIL", e.toString());
            }
            return false;
        }
    });


    @Override
    public void onItemSelection(Book bookname) {
        bdf.updateBookName(bookname);
    }
}
