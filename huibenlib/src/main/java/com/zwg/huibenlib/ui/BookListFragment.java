package com.zwg.huibenlib.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwg.huibenlib.ComBaseActivity;
import com.zwg.huibenlib.MyApplication;
import com.zwg.huibenlib.R;
import com.zwg.huibenlib.Utils.DownFileUtils;
import com.zwg.huibenlib.Utils.LogUtils;
import com.zwg.huibenlib.Utils.MyProgressCallback;
import com.zwg.huibenlib.Utils.SaveUtils;
import com.zwg.huibenlib.Utils.dialog.ChongTryDialog;
import com.zwg.huibenlib.Utils.dialog.ShowMesgDialog;
import com.zwg.huibenlib.adapter.ClassGridAdapter;
import com.zwg.huibenlib.adapter.ClassItemLinstener;
import com.zwg.huibenlib.adapter.MyAdapter;
import com.zwg.huibenlib.bean.ComBean;
import com.zwg.huibenlib.bean.LisBean;
import com.zwg.huibenlib.bean.PathBean;
import com.zwg.huibenlib.db.DataSaveUtils;
import com.zwg.huibenlib.http.AppContents;
import com.zwg.huibenlib.http.MyCallBack;
import com.zwg.huibenlib.http.XutilsHelper;
import com.zwg.huibenlib.inteface.DelFileLinstener;
import com.zwg.huibenlib.inteface.TrySelectLinstener;

import org.xutils.common.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment implements XRecyclerView.LoadingListener,ClassItemLinstener {


    private XRecyclerView mRecyclerView;

    private List<LisBean> lisBeens=new ArrayList<>();


    private PathBean pathBean;
    private DataSaveUtils dataSaveUtils;
    private ComBaseActivity comBaseActivity;

    private ClassGridAdapter mAdapter;

    private int jtype=0;

    private int npost=0;

    private String[] nans2 ;
    private List<String> downStrs=new ArrayList<>();
    private int page=1;

    public int backType=2;//2从索引返回扫封面，，3从索引返回图片视频播放视图

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_book_list, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        nans2= SaveUtils.getvideotag();
        comBaseActivity= (ComBaseActivity) getActivity();

        pathBean=comBaseActivity.getPathBean();

        dataSaveUtils=comBaseActivity.getDataSaveUtils();

        mRecyclerView = (XRecyclerView)v.findViewById(R.id.class_recycleview);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),3);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        // mRecyclerView.setArrowImageView(R.mipmap.pull_icon);
        mRecyclerView.setTextColor(Color.WHITE);

        mRecyclerView.setLoadingListener(this);

        mAdapter = new ClassGridAdapter(lisBeens,getActivity(),pathBean);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);





        ImageView fhIv= (ImageView) v.findViewById(R.id.back_list_iv);
        fhIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(backType==2) {
                    ((ComBaseActivity) getActivity()).fhSuoyin();
                }else if(backType==3){
                    ((ComBaseActivity) getActivity()).fhSuoyin2();
                }else if(backType==1){
                    ((ComBaseActivity) getActivity()).ShowVideoFrag2();
                }
            }
        });
        getLisData();
    }

    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final MyAdapter.ViewHolder vh= (MyAdapter.ViewHolder) view.getTag();
    }










    private void getLisData(){
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("appkey", comBaseActivity.getMyappkey());
        XutilsHelper.post(AppContents.LIB_ALLLSIT_URL, map, new MyCallBack() {
            @Override
            public void onSuccess(String s) {
                ComBean<LisBean> comBean=null;
                mRecyclerView.loadMoreComplete();
                LogUtils.log("aaaid-->",s);
                try {
                    comBean= JSON.parseObject(s,new TypeReference<ComBean<LisBean>>(){});
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(comBean!=null){
                    if(comBean.getCode().equals("1")){
                        List<LisBean> lisBeens2=comBean.getData();
                        if(lisBeens2.size()>0){

                            String videoName;
                            String imageName;
                            for (int i=0;i<lisBeens2.size();i++) {
                                try {
                                    int lastIndex = lisBeens2.get(i).getMedia_url().lastIndexOf("/");
                                    videoName = lisBeens2.get(i).getMedia_url().substring(lastIndex + 1, lisBeens2.get(i).getMedia_url().length()).replace(".mp4", "");

                                    int lastIndex2 = lisBeens2.get(i).getMedia_icon().lastIndexOf("/");
                                    imageName = lisBeens2.get(i).getMedia_icon().substring(lastIndex2 + 1, lisBeens2.get(i).getMedia_icon().length());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    imageName = lisBeens2.get(i).getMedia_icon();
                                    videoName = lisBeens2.get(i).getMedia_url().replace(".mp4", "");
                                }


                                lisBeens2.get(i).setSaveVideoName(videoName);
                                lisBeens2.get(i).setSaveImageName(imageName);
                                lisBeens2.get(i).setAlbumid(lisBeens2.get(i).getAlbumid());

                                if (AppContents.isExit(pathBean.getVideoPath() + videoName)) {
                                    lisBeens2.get(i).setIsexit(true);
                                } else {
                                    lisBeens2.get(i).setIsexit(false);
                                }
                                lisBeens2.get(i).setIsdown(false);
                                if(comBaseActivity.getDataSaveUtils().isUpdateVideo(lisBeens2.get(i),comBaseActivity.getPathBean())){
                                    comBaseActivity.getDataSaveUtils().UpdateVideoInfo(lisBeens2.get(i));
                                }

                            }

                            checkList(lisBeens2,1);
                        }
                    }else {
                        if(!comBean.getMsg().equals("暂无信息")) {

                            MyApplication.getInstance().showToast(comBean.getMsg());
                        }else {
                            mRecyclerView.setNoMore(true);
                        }
                    }
                }else {
                    MyApplication.getInstance().showToast("数据异常");

                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                mRecyclerView.loadMoreComplete();
                if(npost<nans2.length){
                    List<LisBean> lis2=dataSaveUtils.getVideiLists3(pathBean,nans2[npost]);
                    npost++;
                    checkList(lis2, 1);
                    Log.e("=========","you");
                }else {
                    mRecyclerView.setNoMore(true);
                    Log.e("=========","meiyou");
                }
            }
        });

    }


    //添加过滤已经存在的数据列表
    public void checkList(List<LisBean> lisBeen3,int type){


        for (int i=0;i<lisBeen3.size();i++){
            if(!lisBeens.contains(lisBeen3.get(i))){
                lisBeens.add(lisBeen3.get(i));
                Log.e("===","不存在");
            }else {
                Log.e("===","存在");
            }
        }

        mAdapter.notifyDataSetChanged();
        mRecyclerView.loadMoreComplete();

        if(type==2) {
            if (lisBeen3.size() > 0) {
                String aaaid = lisBeen3.get(0).getAlbumid();
                String[] nans = SaveUtils.getvideotag();
                boolean issav = false;
                for (int k = 0; k < nans.length; k++) {
                    if (nans[k].equals(aaaid)) {
                        issav = true;
                        break;
                    }
                }
                if (!issav) {
                    SaveUtils.saveVideotag(aaaid);
                    dataSaveUtils.savevideoInfo(lisBeen3);
                    nans2= SaveUtils.getvideotag();
                }
            }
        }


        for (int la=0;la<lisBeens.size();la++) {
            //缓存图片,以便没有网络的时候可以加载
            String spngname = pathBean.getImagepath() + lisBeens.get(la).getSaveImageName();
            if (AppContents.isExit(spngname)) {

            } else {
                DownFileUtils downFileUtils = new DownFileUtils();
                downFileUtils.down(lisBeens.get(la).getMedia_icon(), spngname, new MyProgressCallback() {
                    @Override
                    public void onLoading(long l, long l1, boolean b) {
                    }

                    @Override
                    public void onSuccess(File file) {
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                    }
                });
            }
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        ++page;
        getLisData();
    }

    @Override
    public void onItemClick(View view, final int postion) {

        Log.e("========",""+postion);
        if(postion<0){
            LogUtils.log("---------->"+postion);
            return;
        }
        if(lisBeens.get(postion-1).isexit()) {
            String mediaFile = pathBean.getVideoPath()+lisBeens.get(postion-1).getSaveVideoName();
            ((ComBaseActivity) getActivity()).playVideo(backType,mediaFile, new DelFileLinstener() {
                @Override
                public void delLintener(boolean isdel) {
                    if (isdel){
                        lisBeens.get(postion-1).setIsexit(false);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }else {
            if(lisBeens.get(postion-1).isdown()){
                lisBeens.get(postion-1).setIsdown(false);
                for (int k=0;k<downStrs.size();k++){
                    for (int j=0;j<lisBeens.size();j++) {
                        if (downStrs.get(k).equals(lisBeens.get(j).getTag_name())) {
                            downStrs.remove(k);
                            break;
                        }
                    }
                }
            }else {
                if( !AppContents.isNetworkAvailable(getActivity())){
                    ShowMesgDialog.show(getActivity(),"网络不可用",2);
                    return;
                }

                downStrs.add(lisBeens.get(postion-1).getTag_name());



                lisBeens.get(postion-1).setIsdown(true);
            }
            mAdapter.notifyDataSetChanged();
        }

    }
    @Override
    public void onLoadFinsh(List<LisBean> list,String tag) {
      /*  lisBeens.clear();
        lisBeens.addAll(list);
        mAdapter.notifyDataSetChanged();*/
        for (int k=0;k<downStrs.size();k++){
            if(downStrs.get(k).equals(tag)){
                downStrs.remove(k);
                break;
            }
        }
    }

    public void cliDown(String tagname){

        if( !AppContents.isNetworkAvailable(getActivity())){
            ShowMesgDialog.show(getActivity(),"网络不可用",2);
            return;
        }

        for (int k=0;k<downStrs.size();k++){
            if(downStrs.get(k).equals(tagname)){
                MyApplication.getInstance().showToast("书本正在下载中，请稍后...");
                return;
            }
        }


        for (int i=0;i<lisBeens.size();i++){
            if(lisBeens.get(i).getTag_name().equals(tagname)){

                final ChongTryDialog chongTryDialog=new ChongTryDialog(getActivity(), new TrySelectLinstener() {
                    @Override
                    public void selectType(int type) {
                        if(type==1){
                            comBaseActivity.addsuoyin(1);
                        }
                    }
                });
                chongTryDialog.show();

                chongTryDialog.setBtnText("    查 看    ");
                chongTryDialog.setTimeDimss(5000);
                Log.e("---","准备下载");
                downStrs.add(tagname);

                mRecyclerView.scrollToPosition(i);
                lisBeens.get(i).setIsdown(true);
                chongTryDialog.setTitle("已添加《"+lisBeens.get(i).getMedia_name()+"》到后台下载\n立刻查看进度?");
                DownFileUtils downFileUtils=new DownFileUtils();
                final int finalI = i;
                downFileUtils.down(lisBeens.get(i).getMedia_url(), pathBean.getVideoPath() + lisBeens.get(i).getSaveVideoName(), new Callback.ProgressCallback<File>() {
                    @Override
                    public void onWaiting() {

                    }

                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onLoading(long l, long l1, boolean b) {

                        Log.e("s-----",l1+l+"");
                        if (l > l1) {

                        } else {
                            lisBeens.get(finalI).setIsexit(true);

                            lisBeens.get(finalI).setIsdown(false);
                            mAdapter.notifyDataSetChanged();

                            for (int j=0;j<downStrs.size();j++){
                                if(lisBeens.get(finalI).getTag_name().equals(downStrs.get(j))){
                                    downStrs.remove(j);
                                    break;
                                }
                            }


                            MyApplication.getInstance().showToast(lisBeens.get(finalI).getMedia_name() + "下载完成");
                        }
                    }

                    @Override
                    public void onSuccess(File file) {

                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {

                        for (int j=0;j<downStrs.size();j++){
                            if(lisBeens.get(finalI).getTag_name().equals(downStrs.get(j))){
                                downStrs.remove(j);
                                break;
                            }
                        }
                        MyApplication.getInstance().showToast("下载失败");
                        chongTryDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(CancelledException e) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
                lisBeens.get(i).setDownFileUtils(downFileUtils);

                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

}
