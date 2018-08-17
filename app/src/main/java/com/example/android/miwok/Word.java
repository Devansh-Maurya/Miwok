package com.example.android.miwok;

/**
 * Created by devansh on 4/8/18.
 */

public class Word {
    private String miwokTranslation;
    private String defaultTranslation;
    private int imageResourceId = IMAGE_IS_SET;
    private static final int IMAGE_IS_SET = -1;
    private int soundResourceId;

    public Word(String miwokTranslation, String defaultTranslation, int soundResourceId) {
        this.miwokTranslation = miwokTranslation;
        this.defaultTranslation = defaultTranslation;
        this.soundResourceId = soundResourceId;
    }

    public Word(String miwokTranslation, String defaultTranslation, int imageResourceId, int soundResourceId) {
        this(miwokTranslation, defaultTranslation, soundResourceId);
        this.imageResourceId = imageResourceId;
    }

    public String getMiwokTranslation() {
        return miwokTranslation;
    }

    public String getDefaultTranslation() {
        return  defaultTranslation;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int getSoundResourceId() {
        return soundResourceId;
    }

    public boolean hasImage() {
        return imageResourceId != IMAGE_IS_SET;
    }
}
