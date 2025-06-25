package com.example.foodapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PromotionAdapter(private val imageList: List<Int>) :
    RecyclerView.Adapter<PromotionAdapter.ImageViewHolder>()
{
    inner class ImageViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.itemPromotion)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_promotion, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ImageViewHolder,
        position: Int,
    ) {
        holder.imageView.setImageResource(imageList[position])
    }
}