package com.goldze.home.ui.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.Arrays;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.ToastUtils;

public class BannerViewModel extends ItemViewModel {

    public String[] images = new String[] {
        "http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg",
                "http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg",
                "http://img.zcool.cn/community/01c8dc56e1428e6ac72531cbaa5f2c.jpg",
                "http://img.zcool.cn/community/01fda356640b706ac725b2c8b99b08.jpg",
                "http://img.zcool.cn/community/01fd2756e142716ac72531cbf8bbbf.jpg",
                "http://img.zcool.cn/community/0114a856640b6d32f87545731c076a.jpg"};

    public BannerViewModel(@NonNull BaseViewModel viewModel) {
        super(viewModel);
    }

    @BindingAdapter(value = {"imageLoader","images"})
    public static void setImageLoader(Banner banner, ImageLoader imageLoader, String[] images) {
        KLog.e("set image loader");
        if (imageLoader != null) {
            banner.setImageLoader(imageLoader);
        }
        if (images != null) {
            banner.setImages(Arrays.asList(images));
        }
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ToastUtils.showShort(String.valueOf(position));
            }
        });
        banner.start();
    }

    public ImageLoader imageLoader(){
        return new ImageLoader(){
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context.getApplicationContext())
                        .load((String) path)
                        .into(imageView);
            }
        };
    }
}
