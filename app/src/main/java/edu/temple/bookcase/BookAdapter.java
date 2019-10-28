package edu.temple.bookcase;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookAdapter extends BaseAdapter {
    String booknames[];
    Context c;
    public BookAdapter(Context c, String booknames[]){
        this.c = c;
        this.booknames = booknames;
    }
    @Override
    public int getCount() {
        return booknames.length;
    }

    @Override
    public Object getItem(int position) {
        return booknames[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView t = new TextView(c);
        t.setText(booknames[position]);
        t.setId(position);
        LinearLayout ll = new LinearLayout(c);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.addView(t);
        return ll;
    }
}
