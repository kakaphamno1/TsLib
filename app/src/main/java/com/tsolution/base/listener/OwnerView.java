package com.tsolution.base.listener;


import android.view.View;

import com.tsolution.base.BaseModel;

public interface OwnerView extends BaseListener{
    public void onClicked(View view, BaseModel baseModel);
}
