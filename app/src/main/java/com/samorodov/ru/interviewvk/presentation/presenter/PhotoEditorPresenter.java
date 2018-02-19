package com.samorodov.ru.interviewvk.presentation.presenter;

import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.samorodov.ru.interviewvk.domain.PhotoEditorInteractor;
import com.samorodov.ru.interviewvk.presentation.view.PhotoEditorView;

import javax.inject.Inject;

import dagger.Lazy;

@InjectViewState
public class PhotoEditorPresenter extends MvpPresenter<PhotoEditorView> {

    @Inject
    Lazy<PhotoEditorInteractor> interactor;

    public void loadStickers() {
        interactor.get().loadStickers()
                .subscribe(
                        stickers -> getViewState().setStickers(stickers),
                        Throwable::printStackTrace
                );
    }

    public void addBackgroundImage(Uri image) {
        getViewState().addBackgroundImageToPicker(image);
    }
}
