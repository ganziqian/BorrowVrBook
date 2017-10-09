package com.zwg.huibenlib.Utils;

/**
 * Created by Administrator on 2016/7/9.
 */
public class FileTransUtils {


    public static boolean parsetStr(String result) {

        try {
            String[] results = result.split("_");
            if (results.length >= 3) {
                FileInfo.id = results[0];
                FileInfo.startTime = Integer.valueOf(results[1]);
                FileInfo.endTime = Integer.valueOf(results[2]);
                return true;
            }
            else if (results.length >= 1) {
                FileInfo.id = results[0];
                FileInfo.endTime = 0;
                FileInfo.startTime = 0;
                return true;
            }
            else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public static int getType(String result){
        if(result.contains("_")){
            return 2;
        }else {
            return 1;
        }
    }


    public static class FileInfo {
        public static int startTime;
        public static int endTime;
        public static String id;

        public FileInfo() {
            startTime = 0;
            endTime = 0;
        }
    };
    /**
     * 解析识别结果字符串，返回需要播放的文件名
     * @param recStr
     * @return
     */
    public static final String translate (String recStr) {

        String[] results = recStr.split("_");
        if (results.length >= 1) {
            return results[0];
        }
        return null;
    }


    public static final String getflashTime (String recStr) {
        String str="";
        try {
            try {
                String[] results = recStr.split("_");

                str=results[1]+results[2];
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return str;
    }
    public static final int getflashTime2 (String recStr) {
        int str=0;
        try {
            try {
                String[] results = recStr.split("_");
                str=Integer.parseInt(results[1])+Integer.parseInt(results[2]);
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return str;
    }

}
