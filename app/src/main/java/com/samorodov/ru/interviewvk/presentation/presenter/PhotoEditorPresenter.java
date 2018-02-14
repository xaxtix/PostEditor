package com.samorodov.ru.interviewvk.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.samorodov.ru.interviewvk.domain.PhotoEditorInteractor;
import com.samorodov.ru.interviewvk.presentation.view.PhotoEditorView;

import javax.inject.Inject;

@InjectViewState
public class PhotoEditorPresenter extends MvpPresenter<PhotoEditorView> {

    @Inject
    PhotoEditorInteractor interactor;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void loadStickers() {
        interactor.loadStickers()
                .subscribe(
                        stickers -> getViewState().setStickers(stickers),
                        Throwable::printStackTrace
                );
    }
}
