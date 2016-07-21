package com.ep.joy.bmob.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ep.joy.bmob.R;
import com.ep.joy.bmob.activity.VideoActivity;
import com.ep.joy.bmob.adapter.HomeAdapter;
import com.ep.joy.bmob.base.BaseFragment;
import com.ep.joy.bmob.bean.Day;
import com.ep.joy.bmob.http.AppDao;
import com.jiongbull.jlog.JLog;
import com.youzan.titan.TitanAdapter;
import com.youzan.titan.TitanRecyclerView;
import com.youzan.titan.internal.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;


/**
 * 类描述:
 * 创建人:lin.ma@renren-inc.com
 * 创建时间:2016 16-4-8 18:34
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 */
public class FragmentTwo extends BaseFragment {


    private static final String ARGS_INSTANCE = FragmentTwo.class.getSimpleName();
    int mInt;
    private TitanRecyclerView mRecyclerView;
    private TitanAdapter<Day.IssueListEntity.ItemListEntity> mAdapter;
    private List<Day.IssueListEntity.ItemListEntity> bean;
    private String pageUrl;


    public static FragmentTwo newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        FragmentTwo fragment = new FragmentTwo();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_two_layout;
    }

    @Override
    protected void initView(View view) {
        Bundle args = getArguments();
        if (args != null) {
            mInt = args.getInt(ARGS_INSTANCE);
        }
        mRecyclerView = (TitanRecyclerView) view.findViewById(R.id.rcy);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initData() {
        bean = new ArrayList<>();
        mAdapter = new HomeAdapter(getActivity());
        mAdapter.setData(bean);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
                Day.IssueListEntity.ItemListEntity mdatas = mAdapter.getItem(position);
                if (!"video".equals(mdatas.getType())){
                    return;
                }
                Bundle bundle = new Bundle();
                //获取到时间
                int duration = mdatas.getData().getDuration();
                int mm = duration / 60;//分
                int ss = duration % 60;//秒
                String second = "";//秒
                String minute = "";//分
                if (ss < 10) {
                    second = "0" + String.valueOf(ss);
                } else {
                    second = String.valueOf(ss);
                }
                if (mm < 10) {
                    minute = "0" + String.valueOf(mm);
                } else {
                    minute = String.valueOf(mm);//分钟
                }
                bundle.putString("time", "#" + mdatas.getData().getCategory() + " / " + minute + "'" + second + '"');
                bundle.putSerializable("bean", mdatas);
                readyGo(VideoActivity.class,bundle);
            }
        });

        mRecyclerView.setOnLoadMoreListener(new TitanRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                AppDao.getNextImg(pageUrl, new Subscriber<Day>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Day day) {
                        mRecyclerView.setHasMore(!pageUrl.equals(day.getNextPageUrl()));
                        for (int i = 0; i < day.getIssueList().size(); i++) {
                            mAdapter.addDataEnd(day.getIssueList().get(i).getItemList());
                            pageUrl = day.getNextPageUrl();

                        }
                    }
                });
            }
        });


        AppDao.getImg(new Subscriber<Day>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                JLog.e(e.getMessage());
            }

            @Override
            public void onNext(Day day) {
                //  bean.addAll(day.getIssueList().get(0).getItemList());
                for (int i = 0; i < day.getIssueList().size(); i++) {
                    mAdapter.addDataEnd(day.getIssueList().get(i).getItemList());
                    pageUrl = day.getNextPageUrl();

                }

            }
        });
    }


}
