package com.tsolution.base;

import android.app.Application;
import android.app.ProgressDialog;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsolution.base.listener.DefaultFunctionActivity;
import com.tsolution.base.listener.ViewActionsListener;

public abstract class BaseDialogFragment extends DialogFragment implements ViewActionsListener, DefaultFunctionActivity {
    protected RecyclerView recyclerView;
    protected ViewDataBinding binding;
    protected BaseViewModel viewModel;
    protected ProgressDialog pd;


    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }


    @Override
    @CallSuper
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        try {
            init(inflater, container, savedInstanceState, getLayoutRes(), getVMClass(), getRecycleResId());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return v;
    }


    public void init(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, @LayoutRes int layoutId,
                     Class<? extends BaseViewModel> clazz, @IdRes int recyclerViewId) throws Exception {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
//        viewModel = clazz.newInstance();//ViewModelProviders.of(getActivity()).get(clazz);
        viewModel = clazz.getDeclaredConstructor(Application.class).newInstance(getBaseActivity().getApplication());//ViewModelProviders.of(getActivity()).get(clazz);
        View view = binding.getRoot();

        recyclerView = view.findViewById(recyclerViewId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        binding.setVariable(BR.viewModel, viewModel);
        binding.setVariable(BR.listener, this);
        viewModel.setView(this::processFromVM);
        viewModel.setAlertModel(getBaseActivity().getViewModel().getAlertModel());
//        viewModel.refresh();
    }

    @Override
    public void action(View view, BaseViewModel baseViewModel) {
        showProcessing(getResources().getString(R.string.wait));
    }

    @Override
    final public void onClicked(View view, BaseViewModel viewModel) {
        ViewActionsListener.super.onClicked(view, viewModel);
    }
}
