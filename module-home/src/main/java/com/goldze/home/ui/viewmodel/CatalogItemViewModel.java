package com.goldze.home.ui.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.request.RequestOptions;
import com.goldze.base.entity.CatelogEntity;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.ItemViewModel;

public class CatalogItemViewModel  extends ItemViewModel {
    public ObservableField<String> text = new ObservableField<>();
    public ObservableField<String>  icon = new ObservableField<>();
    private float radius = 25;
    public CatalogItemViewModel(@NonNull BaseViewModel viewModel, CatelogEntity.ItemsEntity catalog) {
        super(viewModel);
        this.text.set(catalog.getTitle());
        this.icon.set(catalog.getIcon());
    }

    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new
                            RequestOptions().placeholder(placeholderRes))
                    .into(imageView);
        }
    }

    /**
     * @param pool
     * @param source
     * @return
     */
    private  Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        //获取一张位图用来往上面绘制
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        //使用获取的位图新建一张画布
        Canvas canvas = new Canvas(result);
        //新建一个画笔
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        //先画图片，再画圆角矩形，获取交集
        return result;
    }

}
