package com.zwg.huibenlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.zwg.huibenlib.ComBaseActivity;
import com.zwg.huibenlib.R;
import com.zwg.huibenlib.bean.LisBean;
import com.zwg.huibenlib.bean.PathBean;
import com.zwg.huibenlib.http.AppContents;
import com.zwg.huibenlib.view.RoundProgressBar;


import java.io.File;
import java.util.List;

/**
 * Created by wsh-03 on 2016/8/12.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<LisBean> list;
    private PathBean pathBean;

    public MyAdapter(Context context, List<LisBean> list) {
        this.context = context;
        this.list = list;
        pathBean=((ComBaseActivity)context).getPathBean();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {

        ViewHolder vh=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.mylist_item_layout,null);
            vh=new ViewHolder(convertView);
            convertView.setTag(vh);
            list.get(position).setViewHolder(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
            list.get(position).setViewHolder(vh);
        }


        if(!list.get(position).getMedia_name().equals("")) {
            vh.iv.setVisibility(View.VISIBLE);
            vh.shuqianiv.setVisibility(View.VISIBLE);
            vh.zhedangiv.setVisibility(View.VISIBLE);
            vh.touyiniv.setVisibility(View.VISIBLE);
               // is = context.getAssets().open(list.get(position).getCover());

            if(AppContents.isExit(((ComBaseActivity)context).getPathBean().getImagepath()+list.get(position).getSaveImageName())){
                File file = new File(pathBean.getImagepath(), list.get(position).getSaveImageName());
                Glide.with(context).load(file).placeholder(R.drawable.default_icom).into( vh.iv);
            }else {
                Glide.with(context).load(list.get(position).getMedia_icon()).placeholder(R.drawable.default_icom).crossFade(10).into(vh.iv);
            }


          /*  if(list.get(position).getLoadivtype().equals("1")) {
                Glide.with(context).load(list.get(position).getMedia_icon()).placeholder(R.drawable.default_icom).crossFade(10).into(vh.iv);
            }else if(list.get(position).getLoadivtype().equals("2")){
                File file = new File(pathBean.getImagepath(), list.get(position).getImageName());
                Glide.with(context).load(file).placeholder(R.drawable.default_icom).into( vh.iv);
            }else {
                Glide.with(context).load(list.get(position).getMedia_icon()).placeholder(R.drawable.default_icom).crossFade(10).into(vh.iv);
            }*/

            if(list.get(position).isdown()){
                // vh.isIv.setVisibility(View.GONE);
                vh.bacLay.setVisibility(View.VISIBLE);
                vh.bacLay.getBackground().setAlpha(150);
            }else {
                //   vh.isIv.setVisibility(View.VISIBLE);
                vh.bacLay.setVisibility(View.GONE);
            }
        }else {
            vh.iv.setVisibility(View.GONE);
            vh.shuqianiv.setVisibility(View.GONE);
            vh.zhedangiv.setVisibility(View.GONE);
            vh.isIv.setVisibility(View.GONE);
            vh.bacLay.setVisibility(View.GONE);
            vh.touyiniv.setVisibility(View.GONE);
        }
        if(list.get(position).isexit()){
            vh.isIv.setImageResource(R.drawable.paly);
        }else {
            vh.isIv.setImageResource(R.drawable.down);
        }
        if(list.get(position).isdown()){
            vh.isIv.setImageResource(R.drawable.zanting_icom);
        }



        return convertView;
    }


    public interface ItemClickLinsten{
       // public abstract void ItemClick(String );
    }

    public class ViewHolder {
        public ImageView isIv;
        private ImageView iv;
        private ImageView shuqianiv;
        private ImageView zhedangiv;
        private ImageView touyiniv;
        public RoundProgressBar mRoundProgressBar;
        public RelativeLayout bacLay;
        public ViewHolder(View convertView){
            // tv= (TextView) convertView.findViewById(R.id.list_tv1);
            iv= (ImageView) convertView.findViewById(R.id.list_iv1);
            shuqianiv= (ImageView) convertView.findViewById(R.id.shuqian_iv);
            zhedangiv= (ImageView) convertView.findViewById(R.id.zhedang_iv);
            isIv= (ImageView) convertView.findViewById(R.id.list_is_iv);
            touyiniv= (ImageView) convertView.findViewById(R.id.touyin_iv);
            mRoundProgressBar= (RoundProgressBar) convertView.findViewById(R.id.roundProgressBar);
            bacLay= (RelativeLayout) convertView.findViewById(R.id.back_lay);
        }
    }







}
