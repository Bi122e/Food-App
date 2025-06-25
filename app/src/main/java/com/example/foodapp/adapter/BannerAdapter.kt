package com.example.foodapp.adapter
import android.view.LayoutInflater
import com.example.foodapp.R

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BannerAdapter(private val bannerList: List<Int>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.bannerImageView)
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BannerViewHolder {
        val imageView = LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(imageView)
    }

    override fun onBindViewHolder(
        holder: BannerViewHolder,
        position: Int,
    ) {
        val imageUrl = bannerList[position]
        Glide.with(holder.imageView.context)
            .load(imageUrl)
            .into(holder.imageView)
    }
}