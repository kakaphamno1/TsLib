package com.tsolution.base;

import java.io.Serializable;

public class BaseModel implements Serializable {
    public Boolean checked;
    public Integer index;
    public String transactionErrCode;
    public void bindingAction(){

    }
    public String getIndex(){
        return String.valueOf(index);
    }
}
