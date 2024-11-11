package com.library.libraryapp;

public class Book {
    private final String title;
    private final String author;
    private final String description;
    private final String imageResourceName;
    private int imageResource;

    public Book(String title, String author, String description, String imageResourceName) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.imageResourceName = imageResourceName;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getImageResourceName() { return imageResourceName; }
    public int getImageResource() { return imageResource; }
    public void setImageResource(int imageResource) { this.imageResource = imageResource; }
}
