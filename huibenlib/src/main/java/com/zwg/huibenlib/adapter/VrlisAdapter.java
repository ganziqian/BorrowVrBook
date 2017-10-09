package com.zwg.huibenlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwg.huibenlib.ComBaseActivity;
import com.zwg.huibenlib.R;
import com.zwg.huibenlib.bean.PathBean;
import com.zwg.huibenlib.bean.VrlistBean;

import java.util.List;

/**
 * Created by wsh-03 on 2016/8/12.
 */
public class VrlisAdapter extends BaseAdapter {

    private Context context;
    private List<VrlistBean> list;
    private PathBean pathBean;
    private String selStr="";

    public String getSelStr() {
        return selStr;
    }

    private ItemClickLinsten itemClickLinsten;

    public ItemClickLinsten getItemClickLinsten() {
        return itemClickLinsten;
    }

    public void setItemClickLinsten(ItemClickLinsten itemClickLinsten) {
        this.itemClickLinsten = itemClickLinsten;
    }

    public void setSelStr(String selStr) {
        this.selStr = selStr;
    }

    public VrlisAdapter(Context context, List<VrlistBean> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.vrlist_item_layout,null);
            vh=new ViewHolder(convertView);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }

        if(position==0){
            vh.titleTv.setText("返回扫封面");
            vh.titleTv.setTextColor(context.getResources().getColor(R.color.mediumblue));

        }else {
            vh.titleTv.setText(list.get(position).getAlbumname());

            if(selStr!=null){
                if(selStr.equals(list.get(position).getAlbumname())){
                    vh.titleTv.setTextColor(context.getResources().getColor(R.color.dodgerblue));
                }else {
                    vh.titleTv.setTextColor(context.getResources().getColor(R.color.darkturquoise));
                }
            }

        }




        vh.titleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickLinsten!=null){
                    itemClickLinsten.ItemClick(position);
                }
            }
        });
        return convertView;
    }


    public interface ItemClickLinsten{
        public abstract void ItemClick(int posi);
    }

    public class ViewHolder {
        public TextView titleTv;

        public ViewHolder(View convertView){
            // tv= (TextView) convertView.findViewById(R.id.list_tv1);
            titleTv= (TextView) convertView.findViewById(R.id.title_vr_tv);
        }
    }







}
