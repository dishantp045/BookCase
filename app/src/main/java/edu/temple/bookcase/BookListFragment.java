package edu.temple.bookcase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "bookname";

    // TODO: Rename and change types of parameters
    private String bookNames[];
    private ListView listView;
    private Context parent;
    private OnFragmentInteractionListener mListener;

    public BookListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment BookListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookListFragment newInstance(String bookName[]) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, bookName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookNames = getArguments().getStringArray(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_list, container, false);
        listView = v.findViewById(R.id.listView);
        listView.setAdapter(new BookAdapter(parent,bookNames));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String a = bookNames[position];
                mListener.onItemSelection(a);
            }
        });
        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parent = context;
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
        void onItemSelection(String bookname);
    }
}
