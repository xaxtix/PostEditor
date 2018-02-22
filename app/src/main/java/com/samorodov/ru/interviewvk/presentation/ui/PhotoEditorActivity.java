package com.samorodov.ru.interviewvk.presentation.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.di.component.DaggerPhotoEditorComponent;
import com.samorodov.ru.interviewvk.presentation.presenter.PhotoEditorPresenter;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.AdditionalImagePickerAdapter;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.ImagePickerAdapter;
import com.samorodov.ru.interviewvk.presentation.ui.view.PhotoEditorView;
import com.samorodov.ru.interviewvk.presentation.ui.view.SizeNotifierFrameLayout;
import com.samorodov.ru.interviewvk.presentation.ui.view.stickers.StickersPopupView;
import com.samorodov.ru.interviewvk.utilits.AndroidUtilities;
import com.samorodov.ru.interviewvk.utilits.RecyclerViewUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.samorodov.ru.interviewvk.utilits.FileUtils.createImageFile;

public class PhotoEditorActivity extends MvpAppCompatActivity implements
        com.samorodov.ru.interviewvk.presentation.view.PhotoEditorView {

    private final static int PICK_IMAGE_REQUEST = 1;
    private final static int REQUEST_TAKE_PHOTO = 2;

    @BindView(R.id.font_style_button) ImageView fontStyleButton;
    @BindView(R.id.stickers_button) ImageView stickersButton;
    @BindView(R.id.toolbar) FrameLayout toolbar;
    @BindView(R.id.editor_view) PhotoEditorView editorView;
    @BindView(android.R.id.content) ViewGroup content;
    @BindView(R.id.content) SizeNotifierFrameLayout sizeNotifier;
    @BindView(R.id.image_picker) RecyclerView imagePicker;
    @BindView(R.id.bottomKeyboard) FrameLayout bottomPanel;
    @BindView(R.id.bottom_frame) View bottomFrame;
    @BindView(R.id.save) Button save;
    @BindView(R.id.additional_recycler) RecyclerView additionalRecycler;

    File photoFile;

    @Nullable
    StickersPopupView stickersPopup;

    @InjectPresenter
    PhotoEditorPresenter presenter;

    ImagePickerAdapter imagePickerAdapter;
    AdditionalImagePickerAdapter additionalImagePickerAdapter;

    int keyboardHeight;
    boolean keyboardShowing;

    private boolean doubleBackToExitPressedOnce = false;
    private final static int WRITE_EXTERNAL_STORAGE = 1;

    @ProvidePresenter
    public PhotoEditorPresenter providePresenter() {
        PhotoEditorPresenter presenter = new PhotoEditorPresenter();
        DaggerPhotoEditorComponent
                .create()
                .inject(presenter);
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_editor);
        ButterKnife.bind(this);

        keyboardHeight = getResources().getDimensionPixelSize(R.dimen.default_keyboard_size);

        bottomPanel.getLayoutParams().height = keyboardHeight;
        bottomPanel.setVisibility(View.GONE);
        ((SimpleItemAnimator) imagePicker.getItemAnimator()).setSupportsChangeAnimations(false);
        ((SimpleItemAnimator) additionalRecycler.getItemAnimator()).setSupportsChangeAnimations(false);


        stickersButton.setOnClickListener(v -> {
            if (stickersPopup == null) {
                initStickersPopup();
            }

            stickersPopup.toggle();
            AndroidUtilities.hideKeyboard(editorView);
        });

        imagePicker.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false
        ));

        imagePicker.setAdapter(imagePickerAdapter = new ImagePickerAdapter());
        imagePickerAdapter.setOnImageSelectedListener(uri -> {
            editorView.setBackgroundImage(uri);
            additionalImagePickerAdapter.setSelectedPosition(-1);
        });

        imagePickerAdapter.setOnGradientSelectedListener(gradient -> {
            editorView.setBack(gradient);
            additionalImagePickerAdapter.setSelectedPosition(-1);
        });

        sizeNotifier.setKeyboardSizeListener(keyboardHeight -> {
            keyboardShowing = keyboardHeight > 0;
            if (keyboardShowing && this.keyboardHeight != keyboardHeight) {
                this.keyboardHeight = keyboardHeight;
                bottomPanel.getLayoutParams().height = keyboardHeight;
                bottomPanel.requestLayout();
            }
            updateKeyboardState();
        });

        fontStyleButton.setOnClickListener(v ->
                editorView.toggleEditTextStyle()
        );

        imagePickerAdapter.setOnAdditionalListener(showBottomPanel -> {
            if (showBottomPanel)
                AndroidUtilities.hideKeyboard(editorView);
            bottomPanel.setVisibility(showBottomPanel ?
                    View.VISIBLE : View.GONE);
            updateKeyboardState();
        });

        additionalRecycler.setLayoutManager(new GridLayoutManager(
                this,
                2,
                LinearLayoutManager.HORIZONTAL,
                false
        ));

        additionalImagePickerAdapter = new AdditionalImagePickerAdapter();
        additionalRecycler.setAdapter(additionalImagePickerAdapter);
        additionalRecycler.addItemDecoration(new RecyclerViewUtils.ItemOffsetDecoration(
                AndroidUtilities.dp(this, 4)
        ));

        additionalImagePickerAdapter.setOnImageSelectedListener(uri -> {
            editorView.setBackgroundImage(uri);
            imagePickerAdapter.setSelectedPosition(imagePickerAdapter.getItemCount() - 1);

        });

        additionalImagePickerAdapter.setOnPickImageListener(ignore -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        additionalImagePickerAdapter.setTakeFotoListener(ignore -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                photoFile = null;
                try {
                    photoFile = createImageFile(this);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.samorodov.ru.interviewvk.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }

        });

        save.setOnClickListener(v -> {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE
                );
            } else {
                presenter.saveImageAndAddToGallery(editorView);
            }

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    presenter.saveImageAndAddToGallery(editorView);
                }
                break;

            default:
                break;
        }
    }

    private void updateKeyboardState() {
        if (bottomPanel.getVisibility() == View.VISIBLE || keyboardShowing) {
            editorView.setTranslationY(-keyboardHeight >> 1);
            bottomFrame.setTranslationY(-keyboardHeight);
        } else {
            editorView.setTranslationY(0);
            bottomFrame.setTranslationY(0);
        }
    }

    private void initStickersPopup() {
        stickersPopup = new StickersPopupView(PhotoEditorActivity.this);
        stickersPopup.setOnStickerSelectedListener(sticker ->
                editorView.addSticker(sticker)
        );
        content.addView(stickersPopup);
        presenter.loadStickers();
    }


    @Override
    public void setStickers(List<Uri> stickers) {
        if (stickersPopup != null)
            stickersPopup.setStickers(stickers);
    }

    @Override
    public void setBackgroundImagesToPicker(List<Uri> images) {

    }

    @Override
    public void addBackgroundImageToPicker(Uri image) {
        editorView.setBackgroundImage(image);
        additionalImagePickerAdapter.addImage(image);
    }

    @Override
    public void imageSavedSuccess(String uri) {
        Toast.makeText(this, "сохранено", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (stickersPopup != null && stickersPopup.isExpanded()) {
            stickersPopup.toggle();
            return;
        }

        if (bottomPanel.getVisibility() == View.VISIBLE) {
            bottomPanel.setVisibility(View.GONE);
            updateKeyboardState();
            return;
        }

        doubleBackClickPress();
    }

    private void doubleBackClickPress() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce = true;

        Toast.makeText(this, R.string.double_click_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            presenter.addBackgroundImage(uri);
        }

        if (requestCode == REQUEST_TAKE_PHOTO
                && resultCode == RESULT_OK) {
            Uri uri = Uri.fromFile(photoFile);
            presenter.addBackgroundImage(uri);
        }
    }
}
