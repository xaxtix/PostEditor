package com.samorodov.ru.interviewvk.domain;

import android.net.Uri;

import com.samorodov.ru.interviewvk.data.repository.StickersRepository;
import com.samorodov.ru.interviewvk.domain.base.BaseInteractor;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


public class PhotoEditorInteractor extends BaseInteractor {

    private final StickersRepository repository;

    @Inject
    public PhotoEditorInteractor(StickersRepository repository) {
        this.repository = repository;
    }

    public Observable<List<Uri>> loadStickers() {
        return repository.getStickersUriList()
                .compose(applySchedulers());
    }
}
