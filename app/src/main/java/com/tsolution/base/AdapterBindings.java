/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tsolution.base;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link } list.
 */
public class AdapterBindings {

    @BindingAdapter("app:recycleViewItems")
    public static void setItems(RecyclerView listView, List<BaseModel> items) {
        BaseAdapter adapter = (BaseAdapter) listView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(items);
        }
    }

//    @BindingAdapter({"bind:imageUrl"})
//    public static void loadImage(ImageView view, String imageUrl) {
//        Picasso.with(view.getContext())
//                .load(imageUrl)
//                .into(view);
//    }
//    @BindingAdapter({"bind:imageUrl", "bind:width", "bind:height"})
//    public static void loadLocalImage(ImageView view, String imageUrl, float width, float height) {
//        if (imageUrl != null && !"".equals(imageUrl)) {
//            File file = new File(imageUrl);
//            Picasso.with(view.getContext()).load(file).resize((int) width, (int) height).into(view);
//        }
//    }
//    @BindingAdapter({"bind:imageLocalUrl"})
//    public static void loadLocalImage(ImageView view, String imageLocalUrl) {
//        if (imageLocalUrl != null && !"".equals(imageLocalUrl)) {
//            File file = new File(imageLocalUrl);
//            Picasso.with(view.getContext()).load(file).into(view);
//        }
//    }
//    @BindingAdapter(value = {"selectedValue", "selectedValueAttrChanged"}, requireAll = false)
//    public static void bindSpinnerData(AppCompatSpinner pAppCompatSpinner, Object newSelectedValue, final InverseBindingListener newTextAttrChanged) {
//        pAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                newTextAttrChanged.onChange();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        if (newSelectedValue != null) {
//            if (pAppCompatSpinner.getAdapter() != null) {
//                int pos = ((ArrayAdapter<Object>) pAppCompatSpinner.getAdapter()).getPosition(newSelectedValue);
//                pAppCompatSpinner.setSelection(pos, true);
//            }
//
//        }
//    }
//
//    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
//    public static Object captureSelectedValue(AppCompatSpinner pAppCompatSpinner) {
//        return pAppCompatSpinner.getSelectedItem();
//    }

}
