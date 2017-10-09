package com.zwg.huibenlib.bean;

/**
 * Created by Administrator on 2016/12/16.\
 *
 * 路径类
 *
 */
public class PathBean {
    private String basePath;
    private String imagepath;
    private String videoPath;
    private String vuforPath;
    private String xmlName;
    private String dataName;

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVuforPath() {
        return vuforPath;
    }

    public void setVuforPath(String vuforPath) {
        this.vuforPath = vuforPath;
    }
}
