package com.library.libraryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BookReaderFragment extends Fragment {

    public BookReaderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_reader, container, false);

        TextView bookContentTextView = rootView.findViewById(R.id.bookContentTextView);
        if (getArguments() != null) {
            String bookTitle = getArguments().getString("book_title");
            if (bookTitle != null) {
                String bookContent = loadBookContent(bookTitle);
                bookContentTextView.setText(bookContent);
            }
        }

        return rootView;
    }

    private String loadBookContent(String bookTitle) {
        StringBuilder content = new StringBuilder();
        try {
            InputStream inputStream = requireActivity().getAssets().open("books.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(jsonString.toString());
            JSONArray booksArray = jsonObject.getJSONArray("books");

            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject bookObject = booksArray.getJSONObject(i);
                String title = bookObject.getString("title");
                if (title.equals(bookTitle)) {
                    content.append(bookObject.getString("content"));
                    break;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
