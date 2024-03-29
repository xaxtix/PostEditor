package com.samorodov.ru.interviewvk.presentation.view;

import android.net.Uri;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;


public interface PhotoEditorView extends BaseView {

    void setStickers(List<Uri> stickers);

    void addBackgroundImageToPicker(Uri image);

    void imageSavedSuccess(String uri);

}
