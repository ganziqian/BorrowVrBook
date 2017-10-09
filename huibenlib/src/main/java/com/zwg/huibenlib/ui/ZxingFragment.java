package com.zwg.huibenlib.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureHandler;
import com.zwg.huibenlib.R;
import com.zwg.huibenlib.Utils.dialog.ShowJiazaiDia;
import com.zwg.huibenlib.Utils.dialog.ShowOtherDia;
import com.zwg.huibenlib.http.AppContents;
import com.zwg.huibenlib.http.MyCallBack;
import com.zwg.huibenlib.http.XutilsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * s扫码窗口
 * A simple {@link Fragment} subclass.
 */
public class ZxingFragment extends Fragment implements SurfaceHolder.Callback{

    private final static String TAG="FindFriendContentView";
    private RelativeLayout mContainer = null;
    private LinearLayout mCropLayout = null;
    private SurfaceView mSurfaceView=null;
    private SurfaceHolder mSurfaceHolder;
    private CameraManager mCameraManager;
     private int jj=0;

    public ZxingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_zxing, container, false);
        initUI(v);
        initConfig();

        return v;
    }
    private void initUI(View v) {
        Log.v(TAG, "initUI");
        mContainer = (RelativeLayout)v.findViewById(R.id.capture_containter);
        mCropLayout = (LinearLayout)v.findViewById(R.id.capture_crop_layout);
        mSurfaceView = (SurfaceView)v.findViewById(R.id.capture_preview);

        ImageView ivfh= (ImageView) v.findViewById(R.id.back_jihuo_iv);
        ivfh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   getActivity().finish();
            }
        });

        mSurfaceHolder=mSurfaceView.getHolder();
        ImageView mQrLineView = (ImageView)v.findViewById(R.id.capture_scan_line);
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        mQrLineView.startAnimation(animation);
    }

    private void initConfig() {
        Log.v(TAG, "initConfig");
        mCameraManager=new CameraManager(getActivity());
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        Log.v(TAG, "initCamera");
        int x;
        int y;
        int cropWidth;
        int cropHeight;
        try {
            mCameraManager.openDriver(surfaceHolder);
            Point point = mCameraManager.getCameraResolution();
            int width = point.y;
            int height = point.x;
            x = mCropLayout.getLeft() * width / mContainer.getWidth();
            y = mCropLayout.getTop() * height / mContainer.getHeight();
            cropWidth = mCropLayout.getWidth() * width / mContainer.getWidth();
            cropHeight = mCropLayout.getHeight() * height / mContainer.getHeight();
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        handler.begin(mCameraManager, x, y, cropWidth, cropHeight);
    }

    CaptureHandler handler =new CaptureHandler(new CaptureHandler.ResultCallback(){

        @Override
        public void onResult(String data) {
            handleDecode(data);
        }
    });

    public void handleDecode(String result) {
        Log.v(TAG, "handleDecode");
        handler.postAtTime(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(CaptureHandler.restart_preview);
            }
        },4000);

        if(jj==0){
            jj=1;
            jihuoyanzheng(result);
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
        Log.v(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(TAG, "surfaceCreated");
        initCamera(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.closeDriver();
        if(handler!=null){
            handler.quitSynchronously();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.v(TAG,"onStart");
        initConfig();
    }

    @Override
    public void onStop(){
        super.onStop();
        mCameraManager.closeDriver();
        if(handler!=null){
            handler.quitSynchronously();
        }
    }




    //验证激活���
    public void jihuoyanzheng(String codenum){
        Map<String,String> params=new HashMap<>();

        String mac= AppContents.getMacStr(getActivity()).toUpperCase().replace(":","");
        params.put("deviceid",mac );//E0:76:D0:95:A4:45
        Log.e("--mac----",mac);
        // params.put("app_autograph",SignUtils.getSingInfo(this));
        params.put("codenum", codenum);
       // params.put("appkey", ((BaseActivity)getActivity()).getMyappkey());
        params.put("remark","机器激活");
        params.put("order_comefrom","3");
        ShowJiazaiDia.show(getActivity());

        XutilsHelper.post(AppContents.JIHUO_URL, params, new MyCallBack() {
            @Override
            public void onSuccess(String s) {
                Log.e("=====",s);
                ShowJiazaiDia.dimiss();
                try {
                    JSONObject object=new JSONObject(s);
                    if(object!=null) {
                        String code = object.getString("code");
                        if (code.equals("1")) {

                            JSONArray array=object.getJSONArray("data");

                            if(array.length()!=0){
                                JSONObject jsonObject2=array.getJSONObject(0);
                                String resl=jsonObject2.getString("result");


                                Log.e("rel--------",resl);
                                if(resl!=null){

                                    String rel="";
                                    try {
                                        //rel= RSAUtil.getInstance().decrypt(resl,BaseActivity.key);
                                        Log.e("r",rel);
                                    } catch (Exception e) {
                                        Log.e("e--------",e.toString());
                                        e.printStackTrace();
                                    }
                                    if(!rel.equals("")) {
                                        if (rel.equals("ok")) {
                                          //  ((BaseActivity) getActivity()).getZxingResultLinster().zxingResultLinster("ok");
                                        } else {
                                            ShowOtherDia showOtherDia = new ShowOtherDia(getActivity(), R.style.NobackDialog, "激活失败");
                                            showOtherDia.show();
                                            dialogDiss(showOtherDia);
                                        }
                                    }else {
                                        ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,"数据解密异常");
                                        showOtherDia.show();
                                        dialogDiss(showOtherDia);
                                    }
                                }else {
                                    ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,"获取结果为空");
                                    showOtherDia.show();
                                    dialogDiss(showOtherDia);
                                }

                            }else {
                                ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,"获取数据异常");
                                showOtherDia.show();
                                dialogDiss(showOtherDia);
                            }

                        } else {
                            ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,object.getString("msg"));
                            showOtherDia.show();
                            dialogDiss(showOtherDia);
                        }

                    }else {
                        ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,"数据解析失败");
                        showOtherDia.show();
                        dialogDiss(showOtherDia);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,"数据解析失败");
                    showOtherDia.show();
                    dialogDiss(showOtherDia);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                ShowJiazaiDia.dimiss();
                Log.e("---------",throwable.toString());
                ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,"无法连接服务器，请检查网络或稍后重试");
                showOtherDia.show();
            }
        });
    }


    private void dialogDiss(Dialog dialog){
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                jj=0;
            }
        });
    }



}
