package com.example.foodapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.foodapp.R


class ImageSliderAdapter(private val imageList: List<String>): RecyclerView.Adapter<ImageSliderAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageViewSlider = view.findViewById<ImageView>(R.id.imageViewSlider)



        fun bind(imageUrl: String) {
            val glideOption = RequestOptions()
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(250, 250)
                .centerCrop()
                .dontAnimate()
            Glide.with(itemView.context)
                .load(imageUrl)
                .apply(glideOption)
                .into(imageViewSlider)
        }
    }

    override fun getItemCount(): Int {
        return if (imageList.isNotEmpty()) Int.MAX_VALUE else 0
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val realPosition = position % imageList.size
        holder.bind(imageList[realPosition])
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.itemView.context).clear(holder.imageViewSlider)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }
}