package com.library.libraryapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView bookRecyclerView;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        EditText searchBar = rootView.findViewById(R.id.searchBar);
        bookRecyclerView = rootView.findViewById(R.id.bookRecyclerView);


        bookRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setupBookList();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return rootView;
    }


    private void filterBooks(String query) {
        List<Book> filteredBooks = new ArrayList<>();

        for (Book book : getBooks()) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                    book.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredBooks.add(book);
            }
        }

        BookAdapter adapter = new BookAdapter(filteredBooks);
        bookRecyclerView.setAdapter(adapter);
    }


    private void setupBookList() {
        List<Book> allBooks = getBooks();
        BookAdapter adapter = new BookAdapter(allBooks);
        bookRecyclerView.setAdapter(adapter);
    }

    private List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        try {
            InputStream inputStream = requireContext().getAssets().open("books.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            Gson gson = new Gson();
            BooksContainer booksContainer = gson.fromJson(json.toString(), BooksContainer.class);

            for (Book book : booksContainer.getBooks()) {
                @SuppressLint("DiscouragedApi") int imageResId = getResources().getIdentifier(book.getImageResourceName(), "drawable", requireContext().getPackageName());
                book.setImageResource(imageResId);
                books.add(book);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    private static class BooksContainer {
        private final List<Book> books;

        private BooksContainer(List<Book> books) {
            this.books = books;
        }

        public List<Book> getBooks() {
            return books;
        }
    }

}
