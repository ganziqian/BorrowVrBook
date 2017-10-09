package com.zwg.borrowvrbook.fragemnt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwg.borrowvrbook.R;
import com.zwg.huibenlib.http.AppContents;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingFragment extends Fragment {
    private RelativeLayout loadLayout;
    private ImageView loadiv;
    private TextView loadTv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_loading, container, false);


        init(view);

        return view;
    }

    private void init(View view) {

        loadLayout = (RelativeLayout) view.findViewById(R.id.load_layout);
        TextView versionTv = (TextView)  view.findViewById(R.id.main_versionname_tv);
        versionTv.setText(AppContents.getAppVersionName(getActivity()));

        loadiv = (ImageView)  view.findViewById(R.id.main_load_iv);
        loadTv = (TextView)  view.findViewById(R.id.main_load_tv);
    }

}
