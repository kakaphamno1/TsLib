package com.tsolution.base.listener;

import android.view.View;

import com.tsolution.base.BaseViewModel;
import com.tsolution.base.exceptionHandle.AppException;
import com.workable.errorhandler.ErrorHandler;

public interface ViewFunction extends BaseListener {
    void process(String action, View view, BaseViewModel viewModel, Throwable t);

    default void action(String action, View view, BaseViewModel viewModel, Throwable t) throws AppException {
        try {
            process(action, view, viewModel, t);
        } catch (Exception e) {
            ErrorHandler.create().handle(e);
        }
    }

    default void action(String action, BaseViewModel viewModel) {
        try {
            process(action, null, viewModel, null);
        } catch (Exception e) {
            ErrorHandler.create().handle(e);
        }
    }
}
