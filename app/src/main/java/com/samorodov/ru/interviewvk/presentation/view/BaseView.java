package com.samorodov.ru.interviewvk.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface BaseView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void enableView(boolean enable);

}
