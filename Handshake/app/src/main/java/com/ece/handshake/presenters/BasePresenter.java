package com.ece.handshake.presenters;

import de.greenrobot.event.EventBus;

public class BasePresenter implements IBasePresenter{

    @Override
    public void resume() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void pause() {
        EventBus.getDefault().unregister(this);
    }
}
