/*
 *  Copyright 2017 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tsolution.base.listener;


import android.view.View;

import com.tsolution.base.BaseModel;

/**
 * Listener used with data binding to process user actions.
 */
public interface AdapterActionsListener extends DefaultFunctionActivity {

    void adapterAction(View view, BaseModel baseModel);
    default void onAdapterClicked(View view, BaseModel baseModel){
        try {
            adapterAction(view, baseModel);
        }catch (Throwable e){
            processError(null,view,null,e);
        }
    }
}
