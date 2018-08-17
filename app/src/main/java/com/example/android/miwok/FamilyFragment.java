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
public class FamilyFragment extends Fragment {

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
                //Not doing mediaPlayer != null will crash the app on moving from one activity to another
                //We'll treat both cases the same way because our app is playing short sound files.
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_GAIN && mediaPlayer != null) {
                mediaPlayer.start();
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };

    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        words.add(new Word("әpә", "father", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("әṭa","mother", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("angsi", "son", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("tune","daughter", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("taachi", "older brother", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("chalitti", "younger brother", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("teṭe","older sister", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("kolliti","younger sister", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("ama","grandmother", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("paapa","grandfather", R.drawable.family_grandfather, R.raw.family_grandfather));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_family);

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
