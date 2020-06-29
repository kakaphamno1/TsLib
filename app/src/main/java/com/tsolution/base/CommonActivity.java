package com.tsolution.base;


import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tsolution.base.utils.AlertsUtils;

public class CommonActivity extends BaseActivity {


    private BaseFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        try {
            super.onCreate(savedInstanceState);
            Class<?> clazz = (Class) getIntent().getSerializableExtra("FRAGMENT");
            Fragment fragment;
            if (clazz != null) {
                fragment = (Fragment) clazz.newInstance();
            } else {
                String className = getIntent().getStringExtra("FRAGMENT_NAME");
                ;
                fragment = (Fragment) (Class.forName(className).newInstance());

            }

            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// Replace the contents of the container with the new fragment
//            Fragment ordFrag = new OrderFragment();
//            ordFrag.onAttach(this);
            ft.replace(R.id.commonFrag, fragment);
//            ft.addToBackStack(null);

// or ft.add(R.id.your_placeholder, new FooFragment());
// Complete the changes added above
            ft.commit();

            //((OrderFragment) ordFrag).setViewModel(viewModel);
//            adapter = new BaseAdapter(R.layout.item_ordered, viewModel, this);


//            recyclerView.setAdapter(adapter);


//            setContentView(R.layout.activity_list_order);
//            ButterKnife.bind(this);
//            setSupportActionBar(toolbar);
//            if (getSupportActionBar() != null) {
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                getSupportActionBar().setDisplayShowHomeEnabled(true);
//                getSupportActionBar().setElevation(4);
//            }
//            listOrderPresenter = new OrderPresenter(this);
//            OrderDTO orderDTO = OrderDTO.builder().orderType(1).merchantId(1l).langId(1).build();
//            listOrderPresenter.get(this, "getOrders", "getOrdersSuccess", orderDTO);

        } catch (Throwable ex) {
            processError("error", null, viewModel, ex);
        }
    }


    @Override
    public int getLayoutRes() {
        return R.layout.common_activity;
    }

    @Override
    public Class<? extends BaseViewModel> getVMClass() {
        return BaseViewModel.class;
    }

    @Override
    public int getRecycleResId() {
        return 0;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onItemClick(View v, Object o) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        AlertsUtils.register(this);
    }

    @Override
    protected void onPause() {
        AlertsUtils.unregister(this);
        super.onPause();
    }
}
