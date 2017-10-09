package com.zwg.huibenlib.Utils.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zwg.huibenlib.R;
import com.zwg.huibenlib.inteface.TrySelectLinstener;


/**
 * �����ˣ�  ganziqian
 * ���ã�
 * ʱ�䣺2015/8/7
 */
public class ChongTryDialog extends Dialog{
	private TrySelectLinstener trySelectLinstener;


	private TextView titleTv;
	private TextView qudingTv;
	public ChongTryDialog(Context context,  TrySelectLinstener trySelectLinstener) {
		super(context, R.style.NobackDialog);
		this.trySelectLinstener=trySelectLinstener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chong_try_dialog_layout);

		init();
	}

	public  void init(){


		TextView quxaioTv = (TextView) findViewById(R.id.quxiao_try_title_tv);
		titleTv = (TextView) findViewById(R.id.try_dia_tv);
		quxaioTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				trySelectLinstener.selectType(2);
				dismiss();

			}
		});

		qudingTv = (TextView) findViewById(R.id.quding_try_title_tv);
		qudingTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				trySelectLinstener.selectType(1);
				dismiss();
			}
		});



	}
	public void setTitle(String title){
		if(title!=null) {
			if(title!=null) {
				titleTv.setText(title);
			}
		}
	}




	public void setBtnText(String queText){
		qudingTv.setText(queText);
	}
	public void setTimeDimss(final long time){

		new Thread(){
			@Override
			public void run() {
				super.run();

				try {
					sleep(time);
					handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int jj=msg.what;
			if(jj==1){
				if(isShowing()) {
					dismiss();
				}
			}
		}
	};

}
