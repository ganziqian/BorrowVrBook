package com.zwg.huibenlib.ui.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qualcomm.vuforia.CameraDevice;
import com.qualcomm.vuforia.DataSet;
import com.qualcomm.vuforia.ObjectTracker;
import com.qualcomm.vuforia.STORAGE_TYPE;
import com.qualcomm.vuforia.State;
import com.qualcomm.vuforia.Trackable;
import com.qualcomm.vuforia.Tracker;
import com.qualcomm.vuforia.TrackerManager;
import com.qualcomm.vuforia.Vuforia;
import com.shuanghua.utils.tools.TimerUtils;
import com.wsh.aidl.ActionCMDAidlConnect;
import com.zwg.huibenlib.ComBaseActivity;
import com.zwg.huibenlib.ComImageTargetRenderer;
import com.zwg.huibenlib.MyApplication;
import com.zwg.huibenlib.R;
import com.zwg.huibenlib.SampleApplication.SampleApplicationControl;
import com.zwg.huibenlib.SampleApplication.SampleApplicationException;
import com.zwg.huibenlib.SampleApplication.SampleApplicationSession;
import com.zwg.huibenlib.Utils.OtherReceiverHelper;
import com.zwg.huibenlib.Utils.SampleApplicationGLView;
import com.zwg.huibenlib.Utils.Texture;
import com.zwg.huibenlib.Utils.dialog.ChongTryDialog;
import com.zwg.huibenlib.Utils.dialog.ShowOtherDia;
import com.zwg.huibenlib.inteface.LoadFinishLintenter;
import com.zwg.huibenlib.inteface.StopCallBackLinstener;
import com.zwg.huibenlib.inteface.TrySelectLinstener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class ComVrFragment extends Fragment implements SampleApplicationControl ,LoadFinishLintenter{
    private static final String LOGTAG = "brrorbook";

    private DataSet mCurrentDataset;
    private ArrayList<String> mDatasetStrings = new ArrayList<String>();  //装载识别库列表


    private int selectIndex=0;   //选择下标加载哪个识别库

    public boolean isCanTel=true;

    private boolean mExtendedTracking = false;
    // The textures we will use for rendering:
    private Vector<Texture> mTextures;


    SampleApplicationSession vuforiaAppSession;

   // private String imgeTargetsinfo ;

    private TimerUtils timeUtilsAutoFocus;
    private RelativeLayout mUILayout;
    public ComImageTargetRenderer mRenderer;

    private AlertDialog mErrorDialog;
    // Our OpenGL view:
    private SampleApplicationGLView mGlView;

    boolean mIsDroidDevice = false;
    private boolean mSwitchDatasetAsap = false;
    private int isHas=1;//是否初始化完成标记

    private ActionCMDAidlConnect actionCMDAidlConnect;

    public int isflashtime=0;

    private int isoneee=1;
    private OtherReceiverHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       View  view = inflater.inflate(R.layout.fragment_com_vr, container, false);
        mUILayout = (RelativeLayout) view.findViewById(R.id.camera_overlay_layout);
       // imgeTargetsinfo=mDatasetStrings.get(0);
        init();
        return view;
    }

    private void init(){
      //  mDatasetStrings.add(imgeTargetsinfo);
        mTextures = new Vector<Texture>();
        loadTextures();
        mIsDroidDevice = Build.MODEL.toLowerCase().startsWith(
                "droid");
        Log.d(LOGTAG, "init555");

         helper=new OtherReceiverHelper(getActivity(),new StopCallBackLinstener(){
            @Override
            public void myCallBack(int type) {
                if(type==1){
                    if(vuforiaAppSession!=null) {
                        vuforiaAppSession.stopCamera();
                    }
                }else if(type==2){



                    if (vuforiaAppSession!=null){
                        try
                        {
                            vuforiaAppSession.startAR(CameraDevice.CAMERA.CAMERA_FRONT);
                        } catch (SampleApplicationException e2)
                        {
                            Log.e(LOGTAG, e2.getString());
                            setOndimisDia("摄像头调用失败，请检查摄像头是否可用");
                            //showInitializationErrorMessage("摄像头调用失败，请检查摄像头是否可用");
                            return;
                        }
                    }

                }
            }
        });
        helper.init1();
        helper.register();



    }


    public Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if(isHas==1){
                        Log.e("-------->","初始化未完成");
                        return;
                    }
                    headUp();
                    String result=(String) msg.obj;//vu识别文件名

                    sendimageData(result);

                  /*  ImagenameBean imagenameBean= ImageFilenameUtils.getTag(result);

                    if(imagenameBean==null){
                        MyApplication.getInstance().showToast("图片命名格式错误");
                        return;
                    }
*/
                    break;
                case 40:
                case 41:
                    if (msg.arg1 == 1) {
                        //actionCMDAidlConnect.controlCMDwithValue("1",75);
                        headDown();
                        //  headNormal();
                    }
                case 46:


                    break;
                case 63:
                    isCanTel=false;
                    ((ComBaseActivity)getActivity()).ttsConnect.finishTTSListener();

                    ChongTryDialog chongTryDialog=new ChongTryDialog(getActivity(), new TrySelectLinstener() {
                        @Override
                        public void selectType(int type) {
                            if(type==1){
                                ((ComBaseActivity) getActivity()).showIndex();
                                ((ComBaseActivity) getActivity()).setData("index");



                                try {
                                    File file=new File(mDatasetStrings.get(0));
                                    if(file.exists()){
                                        file.delete();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }


                                String lls=mDatasetStrings.get(0).replace(((ComBaseActivity) getActivity()).getPathBean().getVuforPath(),"").replace(".xml","");
                                sendimageData("index_"+lls+"_-1");



                            }else {
                                ((ComBaseActivity) getActivity()).showIndex();
                                ((ComBaseActivity) getActivity()).setData("index");
                                sendimageData("index_index_01");
                            }

                        }
                    });

                    chongTryDialog.show();
                    chongTryDialog.setBtnText("    删 除    ");
                    chongTryDialog.setTitle("文件可能已经损坏\n是否删除该文件");



                    break;
            }


        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(LOGTAG, "onCreate");
        super.onCreate(savedInstanceState);
        timeUtilsAutoFocus = new TimerUtils();
        timeUtilsAutoFocus.setNTimeOutDetect(false);
        timeUtilsAutoFocus.setTimeOutListener(new TimerUtils.TimeOutListener() {
            @Override
            public void onTimeOut() {
            }
            @Override
            public void onTimeBeat() {

                boolean result = CameraDevice.getInstance().setFocusMode(
                        CameraDevice.FOCUS_MODE.FOCUS_MODE_TRIGGERAUTO);
                if (result) {
                    // 对焦成功,下次不继续对焦了
                    Log.v(LOGTAG, "enable trigger autofocus");
                    timeUtilsAutoFocus.stopSend();
                } else
                    Log.v(LOGTAG, "Unable to enable trigger autofocus");
            }
        });
    }



    @Override
    public void onStart () {
        super.onStart();
        vuforiaAppSession = new SampleApplicationSession(this);
        if (mUILayout != null) {
            mUILayout.removeAllViews();
        }
        AppCompatActivity activity2= (AppCompatActivity) getActivity();
        vuforiaAppSession.initAR(activity2, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initAutoFocusTimer();

    }


    @Override
    public void onStop () {
        super.onStop();

        try {
            vuforiaAppSession.stopAR();
        } catch (SampleApplicationException e) {
            e.printStackTrace();
            // Log.e(LOGTAG, e);
        }

        releaseAutoFocusTimer();
    }



    // Called when the activity will start interacting with the user.
    @Override
    public void onResume()
    {
        Log.d(LOGTAG, "onResume");
        super.onResume();
        try
        {
            vuforiaAppSession.resumeAR();
        } catch (SampleApplicationException e)
        {
            Log.e(LOGTAG, e.getString());
        }
        Log.d(LOGTAG, "onResume111111111");
        // Resume the GL view:
        if (mGlView != null)
        {
            mGlView.setVisibility(View.VISIBLE);
            mGlView.onResume();
        }
    }


    // Called when the system is about to start resuming a previous activity.
    @Override
    public void onPause()
    {
        Log.d(LOGTAG, "onPause");
        super.onPause();

        if (mGlView != null)
        {
            mGlView.setVisibility(View.INVISIBLE);
            mGlView.onPause();
        }

        try
        {
            vuforiaAppSession.pauseAR();
        } catch (SampleApplicationException e)
        {
            Log.e(LOGTAG, e.getString());
        }
        //取消注册
        if(isHas==2) {
            try {
                actionCMDAidlConnect.unregister();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    // The final call you receive before your activity is destroyed.
    @Override
    public void onDestroy()
    {
        Log.d(LOGTAG, "onDestroy");
        super.onDestroy();
        // Unload texture:
        helper.unregister();
        mTextures.clear();
        mTextures = null;
        // System.gc();
    }










    private void loadTextures()
    {
        mTextures.add(Texture.loadTextureFromApk("TextureTeapotBrass.png",
                getActivity().getAssets()));
        mTextures.add(Texture.loadTextureFromApk("TextureTeapotBlue.png",
                getActivity().getAssets()));
        mTextures.add(Texture.loadTextureFromApk("TextureTeapotRed.png",
                getActivity().getAssets()));
        mTextures.add(Texture.loadTextureFromApk("ImageTargets/Buildings.jpeg",
                getActivity(). getAssets()));
    }



    //初始化跟踪
    @Override
    public boolean doInitTrackers() {
        // Indicate if the trackers were initialized correctly
        boolean result = true;
        TrackerManager tManager = TrackerManager.getInstance();
        Tracker tracker;
        // Trying to initialize the image tracker
        tracker = tManager.initTracker(ObjectTracker.getClassType());
        if (tracker == null)
        {
            Log.e(LOGTAG,
                    "Tracker not initialized. Tracker already initialized or the camera is already started");
            result = false;
        } else
        {
            Log.i(LOGTAG, "Tracker successfully initialized");
        }
        return result;
    }

    //加载和销毁跟踪数据的方法。
    @Override
    public boolean doLoadTrackersData() {
        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager
                .getTracker(ObjectTracker.getClassType());
        if (objectTracker == null)
            return false;
        if (mCurrentDataset == null)
            mCurrentDataset = objectTracker.createDataSet();

        if (mCurrentDataset == null)
            return false;


            if (!mCurrentDataset.load(mDatasetStrings.get(selectIndex),
                    STORAGE_TYPE.STORAGE_ABSOLUTE)) {

                Log.e("=========,,,",""+isoneee);
                if(isoneee!=1) {
                    handler.sendEmptyMessage(63);
                }
                //showInitializationErrorMessage("识别库加载失败");
                return false;


        /*    if (!mCurrentDataset.load(imgeTargetsinfo, STORAGE_TYPE.STORAGE_APPRESOURCE))
                return false;*/
            //Toast.makeText(getActivity(), "找不到目标数据库", Toast.LENGTH_SHORT).show(); //调用时会出错， Can't create handler inside thread that has not called Looper.prepare()
        }

        if (!objectTracker.activateDataSet(mCurrentDataset))
            return false;

        int numTrackables = mCurrentDataset.getNumTrackables();
        for (int count = 0; count < numTrackables; count++)
        {
            Trackable trackable = mCurrentDataset.getTrackable(count);
            if(isExtendedTrackingActive())
            {
                trackable.startExtendedTracking();
            }
            String name = "Current Dataset : " + trackable.getName();
            trackable.setUserData(name);
           /* Log.e(LOGTAG, "UserData:Set the following user data "
                    + (String) trackable.getUserData());*/
        }
        return true;
    }

    boolean isExtendedTrackingActive()
    {
        return mExtendedTracking;
    }


   //开始追踪
    @Override
    public boolean doStartTrackers() {
        // Indicate if the trackers were started correctly
        boolean result = true;

        Tracker objectTracker = TrackerManager.getInstance().getTracker(
                ObjectTracker.getClassType());
        if (objectTracker != null)
            objectTracker.start();

        return result;
    }

    //停止追踪
    @Override
    public boolean doStopTrackers() {
        // Indicate if the trackers were stopped correctly
        boolean result = true;

        Tracker objectTracker = TrackerManager.getInstance().getTracker(
                ObjectTracker.getClassType());
        if (objectTracker != null)
            objectTracker.stop();
        return result;
    }

    @Override
    public boolean doUnloadTrackersData() {
        // Indicate if the trackers were unloaded correctly
        boolean result = true;
        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager
                .getTracker(ObjectTracker.getClassType());
        if (objectTracker == null)
            return false;

        if (mCurrentDataset != null && mCurrentDataset.isActive())
        {
            if (objectTracker.getActiveDataSet().equals(mCurrentDataset)
                    && !objectTracker.deactivateDataSet(mCurrentDataset))
            {
                result = false;
            } else if (!objectTracker.destroyDataSet(mCurrentDataset))
            {
                result = false;
            }

            mCurrentDataset = null;
        }
        return result;
    }

    @Override
    public boolean doDeinitTrackers() {
        // Indicate if the trackers were deinitialized correctly
        boolean result = true;
        TrackerManager tManager = TrackerManager.getInstance();
        tManager.deinitTracker(ObjectTracker.getClassType());

        return result;
    }

    @Override
    public void onInitARDone(SampleApplicationException e) {
        Log.d(LOGTAG, "onInitARDone");
        if (e == null)
        {
            Log.d(LOGTAG, "exception == null");
            initApplicationAR();
            mRenderer.mIsActive = true;

            // Now add the GL surface view. It is important
            // that the OpenGL ES surface view gets added
            // BEFORE the camera is started and video
            // background is configured.
            /*getActivity().addContentView(mGlView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));*/
            mUILayout.addView(mGlView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            // Sets the UILayout to be drawn in front of the camera
            mUILayout.bringToFront();

            // Sets the layout background to transparent
            mUILayout.setBackgroundColor(Color.TRANSPARENT);
            Log.d(LOGTAG, "vuforiaAppSession.startAR");
            try
            {
                vuforiaAppSession.startAR(CameraDevice.CAMERA.CAMERA_FRONT);
            } catch (SampleApplicationException e2)
            {
                Log.e(LOGTAG, e2.getString());

                setOndimisDia("摄像头调用失败，请检查摄像头是否可用");
                //showInitializationErrorMessage("摄像头调用失败，请检查摄像头是否可用");
                return;
            }
            boolean result = CameraDevice.getInstance().setFocusMode(
                    CameraDevice.FOCUS_MODE.FOCUS_MODE_CONTINUOUSAUTO);

            if (result) {
            } else
                //showInitializationErrorMessage("无法启用连续自动对焦");
                Log.e(LOGTAG, "Unable to enable continuous autofocus");

        } else
        {
            Log.e(LOGTAG, e.getString());

            if(e.toString().contains("Failed to load tracker data")){
                setOndimisDia("文件可能已经损坏\n文件将自动删除\n请重新打开应用");
               // showInitializationErrorMessage("识别文件加载失败");
            }else {
                showInitializationErrorMessage(e.getString());
            }

            return;
        }
        isoneee=2;
        registerHardware();


        actionCMDAidlConnect.register(getActivity().getPackageName(), 1);

        headDown();

        isHas=2;
        finishLoad();


    }


    private void setOndimisDia(String str){

        if(str.contains("文件可能已经损坏")){

            ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,str);
            showOtherDia.show();

            showOtherDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                    try {
                        File file=new File(mDatasetStrings.get(0));
                        if(file.exists()){
                            file.delete();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    getActivity().finish();
                }
            });


        }else {
            ShowOtherDia showOtherDia=new ShowOtherDia(getActivity(),R.style.NobackDialog,str);
            showOtherDia.show();

            showOtherDia.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    getActivity().finish();
                }
            });
        }



    }


    @Override
    public void onQCARUpdate(State state) {
        if (mSwitchDatasetAsap)
        {
            mSwitchDatasetAsap = false;
            TrackerManager tm = TrackerManager.getInstance();
            ObjectTracker ot = (ObjectTracker) tm.getTracker(ObjectTracker
                    .getClassType());
            if (ot == null || mCurrentDataset == null
                    || ot.getActiveDataSet() == null)
            {
                Log.d(LOGTAG, "Failed to swap datasets");
                return;
            }

            doUnloadTrackersData();
            doLoadTrackersData();
        }
    }








    // Initializes AR application components.
    private void initApplicationAR() {
        // Create OpenGL ES view:
        int depthSize = 16;
        int stencilSize = 0;
        boolean translucent = Vuforia.requiresAlpha();
        mGlView = new SampleApplicationGLView(getActivity());
        mGlView.init(translucent, depthSize, stencilSize);
        Log.d(LOGTAG,"initApplication");
        mRenderer = new ComImageTargetRenderer(this, vuforiaAppSession);
        mRenderer.setTextures(mTextures);
        mGlView.setRenderer(mRenderer);

    }




























    public void releaseAutoFocusTimer () {
        if (timeUtilsAutoFocus.timerNum > 0) {
            timeUtilsAutoFocus.stopSend();
        }
    }
    public void initAutoFocusTimer () {
        timeUtilsAutoFocus.sendWithTime(0, 1000);
    }


    private void registerHardware () {
        actionCMDAidlConnect = new ActionCMDAidlConnect();
        actionCMDAidlConnect.setActionListener(new ActionCMDAidlConnect.ActionListener() {

            @Override
            public void receive(String result) {
                try {
                    JSONObject j_root = new JSONObject(result);
                    int id = j_root.getInt("id");
                    String info = j_root.getString("info");
                    Log.i("cccc", "info is " + info);
                    if (com.shuanghua.utils.tools.SystemUtils.UnitUtils.isInteger(info)) {
                        int state = Integer.parseInt(info);
                        Message message = handler.obtainMessage();
                        message.what = id;
                        message.arg1 = state;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void output(String output) {

            }
        });
        actionCMDAidlConnect.init(getActivity());
        //actionCMDAidlConnect.register(getActivity().getPackageName(), 1); //放到onResume中

    }




    // Shows initialization error messages as System dialogs
    public void showInitializationErrorMessage(String message)
    {
        final String errorMessage = message;
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (mErrorDialog != null) {
                    mErrorDialog.dismiss();
                }
                // Generates an Alert Dialog to show the error message
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder
                        .setMessage(errorMessage)
                        .setTitle(getString(R.string.INIT_ERROR))
                        .setCancelable(false)
                        .setIcon(0)
                        .setPositiveButton(getString(R.string.button_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        getActivity().finish();
                                    }
                                });
                mErrorDialog = builder.create();
                mErrorDialog.show();
            }
        });
    }

    public void headUp(){
        actionCMDAidlConnect.controlCMDwithValue("1",102);
    }
    public void headDown() {
        actionCMDAidlConnect.controlCMDwithValue("1",75);
    }
    public void headNormal1() {
        actionCMDAidlConnect.controlCMDwithValue("1",90);
    }

    @Override
    public abstract void sendimageData(String result);
    public abstract void finishLoad();

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public ArrayList<String> getmDatasetStrings() {
        return mDatasetStrings;
    }

    public void setmDatasetStrings(ArrayList<String> mDatasetStrings) {
        this.mDatasetStrings = mDatasetStrings;
    }

}
