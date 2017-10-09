package com.zwg.huibenlib;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wsh.aidl.ITTSAidlConnect;
import com.zwg.huibenlib.Utils.JihuoUtils;
import com.zwg.huibenlib.Utils.MD5Uutils;
import com.zwg.huibenlib.Utils.RSAUtil;
import com.zwg.huibenlib.Utils.dialog.ShowMesgDialog;
import com.zwg.huibenlib.Utils.dialog.ShowOtherDia;
import com.zwg.huibenlib.bean.LisBean;
import com.zwg.huibenlib.bean.PathBean;
import com.zwg.huibenlib.bean.VrlistBean;
import com.zwg.huibenlib.db.DataSaveUtils;
import com.zwg.huibenlib.http.AppContents;
import com.zwg.huibenlib.http.MyCallBack;
import com.zwg.huibenlib.http.XutilsHelper;
import com.zwg.huibenlib.inteface.DelFileLinstener;
import com.zwg.huibenlib.inteface.LoadDateLinstener;
import com.zwg.huibenlib.inteface.ZxingResultLinster;
import com.zwg.huibenlib.ui.BookListFragment;
import com.zwg.huibenlib.ui.ImageFragment;
import com.zwg.huibenlib.ui.IndexFragment;
import com.zwg.huibenlib.ui.VideoFragment;
import com.zwg.huibenlib.ui.ZxingFragment;
import com.zwg.huibenlib.ui.fragment.MyVuforiaFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ComBaseActivity extends AppCompatActivity {

    public List<LisBean> lisBeens=new ArrayList<>();
    public List<VrlistBean> vrlistBeens=new ArrayList<>();



    private FragmentManager manager;
    private DataSaveUtils dataSaveUtils;



    private String  myappkey;

    public String getMyappkey() {
        return myappkey;
    }


    private PathBean pathBean;
    private String basePath="";
    //private String basePath="/mnt/usb_storage/comhuiben/";


    public  ITTSAidlConnect ttsConnect ;
    public static final String key="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI6yetsbr/Xg41TEaF6bs5f5vrzpdI9Fia4ls4lF5dYIw//euQ9wcabhzZbejM5Dxh5gA3MdU90cjtTMIguHGKA2N7ozlZ4LRljmG1h7w2NaHuKUXAMh350NbgrAW7e/C7NnmhE1OjSgnf0Klq+NgC7F8Gs/A2urZdjaz7794I69AgMBAAECgYBFQvhC1aOI0slE4bhPA4AeaghcpWwABp6XOpF0NcsjIkoQLcjhZ5CikcM6UXdCvr6xC6VhAIEuN6hUPWjg84fNsa8nFij6e4SzVRUo8gYmtwRQftMJHUiq4wDre7WLcodqv5j2LXiOTIkodyZ2hWt4Yq91jIuPf8mpamFsJLTTuQJBAM476btNtCOtjSMoXJ8qne2j+6IkyPryc0Hnpaw557n99K5uPy3D/d9aG1O3sUFcXJqb14pQFlyv5aNzrWTNOEsCQQCxIZWdJ8QC/4LRcoJACt3hLkZqeDB53l68aa0qZmsuL9+ZMQ2kxiFWzB9j29dc+dhloTjg5/cjNg+kA5pxAYAXAkEAjo+d76s0JiGXWcFR3XkBOL/Nd3VENSyZ/enafWZ9x/VESbvOEp3UBaxtDX8CmfL11K573ZGlE6dH76hMKU0vZwJANlmUu7Tw6u6VqEiXeKkc7bQyPQcF8M7viKZwUNs+NdzQogOwKQf2QNi/JPfWvBuZb42pkzD53t7+q5fDcrtAHwJBAM3IaBx41dYf6LK2oChZyh2akqfqZOPTIgnhU0GuaLzJG6Ds2gPAmGRSquYtGriHWiQGcHgJd/YWFUEPXFwLiPA=";

    //四个显示的fragment
    public BookListFragment listFrag;
    private ZxingFragment zxingFrag;
    public VideoFragment fFrag;
    public IndexFragment indexFragment;
    public MyVuforiaFragment vfrag;

    public int otherId=0;
    public int  vuforsLayoutid=0;
    private ZxingResultLinster zxingResultLinster;

    private LoadDateLinstener loadDateLinstener;



    private String  telltag="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getSupportActionBar().hide();



        ttsConnect = new ITTSAidlConnect(this);
        ttsConnect.init (this);


        /** 设置配置信息 */
        ttsConnect.setTTsPitch("50");         // 设置音调
        ttsConnect.setEngineType("local");    // 设置本地tts发音还是混合发音
        ttsConnect.setTTsSpeed("50");     // 设置语速
        ttsConnect.setTTsVolume("70");    // 设置语音音量
        ttsConnect.setTTsSpeaker("nannan");   // 设置发音人
        ttsConnect.setHandler(new Handler() {
            @Override
            public void handleMessage (Message msg) {
                int what = msg.what;
                String obj = (String) msg.obj;


            }
        });



        init();

    }

    @Override
    public void onStart () {
        super.onStart();
        ttsConnect.register(this);
    }


    private  void init(){

        manager=getSupportFragmentManager();
        pathBean=new PathBean();
    }

    public String getTelltag() {
        return telltag;
    }

    public void setTelltag(String telltag) {
        this.telltag = telltag;
    }


    public void setData(String tag){

        String hh="";




        if(AppContents.getUDiskPath()!=null){
            if(AppContents.getUDiskPath().equals("?")){
                hh=Environment.getExternalStorageDirectory().toString();
            }else {
                String creapath=AppContents.getUDiskPath()+"/testfile/";
                File file=new File(creapath);
                if(file.exists()){
                    hh=AppContents.getUDiskPath();
                    Log.e("=======","33333333");
                }else {

                    if(file.mkdirs()){
                        hh=AppContents.getUDiskPath();
                        Log.e("=======","11111111");
                    }else {
                        hh=Environment.getExternalStorageDirectory().toString();
                        Log.e("=======","22222222");
                    }
                }
            }
        }else {
            hh= Environment.getExternalStorageDirectory().toString();
        }



        basePath=hh+"/comhuiben/";
        //  basePath="mnt/sdcard/comhuiben/";
        Log.e("==="+AppContents.getUDiskPath()+"====",hh);
        pathBean.setBasePath(basePath);
        pathBean.setImagepath(basePath+"/image/");
        pathBean.setVideoPath(basePath+"/video/");
        pathBean.setVuforPath(basePath+"/vuforia/");
        pathBean.setDataName(tag+".dat");
        pathBean.setXmlName(tag+".xml");
    }
    public void setConfig(int otid,int vurid,String appkey){
        otherId=otid;
        vuforsLayoutid=vurid;
        myappkey=appkey;
        if(dataSaveUtils==null) {
            dataSaveUtils = new DataSaveUtils(this);

        }
        String mac= AppContents.getMacStr(this).toUpperCase().replace(":","");

        if(mac==null){
            MyApplication.getInstance().showToast("获取机器唯一标识失败，请打开一次网络");
            return;
        }
        if(dataSaveUtils.getIsAc().equals(MD5Uutils.MD5(myappkey+mac))){
            JihuoUtils.postJihuo(myappkey,this,key,dataSaveUtils);
            return;
        }

    }

    //初始化识别vrfragment
    public void initVuforia(){
        if(pathBean.getBasePath()==null){
            ShowOtherDia showOtherDia=new ShowOtherDia(this,R.style.NobackDialog,"请设置资源路径");
            showOtherDia.show();
            return;
        }

        if(vuforsLayoutid==0){
            ShowOtherDia showOtherDia=new ShowOtherDia(this,R.style.NobackDialog,"请设置父布局id");
            showOtherDia.show();
            return;
        }
        FragmentTransaction transaction= manager.beginTransaction();
        vfrag=new MyVuforiaFragment();

        if(telltag!=null) {
            if (!telltag.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("start", telltag);
                vfrag.setArguments(bundle);
                Log.e("========", telltag);
            } else {
                Log.e("========", "没指令");
            }
        }
        transaction.add(vuforsLayoutid,vfrag,"vrfragment");
        transaction.commit();
    }

    //初始化列表索引fragment
    public void initFragment1() {

        if(listFrag==null) {
            listFrag = new BookListFragment();
        }
        FragmentTransaction transaction= manager.beginTransaction();
        if(!listFrag.isAdded()){
            transaction.add(otherId,listFrag,"lisyfragment");
        }
        transaction.hide(listFrag);
        transaction.commit();
    }

    //初始化视首次indexFragment和listfrag
    public void initindexFrag(){

        if(indexFragment==null){
            indexFragment = new IndexFragment();
            manager.beginTransaction().add(otherId,indexFragment,"indexFragment").commit();
        }else {
            if(indexFragment.isAdded()){
                manager.beginTransaction().show(indexFragment).commit();
            }else {
                manager.beginTransaction().add(otherId,indexFragment,"indexFragment");
            }
        }
        manager.beginTransaction().show(indexFragment).commit();
        initFragment1();
        initvideFrag1();
    }


    public void showIndex(){
        if(indexFragment!=null){
            manager.beginTransaction().show(indexFragment).commit();
        }
        if(listFrag!=null){
            manager.beginTransaction().hide(listFrag).commit();
        }
        if(fFrag!=null){
            manager.beginTransaction().hide(fFrag).commit();
        }
    }


    //初始化视频播放frag
    public void initvideFrag1(){
        if(fFrag==null){
            fFrag = new VideoFragment();
            manager.beginTransaction().add(otherId,fFrag,"flashfragment").commit();
            manager.beginTransaction().hide(fFrag).commit();
        }
    }


    //显示视频播放frag
    public void showvideFrag(){
        if(fFrag==null){
            fFrag = new VideoFragment();
            manager.beginTransaction().add(otherId,fFrag,"flashfragment").commit();
        }else {
            if(fFrag.isAdded()){
                manager.beginTransaction().show(fFrag).commit();
            }else {
                manager.beginTransaction().add(otherId,fFrag,"flashfragment");
            }
        }
        manager.beginTransaction().hide(indexFragment).commit();
    }


    //激活码扫码fragment
    public void initZxingFragment(){
        if(pathBean==null){
            ShowOtherDia showOtherDia=new ShowOtherDia(this,R.style.NobackDialog,"请设置资源路径");
            showOtherDia.show();
            return;
        }
        if(otherId==0){
            ShowOtherDia showOtherDia=new ShowOtherDia(this,R.style.NobackDialog,"请设置父布局id");
            showOtherDia.show();
            return;
        }
        zxingFrag=new ZxingFragment();
        FragmentTransaction transaction= manager.beginTransaction();
        transaction.add(otherId,zxingFrag,"zxingfrag");
        transaction.show(zxingFrag);
        transaction.commit();
    }



    //切换索引
    public void addsuoyin(int type){
        fFrag.hback=type;
        listFrag.backType=type;
        manager.beginTransaction().show(listFrag).commit();
        manager.beginTransaction().hide(fFrag).commit();
    }
    //索引返回
    public void fhSuoyin(){
        fFrag.hback=1;
        manager.beginTransaction().hide(listFrag).commit();
        manager.beginTransaction().show(indexFragment).commit();
    }

    //索引返回
    public void fhSuoyin2(){
        fFrag.hback=1;
        manager.beginTransaction().hide(listFrag).commit();
        manager.beginTransaction().show(fFrag).commit();
    }

    //索引播放
    public void playVideo(int type,String video, DelFileLinstener delFileLinstener){
        manager.beginTransaction().hide(listFrag).commit();
        manager.beginTransaction().show(fFrag).commit();
        vfrag.headUp();
        fFrag.playVideo(video,delFileLinstener);
        fFrag.hback=type;
    }


    private ImageFragment ivFrag;
    //切换帮助
    public void addImageFrag(){
        if(ivFrag==null) {
            ivFrag=new ImageFragment();
        }
        if(ivFrag.isAdded()) {
            manager.beginTransaction().show(ivFrag).commit();
        }else {
            manager.beginTransaction().add(otherId, ivFrag).commit();
        }
    }

    //帮助返回
    public void fhHelp(){
        getSupportFragmentManager().beginTransaction().hide(ivFrag).commit();
    }


    //显示VieoFragment,隐藏其他
    public void ShowVideoFrag(){
        if(listFrag!=null) {
            manager.beginTransaction().hide(listFrag).commit();
        }
        manager.beginTransaction().show(fFrag).commit();
    }


    //显示VieoFragment,隐藏其他
    public void ShowVideoFrag2(){
        if(listFrag!=null) {
            listFrag.backType=2;
            fFrag.hback=1;
            manager.beginTransaction().hide(listFrag).commit();
        }
        manager.beginTransaction().show(fFrag).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(ttsConnect!=null) {
            ttsConnect.finishTTSListener();
            ttsConnect.unregister();
        }
        System.exit(0);
    }




    //验证激活���
    public void postJihuo(final TextView loadTv){
        Map<String,String> params=new HashMap<>();
        String mac= AppContents.getMacStr(this).toUpperCase().replace(":","");
        params.put("deviceid",mac );//E0:76:D0:95:A4:45

        params.put("appkey",myappkey);

        XutilsHelper.post(AppContents.LIB_JIHUO_URL, params, new MyCallBack() {
            @Override
            public void onSuccess(String s) {
                Log.e("=====",s);
                try {
                    JSONObject object=new JSONObject(s);
                    if(object!=null) {
                        String code = object.getString("code");
                        if (code.equals("1")) {
                            JSONArray array=object.getJSONArray("data");

                            if(array.length()!=0) {
                                JSONObject jsonObject2 = array.getJSONObject(0);
                                String resl = jsonObject2.getString("result");

                                if(resl!=null) {
                                    String rel="";
                                    try {
                                        rel= RSAUtil.getInstance().decrypt(resl,key);
                                        Log.e("r",rel);
                                    } catch (Exception e) {
                                        Log.e("e--------",e.toString());
                                        e.printStackTrace();
                                    }
                                    if(!rel.equals("")) {
                                        if (rel.equals("ok")) {
                                            String mac= AppContents.getMacStr(ComBaseActivity.this).toUpperCase().replace(":","");
                                            dataSaveUtils.saveisAc(MD5Uutils.MD5(myappkey+mac));
                                            //检测识别库是否存在
                                            loadTv.setText("正在检查更新...");
                                            //检测识别库是否存在
                                            if (AppContents.isExit(pathBean.getVuforPath() + pathBean.getXmlName()) && AppContents.isExit(pathBean.getVuforPath() + pathBean.getDataName())) {
                                                // initFragment();
                                                loadTv.setText("正在检查更新...");
                                                if (loadDateLinstener != null) {
                                                    loadDateLinstener.loadType(1);
                                                } else {
                                                    ShowOtherDia showOtherDia = new ShowOtherDia(ComBaseActivity.this, R.style.NobackDialog, "应用发生异常，请联系客服");
                                                    showOtherDia.show();
                                                    dialogFinish(showOtherDia);
                                                }
                                            } else {
                                                if (loadDateLinstener != null) {
                                                    loadDateLinstener.loadType(2);
                                                } else {
                                                    ShowOtherDia showOtherDia = new ShowOtherDia(ComBaseActivity.this, R.style.NobackDialog, "应用发生异常，请联系客服");
                                                    showOtherDia.show();
                                                    dialogFinish(showOtherDia);
                                                }
                                            }
                                        } else {
                                            ShowOtherDia showOtherDia = new ShowOtherDia(ComBaseActivity.this, R.style.NobackDialog, "应用未激活");
                                            showOtherDia.show();
                                            dialogFinish(showOtherDia);
                                            dataSaveUtils.delIsActivi();
                                            // initSaoActivity(loadLayout,loadTv);
                                        }
                                    }else {
                                        ShowOtherDia showOtherDia=new ShowOtherDia(ComBaseActivity.this,R.style.NobackDialog,"数据解密异常");
                                        showOtherDia.show();
                                        dialogFinish(showOtherDia);
                                    }



                                }else {
                                    ShowOtherDia showOtherDia=new ShowOtherDia(ComBaseActivity.this,R.style.NobackDialog,"数据解析异常");
                                    showOtherDia.show();
                                    dialogFinish(showOtherDia);
                                }
                            }else {
                                ShowOtherDia showOtherDia=new ShowOtherDia(ComBaseActivity.this,R.style.NobackDialog,"数据解析异常");
                                showOtherDia.show();
                                dialogFinish(showOtherDia);
                            }
                        } else {
                            dataSaveUtils.delIsActivi();
                            ShowOtherDia showOtherDia=new ShowOtherDia(ComBaseActivity.this,R.style.NobackDialog,object.getString("msg"));
                            showOtherDia.show();
                            dialogFinish(showOtherDia);
                            //    initSaoActivity(loadLayout,loadTv);
                        }

                    }else {
                        ShowOtherDia showOtherDia=new ShowOtherDia(ComBaseActivity.this,R.style.NobackDialog,"数据解析失败");
                        showOtherDia.show();
                        dialogFinish(showOtherDia);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ShowOtherDia showOtherDia=new ShowOtherDia(ComBaseActivity.this,R.style.NobackDialog,"数据解析失败");
                    showOtherDia.show();
                    dialogFinish(showOtherDia);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Log.e("---------",throwable.toString());
              /*  ShowOtherDia showOtherDia=new ShowOtherDia(ComBaseActivity.this,R.style.NobackDialog,"无法连接服务器，请检查网络或稍后重试");
                showOtherDia.show();
                dialogFinish(showOtherDia);*/
                ShowMesgDialog.show(ComBaseActivity.this, "无法连接服务器，请检查网络或稍后重试", 1);
            }
        });
    }
    private void dialogFinish(Dialog dialog){
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }

    //激活启动扫码
    private void initSaoActivity(final RelativeLayout loadLayout, final TextView loadTv){
        //设置扫码回调
        setZxingResultLinster(new ZxingResultLinster() {
            @Override
            public void zxingResultLinster(String result) {
                // MyApplication.getInstance().showToast(result);
                loadLayout.setVisibility(View.VISIBLE);
                String mac= AppContents.getMacStr(ComBaseActivity.this).toUpperCase().replace(":","");
                dataSaveUtils.saveisAc(MD5Uutils.MD5(myappkey+mac));
                if (AppContents.isExit(pathBean.getVuforPath() + pathBean.getXmlName()) && AppContents.isExit(pathBean.getVuforPath() + pathBean.getDataName())) {
                    // initFragment();
                    loadTv.setText("正在检查更新...");
                    loadDateLinstener.loadType(1);

                } else {
                    if (AppContents.isNetworkAvailable(ComBaseActivity.this)) {
                        loadDateLinstener.loadType(2);
                    } else {
                        ShowMesgDialog.show(ComBaseActivity.this, "暂无可用网络", 1);
                    }
                }
                if(zxingFrag!=null){
                    manager.beginTransaction().hide(zxingFrag).commit();
                    zxingFrag.onStop();
                }
            }
        });

        initZxingFragment();
        loadLayout.setVisibility(View.GONE);
    }


    public LoadDateLinstener getLoadDateLinstener() {
        return loadDateLinstener;
    }

    public void setLoadDateLinstener(LoadDateLinstener loadDateLinstener) {
        this.loadDateLinstener = loadDateLinstener;
    }

    public ZxingResultLinster getZxingResultLinster() {
        return zxingResultLinster;
    }

    public void setZxingResultLinster(ZxingResultLinster zxingResultLinster) {
        this.zxingResultLinster = zxingResultLinster;
    }


    public PathBean getPathBean() {
        return pathBean;
    }

    public void setPathBean(PathBean pathBean) {
        this.pathBean = pathBean;
    }
    public List<LisBean> getLisBeens() {
        return lisBeens;
    }

    public void setLisBeens(List<LisBean> lisBeens) {
        this.lisBeens = lisBeens;
        fFrag.setVideoDate(pathBean,lisBeens);
    }

    public List<VrlistBean> getVrlistBeens() {
        return vrlistBeens;
    }

    public void setVrlistBeens(List<VrlistBean> vrlistBeens) {
        this.vrlistBeens = vrlistBeens;
    }


    public abstract void dimssLoad();
    public abstract void showLoad();

    public DataSaveUtils getDataSaveUtils() {
        return dataSaveUtils;
    }

    public void setDataSaveUtils(DataSaveUtils dataSaveUtils) {
        this.dataSaveUtils = dataSaveUtils;
    }
}
