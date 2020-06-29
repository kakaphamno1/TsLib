package com.tsolution.base;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tsolution.base.listener.AdapterListener;
import com.tsolution.base.listener.DefaultFunctionActivity;
import com.tsolution.base.listener.ViewActionsListener;
import com.tsolution.base.utils.locale.BuildUtils;
import com.tsolution.base.utils.locale.ContextWrapperV2;
import com.tsolution.base.utils.locale.LocaleUtils;

import java.lang.reflect.InvocationTargetException;

public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity implements ViewActionsListener, DefaultFunctionActivity, AdapterListener {
    protected ProgressDialog.Builder pdb;
    protected static ProgressDialog pd;
    private static AlertDialog.Builder alertDialog;
    protected BaseViewModel viewModel;
    private static AlertDialog.Builder alertDialogV2;
    protected V binding;

    public BaseActivity() {

    }

    @Override
    public void onItemClick(View v, Object o) {

    }

    public ProgressDialog.Builder getPdb() {
        return pdb;
    }

    public void setPdb(ProgressDialog.Builder pdb) {
        this.pdb = pdb;
    }

    public ProgressDialog getPd() {
        return pd;
    }

    public void setPd(ProgressDialog pd) {
        this.pd = pd;
    }

    public void setAlertDialog(AlertDialog.Builder alertDialog) {
        this.alertDialog = alertDialog;
    }

    public AlertDialog.Builder getAlertDialog() {
        return alertDialog;
    }


    @Override
    public BaseActivity getBaseActivity() {
        return this;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (BuildUtils.isNougat) {
            super.attachBaseContext(ContextWrapperV2.wrap(newBase, LocaleUtils.getsLocale()));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (!BuildUtils.isNougat) {
            LocaleUtils.updateConfig(this);
        }
        try {
            viewModel = getVMClass().getDeclaredConstructor(Application.class).newInstance(getBaseActivity().getApplication());//ViewModelProviders.of(getActivity()).get(clazz);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        binding = DataBindingUtil.setContentView(this, getLayoutRes());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewModel.setView(this::processFromVM);
        binding.setVariable(BR.viewModel, viewModel);
        binding.setVariable(BR.listener, this);
        viewModel.initObs();

        viewModel.getAlertModel().observe(this, alert -> {
            AlertModel a = (AlertModel) alert;
            showAlertDialog(a.msg, a.funcPositive);
        });
    }


    @Override
    public void action(View view, BaseViewModel baseViewModel) {
        showProcessing(getResources().getString(R.string.wait));
    }


    public BaseViewModel getViewModel() {
        return viewModel;
    }


    public Object getListener() {
        return this;
    }


    @Override
    final public void onClicked(View view, BaseViewModel viewModel) {
        ViewActionsListener.super.onClicked(view, viewModel);
    }

    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    public void replaceFragment(@IdRes int containerViewId,
                                @NonNull Fragment fragment,
                                @NonNull String fragmentTag,
                                @Nullable String backStackStateName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeProcess();
    }

    @Override
    public void processFromVM(String action, View view, BaseViewModel viewModel, Throwable t) {
        if ("hideKeyBoard".equals(action)) {
            hideKeyBoard();
        }
    }

    public void hideKeyBoard() {
        View view1 = getBaseActivity().getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager) getBaseActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }

    public void showCustomDialog(Context context, String title, String message) {
        alertDialogV2 = new AlertDialog.Builder(context);
        alertDialogV2.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, which) -> {
                });
        if (!((Activity) context).isFinishing()) {
            getBaseActivity().runOnUiThread(() -> alertDialogV2.show());
        }

    }

    @Override
    public void onItemLongClick(View v, Object o) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
    }
}
