package edu.temple.bookcase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link BookDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "bookname";
    private static final String ARG_PARAM2 = "param2";
    private OnFragmentInteractionListener mListener;
    // TODO: Rename and change types of parameters



    private Book bookName;
    private TextView title;
    private TextView author;
    private TextView year;
    private ImageView cover;
    public BookDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BookDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookDetailsFragment newInstance(Book bookName) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, bookName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           this.bookName = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);
        author  = v.findViewById(R.id.author);
        author.setGravity(Gravity.CENTER);
        title = v.findViewById(R.id.title);
        title.setGravity(Gravity.CENTER);
        year = v.findViewById(R.id.year);
        year.setGravity(Gravity.CENTER);
        cover = v.findViewById(R.id.cover);
        author.setText(bookName.getAuthor());
        Picasso.get().load(bookName.getCoverUrl()).resize(150,150).centerCrop().into(cover);
        title.setText(bookName.getTitle());
        year.setText(bookName.getPublished()+"");
        Button play = v.findViewById(R.id.playButton);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.hitPlay(bookName);
            }
        });
        Button download = v.findViewById(R.id.downloadButton);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.hitDownload(bookName);
            }
        });
        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    public void updateBookName(Book BookName){
        author.setText(BookName.getAuthor());
        title.setText(BookName.getTitle());
        year.setText(BookName.getPublished()+"");
        Picasso.get().load(BookName.getCoverUrl()).resize(150,150).centerCrop().into(cover);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void hitPlay(Book book);

        void hitDownload(Book book);
    }

}
