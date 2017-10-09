package com.zwg.huibenlib.ui;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zwg.huibenlib.ComBaseActivity;
import com.zwg.huibenlib.ComImageTargetRenderer;
import com.zwg.huibenlib.ImageTargetRenderer;
import com.zwg.huibenlib.MyApplication;
import com.zwg.huibenlib.R;
import com.zwg.huibenlib.Utils.dialog.ChongTryDialog;
import com.zwg.huibenlib.Utils.dialog.ShowOtherDia;
import com.zwg.huibenlib.Utils.video.MyUniversalVideoView;
import com.zwg.huibenlib.bean.LisBean;
import com.zwg.huibenlib.bean.PathBean;
import com.zwg.huibenlib.http.AppContents;
import com.zwg.huibenlib.inteface.DelFileLinstener;
import com.zwg.huibenlib.inteface.ErrorVideoLinstener;
import com.zwg.huibenlib.inteface.TrySelectLinstener;
import com.zwg.huibenlib.ui.fragment.ComVrFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 表情界面
 * Created by ZZQ on 2016/5/4.
 * Copyright (c) 2016 Wenshanhu.Co.Ltd. All rights reserved.
 */
public class VideoFragment extends Fragment implements View.OnClickListener ,MediaPlayer.OnCompletionListener {


    private static final String TAG = "flashFragment";
  //  private List<LisBean> lisBeens=new ArrayList<>();


    private ComVrFragment vrFragment;
    private BookListFragment listFragment;

    private int haoj=1;

   // public boolean iscanPaly=true;//是否可以播放标记

    boolean partFlag = false;//允许低头标记
    //播放图片布局
    private ImageView fengmianIv;
    public RelativeLayout fengmianLayout;

    //视频播放控件
    private MyUniversalVideoView mVideoView;
    private View mVideoLayout;


    private PathBean pathBean;

    private String delPath="";

    private DelFileLinstener delFileLinstener;

    public int hback=1;

    private Map<String,LisBean> map=new HashMap<>();

    @Override
    public void onCreate (Bundle b) {
        super.onCreate(b);
        initFragment();
    }

    @Override
    public void onStart () {
        super.onStart();
    }

    @Override
    public void onResume () {
        Log.e(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause () {
        Log.e(TAG, "onPause");
        super.onPause();
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.pause();
        }

    }

    @Override
    public void onStop () {
        super.onStop();
    }

    @Override
    public void onDestroy () {
        Log.v(TAG, "onDestroy");
        super.onDestroy();

    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_video, container, false);
        initVideoview(v);
        initView(v);
        return v;
    }

    private void initVideoview(View v) {
        mVideoLayout = v.findViewById(R.id.video_layout);
        mVideoView = (MyUniversalVideoView)v. findViewById(R.id.videoView);
        fengmianLayout= (RelativeLayout) v.findViewById(R.id.fengmian_layout);
        fengmianIv= (ImageView) v.findViewById(R.id.xianshi_fengmian_iv);

        setVideoAreaSize();
        mVideoView.setOnCompletionListener(this);

        mVideoView.setErrorVideoLinstener(new ErrorVideoLinstener() {
            @Override
            public void errorLinstener() {
                ChongTryDialog chongTryDialog=new ChongTryDialog(getActivity(), new TrySelectLinstener() {
                    @Override
                    public void selectType(int type) {
                        if(type==1){
                            if(delFileLinstener!=null){
                                delFileLinstener.delLintener(true);
                            }
                            try {
                                File file=new File(delPath);
                                if(file.exists()) {
                                    file.delete();
                                }
                            }catch ( Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
                chongTryDialog.show();
                chongTryDialog.setTitle("播放失败\n视频文件可能已损坏\n是否删除");
                chongTryDialog.setBtnText("    删 除    ");
            }
        });
        vrFragment= (ComVrFragment) getActivity().getSupportFragmentManager().findFragmentByTag("vrfragment");
    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height =  ViewGroup.LayoutParams.MATCH_PARENT;
                mVideoLayout.setLayoutParams(videoLayoutParams);
            }
        });
    }


    /**
     *播放视频
     *
     */

    Timer timer;
    public void playVideoBetween(String videotag, int start, int end) {
        this.delFileLinstener=null;
        hback=1;
        pathBean=((ComBaseActivity)getActivity()).getPathBean();
        assert(end >= start);
        String myVideoName="";
        if(timer!=null){
            timer.cancel();
        }

        if(videotag.equals("帮助")){
            myVideoName="帮助";
        }else {

           /* //遍历数组根据id找到对应的视频
            for (int i = 0; i < lisBeens.size(); i++) {
                if (!lisBeens.get(i).getTag_name().equals("")) {
                    if (lisBeens.get(i).getTag_name().equals(videotag)) {
                        myVideoName = lisBeens.get(i).getSaveVideoName();
                        break;
                    }
                }
            }*/


            LisBean lisBean=map.get(videotag);
            if(lisBean!=null){
                myVideoName=lisBean.getSaveVideoName();
            }


        }
        if(!myVideoName.equals("")) {
            if (AppContents.isExit( pathBean.getVideoPath() + myVideoName)) {
                String mp4Path =  pathBean.getVideoPath()+myVideoName;
                Log.e("111111",mp4Path);
                if(start>end){
                    Toast.makeText(getActivity(),"文件命名格式错误",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    {
                        backhomeIv.setVisibility(View.VISIBLE);
                        ((ComBaseActivity)getActivity()).ShowVideoFrag();
                        fengmianLayout.setVisibility(View.GONE);

                        mVideoLayout.setVisibility(View.VISIBLE);

                        backhomeIv.setVisibility(View.VISIBLE);
                        mVideoView.setVideoPath(mp4Path);
                        delPath=mp4Path;
                        mVideoView.requestFocus();
                        if (start > 0) {
                            mVideoView.seekTo(start);
                        }
                        mVideoView.start();

                      //  iscanPaly=false;
                        partFlag = false;
                        if (end != 0) {
                            partFlag = true;
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(55);
                                }
                            }, end - start);
                        }
                    }
                } catch (IllegalStateException e) {
                    {
                        Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                listFragment.cliDown(videotag);
                //listFragment.downVideo(videotag);  开始下载资源

            }
        }else {
            MyApplication.getInstance().showToast("找不到该书资源");
        }
    }
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 55:        //定时器超时message 处理
                  //  iscanPaly=true;
                    // VideoPlayer.getInstance(getActivity()).pause();
                    mVideoView.pause();
                    vrFragment.headDown();         //播完低头
                    break;
                case 66:        //播放结束message 处理
                //    iscanPaly=true;
                    if (!partFlag) vrFragment.headDown();         //播完低头
                    break;
                case 101:
                    haoj=1;
                    backhomeIv.setClickable(true);
                    break;
                default:
                    break;
            }
        }

    };




    public void playVideo(String path, DelFileLinstener delFileLinstener) {
        this.delFileLinstener=delFileLinstener;

        try {
            if (!AppContents.isExit(path)) {
                ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,"视频文件不存在");
                showOtherDia.show();
                showOtherDia.setTimeDimss(5000);
                return;
            }
            delPath=path;
            ((ComBaseActivity)getActivity()).ShowVideoFrag();
            fengmianLayout.setVisibility(View.GONE);

            mVideoLayout.setVisibility(View.VISIBLE);


            mVideoView.setVideoPath(path);

            mVideoView.requestFocus();

            mVideoView.seekTo(0);

            mVideoView.start();

           // iscanPaly=false;
            partFlag = false;
        } catch (IllegalStateException e) {
            {
                Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_LONG).show();
            }
        }


    }


    private void initFragment () {
        FragmentManager fm  = getActivity().getSupportFragmentManager();
        vrFragment = (ComVrFragment) fm.findFragmentByTag("vrfragment");
        listFragment = (BookListFragment) fm.findFragmentByTag("lisyfragment");
    }

    private ImageView backhomeIv;
    private void initView(View v) {
        backhomeIv= (ImageView) v.findViewById(R.id.back_home_iv);
        backhomeIv.setOnClickListener(this);
        setcanClick();
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        ComImageTargetRenderer.LastData="888";
        if (id == R.id.back_home_iv) {//返回键
            mVideoView.pause();
            if(hback==1) {
                if (haoj == 1) {
                    haoj = 0;
                    backhomeIv.setClickable(false);

                    ImageTargetRenderer.LastData="nodedate";
                    ((ComBaseActivity) getActivity()).showIndex();
                    ((ComBaseActivity) getActivity()).setData("index");
                    vrFragment.sendimageData("index_index_01");
                    setcanClick();
                }
            }else if(hback==2){
                hback=1;
                ((ComBaseActivity) getActivity()).addsuoyin(2);
            }
        }
    }







    //显示封面图
    public void playImage(String tag){
        mVideoView.pause();
        hback=1;
        fengmianLayout.setVisibility(View.VISIBLE);
        mVideoLayout.setVisibility(View.GONE);
        backhomeIv.setVisibility(View.VISIBLE);
        ((ComBaseActivity)getActivity()).ShowVideoFrag();

    //    iscanPaly=true;
        LisBean lisBean=map.get(tag);
        if(lisBean!=null){
            if(AppContents.isExit(pathBean.getImagepath()+lisBean.getSaveImageName())){
                File file = new File(pathBean.getImagepath(), lisBean.getSaveImageName());
                Glide.with(getActivity()).load(file).placeholder(R.drawable.default_icom).into( fengmianIv);
            }else {
                Glide.with(getActivity()).load(lisBean.getMedia_icon()).placeholder(R.drawable.default_icom).crossFade(10).into(fengmianIv);
            }
        }else {
            MyApplication.getInstance().showToast("找不到图片路径与链接");
        }


       /* //遍历找到视频文件
        for (int i=0;i<lisBeens.size();i++ ) {
            if (!lisBeens.get(i).getAlbumid().equals("")) {
                String  saImageName="";
                if (lisBeens.get(i).getTag_name().equals(tag)) {
                    saImageName=lisBeens.get(i).getSaveImageName();
                }
                if(!saImageName.equals("")){
                    if(AppContents.isExit(pathBean.getImagepath()+saImageName)){
                        File file = new File(pathBean.getImagepath(), lisBeens.get(i).getSaveImageName());
                        Glide.with(getActivity()).load(file).placeholder(R.drawable.default_icom).into( fengmianIv);
                    }else {
                        Glide.with(getActivity()).load(lisBeens.get(i).getMedia_icon()).placeholder(R.drawable.default_icom).crossFade(10).into(fengmianIv);
                    }
                    break;
                }
            }
        }*/

    }

    public void setcanClick(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(101);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e("=========","播放完成------------");

        mVideoView.pause();
        vrFragment.isflashtime=11;
    }
    public void setVideoDate(PathBean pathBean,List<LisBean> lisBeens2){
        this.pathBean=pathBean;
      /*  lisBeens.clear();
        lisBeens.addAll(lisBeens2);*/


        map.clear();
        if(lisBeens2.size()<0){
            MyApplication.getInstance().showToast("暂无数据内容");
            return;
        }
        for (int i=0;i<lisBeens2.size();i++){
            map.put(lisBeens2.get(i).getTag_name(),lisBeens2.get(i));
        }


    }




}