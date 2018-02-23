package com.samorodov.ru.interviewvk.presentation.presenter;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.samorodov.ru.interviewvk.presentation.view.BaseView;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<View extends BaseView> extends MvpPresenter<View> {


    protected CompositeDisposable subscriptions = new CompositeDisposable();

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.dispose();
    }

    public <T> ObservableTransformer<T, T> syncWaitState() {
        return source -> source
                .doOnSubscribe(ignore -> getViewState().enableView(false))
                .doOnTerminate(() -> getViewState().enableView(true));
    }
}
