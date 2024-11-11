package com.library.libraryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class BookDetailsFragment extends Fragment {

    private Button buyButton;
    private boolean isPurchased = false;

    private final Book book;

    public BookDetailsFragment(Book selectedBook) {
        this.book = selectedBook;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_details, container, false);

        ImageView bookImage = rootView.findViewById(R.id.bookImage);
        TextView bookTitle = rootView.findViewById(R.id.bookTitle);
        TextView bookAuthor = rootView.findViewById(R.id.bookAuthor);
        TextView bookDescription = rootView.findViewById(R.id.bookDescription);
        buyButton = rootView.findViewById(R.id.buyButton);

        bookImage.setImageResource(book.getImageResource());
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookDescription.setText(book.getDescription());

        buyButton.setOnClickListener(v -> {
            if (!isPurchased) {
                isPurchased = true;
                Toast.makeText(getContext(), "Теперь вам доступно чтение книги!", Toast.LENGTH_SHORT).show();
                buyButton.setText("Читать");
            } else {
                BookReaderFragment bookReaderFragment = new BookReaderFragment();

                Bundle args = new Bundle();
                args.putString("book_title", book.getTitle());
                bookReaderFragment.setArguments(args);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, bookReaderFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}
