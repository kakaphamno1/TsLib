package com.tsolution.base.listener;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.tsolution.base.BaseActivity;
import com.tsolution.base.BaseModel;
import com.tsolution.base.BaseViewModel;
import com.tsolution.base.R;
import com.tsolution.base.exceptionHandle.AppException;

import java.lang.reflect.Method;


public interface DefaultFunctionActivity extends BaseListener {
    BaseActivity getBaseActivity();

    @LayoutRes
    int getLayoutRes();

    Class<? extends BaseViewModel> getVMClass();

    @IdRes
    int getRecycleResId();

    @IdRes
    default int getXRecycleResId() {
        return getRecycleResId();
    }


    default void processFromVM(String action, View view, BaseViewModel viewModel, Throwable t) {
        BaseActivity activity = getBaseActivity();
        if (t != null) {
            String msg;
            if (t instanceof AppException)
                msg = ((AppException) t).message;
            else {
                t.printStackTrace();
                msg = t.getMessage();
            }
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
        }
    }

    default void showProcessing(String msg) {
        try {
            BaseActivity activity = getBaseActivity();
            ProgressDialog pd = activity.getPd();
            if (pd == null) {
                pd = new ProgressDialog(activity);
                activity.setPd(pd);
            }
            if (!(getBaseActivity()).isFinishing()) {
                //show dialog
                if (!pd.isShowing()) {
                    pd.setCancelable(false);
                    pd.setMessage(msg);
                    pd.show();
                }
            }
        } catch (WindowManager.BadTokenException e) {
            //use a log message
            e.printStackTrace();
        }
    }


    default void showProcessing(@StringRes int id) {
        String msg = getBaseActivity().getResources().getString(id);
        showProcessing(msg);
    }


    default void closeAlertDialog() {
        BaseActivity activity = getBaseActivity();
        AlertDialog.Builder alertDialog = activity.getAlertDialog();
        if (alertDialog != null) {
//            alertDialog.di
        }
    }

    default void closeProcess() {
        BaseActivity activity = getBaseActivity();
        ProgressDialog pd = activity.getPd();
        if (pd != null) {
            pd.dismiss();
        }

    }

    default void processError(String action, View view, BaseViewModel viewModel, Throwable t) {
        if (t == null) {
            return;
        }
        t.printStackTrace();
        boolean sendError = false;
        //t.printStackTrace();
        String msg;
        if (t instanceof AppException) {
            AppException exception = (AppException) t;
            msg = exception.message;
            try {
                if (exception.code != null) {
                    msg = getBaseActivity().getResources().getString(exception.code, exception.message);
                }
            } catch (Resources.NotFoundException ex) {
                Integer i = AppException.errCode.get(exception.code);
                if (i != null) {
                    try {
                        msg = getBaseActivity().getResources().getString(i, exception.message);
                    } catch (Resources.NotFoundException ex1) {

                    }
                }
            }

//                msg = String.format(getBaseActivity().getResources().getString(exception.code),exception.message);
        } else {
            sendError = true;
//            msg = TsLog.printStackTrace(t);
            t.printStackTrace();
            msg = t.getMessage();
        }
        showError(msg, sendError);
        closeProcess();

    }


    default void showError(String msg, boolean sendError) {
        BaseActivity activity = getBaseActivity();
        AlertDialog.Builder alertDialog = activity.getAlertDialog();
        alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AppCompat_Dialog_Alert);
        activity.setAlertDialog(alertDialog);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(activity.getResources().getString(R.string.OK),
                (dialog, which) -> {
                    dialog.dismiss();
                }
        );
        if (sendError) {
            alertDialog.setNegativeButton(activity.getResources().getString(R.string.sendError),
                    (dialog, which) -> {
                        dialog.dismiss();

                    }
            );
        }
        if (!activity.isFinishing()) {

            //d.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
            AlertDialog d = alertDialog.show();
            if (!sendError) {
                Button b = d.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setVisibility(View.INVISIBLE);
            }
        }
    }

    default void showAlertDialog(@StringRes int msg, AdapterActionsListener action, BaseModel baseModel) {
        BaseActivity activity = getBaseActivity();
        AlertDialog.Builder alertDialog = activity.getAlertDialog();
        alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        activity.setAlertDialog(alertDialog);
        alertDialog.setMessage(activity.getResources().getString(msg));
        alertDialog.setPositiveButton(activity.getResources().getString(R.string.OK),
                (dialog, which) -> {
                    dialog.dismiss();
                    action.onAdapterClicked(null, baseModel);
                }
        );
        alertDialog.setNegativeButton(activity.getResources().getString(R.string.cancel),
                (dialog, which) -> {
                    dialog.dismiss();

                }
        );
        if (!activity.isFinishing()) {
            alertDialog.show();
        }

    }

    default void showAlertDialog(@StringRes int msg, ViewActionsListener action) {
        BaseActivity activity = getBaseActivity();
        AlertDialog.Builder alertDialog = activity.getAlertDialog();
        alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        activity.setAlertDialog(alertDialog);
        alertDialog.setMessage(activity.getResources().getString(msg));
        alertDialog.setPositiveButton(activity.getResources().getString(R.string.OK),
                (dialog, which) -> {
                    dialog.dismiss();
                    action.onClicked(null, null);
                }
        );
        alertDialog.setNegativeButton(activity.getResources().getString(R.string.cancel),
                (dialog, which) -> {
                    dialog.dismiss();

                }
        );
        if (!activity.isFinishing()) {
            alertDialog.show();
        }

    }

    default void showAlertDialog(@StringRes int msg, boolean isCancel) {
        BaseActivity activity = getBaseActivity();
        AlertDialog.Builder alertDialog = activity.getAlertDialog();
        alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        activity.setAlertDialog(alertDialog);
        alertDialog.setMessage(activity.getResources().getString(msg));
        alertDialog.setCancelable(isCancel);
        alertDialog.setPositiveButton(activity.getResources().getString(R.string.OK),
                (dialog, which) -> {
                    dialog.dismiss();

                }
        );
        alertDialog.setNegativeButton(activity.getResources().getString(R.string.cancel),
                (dialog, which) -> {
                    dialog.dismiss();

                }
        );
        if (!activity.isFinishing()) {
            alertDialog.show();
        }

    }

    default void showAlertDialog(@StringRes int msg, @StringRes int title, @StringRes int btnOk, @StringRes int btnCancel, ViewActionsListener action) {
        BaseActivity activity = getBaseActivity();
        AlertDialog.Builder alertDialog = activity.getAlertDialog();
        alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        activity.setAlertDialog(alertDialog);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(activity.getResources().getString(btnOk),
                (dialog, which) -> {
                    dialog.dismiss();
                    action.onClicked(null, null);
                }
        );
        alertDialog.setNegativeButton(activity.getResources().getString(btnCancel),
                (dialog, which) -> {
                    dialog.dismiss();
                }
        );
        if (!activity.isFinishing()) {
            alertDialog.show();
        }
    }


    default void invokeFunc(String methodName, Object... params) {
        try {
//            processing.setValue(R.string.wait);
            showProcessing(R.string.wait);
            Method method = null;
            Class[] arg = null;
            Method[] methods = getClass().getMethods();
            for (Method m : methods) {
                if (methodName.equals(m.getName())) {
                    method = m;
                    break;
                }
            }
            if (method == null) {
                throw new NoSuchMethodException(methodName);
            }

            method.invoke(this, params);


        } catch (Throwable e) {
//            appException.setValue(e);
            processError("error", null, null, e);
        } finally {
//            processing.setValue(null);
            closeProcess();
        }
    }

}
