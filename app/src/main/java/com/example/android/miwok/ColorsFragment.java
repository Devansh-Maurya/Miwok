package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    private ArrayList<Word> words = new ArrayList<Word>();
    private MediaPlayer mediaPlayer;
    private MediaPlayerCompletionListener mediaPlayerCompletionListener = new MediaPlayerCompletionListener();
    /** Handles audio focus when playing a sound file */
    private AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if((focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) && mediaPlayer != null) {
                //We'll treat both cases the same way because our app is playing short sound files.
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_GAIN && mediaPlayer != null ) {
                mediaPlayer.start();
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };

    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        words.add(new Word("weṭeṭṭi", "red", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("chokokki","green", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("ṭakaakki", "brown", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("ṭopoppi","gray", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("kululli", "black", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("kelelli", "white", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("ṭopiisә","dusty yellow", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("chiwiiṭә","mustard yellow", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_colors);

        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new ListItemClickListener());

        audioManager.abandonAudioFocus(onAudioFocusChangeListener);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            // Release the media player if it currently exists because we are about to
            // play a different sound file
            releaseMediaPlayer();

            Word currentWord = words.get(position);

            int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mediaPlayer = MediaPlayer.create(getActivity(), currentWord.getSoundResourceId());
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(mediaPlayerCompletionListener);
            }
        }
    }

    private class MediaPlayerCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //release the media player resources when the sound file has finished playing
            releaseMediaPlayer();
        }
    }

}
