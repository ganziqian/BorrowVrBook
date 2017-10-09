package com.zwg.huibenlib.bean;

import android.util.Log;

import com.zwg.huibenlib.Utils.DownFileUtils;
import com.zwg.huibenlib.Utils.dialog.ShowLoadDia;
import com.zwg.huibenlib.adapter.MyAdapter;

/**
 * Created by Administrator on 2016/9/7.
 * media_name：资源名字
 media_url：资源地址
 file_length：资源大小
 media_icon：资源图片
 media_icon_thumb：资源缩略图
 media_abstract：资源描述
 dateline：资源创建时间
 */
public class LisBean {


    private String tag_name;
    private String media_abstract;
    private String file_length;
    private String media_name;
    private String media_url;
    private String media_icon;
    private String mediaid;
    private String saveVideoName;
    private String saveImageName;
    private String albumid;

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    private ShowLoadDia showLoadDia;

    private MyAdapter.ViewHolder viewHolder;

    public MyAdapter.ViewHolder getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(MyAdapter.ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    public ShowLoadDia getShowLoadDia() {
        return showLoadDia;
    }

    public void setShowLoadDia(ShowLoadDia showLoadDia) {
        this.showLoadDia = showLoadDia;
    }






    private boolean isexit;


    private DownFileUtils downFileUtils;










    public String getSaveImageName() {
        return saveImageName;
    }

    public void setSaveImageName(String saveImageName) {
        this.saveImageName = saveImageName;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getMediaid() {
        return mediaid;
    }

    public void setMediaid(String mediaid) {
        this.mediaid = mediaid;
    }



    public DownFileUtils getDownFileUtils() {
        return downFileUtils;
    }

    public void setDownFileUtils(DownFileUtils downFileUtils) {
        this.downFileUtils = downFileUtils;
    }

    private boolean isdown;

    public boolean isdown() {
        return isdown;
    }

    public void setIsdown(boolean isdown) {
        this.isdown = isdown;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMedia_icon() {
        return media_icon;
    }

    public void setMedia_icon(String media_icon) {
        this.media_icon = media_icon;
    }



    public String getFile_length() {
        return file_length;
    }

    public void setFile_length(String file_length) {
        this.file_length = file_length;
    }

    public String getMedia_abstract() {
        return media_abstract;
    }

    public void setMedia_abstract(String media_abstract) {
        this.media_abstract = media_abstract;
    }


    public String getSaveVideoName() {
        return saveVideoName;
    }

    public void setSaveVideoName(String saveVideoName) {
        this.saveVideoName = saveVideoName;
    }

    public boolean isexit() {
        return isexit;
    }

    public void setIsexit(boolean isexit) {
        this.isexit = isexit;
    }




    @Override
    public boolean equals(Object object) {
        if (object instanceof LisBean) {
            LisBean otherUser = (LisBean) object;
            if (this.tag_name.equals(otherUser.getTag_name())) {
                return true;
            }
        }
        return false;
    }
}
