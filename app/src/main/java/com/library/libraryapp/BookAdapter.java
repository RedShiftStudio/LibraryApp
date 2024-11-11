package com.library.libraryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final List<Book> books;

    public BookAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.description.setText(book.getDescription());
        if (book.getImageResource() != 0) {
            holder.image.setImageResource(book.getImageResource());
        }
        holder.itemView.setOnClickListener(v -> {
            BookDetailsFragment bookDetailsFragment = new BookDetailsFragment(book);

            Bundle args = new Bundle();
            args.putString("title", book.getTitle());
            args.putString("author", book.getAuthor());
            args.putString("description", book.getDescription());
            bookDetailsFragment.setArguments(args);

            ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new BookDetailsFragment(book))
                    .addToBackStack(null)
                    .commit();
        });
    }


    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, description;
        ImageView image;

        public BookViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookTitle);
            author = itemView.findViewById(R.id.bookAuthor);
            description = itemView.findViewById(R.id.bookDescription);
            image = itemView.findViewById(R.id.bookImage);
        }
    }
}
