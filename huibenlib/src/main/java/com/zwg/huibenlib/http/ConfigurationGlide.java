package com.zwg.huibenlib.http;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * Created by Administrator on 2016/10/20.
 */
public class ConfigurationGlide implements GlideModule {
    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        //配置
     //   builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, 100 * 1024 * 1024));//磁盘缓存到外部存储
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
      /*  String downLoadPath = Environment.getDownloadCacheDirectory().getPath();
        builder.setDiskCache(new DiskLruCacheFactory(downLoadPath,  1024 * 1024 * 100));
        //指定缓存目录2
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                File cacheLocation = new File(context.getExternalCacheDir(), "jhhihih");
                cacheLocation.mkdirs();

                return DiskLruCacheWrapper.get(cacheLocation, 1024 * 1024 * 100);
            }
        });*/
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}