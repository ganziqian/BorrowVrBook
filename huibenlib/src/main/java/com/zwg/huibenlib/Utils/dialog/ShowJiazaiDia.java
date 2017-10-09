package com.zwg.huibenlib.Utils.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwg.huibenlib.R;


/**
 * �����ˣ�  ganziqian
 * ���ã�
 * ʱ�䣺2015/8/7
 */
public class ShowJiazaiDia {

	private static Dialog dialog;


	public static void show(Context context){
		try{
			
				dialog=new Dialog(context, R.style.NobackDialog);
				View view= LayoutInflater.from(context).inflate(R.layout.loading_text_dialog_layout,null);

				LinearLayout layout= (LinearLayout) view.findViewById(R.id.appli_lin_lay);
				layout.getBackground().setAlpha(120);

				/*TextView titleView=(TextView) view.findViewById(R.id.appli_load_title);
			titleView.setText(title);
				 */
				dialog.setContentView(view);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
		
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public static void dimiss(){

		if(dialog!=null){
			dialog.dismiss();
			dialog=null;
		}
	}



}
