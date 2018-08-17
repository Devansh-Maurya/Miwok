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
public class NumbersFragment extends Fragment {

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

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        words.add(new Word("Lutti", "One", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("Otilko","Two", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("Tolookosu", "Three", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("Oyyisa","Four", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("Massaokka", "Five", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("Temmokka", "Six", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("Kenekaku","Seven", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("Kawinta","Eight", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("Wo'e","Nine", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("Na'aacha","Ten", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

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
