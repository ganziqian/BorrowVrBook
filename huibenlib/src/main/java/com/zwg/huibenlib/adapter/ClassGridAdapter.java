package com.zwg.huibenlib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.zwg.huibenlib.ComBaseActivity;
import com.zwg.huibenlib.MyApplication;
import com.zwg.huibenlib.R;
import com.zwg.huibenlib.Utils.DownFileUtils;
import com.zwg.huibenlib.Utils.LogUtils;
import com.zwg.huibenlib.bean.LisBean;
import com.zwg.huibenlib.bean.PathBean;
import com.zwg.huibenlib.http.AppContents;
import com.zwg.huibenlib.view.RoundProgressBar;

import org.xutils.common.Callback;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/9/3.
 */
public class ClassGridAdapter  extends RecyclerView.Adapter<ClassGridAdapter.ViewHolder> {


    private ClassItemLinstener mItemClickListener;
    private Context context;
    private PathBean pathBean;

    private List<LisBean> list;
    public ClassGridAdapter(List<LisBean> datas, Context context, PathBean pathBean) {
        this.list = datas;
        this.context=context;
        this.pathBean=pathBean;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mylist_item_layout,viewGroup,false);

        return new ViewHolder(view,mItemClickListener);
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if(!list.get(position).getMedia_name().equals("")) {
            viewHolder.iv.setVisibility(View.VISIBLE);
            viewHolder.shuqianiv.setVisibility(View.VISIBLE);
            viewHolder.zhedangiv.setVisibility(View.VISIBLE);
            viewHolder.touyiniv.setVisibility(View.VISIBLE);
            // is = context.getAssets().open(list.get(position).getCover());

            if(AppContents.isExit(((ComBaseActivity)context).getPathBean().getImagepath()+list.get(position).getSaveImageName())){
                File file = new File(pathBean.getImagepath(), list.get(position).getSaveImageName());
                Glide.with(context).load(file).placeholder(R.drawable.default_icom).into( viewHolder.iv);
               // Glide.with(context).load(file).placeholder(R.drawable.default_icom).override(120, 140).into( viewHolder.iv);
            }else {
                Glide.with(context).load(list.get(position).getMedia_icon()).placeholder(R.drawable.default_icom).crossFade(10).into(viewHolder.iv);
            }

            if(list.get(position).isdown()){
                // vh.isIv.setVisibility(View.GONE);
                viewHolder.bacLay.setVisibility(View.VISIBLE);
                viewHolder.bacLay.getBackground().setAlpha(150);
            }else {
                //   vh.isIv.setVisibility(View.VISIBLE);
                viewHolder.bacLay.setVisibility(View.GONE);
            }
        }else {
            viewHolder.iv.setVisibility(View.GONE);
            viewHolder.shuqianiv.setVisibility(View.GONE);
            viewHolder.zhedangiv.setVisibility(View.GONE);
            viewHolder.isIv.setVisibility(View.GONE);
            viewHolder.bacLay.setVisibility(View.GONE);
            viewHolder.touyiniv.setVisibility(View.GONE);
        }
        if(list.get(position).isexit()){
            viewHolder.isIv.setImageResource(R.drawable.paly);
        }else {
            viewHolder.isIv.setImageResource(R.drawable.down);
        }
        if(list.get(position).isdown()){
            viewHolder.isIv.setImageResource(R.drawable.zanting_icom);
        }


        if(!list.get(position).isexit()){
            if(list.get(position).isdown()){


                viewHolder.isIv.setImageResource(R.drawable.zanting_icom);
                //  finalVh.isIv.setVisibility(View.GONE);
                viewHolder.bacLay.setVisibility(View.VISIBLE);
                viewHolder.bacLay.getBackground().setAlpha(150);
                DownFileUtils downFileUtils=null;
                if(list.get(position).getDownFileUtils()==null){
                     downFileUtils = new DownFileUtils();
                }else {
                    downFileUtils= list.get(position).getDownFileUtils();
                }

                list.get(position).setDownFileUtils(downFileUtils);
                list.get(position).setIsdown(true);
                if(downFileUtils!=null) {
                    downFileUtils.down(list.get(position).getMedia_url(), pathBean.getVideoPath() + list.get(position).getSaveVideoName(), new Callback.ProgressCallback<File>() {
                        @Override
                        public void onWaiting() {
                        }

                        @Override
                        public void onStarted() {
                        }

                        @Override
                        public void onLoading(long l, long l1, boolean b) {
                            if (l > l1) {
                                viewHolder.mRoundProgressBar.setProgress((int) (l1 * 100 / l));
                                LogUtils.log("================", l1 + "/" + l);
                            } else {
                                list.get(position).setIsexit(true);
                                viewHolder.bacLay.setVisibility(View.GONE);
                                viewHolder.isIv.setImageResource(R.drawable.paly);

                                list.get(position).setIsdown(false);
                                notifyDataSetChanged();
                                LogUtils.log("---------->", "下载完成");
                                mItemClickListener.onLoadFinsh(list,list.get(position).getTag_name());
                                MyApplication.getInstance().showToast(list.get(position).getMedia_name() + "下载完成");
                            }

                        }

                        @Override
                        public void onSuccess(File file) {
                        }

                        @Override
                        public void onError(Throwable throwable, boolean b) {
                            viewHolder.isIv.setImageResource(R.drawable.down);
                            viewHolder.bacLay.setVisibility(View.GONE);
                            list.get(position).setIsdown(false);
                            mItemClickListener.onLoadFinsh(list,list.get(position).getTag_name());

                            notifyDataSetChanged();
                            MyApplication.getInstance().showToast("下载失败");

                        }

                        @Override
                        public void onCancelled(CancelledException e) {
                        }

                        @Override
                        public void onFinished() {
                        }
                    });
                }

            }else {


                if(list.get(position).getDownFileUtils()!=null) {
                    list.get(position).getDownFileUtils().stopdown();
                }
                viewHolder.bacLay.setVisibility(View.GONE);
                viewHolder.isIv.setImageResource(R.drawable.down);
                viewHolder.isIv.setVisibility(View.VISIBLE);

            }
        }

    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return list.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView isIv;
        private ImageView iv;
        private ImageView shuqianiv;
        private ImageView zhedangiv;
        private ImageView touyiniv;
        public RoundProgressBar mRoundProgressBar;
        public RelativeLayout bacLay;
        private ClassItemLinstener mListener;
        public ViewHolder(View view,ClassItemLinstener linstener){
            super(view);
            mListener=linstener;
            view.setOnClickListener(this);
            iv= (ImageView) view.findViewById(R.id.list_iv1);
            shuqianiv= (ImageView) view.findViewById(R.id.shuqian_iv);
            zhedangiv= (ImageView) view.findViewById(R.id.zhedang_iv);
            isIv= (ImageView) view.findViewById(R.id.list_is_iv);
            touyiniv= (ImageView) view.findViewById(R.id.touyin_iv);
            mRoundProgressBar= (RoundProgressBar) view.findViewById(R.id.roundProgressBar);
            bacLay= (RelativeLayout) view.findViewById(R.id.back_lay);

        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.onItemClick(v,getAdapterPosition());
            }
        }
    }



    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(ClassItemLinstener listener){
        this.mItemClickListener = listener;
    }
}
