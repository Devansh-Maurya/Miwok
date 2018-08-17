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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

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

    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        words.add(new Word("minto wuksus", "Where are you going?", R.raw.phrase_where_are_you_going));
        words.add(new Word("tinnә oyaase'nә","What is your name?", R.raw.phrase_what_is_your_name));
        words.add(new Word("oyaaset...", "My name is...", R.raw.phrase_my_name_is));
        words.add(new Word("michәksәs?","How are you feeling?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("kuchi achit", "I'm feeling good.",  R.raw.phrase_im_feeling_good));
        words.add(new Word("әәnәs'aa?", "Are you coming.", R.raw.phrase_are_you_coming));
        words.add(new Word("hәә’әәnәm","Yes, I'm coming.", R.raw.phrase_yes_im_coming));
        words.add(new Word("әәnәm","I'm coming.", R.raw.phrase_im_coming));
        words.add(new Word("yoowutis","Let's go.", R.raw.phrase_lets_go));
        words.add(new Word("әnni'nem","Come here", R.raw.phrase_come_here));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

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
