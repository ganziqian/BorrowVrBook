package com.zwg.huibenlib.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zwg.huibenlib.ComBaseActivity;
import com.zwg.huibenlib.MyApplication;
import com.zwg.huibenlib.R;
import com.zwg.huibenlib.Utils.BeepManager;
import com.zwg.huibenlib.Utils.JumpingBeans;
import com.zwg.huibenlib.Utils.dialog.ShowMesgDialog;
import com.zwg.huibenlib.http.AppContents;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment implements View.OnClickListener{
    private TextView tv1,tv2;
    public long exitTime=0;//程序退出点击时间
    private JumpingBeans jumpingBeans2;
    private ComBaseActivity comBaseActivity;
    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_index, container, false);
        init(view);
        return view;
    }

    private void init(View view) {




        comBaseActivity= (ComBaseActivity) getActivity();
        tv1= (TextView) view.findViewById(R.id.index_tv1);
        tv2= (TextView) view.findViewById(R.id.index_tv2);
        ImageView fhIv= (ImageView) view.findViewById(R.id.back_index_iv);
        fhIv.setOnClickListener(this);


        jumpingBeans2 = JumpingBeans.with(tv1)
                .makeTextJump(0, tv1.getText().length())
                .setIsWave(true)
                .setLoopDuration(2000)
                .build();


        ImageView helpIv= (ImageView) view.findViewById(R.id.help_iv);
        ImageView suoyinIv= (ImageView) view.findViewById(R.id.suoyi_iv);
        helpIv.setOnClickListener(this);
        suoyinIv.setOnClickListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("=======","pus");
        jumpingBeans2.stopJumping();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("=======","re");

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id == R.id.back_index_iv) {
           exit();
        }else if(id==R.id.suoyi_iv){
            ((ComBaseActivity) getActivity()).addsuoyin(2);
        }else if(R.id.help_iv==id){
            ((ComBaseActivity)getActivity()).addImageFrag();
        }
    }

    public void setText(){
        tv1.setText("正在获取资源");
        tv2.setText("");
    }

    /**
     * 退出程序的方法。
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 3000) {
            MyApplication.getInstance().showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
            //  ((MainActivity)getActivity()).finish();
        }
    }

}
