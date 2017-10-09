package com.zwg.huibenlib.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zwg.huibenlib.ComBaseActivity;
import com.zwg.huibenlib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {


    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_image, container, false);

        initView(v);
        return v;
    }

    private void initView(View v) {
        ImageView fhIv= (ImageView) v.findViewById(R.id.image_fh_iv);
        fhIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ComBaseActivity)getActivity()).fhHelp();
            }
        });


    }

}
