//package com.example.foodapp.ui.home
//
//import android.view.LayoutInflater
//import com.example.foodapp.R
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.foodapp.data.model.FoodCategory
//
//class FoodImg(private val foodImgLst: List<FoodCategory>) : RecyclerView.Adapter<FoodImg.viewHolder>() {
//    inner class viewHolder(view: View): RecyclerView.ViewHolder(view) {
//        val foodImg = view.findViewById<ImageView>(R.id.imgFood)
//        val tvFood = view.findViewById<TextView>(R.id.tvFoodName)
//    }
//
//    override fun getItemCount(): Int {
//        return foodImgLst.size
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int,
//    ): viewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_food, parent, false)
//        return viewHolder(view)
//    }
//
//    override fun onBindViewHolder(
//        holder: viewHolder,
//        position: Int,
//    ) {
//        holder.foodImg.setImageResource(foodImgLst[position].iconRes)
//        holder.tvFood.text = foodImgLst[position].name
//    }
//}
package com.example.foodapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.foodapp.R
import com.example.foodapp.data.model.FoodCategory

class FoodImg(private val foodImgLst: List<FoodCategory>) : RecyclerView.Adapter<FoodImg.viewHolder>() {

    inner class viewHolder(view: View): RecyclerView.ViewHolder(view) {
        val foodImg = view.findViewById<ImageView>(R.id.imgFood)
        val tvFood = view.findViewById<TextView>(R.id.tvFoodName)

        // ✅ Tạo RequestOptions 1 lần để tái sử dụng - giảm lag
        private val glideOptions = RequestOptions()
            .placeholder(R.drawable.img_pizza) // Placeholder khi loading
            .error(R.drawable.img_pizza) // Image khi lỗi
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache toàn bộ
            .override(250, 250) // Resize cố định
            .centerCrop()
            .dontAnimate() // Tắt animation để tăng performance

        fun bind(foodCategory: FoodCategory) {
            // ✅ Dùng Glide thay vì setImageResource để giảm lag
            Glide.with(itemView.context)
                .load(foodCategory.iconRes)
                .apply(glideOptions)
                .into(foodImg)

            tvFood.text = foodCategory.name
        }
    }

    override fun getItemCount(): Int {
        return foodImgLst.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        // ✅ Gọi bind function thay vì set trực tiếp
        holder.bind(foodImgLst[position])
    }

    // ✅ Tối ưu view recycling
    override fun getItemViewType(position: Int): Int {
        return 0 // Tất cả items cùng loại
    }

    // ✅ QUAN TRỌNG: Clear Glide khi ViewHolder bị recycle để tránh memory leak
    override fun onViewRecycled(holder: viewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.itemView.context).clear(holder.foodImg)
    }

    // ✅ Clear animation khi detach
    override fun onViewDetachedFromWindow(holder: viewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }
}