package com.example.foodapp.ui.home
import android.view.LayoutInflater
import android.view.RoundedCorner
import com.example.foodapp.R
import com.example.foodapp.data.model.FoodCategory
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class FoodCategoryAdapter (private val categoryList: List <FoodCategory>): RecyclerView.Adapter<FoodCategoryAdapter.viewHolder>() {
    inner class viewHolder (view: View): RecyclerView.ViewHolder(view) {
        val imgIcon = view.findViewById<ImageView>(R.id.imgCategory)
        val txtIcon = view.findViewById<TextView>(R.id.txtCategory)
        val frameLayout = view.findViewById<FrameLayout>(R.id.frameLayout)
        fun bind(category: FoodCategory) {
            val glideOption = RequestOptions()
//                .transform(RoundedCorners(20))
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(70,70)
                .centerCrop()
                .dontAnimate()
            Glide.with(itemView.context)
                .load(category.imgRes)
                .apply(glideOption)
                .into(imgIcon)

            txtIcon.text = category.name
        }



    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_category, parent, false)
            return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = categoryList[position]
        holder.bind(item)

        // Gán trạng thái selected cho FrameLayout
        holder.frameLayout.isSelected = item.isSelected
        holder.txtIcon.isSelected = item.isSelected

        holder.frameLayout.setOnClickListener {
            categoryList.forEach { it.isSelected = false }
            item.isSelected = true
            notifyDataSetChanged()
        }
    }


    override fun onViewRecycled(holder: viewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.itemView.context).clear(
            holder.itemView.findViewById<ImageView>(R.id.imgCategory)
        )
    }

    override fun onViewDetachedFromWindow(holder: viewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }
}




//package com.example.foodapp.ui.home
//import android.view.LayoutInflater
//import com.example.foodapp.R
//import com.example.foodapp.data.model.FoodCategory
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//class FoodCategoryAdapter(private val list: List<FoodCategory>): RecyclerView.Adapter<FoodCategoryAdapter.viewHolder>() {
//    inner class viewHolder (val view: View): RecyclerView.ViewHolder(view) {
//        val imageviewHolder = view.findViewById<ImageView>(R.id.imgCategory)
//        val textView = view.findViewById<TextView>(R.id.tvCategory)
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int,
//    ): viewHolder {
//        val viewHolder = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_food_category, parent, false)
//        return viewHolder(viewHolder)
//    }
//
//    override fun onBindViewHolder(
//        holder: viewHolder,
//        position: Int,
//    ) {
//        holder.imageviewHolder.setImageResource(list[position].iconRes)
//        holder.textView.text = list[position].name
//    }
//}