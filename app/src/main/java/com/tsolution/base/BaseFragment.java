package com.tsolution.base;


import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tsolution.base.listener.AdapterActionsListener;
import com.tsolution.base.listener.AdapterListener;
import com.tsolution.base.listener.DefaultFunctionActivity;
import com.tsolution.base.listener.ViewActionsListener;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseFragment extends Fragment implements DefaultFunctionActivity, AdapterActionsListener, ViewActionsListener, AdapterListener {
    protected RecyclerView recyclerView;
    protected ViewDataBinding binding;
    protected BaseViewModel viewModel;

    public void unregisterEvent() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Nullable
    @CallSuper
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            super.onCreateView(inflater, container, savedInstanceState);
            binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
            if (getVMClass() != null) {
                init(savedInstanceState, getLayoutRes(), getVMClass(), getRecycleResId());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    public void init(@Nullable Bundle savedInstanceState, @LayoutRes int layoutId,
                     Class<? extends BaseViewModel> clazz, @IdRes int recyclerViewId) throws Throwable {
        viewModel = clazz.getDeclaredConstructor(Application.class).newInstance(getBaseActivity().getApplication());//ViewModelProviders.of(getActivity()).get(clazz);
        View view = binding.getRoot();
        binding.setVariable(BR.viewModel, viewModel);
        binding.setVariable(BR.listener, this);
        viewModel.setView(this::processFromVM);
        viewModel.setAlertModel(getBaseActivity().getViewModel().getAlertModel());
        if (recyclerViewId != 0) {
            recyclerView = view.findViewById(recyclerViewId);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    public void init(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, @LayoutRes int layoutId,
                     Class<? extends BaseViewModel> clazz, @IdRes int recyclerViewId) throws Exception {
        viewModel = clazz.getDeclaredConstructor(Application.class).newInstance(getBaseActivity().getApplication());//ViewModelProviders.of(getActivity()).get(clazz);
        View view = binding.getRoot();
        binding.setVariable(BR.viewModel, viewModel);
        binding.setVariable(BR.listener, this);
        viewModel.setView(this::processFromVM);
        viewModel.setAlertModel(getBaseActivity().getViewModel().getAlertModel());
        if (recyclerViewId != 0) {
            recyclerView = view.findViewById(recyclerViewId);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void adapterAction(View view, BaseModel baseModel) {

    }

    @Override
    final public void onAdapterClicked(View view, BaseModel bm) {
        AdapterActionsListener.super.onAdapterClicked(view, bm);
    }

    @Override
    public void action(View view, BaseViewModel baseViewModel) {

    }

    @Override
    public void processFromVM(String action, View view, BaseViewModel viewModel, Throwable t) {
        if ("hideKeyBoard".equals(action)) {
            hideKeyBoard();
        }
    }

    private void hideKeyBoard() {
        View view1 = getBaseActivity().getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager) getBaseActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }

    //
    @Override
    final public void onClicked(View view, BaseViewModel viewModel) {
        ViewActionsListener.super.onClicked(view, viewModel);
    }

    public BaseViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(BaseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onItemClick(View v, Object o) {

    }

    @Override
    public void onItemLongClick(View v, Object o) {

    }
}
