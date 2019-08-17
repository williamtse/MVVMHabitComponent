package com.goldze.home.ui.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.goldze.base.entity.ProductEntity;
import com.goldze.base.utils.MyLayoutManager;
import com.goldze.home.R;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.ItemViewModel;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class ProductItemViewModel extends ItemViewModel {
    public ObservableField<String> text = new ObservableField<>();
    public String img;
    public float price;
    public float originPrice;
    public int totalSale;
    public MyLayoutManager mlayoutManger = new MyLayoutManager();

    public ProductItemViewModel(@NonNull BaseViewModel viewModel, ProductEntity product) {
        super(viewModel);
        this.text.set(product.getTitle());
        this.img = product.getMainImg();
        this.price = product.getPrice();
        this.originPrice = product.getOriginPrice();
        this.totalSale = product.getTotalSale();

        String tags = product.getTags();
        if(!TextUtils.isEmpty(tags)){
            String[] tagList = tags.split(",");
            for(String tag:tagList){
                observableProductTagList.add(new ProductTagItemViewModel(viewModel, tag));
            }
        }
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

    //给RecyclerView添加ObservableList
    public ObservableList<ProductTagItemViewModel> observableProductTagList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<ProductTagItemViewModel> productTagItemBinding = ItemBinding.of(com.goldze.home.BR.viewModel, R.layout.product_tag);
}
