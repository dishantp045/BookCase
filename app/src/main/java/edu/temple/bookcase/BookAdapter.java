package edu.temple.bookcase;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.*;
public class BookAdapter extends BaseAdapter {
    ArrayList<Book> booknames;
    Context c;
    public BookAdapter(Context c, ArrayList<Book> booknames){
        this.c = c;
        this.booknames = booknames;
    }
    @Override
    public int getCount() {
        return booknames.size();
    }

    @Override
    public Object getItem(int position) {
        return booknames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView t = new TextView(c);
        t.setText(booknames.get(position).getTitle());
        t.setId(position);
        LinearLayout ll = new LinearLayout(c);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.addView(t);
        return ll;
    }
}
