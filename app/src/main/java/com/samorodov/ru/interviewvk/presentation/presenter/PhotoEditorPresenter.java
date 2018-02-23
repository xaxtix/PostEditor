package com.samorodov.ru.interviewvk.presentation.presenter;

import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.samorodov.ru.interviewvk.domain.PhotoEditorInteractor;
import com.samorodov.ru.interviewvk.presentation.view.PhotoEditorView;

import javax.inject.Inject;

import dagger.Lazy;

import static android.text.TextUtils.isEmpty;

@InjectViewState
public class PhotoEditorPresenter extends BasePresenter<PhotoEditorView> {

    @Inject
    Lazy<PhotoEditorInteractor> interactor;

    public void loadStickers() {
        subscriptions.add(
                interactor.get().loadStickers()
                        .subscribe(
                                stickers -> getViewState().setStickers(stickers),
                                Throwable::printStackTrace
                        ));
    }

    public void addBackgroundImage(Uri image) {
        getViewState().addBackgroundImageToPicker(image);
    }

    public void saveImageAndAddToGallery(com.samorodov.ru.interviewvk.presentation.ui.view.PhotoEditorView
                                                 editorView) {
        subscriptions.add(
                interactor.get().saveAndAddToGallery(editorView)
                        .compose(syncWaitState())
                        .subscribe(uri -> {
                            if (!isEmpty(uri)) getViewState().imageSavedSuccess(uri);
                        }, Throwable::printStackTrace)
        );
    }
}
