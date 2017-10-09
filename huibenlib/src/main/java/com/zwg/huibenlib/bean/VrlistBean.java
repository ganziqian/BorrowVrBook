package com.zwg.huibenlib.bean;

/**
 * Created by Administrator on 2017/2/22.
 */

public class VrlistBean {
    private String  tag_name;       //识别库tag，与高通图片命名关联起来的一个标识
    private String  albumname;       //识别库系列的名称   、比如品格故事、五大领域等
    private String  xml_url;         //识别库xml的下载链接
    private String  dat_url;          //识别库dat的下载链接
    private String  icon_path;     //系列图标地址
    private String  albumid;              //通过该id获取改识别库下的视频资源和其他资源等
    private String  album_abstract;
    private String  savexmlName;
    private String  savedatName;
    private boolean isRefrsh=false;

    public boolean isRefrsh() {
        return isRefrsh;
    }

    public void setRefrsh(boolean refrsh) {
        isRefrsh = refrsh;
    }

    public String getSavedatName() {
        return savedatName;
    }

    public void setSavedatName(String savedatName) {
        this.savedatName = savedatName;
    }

    public String getSavexmlName() {
        return savexmlName;
    }

    public void setSavexmlName(String savexmlName) {
        this.savexmlName = savexmlName;
    }

    public String getAlbum_abstract() {
        return album_abstract;
    }

    public void setAlbum_abstract(String album_abstract) {
        this.album_abstract = album_abstract;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getXml_url() {
        return xml_url;
    }

    public void setXml_url(String xml_url) {
        this.xml_url = xml_url;
    }

    public String getDat_url() {
        return dat_url;
    }

    public void setDat_url(String dat_url) {
        this.dat_url = dat_url;
    }

    public String getIcon_path() {
        return icon_path;
    }

    public void setIcon_path(String icon_path) {
        this.icon_path = icon_path;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }


    @Override
    public boolean equals(Object object) {
        if (object instanceof VrlistBean) {
            VrlistBean otherUser = (VrlistBean) object;
            if (this.getAlbumid().equals(otherUser.getAlbumid())) {
                return true;
            }
        }
        return false;
    }
}
