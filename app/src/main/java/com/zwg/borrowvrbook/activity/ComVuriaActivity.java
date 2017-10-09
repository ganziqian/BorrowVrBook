package com.zwg.borrowvrbook.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zwg.borrowvrbook.R;
import com.zwg.huibenlib.ComBaseActivity;
import com.zwg.huibenlib.ImageTargetRenderer;
import com.zwg.huibenlib.Utils.DownFileUtils;
import com.zwg.huibenlib.Utils.LogUtils;
import com.zwg.huibenlib.Utils.MD5Uutils;
import com.zwg.huibenlib.Utils.MyProgressCallback;
import com.zwg.huibenlib.Utils.SaveUtils;
import com.zwg.huibenlib.Utils.dialog.ChongTryDialog;
import com.zwg.huibenlib.Utils.dialog.ShowMesgDialog;
import com.zwg.huibenlib.Utils.dialog.ShowOtherDia;
import com.zwg.huibenlib.bean.ComBean;
import com.zwg.huibenlib.bean.UpdateBean;
import com.zwg.huibenlib.bean.VrlistBean;
import com.zwg.huibenlib.db.DBManager;
import com.zwg.huibenlib.db.DataSaveUtils;
import com.zwg.huibenlib.http.AppContents;
import com.zwg.huibenlib.http.MyCallBack;
import com.zwg.huibenlib.http.XutilsHelper;
import com.zwg.huibenlib.inteface.LoadDateLinstener;
import com.zwg.huibenlib.inteface.TrySelectLinstener;
import com.zwg.huibenlib.server.ApkDownServer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComVuriaActivity extends ComBaseActivity {
    private RelativeLayout loadLayout;
    private ImageView loadiv;
    private DataSaveUtils dataSaveUtils;

    private TextView loadTv;
    private  String seappkey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initView();
    }


    private void initView() {
        ImageTargetRenderer.LastData="nodedate";
     /*   DownApkdialog downApkdialog=new DownApkdialog(this);
        downApkdialog.show();*/

        dataSaveUtils = new DataSaveUtils(this);


        if(SaveUtils.getOnedate().equals("")) {
            if (dataSaveUtils.getVRLists().size() == 0) {
                DBManager dbManager = new DBManager(this);
                dbManager.DBManager(getPackageName());
                dataSaveUtils = new DataSaveUtils(this);
                SaveUtils.saveoneSavedate("sss");
                SaveUtils.saveVideotag("2_3_4_5_6_7_8_9_10_11_12_13");
            }
        }

        delpakpath();

        setDataSaveUtils(dataSaveUtils);
        loadLayout = (RelativeLayout) findViewById(R.id.load_layout);

        TextView versionTv = (TextView) findViewById(R.id.main_versionname_tv);
        versionTv.setText(AppContents.getAppVersionName(this));

        loadiv = (ImageView) findViewById(R.id.main_load_iv);
        loadTv = (TextView) findViewById(R.id.main_load_tv);



        String stringtag=getIntent().getStringExtra("tag");
        if(stringtag!=null){
            setTelltag(stringtag);
        }

        //  seappkey= SignUtils.getSingInfo(getPackageName(),this);
        seappkey = "906BC340F1C89ED56E87A22388B8833C";
        setConfig(R.id.other_layout, R.id.vuforia_layout, seappkey);
        setData("index");
        DelVufusUtils.delVus(this);
        String mac= AppContents.getMacStr(this).toUpperCase().replace(":","");
        if(!dataSaveUtils.getIsAc().equals(MD5Uutils.MD5(seappkey+mac))){
            posYanJIhuo();
            return;
        }

        checkUpdateapk();
        if (AppContents.isExit(getPathBean().getVuforPath() + getPathBean().getXmlName()) && AppContents.isExit(getPathBean().getVuforPath() + getPathBean().getDataName())) {

            loadTv.setText("正在检查更新...");
            postVRData(1);
        } else {
            postVRData(2);
        }
    }

    //删除下载过的apk更新文件
    private void delpakpath() {

        try {
            if(!SaveUtils.getdelpath().equals("")) {
                File file = new File(SaveUtils.getdelpath());
                if(file.exists()){
                    file.delete();
                    SaveUtils.deletdelpathh();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void posYanJIhuo(){
        setLoadDateLinstener(new LoadDateLinstener() {
            @Override
            public void loadType(int type) {
                if(type==1){
                    postVRData(1);
                }else if(type==2){
                    postVRData(2);
                }
            }
        });
        postJihuo(loadTv);
    }




    //加载
    private void postVRData( final int type) {
        loadTv.setText("正在检查更新...");
        Map<String, String> map = new HashMap<>();
        map.put("appkey", seappkey);
        XutilsHelper.post(AppContents.LIB_VRDATA_URL, map, new MyCallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtils.log(s);
                loadTv.setText("正在初始化...");
                ComBean<VrlistBean> comBean=null;
                try {
                    comBean= JSON.parseObject(s,new TypeReference<ComBean<VrlistBean>>(){});
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(comBean!=null){
                    if(comBean.getCode().equals("1")){
                        if(comBean.getData().size()!=0){
                            List<VrlistBean> hvrlists=new ArrayList<VrlistBean>();
                            List<VrlistBean> hvrlists2=new ArrayList<VrlistBean>();

                            hvrlists2.addAll(dataSaveUtils.getVRLists());

                            hvrlists.addAll(comBean.getData());

                            for (int i=0;i<hvrlists.size();i++){
                                String saveXmlName="";
                                String saveDatName="";
                                try {
                                    int lastIndex = comBean.getData().get(i).getXml_url().lastIndexOf("/");
                                    saveXmlName = comBean.getData().get(i).getXml_url().substring(lastIndex + 1, comBean.getData().get(i).getXml_url().length());

                                    int lastIndex2 = comBean.getData().get(i).getDat_url().lastIndexOf("/");
                                    saveDatName = comBean.getData().get(i).getDat_url().substring(lastIndex2 + 1, comBean.getData().get(i).getDat_url().length());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    saveXmlName = comBean.getData().get(i).getXml_url();
                                    saveDatName = comBean.getData().get(i).getDat_url();
                                }
                                hvrlists.get(i).setSavexmlName(saveXmlName);
                                hvrlists.get(i).setSavedatName(saveDatName);
                            }

                            if(hvrlists2.size()==0){
                                dataSaveUtils.saveVrInfo(hvrlists);
                            }

                            try {  //检查所有库是否存在更新
                                for (int m=0;m<hvrlists.size();m++){

                                    for (int h=0;h<hvrlists2.size();h++){

                                        if(hvrlists.get(m).getAlbumid().equals(hvrlists2.get(h).getAlbumid())){
                                            Log.e("======", hvrlists2.get(h).getTag_name()+"=="+hvrlists2.get(h).getSavexmlName());
                                            if(hvrlists.get(m).getSavexmlName().equals(hvrlists2.get(h).getSavexmlName())){

                                            }else {
                                                Log.e("库更新===", hvrlists.get(m).getTag_name());
                                                hvrlists.get(m).setRefrsh(true);
                                            }
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            setVrlistBeens(hvrlists);

                            if(hvrlists2.size()==0){//如果数据库没数据
                                dataSaveUtils.saveVrInfo(hvrlists);
                                int kj=0;
                                for (int j = 0; j < hvrlists.size(); j++) {
                                    if (hvrlists.get(j).getTag_name().equals("index")) {
                                        if (AppContents.isExit(getPathBean().getVuforPath() + getPathBean().getXmlName()) && AppContents.isExit(getPathBean().getVuforPath() + getPathBean().getDataName())) {
                                            loadShibie(2, hvrlists.get(j).getDat_url(), hvrlists.get(j).getXml_url(),hvrlists.get(j));
                                        } else {
                                            loadShibie(1, hvrlists.get(j).getDat_url(), hvrlists.get(j).getXml_url(),hvrlists.get(j));
                                        }
                                        kj=1;
                                    }
                                }
                                if(kj==0){
                                    if(type==2) {
                                        ShowOtherDia showOtherDia = new ShowOtherDia(ComVuriaActivity.this, R.style.NobackDialog, "找不到封面识别库");
                                        showOtherDia.show();
                                    }else {
                                        initVuforia();
                                        initindexFrag();
                                    }
                                }

                            }else {
                                for (int b=0;b<hvrlists.size();b++){
                                    if(!hvrlists2.contains(hvrlists.get(b))){
                                       // Log.e("=====保存库=", hvrlists.get(b).getTag_name());
                                        dataSaveUtils.saveVrlibInfo(hvrlists.get(b));
                                    }
                                }


                                int kj2 = 0;
                                //检查封面库是否更新，如果更新则先下载
                                for (int i = 0; i < hvrlists.size(); i++) {
                                    if (hvrlists.get(i).getTag_name().equals("index")) {
                                        kj2 = 1;
                                        if (AppContents.isExit(getPathBean().getVuforPath() + getPathBean().getXmlName()) && AppContents.isExit(getPathBean().getVuforPath() + getPathBean().getDataName())) {
                                            if (!hvrlists.get(i).isRefrsh()) {  //不更新

                                                loadTv.setText("正在初始化...");
                                                initVuforia();
                                                initindexFrag();
                                            } else {                //更新

                                                loadShibie(2, hvrlists.get(i).getDat_url(), hvrlists.get(i).getXml_url(),hvrlists.get(i));
                                            }
                                        } else {
                                            loadShibie(1, hvrlists.get(i).getDat_url(), hvrlists.get(i).getXml_url(),hvrlists.get(i));
                                        }
                                    }
                                }
                                if (kj2 == 0) {
                                    ShowOtherDia showOtherDia = new ShowOtherDia(ComVuriaActivity.this, R.style.NobackDialog, "找不到封面识别库");
                                    showOtherDia.show();
                                }

                            }




/*
                            if(hvrlists.size()==hvrlists2.size()) {
                              *//*  int kj2=0;
                                int kj3=0;
                                for (int j = 0; j < hvrlists.size(); j++) {
                                    if (hvrlists.get(j).getTag_name().equals("index")) {
                                        if (AppContents.isExit(getPathBean().getVuforPath() + getPathBean().getXmlName()) && AppContents.isExit(getPathBean().getVuforPath() + getPathBean().getDataName())) {

                                            if (hvrlists.get(j).getSavexmlName().equals(hvrlists2.get(j).getSavexmlName())){  //是否更新
                                                loadTv.setText("正在初始化...");
                                                initVuforia();
                                                initindexFrag();
                                            }else {
                                                kj3=1;
                                                loadShibie(2, hvrlists.get(j).getDat_url(), hvrlists.get(j).getXml_url());
                                            }

                                        } else {
                                            kj3=1;
                                            loadShibie(1, hvrlists.get(j).getDat_url(), hvrlists.get(j).getXml_url());
                                        }
                                        kj2=1;
                                    }
                                }
                                if(kj3==1){
                                    dataSaveUtils.saveVrInfo(hvrlists);
                                }
                                if(kj2==0){
                                    ShowOtherDia showOtherDia=new ShowOtherDia(ComVuriaActivity.this,R.style.NobackDialog,"找不到封面识别库");
                                    showOtherDia.show();
                                }*//*
                            }else {

                            }*/
                        }
                    }else {
                        if(type==2) {
                            ShowOtherDia showOtherDia = new ShowOtherDia(ComVuriaActivity.this, R.style.NobackDialog, comBean.getMsg());
                            showOtherDia.show();
                            dimissDia(showOtherDia);
                        }else {
                            setVrlistBeens(dataSaveUtils.getVRLists());
                            initVuforia();
                            initindexFrag();
                        }
                    }
                }else {
                    if(type==2) {
                        ShowOtherDia showOtherDia = new ShowOtherDia(ComVuriaActivity.this, R.style.NobackDialog, "数据异常");
                        showOtherDia.show();
                        dimissDia(showOtherDia);
                    }else {
                        setVrlistBeens(dataSaveUtils.getVRLists());
                        initVuforia();
                        initindexFrag();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (type == 2) {
                    ShowMesgDialog.show(ComVuriaActivity.this, "无法连接服务器，请检查网络或稍后重试", 1);
                } else {
                    if(dataSaveUtils.getVRLists().size()!=0){
                        setVrlistBeens(dataSaveUtils.getVRLists());
                        loadTv.setText("正在初始化...");
                        initVuforia();
                        initindexFrag();
                    }else {
                        ShowMesgDialog.show(ComVuriaActivity.this, "无法连接服务器，请检查网络或稍后重试", 1);
                    }
                }
            }
        });
    }

    //下载识别库
    public void loadShibie(final int num, final String dataUrl, final String xmlurl, final VrlistBean myVrListBean) {
        DownFileUtils downFileUtils = new DownFileUtils();
        downFileUtils.down(dataUrl, getPathBean().getVuforPath() +getPathBean().getDataName(), new MyProgressCallback() {
            @Override
            public void onLoading(long l, long l1, boolean b) {
                if (l > l1) {
                    if (num == 1) {
                        loadTv.setText("正在下载识别库..." + (int) (l1 * 100 / l) + "%");
                    } else {
                        loadTv.setText("正在更新识别库..." + (int) (l1 * 100 / l) + "%");
                    }
                } else {
                    if (num == 1) {
                        loadTv.setText("识别库下载完成，正在下载配置文件...");
                    } else {
                        loadTv.setText("识别库更新完成，正在更新配置文件...");
                    }
                    new DownFileUtils().down(xmlurl, getPathBean().getVuforPath() + getPathBean().getXmlName(), new MyProgressCallback() {
                        @Override
                        public void onLoading(long l, long l1, boolean b) {
                            if (l > l1) {
                                if (num == 1) {
                                    loadTv.setText("正在下载配置文件..." + (int) (l1 * 100 / l) + "%");
                                } else {
                                    loadTv.setText("正在更新配置文件..." + (int) (l1 * 100 / l) + "%");
                                }
                            } else {
                                if (num == 1) {
                                    loadTv.setText("下载完成");
                                } else {
                                    loadTv.setText("更新完成");
                                }

                                dataSaveUtils.updatevrlibInfo(myVrListBean);
                                loadTv.setText("正在初始化...");
                                initVuforia();
                                initindexFrag();
                            }
                        }

                        @Override
                        public void onSuccess(File file) {

                        }

                        @Override
                        public void onError(Throwable throwable, boolean b) {
                            if (num == 1) {
                             /*   ShowOtherDia showLoad=new ShowOtherDia(ComVuriaActivity.this,R.style.NobackDialog,"识别库下载失败，请退出重试");
                                showLoad.show();
                                dimissDia(showLoad);*/
                                tryDowm(num,dataUrl,xmlurl,myVrListBean);
                                // loadTv.setText("识别库下载失败，请退出重试");
                            } else {
                                loadTv.setText("正在初始化请稍后...");
                                initVuforia();
                                initindexFrag();
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
                    // ShowMesgDialog.show(MainActivity.this,"下载失败，请退出重试",1);
                   /* ShowOtherDia showLoad=new ShowOtherDia(ComVuriaActivity.this,R.style.NobackDialog,"识别库下载失败，请退出重试");
                    showLoad.show();
                    dimissDia(showLoad);*/
                    tryDowm(num,dataUrl,xmlurl,myVrListBean);
                } else {
                    loadTv.setText("正在初始化请稍后...");
                    initVuforia();
                    initindexFrag();
                }
            }
        });

    }













    private void dimissDia(Dialog dialog){
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }




    private void tryDowm(final int num, final String dataUrl, final String xmlUrl, final VrlistBean myVrListBean){
        ChongTryDialog chongTryDialog=new ChongTryDialog(this, new TrySelectLinstener() {
            @Override
            public void selectType(int type) {
                if(type==1){
                    loadShibie(num,dataUrl,xmlUrl,myVrListBean);
                }else if(type==2){
                    finish();
                }

            }
        });
        chongTryDialog.show();
        chongTryDialog.setCanceledOnTouchOutside(false);

    }








    //隐藏加载页面
    @Override
    public void dimssLoad() {

        if (loadLayout != null) {
            loadLayout.setVisibility(View.GONE);
        }
        if (loadiv != null) {
            loadiv.clearAnimation();
            AnimationDrawable animationDrawable = (AnimationDrawable) loadiv.getDrawable();
            animationDrawable.stop();
        }

    }


    //显示加载页面
    @Override
    public void showLoad() {
        if (loadLayout != null) {
            loadLayout.setVisibility(View.VISIBLE);
        }
        if (loadiv != null) {
            AnimationDrawable animationDrawable = (AnimationDrawable) loadiv.getDrawable();
            animationDrawable.start();
        }
    }


    //版本检查更新
    private void checkUpdateapk(){
        Map<String, String> map = new HashMap<>();
        String mac= AppContents.getMacStr(this).toUpperCase().replace(":","");
        map.put("appkey", seappkey);
        map.put("deviceid", mac);
        map.put("versioncode_old", AppContents.getAppVersioncode(this)+"");
        XutilsHelper.post(AppContents.CHEK_UPDATE_URL, map, new MyCallBack() {

            @Override
            public void onSuccess(String s) {
                Log.e("===============",s);
                try {
                    ComBean<UpdateBean> comBean=JSON.parseObject(s,new TypeReference<ComBean<UpdateBean>>(){});

                    if(comBean.getCode().equals("1")){

                        UpdateBean updateBean=comBean.getData().get(0);
                        if(updateBean.getResult().equals("1")||updateBean.getResult().equals("1")){

                            if(updateBean.getUrl()!=null){
                                Intent intent = new Intent(ComVuriaActivity.this, ApkDownServer.class);
                                intent.putExtra("url",updateBean.getUrl());

                                String apkname="";
                                try {
                                    int lastIndex = comBean.getData().get(0).getUrl().lastIndexOf("/");
                                    apkname = comBean.getData().get(0).getUrl().substring(lastIndex + 1, comBean.getData().get(0).getUrl().length());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                intent.putExtra("name",apkname);
                                startService(intent);
                            }

                        }

                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Log.e("===============",throwable.toString());
            }
        });
    }


}
