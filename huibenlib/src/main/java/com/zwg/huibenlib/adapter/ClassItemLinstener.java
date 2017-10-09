package com.zwg.huibenlib.adapter;

import android.view.View;

import com.zwg.huibenlib.bean.LisBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface ClassItemLinstener {
    public void onItemClick(View view, int postion);
    public void onLoadFinsh(List<LisBean> list,String tag);
}
