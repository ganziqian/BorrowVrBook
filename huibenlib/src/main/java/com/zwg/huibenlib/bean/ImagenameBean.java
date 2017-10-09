package com.zwg.huibenlib.bean;

/**
 * Created by Administrator on 2017/2/18.
 */

public class ImagenameBean {
    private String   vuriaTag;   //识别库标识
    private String   gotoTag;    //要跳转的识别库标识
    private String   videoTag;   //播放哪个视频标识
    private int   isVideo;   //播放图片还是视频  1视频    2图片
    private int   start;    //开始时间
    private int   end;     //结束时间
    private String   page;  //页数
    private String   imageTag;  //区分同一页标识

    public int getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(int isVideo) {
        this.isVideo = isVideo;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getVuriaTag() {
        return vuriaTag;
    }

    public void setVuriaTag(String vuriaTag) {
        this.vuriaTag = vuriaTag;
    }

    public String getGotoTag() {
        return gotoTag;
    }

    public void setGotoTag(String gotoTag) {
        this.gotoTag = gotoTag;
    }

    public String getVideoTag() {
        return videoTag;
    }

    public void setVideoTag(String videoTag) {
        this.videoTag = videoTag;
    }


}
