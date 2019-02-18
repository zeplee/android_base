package com.aac.base.helper

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

/**
 * 加载图片 glid与生命周期相关
 * 可以传图片URL 资源ID file文件
 * https://blog.csdn.net/guolin_blog/article/details/78582548
 * https://blog.csdn.net/u010377970/article/details/78656970
 * https://www.jianshu.com/p/d82bb995db4d
 * https://www.jianshu.com/p/2942a57401eb
 */
class ImageHelper {
    companion object {
        @JvmStatic
        @BindingAdapter("imgRes")
        fun loadImg(iv: ImageView, imgRes: Any) {
            val options = RequestOptions()
//                    .dontAnimate()
            Glide.with(iv)
                    .load(imgRes)
                    //没有占位图的时候可以用动画，刷新也不会闪烁，有占位图就不能用这个动画了，会闪
                    .transition(DrawableTransitionOptions.withCrossFade(600))//适用于Drawable，过渡动画持续600ms
                    .apply(options)
                    .into(iv)
        }

        @JvmStatic
        @BindingAdapter("imgRes", "imgErr")
        fun loadImgWithErr(iv: ImageView,
                           imgRes: Any,
                           imgErr: Any) {
            val options = RequestOptions().apply {
                when (imgErr) {
                    is Drawable -> error(imgErr)
                    is Int -> error(imgErr)
                }
            }
            Glide.with(iv)
                    .load(imgRes)
                    .transition(DrawableTransitionOptions.withCrossFade(600))//适用于Drawable，过渡动画持续600ms
                    .apply(options)
                    .into(iv)
        }

        @JvmStatic
        @BindingAdapter("imgRes", "imgPlace")
        fun loadImgWithPlace(iv: ImageView,
                             imgRes: Any,
                             imgPlace: Any) {
            val options = RequestOptions().apply {
                when (imgPlace) {
                    is Drawable -> placeholder(imgPlace)
                    is Int -> placeholder(imgPlace)
                }
            }
            Glide.with(iv)
                    .load(imgRes)
                    .transition(DrawableTransitionOptions.withCrossFade(600))//适用于Drawable，过渡动画持续600ms
                    .apply(options)
                    .into(iv)
        }

        @JvmStatic
        @BindingAdapter("imgRes", "imgErr", "imgPlace")
        fun loadImg(iv: ImageView,
                    imgRes: Any,
                    imgErr: Any,
                    imgPlace: Any) {
            val options = RequestOptions().apply {
                when (imgPlace) {
                    is Drawable -> placeholder(imgPlace)
                    is Int -> placeholder(imgPlace)
                }
                when (imgErr) {
                    is Drawable -> error(imgErr)
                    is Int -> error(imgErr)
                }
            }
            Glide.with(iv)
                    .load(imgRes)
                    .transition(DrawableTransitionOptions.withCrossFade(600))//适用于Drawable，过渡动画持续600ms
                    .apply(options)
                    .into(iv)
        }


        @JvmStatic
        @BindingAdapter("imgRes", "imgRadius")
        fun loadRoundImg(iv: ImageView,
                         imgRes: Any,
                         imgRadius: Int) {
            val options = RequestOptions()
                    .transform(RoundedCorners(imgRadius))
            Glide.with(iv)
                    .load(imgRes)
                    .transition(DrawableTransitionOptions.withCrossFade(600))//适用于Drawable，过渡动画持续600ms
                    .apply(options)
                    .into(iv)
        }

        /**
         * 加载圆形的图片--可以自定义默认图片
         */
        @JvmStatic
        @BindingAdapter("imgResCircle")
        fun loadCircleImg(iv: ImageView, imgResCircle: Any) {
            val options = RequestOptions()
                    .circleCrop()
            Glide.with(iv)
                    .load(imgResCircle)
                    .transition(DrawableTransitionOptions.withCrossFade(600))//适用于Drawable，过渡动画持续600ms
                    .apply(options)
                    .into(iv)
        }

//        /**
//         * 加载四边有圆角处理的图片--可以自定义默认图片
//         */
//        @JvmStatic
//        @BindingAdapter("imgRes", "imgRadius", "imgErr", "imgPlace")
//        fun loadRoundImg(iv: ImageView,
//                         imgRes: Any,
//                         imgRadius: Int,
//                         @DrawableRes imgErr: Int,
//                         @DrawableRes imgPlace: Int) {
//            val options = RequestOptions()
//                    .transform(RoundedCorners(imgRadius))
//                    .placeholder(imgPlace)
//                    .error(imgErr)
//            Glide.with(iv.context)
//                    .load(imgRes)
//                    .apply(options)
//                    .into(iv)
//        }

        /**
         * 下载网络图片
         */
        @JvmStatic
        fun downImg(imgRes: String,
                    callback: ImgDownCallback) {
            Glide.with(Utils.getApp())
                    .load(imgRes)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            callback.onError()
                            return true
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            callback.onSuccess(resource)
                            return true
                        }
                    })
                    .preload()
        }


//        /**
//         * 预加载网络图片
//         */
//        @JvmStatic
//        fun preloadImg(context: Context,
//                       imgRes: String) {
//            Glide.with(context)
//                    .load(imgRes)
//                    .preload();
//        }
    }

    abstract class ImgDownCallback {

        open fun onSuccess(drawable: Drawable?) {}

        open fun onError() {}
    }
    //        /**
//         * 优先级高加载图片--可以自定义默认图片
//         *
//         * @param aty_fragment_context fragment activity都可以传入进来 glid与生命周期相关
//         * @param url_res_file         可以传图片URL 资源ID file文件
//         * @param imageView            目标imageview
//         * @param imgErr             自定义加载默认图片
//         * @param isPlace              是否展示占位图片
//         */
//        @JvmStatic
//        fun loadCacheImg(imageView: ImageView, url_res_file: Any, @DrawableRes imgErr: Int) {
//            val options = RequestOptions()
//                    .error(imgErr)
//                    .priority(Priority.HIGH)
//            Glide.with(imageView.context)
//                    .load(url_res_file)
//                    .apply(options)
//                    .into(imageView)
//        }
}