package com.tsolution.base;

import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import android.view.View;

public abstract class BaseBinding extends ViewDataBinding {


    protected BaseBinding(androidx.databinding.DataBindingComponent bindingComponent, View root, int localFieldCount) {
        super(bindingComponent, root, localFieldCount);
    }

    protected BaseBinding(Object bindingComponent, View root, int localFieldCount) {
        super(bindingComponent, root, localFieldCount);
    }
    public void setViewModel(BaseViewModel vm){

    }

}
