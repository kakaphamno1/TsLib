package com.tsolution.base;


import androidx.annotation.StringRes;

import java.util.HashMap;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ErrMsg extends BaseModel{
    HashMap<String, String> errorMsg;

    public ErrMsg() {
        errorMsg = new HashMap<>();
    }
    public void addError(String code, String msg){
        if(errorMsg == null){
            errorMsg = new HashMap<>();
        }
        errorMsg.put(code, msg);
    }
    public String getError(String code){
        return errorMsg == null? "":errorMsg.get(code);
    }
}
