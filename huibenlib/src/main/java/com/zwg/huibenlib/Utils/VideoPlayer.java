package com.zwg.huibenlib.Utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @ClassName: VideoPlayer
 * @Description: 视频播放器
 * @Author: YHD
 * @Date: 2016/4/13
 * @Copyright: (c) 2016 Wenshanhu.Co.Ltd. All rights reserved.
 */
public class VideoPlayer {
	public static final String TAG="VideoPlayer";
	private static Context context;
	private String[] ext={".3gp",".3GP",".mp4", ".MP4",".mp3", ".ogg",".OGG",".MP3",".wav",".WAV"};//定义我们支持的文件格式
	public Holder uiHolder;
	private SurfaceHolder.Callback surfaceHolderCallBack;
	private MediaPlayer.OnPreparedListener mediaPlayerOnPreparedListener;
	private MediaPlayer.OnCompletionListener mediaPlayerOnCompletionListener;
	private MediaPlayer.OnErrorListener mediaPlayerOnErrorListener;
	private MediaPlayer.OnInfoListener mediaPlayerOnInfoListener;
	private MediaPlayer.OnSeekCompleteListener mediaPlayerOnSeekCompleteListener;
	private MediaPlayer.OnVideoSizeChangedListener mediaPlayerOnVideoSizeChangedListener;
	private MediaPlayer.OnBufferingUpdateListener mediaPlayerOnBufferingUpdateListener;
	private VideoPlayerCallBack videoPlayerCallBack=null;
	private static VideoPlayer instance;
	private int delaySecondTime=1000;
	private  boolean isAready=false;
	private boolean isMusic=false;
	/** 状态枚举 */
	public enum CallBackState{
		PREPARE("准备好了"),
		COMPLETE("播放结束"),
		ERROR("播放错误"),
		EXCEPTION("播放服务异常"),
		INFO("播放信息回调"),
		PROGRESS("播放进度回调"),
		SEEK_COMPLETE("拖动到尾端"),
		VIDEO_SIZE_CHANGE("视频大小改变"),
		BUFFER_UPDATE("视频流更新"),
		FORMATE_NOT_SURPORT("视频格式不支持"),
		PLAYTIME_SO_BIG("设置播放起点超过视频大小"),
		SURFACEVIEW_NULL("SurfaceView还没初始化"),
		SURFACEVIEW_NOT_ARREADY("SurfaceView还没准备好"),
		HOLDER_CHANGE("Holder改变"),
		HOLDER_CREATE("Holder创建"),
		HOLDER_DESTROY("Holder销毁");

		private final String state;

		CallBackState(String var3) {
			this.state = var3;
		}

		public String toString() {
			return this.state;
		}
	}

	/**
	 * 获得静态类
	 * @param mcontext 对象
	 * @return 类对象
	 */
	public static synchronized VideoPlayer getInstance(Context mcontext){
		context=mcontext;
		if(instance == null){
			instance=new VideoPlayer();
		}
		return instance;
	}

	/**
	 * 构造函数
	 */
	public VideoPlayer() {
		initCallback();
		this.uiHolder = new Holder ();
		//下面开始实例化MediaPlayer对象
		uiHolder.player = new MediaPlayer();
		uiHolder.player.setOnCompletionListener(mediaPlayerOnCompletionListener);
		uiHolder.player.setOnErrorListener(mediaPlayerOnErrorListener);
		uiHolder.player.setOnInfoListener(mediaPlayerOnInfoListener);
		uiHolder.player.setOnPreparedListener(mediaPlayerOnPreparedListener);
		uiHolder.player.setOnSeekCompleteListener(mediaPlayerOnSeekCompleteListener);
		uiHolder.player.setOnVideoSizeChangedListener(mediaPlayerOnVideoSizeChangedListener);
		uiHolder.player.setOnBufferingUpdateListener(mediaPlayerOnBufferingUpdateListener);
	}

	/**
	 * 设置播放进度时间间隔
	 * @param time 时间
	 * @return 类对象
	 */
	public VideoPlayer setProgressInterval(int time){
		delaySecondTime=time;
		return instance;
	}

	/**
	 * 设置SurfaceView
	 * @param surfaceView 控件
	 * @return 类对象
	 */
	public VideoPlayer setSurfaceView (SurfaceView surfaceView) {
		if(surfaceView==null){
			if(videoPlayerCallBack!=null) {
				videoPlayerCallBack.onCallBack(CallBackState.SURFACEVIEW_NULL,surfaceView);
				return instance;
			}
		}
		uiHolder.surfaceView = surfaceView;
		//给SurfaceView添加CallBack监听
		uiHolder.surfaceHolder = uiHolder.surfaceView.getHolder();
		uiHolder.surfaceHolder.addCallback(surfaceHolderCallBack);
		return instance;
	}

	/**
	 * 添加回调函数
	 */
	private void initCallback() {
		this.surfaceHolderCallBack = new SurfaceHolder.Callback() {
			
			@Override 
		    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) { 
		        // 当Surface尺寸等参数改变时触发
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.HOLDER_CHANGE, arg0, arg1, arg2, arg3);
				}
		    }

		    @Override 
		    public void surfaceCreated(SurfaceHolder holder) {
		        // 当SurfaceView中的Surface被创建的时候被调用
		        //在这里我们指定MediaPlayer在当前的Surface中进行播放
				if(uiHolder.player!=null&&holder!=null){
					uiHolder.player.setDisplay(holder);
				}
				isAready=true;
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.HOLDER_CREATE, holder);
				}
		    }

		    @Override 
		    public void surfaceDestroyed(SurfaceHolder holder) {
				isAready=false;
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.HOLDER_DESTROY, holder);
				}
		    } 
		};

		this.mediaPlayerOnPreparedListener = new MediaPlayer.OnPreparedListener() {
			
			@Override 
		    public void onPrepared(MediaPlayer mp) {
				refress_time_handler.postDelayed(refress_time_Thread, 0);

				try {
					uiHolder.player.setDisplay(uiHolder.surfaceHolder);
					uiHolder.player.start();
				} catch (Exception e) {
					videoPlayerCallBack.onCallBack(CallBackState.EXCEPTION, mp);
				}

				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.PREPARE, mp);
				}
		    }
			
		};
		this.mediaPlayerOnCompletionListener = new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.COMPLETE, mp);
				}
			}
		};
		this.mediaPlayerOnErrorListener = new MediaPlayer.OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.ERROR, mp, what, extra);
				}
				return false;
			}
		};
		this.mediaPlayerOnInfoListener = new MediaPlayer.OnInfoListener() {
			
			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.INFO, mp, what, extra);
				}
				return false;
			}
		};
		this.mediaPlayerOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
			
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.SEEK_COMPLETE, mp);
				}
			}
		};
		this.mediaPlayerOnVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
			
			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.VIDEO_SIZE_CHANGE, mp, width, height);
				}
			}
		};
		this.mediaPlayerOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
			
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.BUFFER_UPDATE, mp, percent);
				}
			}
		};
	}

	/**
	 * 设置回调
	 * @param videoPlayerCallBack 回调
	 * @return 类对象
	 */
	public VideoPlayer setVideoPlayerCallBack (VideoPlayerCallBack videoPlayerCallBack) {
		this.videoPlayerCallBack = videoPlayerCallBack;
		return instance;
	}

	/**
	 * 是否要播放音乐
	 * @param isMusic
	 * @return
	 */
	public VideoPlayer setMusicMode (boolean isMusic) {
		this.isMusic = isMusic;
		return instance;
	}

	/**
	 * 释放资源
	 */
	public void release () {
		if(uiHolder.player != null){
			uiHolder.player.release();
			uiHolder.player = null;
		}
		refress_time_handler.removeCallbacks(refress_time_Thread);
	}

	/**
	 * 通过Assets文件名播放Assets目录下的文件
	 * @param assetName 名字
	 * @return 是否成功
	 */
	public boolean playWithAssetName (String assetName) {
		if(!checkAvalable(assetName)){
			return false;
		}
		AssetManager assetMg= context.getAssets();
		try {
			uiHolder.assetDescriptor = assetMg.openFd(assetName);
			uiHolder.player.setDisplay(null);
			uiHolder.player.reset();
			uiHolder.player.setDataSource(uiHolder.assetDescriptor.getFileDescriptor(), uiHolder.assetDescriptor.getStartOffset(), uiHolder.assetDescriptor.getLength()); 
			uiHolder.player.prepare();
        } catch (Exception e) { 
			if(videoPlayerCallBack!=null) {
				videoPlayerCallBack.onCallBack(CallBackState.ERROR,uiHolder.player);
			}
			return false;
        }
		return true;
	}

	/**
	 * 通过文件路径播放视频
	 * @param path 路径
	 * @return 是否成功
	 */
	public boolean playWithFilePath (String path) {
		if(!checkAvalable(path)){
			return false;
		}
		try {
			/**
			 * 其实仔细观察优酷app切换播放网络视频时的确像是这样做的：先暂停当前视频，
			 * 让mediaplayer与先前的surfaceHolder脱离“绑定”,当mediaplayer再次准备好要start时，
			 * 再次让mediaplayer与surfaceHolder“绑定”在一起，显示下一个要播放的视频。
			 * 注：MediaPlayer.setDisplay()的作用： 设置SurfaceHolder用于显示的视频部分媒体。
			 */
			uiHolder.player.setDisplay(null);
			uiHolder.player.reset();
			uiHolder.player.setDataSource(path); 
			uiHolder.player.prepare();
        } catch (Exception e) {
			if(videoPlayerCallBack!=null) {
				videoPlayerCallBack.onCallBack(CallBackState.ERROR,uiHolder.player);
			}
			return false;
        }
		return true;
	}

	/**
	 * 设置播放的起点
	 * @param msec
	 * @param path 路径
	 */
	public boolean playAtTime(int msec,String path){
		if(!checkAvalable(path)){
			return false;
		}
		if(msec>uiHolder.player.getDuration()){
			if(videoPlayerCallBack!=null) {
				videoPlayerCallBack.onCallBack(CallBackState.PLAYTIME_SO_BIG,uiHolder.player);
			}
			return false;
		}
		try {
			uiHolder.player.reset();
			uiHolder.player.setDataSource(path);
			uiHolder.player.prepare();
			uiHolder.player.start();
			uiHolder.player.seekTo(msec);
		} catch (Exception e) {
			// e.printStackTrace();
			if(videoPlayerCallBack!=null) {
				videoPlayerCallBack.onCallBack(CallBackState.ERROR,uiHolder.player);
			}
			return false;
		}
		return true;
	}

	/**
	 * 暂停播放
	 * @return 是否成功
	 */
	public boolean pause(){
		if(uiHolder.player==null){
			return false;
		}
		// if(uiHolder.player.isPlaying()){
			uiHolder.player.pause();
		// }
		return true;
	}

	/**
	 * 开始播放
	 * @return 是否成功
	 */
	public boolean start(){
		if(uiHolder.player==null){
			return false;
		}
		uiHolder.player.start();
		return true;
	}

	/**
	 * 获得流媒体对象
	 * @return 对象
	 */
	public MediaPlayer getMediaPlayer(){
		return uiHolder.player;
	}

	/**
	 * 检查是否可以播放
	 * @param param 参数
	 * @return 结果
	 */
	private boolean checkAvalable(String param){
		refress_time_handler.removeCallbacks(refress_time_Thread);
		if(!isMusic){
			if(uiHolder.surfaceView==null){
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.SURFACEVIEW_NULL, uiHolder.surfaceView);
				}
				return false;
			}
			if(!isAready){
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.SURFACEVIEW_NOT_ARREADY, uiHolder.surfaceHolder);
				}
				return false;
			}
		}

		boolean surport=false;
		for(int i=0;i<ext.length;i++){
			if(param.endsWith(ext[i])){
				surport=true;
			}
		}
		if(!surport){
			if(videoPlayerCallBack!=null) {
				videoPlayerCallBack.onCallBack(CallBackState.FORMATE_NOT_SURPORT, uiHolder.player);
			}
		}
		return true;
	}

	Handler refress_time_handler = new Handler();
	Runnable refress_time_Thread = new Runnable(){
		public void run() {
			if(uiHolder.player!=null&&uiHolder.player.isPlaying()){
				if(videoPlayerCallBack!=null) {
					videoPlayerCallBack.onCallBack(CallBackState.PROGRESS, uiHolder.player);
				}
			}
			refress_time_handler.postDelayed(refress_time_Thread,delaySecondTime);
		}
	};

	/**
	 * 封装UI
	 */
	public static final class Holder {
		public SurfaceHolder surfaceHolder; 
	    public MediaPlayer player;
	    public SurfaceView surfaceView;
		public AssetFileDescriptor assetDescriptor;
	}
	
	/**
	 * 回调接口
	 */
	public interface VideoPlayerCallBack {
		/**
		 * 状态回调
		 * @param state 状态
		 * @param obj Object
		 * @param args 若干参数
		 */
		void onCallBack(CallBackState state, Object obj, Object... args);
	}
	
}
