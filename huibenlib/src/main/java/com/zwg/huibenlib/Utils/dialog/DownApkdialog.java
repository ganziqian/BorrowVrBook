package com.zwg.huibenlib.Utils.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.zwg.huibenlib.R;
import com.zwg.huibenlib.inteface.TrySelectLinstener;

import java.util.Timer;
import java.util.TimerTask;


/**
 * �����ˣ�  ganziqian
 * ���ã�
 * ʱ�䣺2015/8/7
 */
public class DownApkdialog extends Dialog{

	private NumberProgressBar  bnp;
	public DownApkdialog(Context context) {
		super(context, R.style.NobackDialog);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apk_down_dialog_layout);

		init();
	}

	public  void init(){
		bnp = (NumberProgressBar)findViewById(R.id.numberbar1);

		bnp.incrementProgressBy(20);

	}






}
