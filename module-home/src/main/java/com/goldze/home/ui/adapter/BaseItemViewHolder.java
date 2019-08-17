package com.goldze.home.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

abstract class BaseItemViewHolder<T extends AbsBaseEntity> extends RecyclerView.ViewHolder {
    Context mContext;

    BaseItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
    }

    public abstract void onBindViewHolder(T entity);


    private void setHeadViewOnClickListener(final T entity, View view) {
        if (view == null) {
            return;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHeadViewClick(entity);
            }
        });
    }

    /**
     * 头部被点击
     * @param entity T
     */
    void onHeadViewClick(T entity){
    }
}
