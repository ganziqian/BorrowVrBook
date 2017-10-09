package com.zwg.huibenlib.db;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.zwg.huibenlib.bean.LisBean;
import com.zwg.huibenlib.bean.PathBean;
import com.zwg.huibenlib.bean.TezhengInfoBean;
import com.zwg.huibenlib.bean.VrlistBean;
import com.zwg.huibenlib.http.AppContents;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public class DataSaveUtils {

    private MyDataBaseHelper myDataBaseHelper;

    public SQLiteDatabase sqLiteDatabase;

    public DataSaveUtils(Context context) {
        SQLiteDatabase.loadLibs(context);
        myDataBaseHelper=new MyDataBaseHelper(context);
        sqLiteDatabase= myDataBaseHelper.getReadableDatabase("xjkb88");

    }


    /**
     * 保存特征库数据
     * @param tezhengInfoBean
     */
    public void saveTezhengInfo(TezhengInfoBean tezhengInfoBean){
        ContentValues values=new ContentValues();
        values.put(DataTags.APPKEY,tezhengInfoBean.getAppkey());
        values.put(DataTags. DAT_URL,tezhengInfoBean.getDat_url());
        values.put(DataTags.XML_URL,tezhengInfoBean.getXml_url());
        values.put(DataTags.BOOK_ABSTRA,tezhengInfoBean.getBook_abstract());
        values.put(DataTags.CREATE_TIME,tezhengInfoBean.getDateline());
        // sqLiteDatabase.insert(MyDataBaseHelper.TABLE_TEZHENG_NAME,"",values);
    }


    /**
     */
    public void saveisAc(String txt){
        sqLiteDatabase.delete(MyDataBaseHelper.TABLE_ISA_NAME,null,null);
        ContentValues values=new ContentValues();
        values.put(DataTags.TAG_ACT,txt);
        sqLiteDatabase.insert(MyDataBaseHelper.TABLE_ISA_NAME,"",values);
    }
    public String getIsAc(){
        String strTag="";
        try {
            Cursor cursor = sqLiteDatabase.query(MyDataBaseHelper.TABLE_ISA_NAME, null, null , null, null,
                    null, null);
            cursor.moveToFirst();
            Log.e("----------->>",cursor.getCount()+"");
            //  while (!cursor.isAfterLast() && (cursor.getString(0) != null )) {
            strTag=cursor.getString(1);
            // }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return strTag;
    }


    public void getTezhengku(){
        String[] columns = new String[] { "id", "username", "info" };
        String selection = "id=?";
        String[] selectionArgs = { "1" };
        String groupBy = null;
        String having = null;
        String orderBy = null;
        // sqLiteDatabase.query(MyDataBaseHelper.TABLE_TEZHENG_NAME, columns, selection,selectionArgs, groupBy, having, orderBy);
    }

    /**
     * 保存视频列表数据
     */
    public void savevideoInfo(List<LisBean> list){
        try {
            for (int i=0;i<list.size();i++) {
                ContentValues values = new ContentValues();
                values.put(DataTags.LIS_TAG_NAME, list.get(i).getTag_name());

                values.put(DataTags.LIS_ABSTRA, list.get(i).getMedia_abstract());

                values.put(DataTags.LIS_MEA_NAME, list.get(i).getMedia_name());


                values.put(DataTags.LIS_LENGTH, list.get(i).getFile_length());

                values.put(DataTags.LIS_MEDIA_URL, list.get(i).getMedia_url());

                values.put(DataTags.LIS_ICOM_URL, list.get(i).getMedia_icon());

                values.put(DataTags.LIS_ID, list.get(i).getMediaid());
                values.put(DataTags.LIS_SAVE_NAME, list.get(i).getSaveVideoName());
                values.put(DataTags.LIS_IV_NAME, list.get(i).getSaveImageName());
                values.put(DataTags.LIS_GET_NAME, list.get(i).getAlbumid());


                sqLiteDatabase.insert(MyDataBaseHelper.TABLE_VIDEO_NAME, "", values);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public  List<LisBean> getVideiLists(PathBean pathBean,String getname){


        List<LisBean> lisBeens = new ArrayList<LisBean>();
        try {
            Cursor cursor = sqLiteDatabase.query(MyDataBaseHelper.TABLE_VIDEO_NAME, null, null , null, null,
                    null, null);
            cursor.moveToFirst();
            Log.e("----------->>",cursor.getCount()+"");
            while (!cursor.isAfterLast() && (cursor.getString(1) != null )) {
                LisBean lisBean = new LisBean();
                // lisBean.setId(cursor.getString(0));
                lisBean.setTag_name(cursor.getString(1));
                lisBean.setMedia_abstract(cursor.getString(2));
                lisBean.setMedia_name(cursor.getString(3));
                lisBean.setFile_length(cursor.getString(4));
                lisBean.setMedia_url(cursor.getString(5));
                lisBean.setMedia_icon(cursor.getString(6));
                lisBean.setMediaid(cursor.getString(7));
                lisBean.setSaveVideoName(cursor.getString(8));
                lisBean.setSaveImageName(cursor.getString(9));
                lisBean.setAlbumid(cursor.getString(10));

                if(AppContents.isExit(pathBean.getVideoPath()+lisBean.getSaveVideoName())){
                    lisBean.setIsexit(true);
                }else {
                    lisBean.setIsexit(false);
                }
                lisBean.setIsdown(false);

                if(cursor.getString(1)!=null) {
                    if(cursor.getString(10).equals(getname)) {
                        lisBeens.add(lisBean);
                    }
                }
                cursor.moveToNext();
            }
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return lisBeens;
    }


    //检查是否更新文件
    public  boolean isUpdateVideo(LisBean lisBean,PathBean pathBean){
        Log.e("===========","ijjjj");
        Cursor cursor=null;
        try {
            cursor=sqLiteDatabase.query(MyDataBaseHelper.TABLE_VIDEO_NAME,null,DataTags.LIS_TAG_NAME+"= ?",new String[]{lisBean.getTag_name()},null,null,null);

            if(cursor.getCount()!=0) {
                cursor.moveToFirst();
                String jjj=cursor.getString(3);
                String videoName = cursor.getString(8);
                String imageName = cursor.getString(9);
                cursor.close();

                if (lisBean.getSaveVideoName().equals(videoName) && lisBean.getSaveImageName().equals(imageName)) {
                    return false;
                } else {
                    if (!lisBean.getSaveVideoName().equals(videoName)) {
                        File file = new File(pathBean.getVideoPath() + videoName);
                        if (file.exists()) {
                            file.delete();
                            Log.e("更新删除=======",jjj+videoName);
                        }
                    }
                    if (!lisBean.getSaveImageName().equals(imageName)) {
                        File file = new File(pathBean.getImagepath() + imageName);
                        if (file.exists()) {
                            file.delete();
                        }
                    }

                }
            }

        }catch ( Exception e){
            e.printStackTrace();
        }
        if(cursor!=null){
            cursor.close();
        }
        return true;
    }




    public  List<LisBean> getVideiLists3(PathBean pathBean,String abbid){


        List<LisBean> lisBeens = new ArrayList<LisBean>();
        try {
            Cursor cursor = sqLiteDatabase.query(MyDataBaseHelper.TABLE_VIDEO_NAME, null,DataTags.LIS_GET_NAME+"= ?",new String[]{abbid}, null,null, null);
            cursor.moveToFirst();
            Log.e("----------->>",cursor.getCount()+"");

            while (!cursor.isAfterLast() && (cursor.getString(1) != null )) {

                LisBean lisBean = new LisBean();
                // lisBean.setId(cursor.getString(0));
                lisBean.setTag_name(cursor.getString(1));
                lisBean.setMedia_abstract(cursor.getString(2));
                lisBean.setMedia_name(cursor.getString(3));
                lisBean.setFile_length(cursor.getString(4));
                lisBean.setMedia_url(cursor.getString(5));
                lisBean.setMedia_icon(cursor.getString(6));
                lisBean.setMediaid(cursor.getString(7));
                lisBean.setSaveVideoName(cursor.getString(8));
                lisBean.setSaveImageName(cursor.getString(9));
                lisBean.setAlbumid(cursor.getString(10));

                if(AppContents.isExit(pathBean.getVideoPath()+lisBean.getSaveVideoName())){
                    lisBean.setIsexit(true);
                }else {
                    lisBean.setIsexit(false);
                }
                lisBean.setIsdown(false);


                if(cursor.getString(1)!=null) {

                    lisBeens.add(lisBean);

                }
                cursor.moveToNext();
            }
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return lisBeens;
    }
















    /**
     * 删除视频列表数据
     */
    public void delvideoInfo(List<LisBean> list){
        sqLiteDatabase.delete(MyDataBaseHelper.TABLE_VIDEO_NAME,null,null);
        // sqLiteDatabase.delete(MyDataBaseHelper.TABLE_TEZHENG_NAME, null, null);
    }


    /**
     * 删除视频列表数据
     */
    public void delIsActivi(){
        sqLiteDatabase.delete(MyDataBaseHelper.TABLE_ISA_NAME,null,null);
        // sqLiteDatabase.delete(MyDataBaseHelper.TABLE_TEZHENG_NAME, null, null);
    }

    //更新视频列表数据
    public void UpdateVideoInfo(LisBean lisBean){
        ContentValues values=new ContentValues();
        values.put(DataTags.LIS_TAG_NAME,lisBean.getTag_name());

        values.put(DataTags.LIS_ABSTRA,lisBean.getMedia_abstract());

        values.put(DataTags.LIS_MEA_NAME, lisBean.getMedia_name());


        values.put(DataTags.LIS_LENGTH,lisBean.getFile_length());

        values.put(DataTags.LIS_MEDIA_URL, lisBean.getMedia_url());

        values.put(DataTags.LIS_ICOM_URL, lisBean.getMedia_icon());

        values.put(DataTags.LIS_ID, lisBean.getMediaid());
        values.put(DataTags.LIS_SAVE_NAME, lisBean.getSaveVideoName());
        values.put(DataTags.LIS_IV_NAME, lisBean.getSaveImageName());
        values.put(DataTags.LIS_GET_NAME, lisBean.getAlbumid());
        sqLiteDatabase.update(MyDataBaseHelper.TABLE_VIDEO_NAME,values, DataTags.LIS_TAG_NAME+"= ?" ,new String[]{lisBean.getTag_name()});

    }








    /**
     * 保存特征库数据
     */
    public void saveVrInfo(List<VrlistBean> list){
        sqLiteDatabase.delete(MyDataBaseHelper.TABLE_VR_LIST,null,null);
        try {
            for (int i=0;i<list.size();i++) {
                ContentValues values = new ContentValues();

                values.put(DataTags.ID, list.get(i).getAlbumid());

                values.put(DataTags.VR_TAG, list.get(i).getTag_name());

                values.put(DataTags.VR_NAME, list.get(i).getAlbumname());

                values.put(DataTags.VR_XMLA_URL, list.get(i).getXml_url());


                values.put(DataTags.VR_DATA_URL, list.get(i).getDat_url());

                values.put(DataTags.VR_IV_URL, list.get(i).getIcon_path());

                values.put(DataTags.VR_MIAOSHU, list.get(i).getAlbum_abstract());

                values.put(DataTags.VR_BOOK_ID, list.get(i).getAlbumid());
                values.put(DataTags.VR_SAXML_NAME, list.get(i).getSavexmlName());
                values.put(DataTags.VR_SADAT_NAME, list.get(i).getSavedatName());

                sqLiteDatabase.insert(MyDataBaseHelper.TABLE_VR_LIST, "", values);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //单个保存
    public void saveVrlibInfo(VrlistBean vrlistBean) {

        try {
            ContentValues values = new ContentValues();

            values.put(DataTags.ID, vrlistBean.getAlbumid());

            values.put(DataTags.VR_TAG, vrlistBean.getTag_name());

            values.put(DataTags.VR_NAME, vrlistBean.getAlbumname());

            values.put(DataTags.VR_XMLA_URL, vrlistBean.getXml_url());


            values.put(DataTags.VR_DATA_URL, vrlistBean.getDat_url());

            values.put(DataTags.VR_IV_URL, vrlistBean.getIcon_path());

            values.put(DataTags.VR_MIAOSHU, vrlistBean.getAlbum_abstract());

            values.put(DataTags.VR_BOOK_ID, vrlistBean.getAlbumid());
            values.put(DataTags.VR_SAXML_NAME, vrlistBean.getSavexmlName());
            values.put(DataTags.VR_SADAT_NAME, vrlistBean.getSavedatName());

            sqLiteDatabase.insert(MyDataBaseHelper.TABLE_VR_LIST, "", values);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    //根据识别库标识tag更新数据
    public int updatevrlibInfo(VrlistBean vrlistBean) {
        int id=3;
        try {
            ContentValues values = new ContentValues();
            values.put(DataTags.VR_SAXML_NAME,vrlistBean.getSavexmlName());
            values.put(DataTags.VR_SADAT_NAME,vrlistBean.getSavedatName());
             id = sqLiteDatabase.update(MyDataBaseHelper.TABLE_VR_LIST,values,DataTags.VR_TAG+"=?",new String[]{vrlistBean.getTag_name()});
        }catch (Exception e){
            e.printStackTrace();
        }
      //  Log. e("UpdatevrlibInfo", id + "");
        return id;
    }





    public  List<VrlistBean> getVRLists(){

        List<VrlistBean> lisBeens = new ArrayList<VrlistBean>();
        try {
            Cursor cursor = sqLiteDatabase.query(MyDataBaseHelper.TABLE_VR_LIST, null, null , null, null,
                    null, null);
            cursor.moveToFirst();
            Log.e("----------->>",cursor.getCount()+"");
            while (!cursor.isAfterLast() && (cursor.getString(1) != null )) {
                VrlistBean lisBean = new VrlistBean();
                lisBean.setAlbumid(cursor.getString(0));
                lisBean.setTag_name(cursor.getString(1));
                lisBean.setAlbumname(cursor.getString(2));
                lisBean.setXml_url(cursor.getString(3));
                lisBean.setDat_url(cursor.getString(4));
                lisBean.setIcon_path(cursor.getString(5));
                lisBean.setAlbum_abstract(cursor.getString(6));
                lisBean.setAlbumid(cursor.getString(7));
                lisBean.setSavexmlName(cursor.getString(8));
                lisBean.setSavedatName(cursor.getString(9));

                if(cursor.getString(1)!=null) {
                    lisBeens.add(lisBean);
                }
                cursor.moveToNext();
            }
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return lisBeens;
    }







}
