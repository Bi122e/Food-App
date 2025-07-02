package com.example.foodapp.ui.home
import android.view.LayoutInflater
import com.example.foodapp.R
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.foodapp.data.model.FoodImaHome

class FoodCategory (private val imgList: List<FoodImaHome>): RecyclerView.Adapter<FoodCategory.viewHolder>() {
    inner class viewHolder (view: View): RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.txtNameFood)
        val img = view.findViewById<ImageView>(R.id.imgFood)
        val price = view.findViewById<TextView>(R.id.txtPrice)
        val restaurant = view.findViewById<TextView>(R.id.txtNameRestaurant)

        fun bind(food: FoodImaHome) {
            val glideOption = RequestOptions()
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()
            Glide.with(itemView.context)
                .load(food.foodImg)
                .apply(glideOption)
                .into(img)
            name.text = food.Name
            price.text = food.price
            restaurant.text = food.restaurant
        }


    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(
        holder: viewHolder,
        position: Int,
    ) {
        holder.bind(imgList[position])
    }

//    override fun onViewRecycled(holder: viewHolder) {
//        super.onViewRecycled(holder)
//        Glide.with(holder.itemView.context).clear(
//            holder.itemView.findViewById<ImageView>(R.id.imgFood)
//        )
//    }
//
//    override fun onViewDetachedFromWindow(holder: viewHolder) {
//        super.onViewDetachedFromWindow(holder)
//        holder.itemView.clearAnimation()
//    }
    override fun onViewRecycled(holder: viewHolder) {
        super.onViewRecycled(holder)
    Glide.with(holder.itemView.context).clear(
        holder.itemView.findViewById<ImageView>(R.id.imgFood)
    )
    }

    override fun onViewDetachedFromWindow(holder: viewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }
}