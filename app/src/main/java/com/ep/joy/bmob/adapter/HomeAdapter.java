package com.ep.joy.bmob.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ep.joy.bmob.R;
import com.ep.joy.bmob.bean.Day;
import com.ep.joy.bmob.utils.GlideProxy;
import com.youzan.titan.TitanAdapter;

/**
 * author   Joy
 * Date:  2016/7/19.
 * version:  V1.0
 * Description:
 */
public class HomeAdapter extends TitanAdapter<Day.IssueListEntity.ItemListEntity> {

    public enum ITEM_TYPE {
        ITEM_TYPE_IMAGE,
        ITEM_TYPE_DAY
    }

    private Context mContext;
    private String time;


    public HomeAdapter(Context context) {
        mContext = context;

    }

    @Override
    protected RecyclerView.ViewHolder createVHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal()) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_vedio_item, parent, false));
        } else {
            return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_text_item, parent, false));
        }
    }

    @Override
    protected void showItemView(RecyclerView.ViewHolder holder, int position) {
        String feed = "1";
        String title = "1";
        String category = "1";
        int duration = 0;
        String text = "1";
        Day.IssueListEntity.ItemListEntity itemListEntity = mData.get(position);

        if (holder instanceof ViewHolder) {
            feed = itemListEntity.getData().getCover().getFeed();
            title = itemListEntity.getData().getTitle();
            category = itemListEntity.getData().getCategory();
            category = "#" + category + "  /  ";

            duration = itemListEntity.getData().getDuration();

            int last = duration % 60;
            String stringLast;
            if (last <= 9) {
                stringLast = "0" + last;
            } else {
                stringLast = last + "";
            }

            String durationString;
            int minit = duration / 60;
            if (minit < 10) {
                durationString = "0" + minit;

            } else {
                durationString = "" + minit;

            }
            String stringTime = durationString + "' " + stringLast + '"';

            time = category + stringTime;
            Uri uri = Uri.parse(feed);

            GlideProxy.getInstance().loadUriImage(mContext, uri, ((ViewHolder) holder).imageView);

            ((ViewHolder) holder).tvTitle.setText(title);
            ((ViewHolder) holder).tvTime.setText(time);

        } else if (holder instanceof ViewHolder2) {
            text = itemListEntity.getData().getText();
            String image = mData.get(position).getData().getImage();
            ((ViewHolder2) holder).tvTime.setText(text);

            if (!TextUtils.isEmpty(image)) {
                ((ViewHolder2) holder).tvTime.setTextSize(20);
                ((ViewHolder2) holder).tvTime.setText("-Weekend  special-");
            }
        }
    }

    @Override
    public int getAttackItemViewType(int position) {
        Day.IssueListEntity.ItemListEntity itemListEntity = mData.get(position);
        return "video".equals(itemListEntity.getType()) ? ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal() : ITEM_TYPE.ITEM_TYPE_DAY.ordinal();
    }

    @Override
    public long getAdapterItemId(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle;
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView tvTime;

        public ViewHolder2(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_home_text);
        }
    }
}
