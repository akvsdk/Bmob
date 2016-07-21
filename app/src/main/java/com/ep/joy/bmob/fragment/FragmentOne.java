package com.ep.joy.bmob.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ep.joy.bmob.R;
import com.ep.joy.bmob.activity.SavaActivity;
import com.ep.joy.bmob.base.BaseFragment;
import com.ep.joy.bmob.bean.ImageInfo;
import com.ep.joy.bmob.utils.GlideProxy;
import com.ep.joy.bmob.utils.JsoupTool;
import com.ep.joy.bmob.utils.ShareElement;
import com.jiongbull.jlog.JLog;
import com.youzan.titan.QuickAdapter;
import com.youzan.titan.TitanRecyclerView;
import com.youzan.titan.holder.AutoViewHolder;
import com.youzan.titan.internal.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class FragmentOne extends BaseFragment {

    private static final String ARGS_INSTANCE = FragmentOne.class.getSimpleName();
    int mInt;
    private int pages = 1;
    private TitanRecyclerView mRecyclerView;
    private QuickAdapter<ImageInfo> mQuickAdapter;
    private List<ImageInfo> mdatas = new ArrayList<>();


    public static FragmentOne newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        FragmentOne fragment = new FragmentOne();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_one_layout;
    }

    @Override
    protected void initView(View view) {
        Bundle args = getArguments();
        if (args != null) {
            mInt = args.getInt(ARGS_INSTANCE);
        }
        mRecyclerView = (TitanRecyclerView) view.findViewById(R.id.rcy);
    }

    @Override
    protected void initData() {
        mQuickAdapter = new QuickAdapter<ImageInfo>(R.layout.item_zb, mdatas) {
            @Override
            public void bindView(AutoViewHolder holder, int position, ImageInfo model) {
                ShareElement.shareDrawable = holder.getImageView(R.id.iv_index_photo).getDrawable();
                holder.getTextView(R.id.tv_time).setText(model.getContent());
                GlideProxy.getInstance().loadImage(getActivity(), model.getImgurl(), holder.getImageView(R.id.iv_index_photo));
            }
        };
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(mQuickAdapter);
        loaddate();
        mRecyclerView.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
                ImageInfo img = mQuickAdapter.getItem(position);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, "share");
                Intent intent = new Intent(getActivity(), SavaActivity.class);
                intent.putExtra(SavaActivity.BEAN, img);
                ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
            }
        });

        mRecyclerView.setOnLoadMoreListener(new TitanRecyclerView.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                pages++;
                if (pages < 44) {
                    mRecyclerView.setHasMore(true);
                    loaddate();
                } else {
                    mRecyclerView.setHasMore(false);
                }
            }
        });

    }

    private void loaddate() {

        Observable observable = Observable.create(new Observable.OnSubscribe<List<ImageInfo>>() {
            @Override
            public void call(Subscriber<? super List<ImageInfo>> subscriber) {
                subscriber.onNext(JsoupTool.getInstance().getContent("http://zhuangbi.info/?page=" + pages));
            }
        });
        final Subscriber subscribe = new Subscriber<List<ImageInfo>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                JLog.e(e.getMessage());
            }

            @Override
            public void onNext(List<ImageInfo> s) {
                mQuickAdapter.addDataEnd(s);
            }
        };

        addSubscription(observable, subscribe);


    }

}
