package com.example.android.miwok;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by devansh on 4/8/18.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int colorResourceId;

    public WordAdapter(Activity context, ArrayList<Word> words, int colorResourceId) {
        super(context, 0, words);
        this.colorResourceId = colorResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Check if the existing view is being reused, otherwise inflate the view
        View wordsListView = convertView;
        if (convertView == null) {
            wordsListView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Word currentWord = getItem(position);

        LinearLayout textViewsLayout = (LinearLayout) wordsListView.findViewById(R.id.textViewsLayout);
        int color = ContextCompat.getColor(getContext(), colorResourceId);
        textViewsLayout.setBackgroundColor(color);

        TextView miwokText = (TextView) wordsListView.findViewById(R.id.miwok_text);
        miwokText.setText(currentWord.getMiwokTranslation());

        TextView defaultText = (TextView) wordsListView.findViewById(R.id.default_text);
        defaultText.setText(currentWord.getDefaultTranslation());

        ImageView image = (ImageView) wordsListView.findViewById(R.id.image);
        if(currentWord.hasImage()) {
            image.setImageResource(currentWord.getImageResourceId());
            image.setContentDescription(currentWord.getDefaultTranslation());
            image.setVisibility(View.VISIBLE);
        }
        else {
            image.setVisibility(View.GONE);
        }

        return wordsListView;
    }
}
