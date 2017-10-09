package com.zwg.huibenlib.Utils;

import com.zwg.huibenlib.bean.ImagenameBean;

/**
 * Created by Administrator on 2017/2/18.
 */

public class ImageFilenameUtils {


    public static ImagenameBean getTag(String result){

        try {
            ImagenameBean  imagenameBean=new ImagenameBean();
            String str[]=result.split("_");
            if(str.length==3){
                imagenameBean.setVuriaTag(str[0]);
                imagenameBean.setGotoTag(str[1]);
                imagenameBean.setImageTag(str[2]);
            }else if(str.length==8){
                imagenameBean.setVuriaTag(str[0]);
                imagenameBean.setGotoTag(str[1]);
                imagenameBean.setVideoTag(str[2]);
                imagenameBean.setIsVideo(Integer.parseInt(str[3]));
                imagenameBean.setStart(Integer.parseInt(str[4]));
                imagenameBean.setEnd(Integer.parseInt(str[5]));
                imagenameBean.setPage(str[6]);
                imagenameBean.setImageTag(str[7]);
            }else {
                return null;
            }
            return imagenameBean;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


}
