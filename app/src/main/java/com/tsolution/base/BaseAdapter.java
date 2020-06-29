package com.tsolution.base;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tsolution.base.holder.GenericViewHolder;
import com.tsolution.base.listener.AdapterActionsListener;
import com.tsolution.base.listener.ILoadMore;
import com.tsolution.base.listener.OnBottomReachedListener;
import com.tsolution.base.listener.OwnerView;

import java.util.List;

public class BaseAdapter extends RecyclerView.Adapter<GenericViewHolder> implements AdapterActionsListener { //RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;

    final int visibleThreshold = 5;
    BaseViewModel viewModel;
    OwnerView listener;
    private int layoutId;
    private BaseActivity activity;
    private ObservableField<List> datas;
    private OnBottomReachedListener onBottomReachedListener;
    private Integer lastItemRefresh = 0;

//    boolean isLoading = false;


    public BaseAdapter(@LayoutRes int loId, BaseViewModel vm, BaseActivity a) {
        layoutId = loId;
        this.viewModel = vm;
        this.activity = a;

        datas = viewModel.baseModels;
    }


    public BaseAdapter(@LayoutRes int loId, ObservableField data, BaseActivity a) {
        layoutId = loId;
        datas = data;
        this.activity = a;
        viewModel = new BaseViewModel(a.getApplication());
        viewModel.baseModels = data;
    }


    public BaseAdapter(@LayoutRes int layoutId, BaseViewModel viewModel, OwnerView listener, BaseActivity a) {

        this.viewModel = viewModel;
        this.listener = listener;
        this.layoutId = layoutId;
        this.activity = a;
        datas = viewModel.baseModels;
    }

    public BaseAdapter(@LayoutRes int layoutId, ObservableField data, OwnerView listener, BaseActivity a) {

        this.viewModel = viewModel;
        this.listener = listener;
        this.layoutId = layoutId;
        this.activity = a;
        datas = data;
        viewModel = new BaseViewModel(a.getApplication());
        viewModel.baseModels = data;
    }


    public void replaceData(List<BaseModel> tasks) {
        final Integer pos = viewModel.getLastPos();

        List<BaseModel> lst = (List) viewModel.baseModels.get();
        if (lst != null && lst.size() > pos && lst.get(pos) == null) {
            lst.remove(pos.intValue());
        }
        switch (viewModel.getUpdateAdappType()) {
            case 0://notity all

                lastItemRefresh = viewModel.getTotalCount() - 1;
                notifyDataSetChanged();

                break;
            case 1://notify insert

                if (lastItemRefresh == 0) {
                    if (lst != null) {
                        lastItemRefresh = lst.size() - 1;
                    }
                    notifyDataSetChanged();
                    break;
                }


                Integer count = viewModel.getFetchSize();
                if (count + lastItemRefresh >= lst.size()) {
                    count = lst.size() - lastItemRefresh - 3;
                }
                notifyItemRangeInserted(lastItemRefresh + 2, count);
                notifyItemChanged(lastItemRefresh + 1);
                lastItemRefresh = lst.size() - 1;
//                viewModel.setLoadingInfo(false);
                break;
        }
        viewModel.setLoadingInfo(false);
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }


        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                this.layoutId, parent, false);
        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
//        ObservableField<List<BaseModel>> lst = viewModel.getModels();
        if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
            return;
        }
        if (datas.get() == null || datas.get().size() == 0 || position >= datas.get().size()) {
            return;
        }
        BaseModel bm = (BaseModel) datas.get().get(position);
        bm.index = position + 1;


        holder.setBinding(bm, viewModel, this);
    }

    public void registerScroll(RecyclerView recyclerView, ILoadMore loadMore) {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                try {
                    super.onScrolled(recyclerView, dx, dy);

                    Integer totalItemCount = linearLayoutManager.getItemCount();
                    Integer lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    Boolean isLoading = viewModel.getLoadingInfo();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        loadData(loadMore);

//                    isLoading = true;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void registerScroll(RecyclerView recyclerView, NestedScrollView scroll, ILoadMore loadMore) {
        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        NestedScrollView scroll = activity.findViewById(scrollId);
        scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        Integer visibleItemCount = mLayoutManager.getChildCount();
                        Integer totalItemCount = mLayoutManager.getItemCount();
                        Integer pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                        if (!viewModel.getLoadingInfo()) {

                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                loadData(loadMore);

                            }
                        }
                    }
                }
            }
        });
    }

    private void loadData(ILoadMore loadMore) {
        viewModel.setLoadingInfo(true);
        Integer fetchSize = viewModel.getFetchSize();
        List lstData = (List) viewModel.baseModels.get();
        if (lstData != null && lstData.size() > 0) {
            if (lastItemRefresh + fetchSize < lstData.size()) {
                viewModel.add1Fetch();
                replaceData(null);

                return;
            }
        }
        final int pos = datas.get().size();
        datas.get().add(null);
        viewModel.setLastPos(pos);

        if (loadMore != null) {
            loadMore.onLoadMore();


        }
    }


    @Override
    public int getItemCount() {
        return datas.get() == null ? 0 : datas.get().size();
    }

    @Override
    public void adapterAction(View view, BaseModel baseModel) {

        if (listener != null) {

            listener.onClicked(view, baseModel);
        }
    }

    @Override
    final public void onAdapterClicked(View view, BaseModel bm) {
        AdapterActionsListener.super.onAdapterClicked(view, bm);
    }


    @Override
    public int getItemViewType(int position) {
        if (datas.get() == null || datas.get().size() == 0 || position >= datas.get().size()) {
            return VIEW_TYPE_ITEM;
        }
        return datas.get().get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public BaseActivity getBaseActivity() {
        return activity;
    }

    @Override
    public int getLayoutRes() {
        return layoutId;
    }

    @Override
    public Class<? extends BaseViewModel> getVMClass() {
        return viewModel.getClass();
    }

    @Override
    public int getRecycleResId() {
        return 0;
    }


    class LoadingViewHolder extends GenericViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    public BaseViewModel getVM() {
        return viewModel;
    }
}
