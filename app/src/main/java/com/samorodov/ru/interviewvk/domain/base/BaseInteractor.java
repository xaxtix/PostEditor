package com.samorodov.ru.interviewvk.domain.base;


import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseInteractor {

    private final ObservableTransformer schedulerTransformer;

    public BaseInteractor() {
        schedulerTransformer = o -> o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @SuppressWarnings("unchecked")
    protected <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulerTransformer;
    }
}
