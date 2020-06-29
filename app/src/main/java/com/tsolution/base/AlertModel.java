package com.tsolution.base;


import androidx.annotation.StringRes;

import com.tsolution.base.listener.ViewActionsListener;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AlertModel extends BaseModel{
    public @StringRes
    int msg;
    public ViewActionsListener funcPositive;
    public ViewActionsListener funcNegative;

    public AlertModel(int msg, ViewActionsListener funcPositive, ViewActionsListener funcNegative) {
        this.msg = msg;
        this.funcPositive = funcPositive;
        this.funcNegative = funcNegative;
    }
}
