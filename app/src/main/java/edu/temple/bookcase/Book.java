package edu.temple.bookcase;

public class Book {

    private int id;
    private String title;
    private String author;
    private int published;
    private String coverUrl;

    public Book(int id, String title, String author, int published, String coverUrl){
        this.id = id;
        this.title = title;
        this.author = author;
        this.published = published;
        this.coverUrl = coverUrl;
    }

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
}
