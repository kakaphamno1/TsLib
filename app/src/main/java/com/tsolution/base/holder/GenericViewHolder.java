package com.tsolution.base.holder;


import androidx.databinding.ObservableField;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tsolution.base.BR;
import com.tsolution.base.BaseModel;
import com.tsolution.base.BaseViewModel;
import com.tsolution.base.listener.AdapterActionsListener;


public class GenericViewHolder extends RecyclerView.ViewHolder {
    final ViewDataBinding  binding;

    public GenericViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = null;
    }

    public GenericViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }
    public void setBinding(BaseModel obj, BaseViewModel viewModel, AdapterActionsListener listener) {
        binding.setVariable(BR.viewHolder, obj);
        binding.setVariable(BR.viewModel, viewModel);
        binding.setVariable(BR.listener, listener);

//        obj.bindingAction();
        binding.executePendingBindings();
    }


    public void setBinding(BaseModel obj, AdapterActionsListener listener) {
        if(binding == null){
            return;
        }
        binding.setVariable(BR.viewHolder, obj);
//        binding.setVariable(BR.viewModel, viewModel);
        binding.setVariable(BR.listener, listener);

//        obj.bindingAction();
        binding.executePendingBindings();
    }


}
