package com.ep.joy.bmob.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ep.joy.bmob.R;
import com.ep.joy.bmob.base.BaseFragment;

import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;


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

    public static final int REQUEST_CODE_CHOOSE = 1;
    private static final String ARGS_INSTANCE = FragmentTwo.class.getSimpleName();
    int mInt;
    TextView tv;

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
        tv = (TextView) view.findViewById(R.id.tv_two);
    }

    @Override
    protected void initData() {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picker.from(getActivity())
                        .count(3)
                        .enableCamera(true)
                        .setEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });
    }


}
