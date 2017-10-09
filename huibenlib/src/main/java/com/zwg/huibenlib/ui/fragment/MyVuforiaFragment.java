package com.zwg.huibenlib.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.qualcomm.vuforia.CameraDevice;
import com.zwg.huibenlib.ComBaseActivity;
import com.zwg.huibenlib.ComImageTargetRenderer;
import com.zwg.huibenlib.MyApplication;
import com.zwg.huibenlib.R;
import com.zwg.huibenlib.Utils.BeepManager;
import com.zwg.huibenlib.Utils.DownFileUtils;
import com.zwg.huibenlib.Utils.FileTransUtils;
import com.zwg.huibenlib.Utils.ImageFilenameUtils;
import com.zwg.huibenlib.Utils.LogUtils;
import com.zwg.huibenlib.Utils.MyProgressCallback;
import com.zwg.huibenlib.Utils.dialog.ChongTryDialog;
import com.zwg.huibenlib.Utils.dialog.ShowLoadDat;
import com.zwg.huibenlib.Utils.dialog.ShowMesgDialog;
import com.zwg.huibenlib.Utils.dialog.ShowOtherDia;
import com.zwg.huibenlib.bean.ComBean;
import com.zwg.huibenlib.bean.ImagenameBean;
import com.zwg.huibenlib.bean.LisBean;
import com.zwg.huibenlib.bean.VrlistBean;
import com.zwg.huibenlib.db.DataSaveUtils;
import com.zwg.huibenlib.http.AppContents;
import com.zwg.huibenlib.http.MyCallBack;
import com.zwg.huibenlib.http.XutilsHelper;
import com.zwg.huibenlib.inteface.TrySelectLinstener;
import com.zwg.huibenlib.ui.VideoFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyVuforiaFragment extends ComVrFragment {
    private ArrayList<String> list=new ArrayList<>();
    private VideoFragment _flashFragment;
    private ComBaseActivity comBaseActivity;
    private ShowLoadDat showLoadDat;
    private String botuTagg="";
    private String videoTitle="";
    private boolean isShowa=false;


    private int hhu1=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        comBaseActivity= (ComBaseActivity) getActivity();
        list.add(comBaseActivity.getPathBean().getVuforPath()+comBaseActivity.getPathBean().getXmlName());
        // list.add("kubaofiveability20160113.xml");
        setmDatasetStrings(list);
       // sendimageData(12);
    }

    //扫描图片数据返回回调
    @Override
    public void sendimageData(String result) {

        Log.e("====",result);

        comBaseActivity.ttsConnect.finishTTSListener();
        isCanTel=true;

        if(comBaseActivity.getPathBean().getXmlName().equals("index.xml")){  //启动新的识别库
            if(showLoadDat==null) {
                showLoadDat = new ShowLoadDat(getActivity(), R.style.NobackDialog, "数据准备中...");
                showLoadDat.setCanceledOnTouchOutside(false);
            }
            showLoadDat.show();
            showLoadDat.setType("数据准备中...");
            ImagenameBean imagenameBean= ImageFilenameUtils.getTag(result);

            if(isShowa){
                return;
            }
            isShowa=true;

            if(imagenameBean==null){
                showLoadDat.dismiss();
                MyApplication.getInstance().showToast("图片识别失败");
                isShowa=false;
                return;
            }

            if(imagenameBean.getImageTag().equals("-1")) {

            }else {
                botuTagg = imagenameBean.getImageTag();
            }



            boolean isHtag=false;
            for (int kk=0;kk<comBaseActivity.getVrlistBeens().size();kk++){
                if(imagenameBean.getGotoTag().equals(comBaseActivity.getVrlistBeens().get(kk).getTag_name())){
                    isHtag=true;
                    videoTitle=comBaseActivity.getVrlistBeens().get(kk).getAlbumname();
                    break;
                }
            }


            if(!isHtag){
                comBaseActivity.ttsConnect.setTTsTextToSpeach("哎呀，找不到您这本书的资源");
                comBaseActivity.ttsConnect.launchTTSListener();
                ShowOtherDia showOtherDia1=new ShowOtherDia(getActivity(),R.style.NobackDialog,"暂无该书识别库");
                showOtherDia1.show();
                showOtherDia1.setTimeDimss(3000);
                isShowa=false;
                showLoadDat.dismiss();
                return;
            }

            if(_flashFragment==null) {
                _flashFragment = comBaseActivity.fFrag;
            }

            if(!imagenameBean.getGotoTag().equals("index")){
                comBaseActivity.ttsConnect.setTTsTextToSpeach("小朋友，请稍等一会喔");
                comBaseActivity.ttsConnect.launchTTSListener();
            }

            comBaseActivity.setData(imagenameBean.getGotoTag());

            list.clear();
            list.add(comBaseActivity.getPathBean().getVuforPath()+comBaseActivity.getPathBean().getXmlName());
           // list.add(comBaseActivity.getPathBean().getBasePath()+"index/vuforia/index.xml");
            setmDatasetStrings(list);

            int hhj=0;

            if(AppContents.isExit(comBaseActivity.getPathBean().getVuforPath()+comBaseActivity.getPathBean().getDataName())&&AppContents.isExit(comBaseActivity.getPathBean().getVuforPath()+comBaseActivity.getPathBean().getXmlName())){

                for (int i=0;i<comBaseActivity.getVrlistBeens().size();i++){
                    if(comBaseActivity.getVrlistBeens().get(i).getTag_name().equals(imagenameBean.getGotoTag())){
                        hhj=1;
                        if(comBaseActivity.getVrlistBeens().get(i).isRefrsh()){//存在更新
                            loadShibie(2,comBaseActivity.getVrlistBeens().get(i));
                        }else {
                            if(imagenameBean.getGotoTag().equals("index")){
                                showLoadDat.setType("正在返回请稍后...");
                                if(doUnloadTrackersData()){
                                    ((ComBaseActivity) getActivity()).dimssLoad(); //取消进度条显示
                                }
                                new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        if(doLoadTrackersData()){
                                        }
                                        comBaseActivity.showIndex();
                                        showLoadDat.dismiss();
                                        comBaseActivity.ttsConnect.setTTsTextToSpeach("小朋友，请拿出书本封面图扫一扫");
                                        comBaseActivity.ttsConnect.launchTTSListener();
                                        isShowa=false;
                                    }
                                }.start();

                            }else {

                                showLoadDat.setType("资源加载中请稍后...");
                                getLisData(comBaseActivity.getVrlistBeens().get(i).getAlbumid());
                            }

                        }

                    }
                }
            }else {
                for (int i=0;i<comBaseActivity.getVrlistBeens().size();i++){
                    if(comBaseActivity.getVrlistBeens().get(i).getTag_name().equals(imagenameBean.getGotoTag())){
                        hhj=1;
                        loadShibie(1,comBaseActivity.getVrlistBeens().get(i));
                    }
                }

            }
            if(hhj==0){
                showLoadDat.dismiss();
                ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,"遍历不到该书识别库");
                showOtherDia.show();
            }

        }else{  //播放图片或视频

            ImagenameBean imagenameBean= ImageFilenameUtils.getTag(result);

            if(imagenameBean==null){   //旧版图片定义解析



                if(FileTransUtils.getType(result)==1){
                    _flashFragment.playImage(result);
                    isflashtime=0;
                }else {

                    if(isflashtime==FileTransUtils.getflashTime2(result)){
                   /* if(!_flashFragment.iscanPaly){
                        LogUtils.log("-------->","页面重复");

                    }*/
                        return;

                    }else {
                        isflashtime=FileTransUtils.getflashTime2(result);
                    }


                    boolean flag = FileTransUtils.parsetStr(result);
                    if (flag) {
                        _flashFragment.playVideoBetween(FileTransUtils.FileInfo.id, FileTransUtils.FileInfo.startTime, FileTransUtils.FileInfo.endTime);
                    }else {
                        MyApplication.getInstance().showToast("数据解析失败");
                        isflashtime=0;
                    }
                }
            }else {      //新版图片定义解析
                if (_flashFragment == null) {
                    _flashFragment = (VideoFragment) getActivity().getSupportFragmentManager().findFragmentByTag("flashfragment");
                }
                if (_flashFragment == null) {
                    ShowOtherDia showOtherDia = new ShowOtherDia(getActivity(), R.style.NobackDialog, "应用视频视图加载失败");
                    showOtherDia.show();
                    return;
                }

                if (imagenameBean.getIsVideo() == 1) {

                    //限制遇到同一面图重复播放
                    if (isflashtime == (imagenameBean.getStart() + imagenameBean.getEnd())) {
                   /* if (!_flashFragment.iscanPaly) {
                        Log.e("-------->", "页面重复");
                        return;
                    }*/
                        return;
                    } else {
                        isflashtime = imagenameBean.getStart() + imagenameBean.getEnd();
                    }


                    _flashFragment.playVideoBetween(imagenameBean.getVideoTag(), imagenameBean.getStart(), imagenameBean.getEnd());
                } else if (imagenameBean.getIsVideo() == 2) {
                    _flashFragment.playImage( imagenameBean.getVideoTag());
                    isflashtime =-11;
                }
            }
        }
        LogUtils.log("===========",result);
    }


    //识别库加载完成回调
    @Override
    public void finishLoad() {
        comBaseActivity.ttsConnect.setTTsTextToSpeach("小朋友，请拿出书本封面图扫一扫");
        comBaseActivity.ttsConnect.launchTTSListener();
        ((ComBaseActivity)getActivity()).dimssLoad();
        CameraDevice.getInstance().setFlashTorchMode(true);

        try {
            if(hhu1==1){
                hhu1=2;
                Bundle bundle=getArguments();
                if(bundle!=null) {
                    String telltag = bundle.getString("start");
                    if (telltag != null) {
                        if (!telltag.equals("")) {
                            sendimageData(telltag);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    //下载识别库
    public void loadShibie(final int num, final VrlistBean myvrlistBean) {
        DownFileUtils downFileUtils = new DownFileUtils();
        downFileUtils.down(myvrlistBean.getDat_url(), comBaseActivity.getPathBean().getVuforPath() +comBaseActivity.getPathBean().getDataName(), new MyProgressCallback() {
            @Override
            public void onLoading(long l, long l1, boolean b) {
                if (l > l1) {
                    if (num == 1) {
                        showLoadDat.setType("正在下载识别库..."+(l1 * 100 / l)+"%");
                        showLoadDat.setProbar((int) (l1 * 100 / l));
                    } else {
                        showLoadDat.setType("正在更新识别库..."+(l1 * 100 / l)+"%");
                        showLoadDat.setProbar((int) (l1 * 100 / l));
                    }
                } else {

                    new DownFileUtils().down(myvrlistBean.getXml_url(), comBaseActivity.getPathBean().getVuforPath() + comBaseActivity.getPathBean().getXmlName(), new MyProgressCallback() {
                        @Override
                        public void onLoading(long l, long l1, boolean b) {
                            if (l > l1) {
                                if (num == 1) {
                                    showLoadDat.setType("正在下载配置文件");
                                    showLoadDat.setProbar((int) (l1 / l));
                                } else {
                                    showLoadDat.setType("正在更新配置文件");
                                    showLoadDat.setProbar((int) (l1  / l));
                                }
                            } else {
                                comBaseActivity.getDataSaveUtils().updatevrlibInfo(myvrlistBean);

                                for (int k=0;k<comBaseActivity.getVrlistBeens().size();k++){
                                    if(myvrlistBean.getAlbumid().equals(comBaseActivity.getVrlistBeens().get(k).getAlbumid())){
                                        comBaseActivity.getVrlistBeens().get(k).setRefrsh(false);
                                    }
                                }

                                showLoadDat.setType("数据加载中请稍后...");
                                showLoadDat.setProbarGone();
                                getLisData(myvrlistBean.getAlbumid());
                            }
                        }

                        @Override
                        public void onSuccess(File file) {

                        }

                        @Override
                        public void onError(Throwable throwable, boolean b) {
                            if (num == 1) {
                                showLoadDat.dismiss();
                                /*
                                ShowOtherDia showLoad=new ShowOtherDia(getActivity(),R.style.NobackDialog,"识别库下载失败，请退出重试");
                                showLoad.show();*/
                                tryDowm(num,myvrlistBean);
                            } else {
                                getLisData(myvrlistBean.getAlbumid());
                            }
                        }
                    });
                }
            }

            @Override
            public void onSuccess(File file) {
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (num == 1) {
                    showLoadDat.dismiss();
                    tryDowm(num,myvrlistBean);
                } else {
                    getLisData(myvrlistBean.getAlbumid());

                }
            }
        });
    }

    private void getLisData(final String albumid){

        Map<String, String> map = new HashMap<>();
        map.put("albumid", albumid);
        XutilsHelper.post(AppContents.LIB_LSIT_URL, map, new MyCallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtils.log("aaaid-->",s);
                ComBean<LisBean> comBean=null;
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
                                lisBeens2.get(i).setAlbumid(albumid);



                                if (AppContents.isExit(comBaseActivity.getPathBean().getVideoPath() + videoName)) {
                                    lisBeens2.get(i).setIsexit(true);
                                } else {
                                    lisBeens2.get(i).setIsexit(false);
                                }
                                lisBeens2.get(i).setIsdown(false);
                                if(comBaseActivity.getDataSaveUtils().isUpdateVideo(lisBeens2.get(i),comBaseActivity.getPathBean())){
                                     comBaseActivity.getDataSaveUtils().UpdateVideoInfo(lisBeens2.get(i));
                                }
                            }
                            comBaseActivity.listFrag.checkList(lisBeens2,2);
                            comBaseActivity.getLisBeens().clear();
                            comBaseActivity.setLisBeens(lisBeens2);
                        }
                        showLoadDat.setType("数据加载中请稍后...");
                        if(doUnloadTrackersData()){
                            ((ComBaseActivity) getActivity()).dimssLoad(); //取消进度条显示
                        }
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                if(doLoadTrackersData()){

                                }
                                showLoadDat.dismiss();
                                myHandler.sendEmptyMessage(3);
                            }
                        }.start();
                    }else {
                        showLoadDat.dismiss();
                        ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,comBean.getMsg());
                        showOtherDia.show();
                    }
                }else {
                    showLoadDat.dismiss();
                    ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,"数据异常");
                    showOtherDia.show();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                List<LisBean> lisBeenss1=new ArrayList<LisBean>();
                lisBeenss1.addAll(comBaseActivity.getDataSaveUtils().getVideiLists3(comBaseActivity.getPathBean(), albumid));
                if (lisBeenss1.size() > 0) {
                    comBaseActivity.getLisBeens().clear();
                    comBaseActivity.setLisBeens(lisBeenss1);
                    comBaseActivity.listFrag.checkList(lisBeenss1,2);
                    showLoadDat.setType("数据加载中请稍后...");
                    if (doUnloadTrackersData()) {
                        ((ComBaseActivity) getActivity()).dimssLoad(); //取消进度条显示
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            if (doLoadTrackersData()) {

                            }
                            showLoadDat.dismiss();

                            myHandler.sendEmptyMessage(3);
                        }
                    }.start();
                }else {
                    ShowMesgDialog.show(getActivity(), "无法连接服务器，请检查网络或稍后重试", 1);
                }
            }
        });

    }

    private  Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ComImageTargetRenderer.LastData="00";
            int ss=msg.what;
            if(ss==3){
                isShowa=false;
                if(comBaseActivity.getPathBean().getXmlName().contains("index")){
                    comBaseActivity.showIndex();
                    showLoadDat.dismiss();
                    if(isCanTel) {
                        comBaseActivity.ttsConnect.setTTsTextToSpeach("小朋友，请拿出书本封面图扫一扫");
                        comBaseActivity.ttsConnect.launchTTSListener();
                    }
                }else {

                    comBaseActivity.showvideFrag();
                    if(  isCanTel) {
                        comBaseActivity.ttsConnect.setTTsTextToSpeach("小朋友，请翻开书本再摸摸我的耳朵开始读书吧");
                        comBaseActivity.ttsConnect.launchTTSListener();
                    }

                    _flashFragment.playImage(botuTagg);
                }
            }
        }
    };


    private void tryDowm(final int num, final VrlistBean myvrlistBean){

        if( !AppContents.isNetworkAvailable(getActivity())){
            ShowMesgDialog.show(getActivity(),"网络不可用",2);
            comBaseActivity.setData("index");
            comBaseActivity.showIndex();
            return;
        }


        ChongTryDialog chongTryDialog=new ChongTryDialog(getActivity(), new TrySelectLinstener() {
            @Override
            public void selectType(int type) {
                if(type==1){
                    loadShibie(num,myvrlistBean);
                }else if(type==2){
                    comBaseActivity.setData("index");
                    comBaseActivity.showIndex();
                }
            }
        });
        chongTryDialog.show();
        chongTryDialog.setCanceledOnTouchOutside(false);
    }



    private void checkUpda(List<LisBean> lisBeens6,List<LisBean> lisBeens7){

        for (int i=0;i<lisBeens6.size();i++){
            for (int j=0;j<lisBeens7.size();j++){
                if(lisBeens6.get(i).getSaveVideoName().equals(lisBeens7.get(j).getSaveVideoName())){

                }
            }
        }
    }


}
