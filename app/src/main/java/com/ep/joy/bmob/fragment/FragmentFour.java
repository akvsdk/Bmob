package com.ep.joy.bmob.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ep.joy.bmob.R;
import com.ep.joy.bmob.base.BaseFragment;



/**
 * 类描述:
 * 创建人:lin.ma@renren-inc.com
 * 创建时间:2016 16-4-8 18:34
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 */
public class FragmentFour extends BaseFragment {


    private static final String ARGS_INSTANCE = FragmentFour.class.getSimpleName();
    int mInt;
    TextView tv;

    public static FragmentFour newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        FragmentFour fragment = new FragmentFour();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_four_layout;
    }

    @Override
    protected void initView(View view) {
        Bundle args = getArguments();
        if (args != null) {
            mInt = args.getInt(ARGS_INSTANCE);
        }
        tv = (TextView) view.findViewById(R.id.tv_four);

    }

    @Override
    protected void initData() {
    }

}
