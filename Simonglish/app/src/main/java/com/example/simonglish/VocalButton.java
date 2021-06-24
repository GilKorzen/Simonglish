package com.example.simonglish;

import android.widget.ImageButton;

public class VocalButton {

    private ImageButton imageButton;
    private Pair pair;

    public VocalButton(ImageButton imageButton,Pair pair)
    {
        this.imageButton=imageButton;
        this.pair=pair;
    }
    public ImageButton getImageButton() {
        return imageButton;
    }

    public String getEnglishWord() {
        return pair.getEnglish();
    }
    public String getHebrewWord()
    {
        return pair.getHebrew();
    }
}
