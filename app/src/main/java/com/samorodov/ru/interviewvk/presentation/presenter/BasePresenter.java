package com.samorodov.ru.interviewvk.presentation.presenter;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<View extends MvpView> extends MvpPresenter<View> {


    protected CompositeDisposable subscriptions = new CompositeDisposable();

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.dispose();
    }
}
