package com.samorodov.ru.interviewvk.di.component;

import com.samorodov.ru.interviewvk.presentation.presenter.PhotoEditorPresenter;

import dagger.Component;

@Component
public interface PhotoEditorComponent {

    void inject(PhotoEditorPresenter presenter);
}
