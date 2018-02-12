package com.samorodov.ru.interviewvk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.samorodov.ru.interviewvk.view.PhotoEditorView;
import com.samorodov.ru.interviewvk.view.stickers.StickersPopupView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.font_style_button) ImageView fontStyleButton;
    @BindView(R.id.stickers_button) ImageView stickersButton;
    @BindView(R.id.toolbar) FrameLayout toolbar;
    @BindView(R.id.editor_view) PhotoEditorView editorView;
    @BindView(R.id.content) ViewGroup content;

    @Nullable
    StickersPopupView stickersPopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        stickersButton.setOnClickListener(v -> {
            if(stickersPopup == null) {
                initStickersPopup();
            }

            stickersPopup.toggle();
        });


    }

    private void initStickersPopup() {
        stickersPopup = new StickersPopupView(MainActivity.this);
        stickersPopup.setOnStickerSelectedListener(sticker -> {
            editorView.addSticker(sticker);
        });
        content.addView(stickersPopup);
    }
}
