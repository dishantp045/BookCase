package edu.temple.bookcase;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.*;
import edu.temple.audiobookplayer.AudiobookService;
public class MainActivity extends AppCompatActivity implements BookListFragment.OnFragmentInteractionListener, BookDetailsFragment.OnFragmentInteractionListener {
    private boolean twoPane = false;
    private static String nowPlayingTitle;
    private static int nowPlayingDuration;
    private static int nowPlayingStatus;
    BookListFragment blf;
    BookDetailsFragment bdf;
    boolean reload = false;
    AudiobookService.MediaControlBinder audioPlayer;
    SeekBar seekBar;
    Button pauseBtn;
    Button stopBtn;
    TextView titlePlaying;
    boolean isBound = false;
    Intent audioServiceIntent;
    SharedPreferences sharedPreferences;
    boolean autoSave;
   // Handler bookHandler;

    private ServiceConnection audioConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            audioPlayer = (AudiobookService.MediaControlBinder) service;
            audioPlayer.setProgressHandler(seekBarHandler);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        autoSave = sharedPreferences.getBoolean("autoSave",false);
        twoPane = findViewById(R.id.container2) == null;
        pauseBtn = findViewById(R.id.pauseButton);
        stopBtn = findViewById(R.id.stopButton);
        seekBar = findViewById(R.id.seekBar);
        titlePlaying = findViewById(R.id.textView);
        audioServiceIntent = new Intent(this,AudiobookService.class);
        bindService(audioServiceIntent,audioConnection,Context.BIND_AUTO_CREATE);
        titlePlaying.setText(nowPlayingTitle);
        seekBar.setProgress(MainActivity.nowPlayingStatus);
        seekBar.setMax(MainActivity.nowPlayingDuration);

        final EditText searchBar = findViewById(R.id.searchbar);
        Thread t = new Thread(){
            @Override
            public void run() {
                String searchString = sharedPreferences.getString("search","");
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
        if(!autoSave) {
            t.start();
        }
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(){
                    @Override
                    public void run() {
                        String searchString = searchBar.getText().toString();
                        sharedPreferences.edit().putString("search",searchString);
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



        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Stopping",Toast.LENGTH_LONG).show();
                if(isBound){
                    //Toast.makeText(getApplicationContext(),"Stopping",Toast.LENGTH_LONG).show();
                    audioPlayer.stop();
                    nowPlayingTitle = "";
                    titlePlaying.setText(nowPlayingTitle);
                    seekBar.setProgress(0);
                    stopService(audioServiceIntent);
                }
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pausing",Toast.LENGTH_LONG).show();
                if(isBound && audioPlayer.isPlaying()){
                    Toast.makeText(getApplicationContext(),"Pausing",Toast.LENGTH_LONG).show();
                    audioPlayer.pause();
                } else if(isBound){
                    audioPlayer.pause();
                    Toast.makeText(getApplicationContext(),"Un-Pausing",Toast.LENGTH_LONG).show();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    if(isBound){
                        audioPlayer.seekTo(progress);
                    } else {
                        nowPlayingStatus = progress;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    Handler seekBarHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.obj != null){
                seekBar = findViewById(R.id.seekBar);
                if(seekBar != null){
                    AudiobookService.BookProgress bookProgress = (AudiobookService.BookProgress) msg.obj;
                    if(bookProgress.getProgress() < 0 || bookProgress.getProgress() > 100000){
                        if(isBound){
                            audioPlayer.stop();
                            nowPlayingTitle = "";
                            titlePlaying.setText(nowPlayingTitle);
                            seekBar.setProgress(0);
                        }
                    }
                    if(bookProgress.getProgress()<MainActivity.nowPlayingDuration){
                        seekBar.setProgress(bookProgress.getProgress());
                    }
                    if(bookProgress.getProgress()==MainActivity.nowPlayingDuration){
                        audioPlayer.stop();
                        nowPlayingTitle = "";
                        titlePlaying.setText(nowPlayingTitle);
                        seekBar.setProgress(0);
                        stopService(audioServiceIntent);
                    }
                }
            }
            return false;
        }
    });

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
    public void hitPlay(Book book) {
        startService(audioServiceIntent);
        Toast.makeText(getApplicationContext(), "Playing", Toast.LENGTH_SHORT).show();
        Log.d("bound", isBound+"");
        if(isBound){
            //Toast.makeText(getApplicationContext(), "Playing", Toast.LENGTH_SHORT).show();
            seekBar.setMax(book.getDuration());
            nowPlayingTitle = book.getTitle();
            nowPlayingDuration = book.getDuration();
            String a = "Now Playing: " + nowPlayingTitle;
            titlePlaying.setText(a);
            audioPlayer.play(book.getId());
        }
    }

    @Override
    public void hitDownload(Book book) {
        final Book b = book;
        Thread mp3 = new Thread(){
            @Override
            public void run() {
                FileInputStream fis = null;
                try {
                    fis = openFileInput("audio_"+b.getId()+".mp3");
                } catch(IOException e){
                    Log.e("fis opening", "run: ", e);
                }
                if(fis == null){
                    String toDownload = "https://kamorris.com/lab/audlib/download.php?id="+b.getId();
                    int count;
                    try{
                        URL url = new URL(toDownload);
                        URLConnection conn = url.openConnection();
                        conn.connect();
                        int fileLength = conn.getContentLength();
                        Log.d("ANDRO_ASYNC", "Length of file: " + fileLength);
                        InputStream input = new BufferedInputStream(url.openStream());
                        Log.d("INPUT", "run: ");
                        OutputStream output = new FileOutputStream("audio_"+b.getId()+".mp3");
                        Log.d("OUTPUT", "run: ");
                        byte data[] = new byte[1024];
                        long total = 0;
                        while((count = input.read(data)) != -1){
                            total+=count;
                            output.write(data,0,count);
                        }
                        output.flush();
                        output.close();
                        input.close();
                    } catch(MalformedURLException e){
                        Log.e("DOWNLOAD ERROR","URL FAILED FOR "+b.getTitle(),e);
                    } catch(IOException e){
                        Log.e("DOWNLOAD ERROR","Opening Connection failed FOR "+b.getTitle(),e);
                    }
                } else {
                    File dir = getFilesDir();
                    File file = new File(dir,"audio_"+b.getId()+".mp3");
                    boolean closed = file.delete();
                    Log.d("FILE DELETED", b.getId()+"was deleted is "+closed);
                }

            }
        };
        mp3.start();

    }

}
