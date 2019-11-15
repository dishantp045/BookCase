package edu.temple.bookcase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        Picasso.get().load(bookName.getCoverUrl()).resize(25,25).centerCrop().into(cover);
        title.setText(bookName.getTitle());
        year.setText(bookName.getPublished()+"");
        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void updateBookName(Book BookName){
        author.setText(BookName.getAuthor());
        title.setText(BookName.getTitle());
        year.setText(BookName.getPublished()+"");
        Picasso.get().load(BookName.getCoverUrl()).resize(25,25).centerCrop().into(cover);
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

}
