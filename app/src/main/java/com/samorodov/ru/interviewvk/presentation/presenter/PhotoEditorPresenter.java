package com.samorodov.ru.interviewvk.presentation.presenter;

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

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void loadStickers() {
        interactor.get().loadStickers()
                .subscribe(
                        stickers -> getViewState().setStickers(stickers),
                        Throwable::printStackTrace
                );
    }
}
