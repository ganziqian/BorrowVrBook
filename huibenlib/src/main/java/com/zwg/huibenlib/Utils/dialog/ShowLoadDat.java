package com.zwg.huibenlib.Utils.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zwg.huibenlib.R;


/**
 */
public class ShowLoadDat extends Dialog{

    private   String title="";
    private TextView typeTv;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;

    public ShowLoadDat(Context context, String title) {
        super(context);
        this.title=title;
    }

    public ShowLoadDat(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_dat_layout);

        init();
    }

    public  void init(){

          /*  LinearLayout layout = (LinearLayout) findViewById(R.id.show_re_layout);
            layout.getBackground().setAlpha(120);*/

        typeTv= (TextView) findViewById(R.id.loaddat_type_tv);

        progressBar= (ProgressBar) findViewById(R.id.pb_progressbar);
        progressBar2= (ProgressBar) findViewById(R.id.jjj_pro);
        progressBar.setVisibility(View.GONE);

    }

    public void setType(String  typeStr){
        typeTv.setText(typeStr);
    }
    public void setProbar(int pro){
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.GONE);
        progressBar.setProgress(pro);
    }

    public void setProbarGone(){
        progressBar.setVisibility(View.GONE);
        progressBar2.setVisibility(View.VISIBLE);

    }

    public void dimssDia(){
        dismiss();
    }



}
