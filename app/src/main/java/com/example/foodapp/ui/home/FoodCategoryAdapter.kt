package com.example.foodapp.ui.home
import android.view.LayoutInflater
import com.example.foodapp.R
import com.example.foodapp.data.model.FoodCategory
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodCategoryAdapter(private val list: List<FoodCategory>): RecyclerView.Adapter<FoodCategoryAdapter.viewHolder>() {
    inner class viewHolder (val view: View): RecyclerView.ViewHolder(view) {
        val imageviewHolder = view.findViewById<ImageView>(R.id.imgCategory)
        val textView = view.findViewById<TextView>(R.id.tvCategory)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): viewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_category, parent, false)
        return viewHolder(viewHolder)
    }

    override fun onBindViewHolder(
        holder: viewHolder,
        position: Int,
    ) {
        holder.imageviewHolder.setImageResource(list[position].iconRes)
        holder.textView.text = list[position].name
    }
}