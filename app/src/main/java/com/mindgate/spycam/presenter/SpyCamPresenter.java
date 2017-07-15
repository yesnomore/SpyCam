package com.mindgate.spycam.presenter;

import android.content.Context;

import com.mindgate.spycam.interactor.SpyCamInteractor;
import com.mindgate.spycam.view.SpyCamMainActivity;
import com.mindgate.spycam.view.SpyCamMainActivityInterface;

public class SpyCamPresenter implements SpyCamPresenterInterface {
    Context context;
    SpyCamInteractor interactor;
    SpyCamMainActivityInterface viewInterface;

    public SpyCamPresenter(Context context, SpyCamMainActivityInterface viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;
    }


}
