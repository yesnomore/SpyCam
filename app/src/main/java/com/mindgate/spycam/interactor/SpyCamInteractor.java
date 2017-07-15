package com.mindgate.spycam.interactor;

import android.content.Context;

import com.mindgate.spycam.presenter.SpyCamPresenterInterface;

public class SpyCamInteractor implements SpyCamInteractorInterface {
    Context context;
    SpyCamPresenterInterface presenterInterface;

    public SpyCamInteractor(Context context, SpyCamPresenterInterface presenterInterface) {
        this.context = context;
        this.presenterInterface = presenterInterface;
    }
}
