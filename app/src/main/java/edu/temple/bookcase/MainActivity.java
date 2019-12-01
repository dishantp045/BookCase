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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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
import edu.temple.audiobookplayer.AudiobookService;
public class MainActivity extends AppCompatActivity implements BookListFragment.OnFragmentInteractionListener, BookDetailsFragment.OnFragmentInteractionListener {
    private boolean twoPane = false;
    BookListFragment blf;
    BookDetailsFragment bdf;
    boolean reload = false;
   // Handler bookHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twoPane = findViewById(R.id.container2) == null;
        final EditText searchBar = findViewById(R.id.searchbar);
        Thread t = new Thread(){
            @Override
            public void run() {
                String searchString = searchBar.getText().toString();
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
                    Log.d("Handler", response);

                    Message msg = Message.obtain();
                    msg.obj = response;
                    bookHandler.sendMessage(msg);
                } catch (Exception e) {
                    Log.e("Fail", e.toString());
                }

            }
        };
        if(reload == false){
            t.start();
        }
        reload = true;
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(){
                    @Override
                    public void run() {
                        String searchString = searchBar.getText().toString();
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
                            Message msg = Message.obtain();
                            msg.obj = response;
                            bookHandler.sendMessage(msg);
                        } catch (Exception e) {
                            Log.d("Fail", e.toString());
                        }

                    }
                };
                t.start();
                CharSequence toast = "Getting new Data";
                Toast.makeText(getApplicationContext(),toast, Toast.LENGTH_LONG);
            }
        });


        Button stop = findViewById(R.id.stopButton);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Stopping",Toast.LENGTH_LONG).show();
            }
        });

        SeekBar seekBar = findViewById(R.id.seekBar);



    }

    Handler bookHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String response = (String) msg.obj;
            ArrayList<Book> names = new ArrayList<Book>();
            try{
                JSONArray tmp = new JSONArray(response);
                for(int i = 0; i < tmp.length(); i++){
                    JSONObject a = tmp.getJSONObject(i);
                    int id = a.getInt("book_id");
                    String title = a.getString("title");
                    String author = a.getString("author");
                    int published =  a.getInt("published");
                    String coverUrl = a.getString("cover_url");
                    int duration = a.getInt("duration");
                    Book book = new Book(id,title,author,published,coverUrl,duration);
                    names.add(book);
                }
                ViewPagerFragment vpf = ViewPagerFragment.newInstance(names);
                blf = BookListFragment.newInstance(names);
                bdf = BookDetailsFragment.newInstance(names.get(0));
                boolean checkPanes = findViewById(R.id.container2)==null;
                if(checkPanes){

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container1,vpf);
                    ft.commit();
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container1,blf);
                    ft.replace(R.id.container2,bdf);
                    ft.commit();
                }
                Log.d("List Size", names.size()+"");
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

    @Override
    public void hitPlay() {

    }
}
