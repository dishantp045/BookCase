package edu.temple.bookcase;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    private int id;
    private String title;
    private String author;
    private int published;
    private String coverUrl;
    private int duration;

    public Book(int id, String title, String author, int published, String coverUrl, int duration){
        this.id = id;
        this.title = title;
        this.author = author;
        this.published = published;
        this.coverUrl = coverUrl;
        this.duration = duration;
    }

    protected Book(Parcel in) {
        id = in.readInt();
        title = in.readString();
        author = in.readString();
        published = in.readInt();
        coverUrl = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public int getPublished(){
        return published;
    }

    public String getCoverUrl(){
        return coverUrl;
    }

    public int getDuration(){return duration;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(published);
        dest.writeString(coverUrl);
        dest.writeInt(duration);
    }
}
