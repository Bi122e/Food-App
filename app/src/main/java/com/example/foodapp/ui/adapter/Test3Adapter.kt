//package com.example.foodapp.ui.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.foodapp.R
//
//
//class Test3Adapter(private val imageList: List<String>): RecyclerView.Adapter<Test3Adapter.viewHolder>() {
//    inner class viewHolder (view: View): RecyclerView.ViewHolder(view) {
//        val image = view.findViewById<ImageView>(R.id.imageView)
//    }
//
//    override fun getItemCount(): Int {
//        return Int.MAX_VALUE
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int,
//    ): viewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_slider_image, parent, false)
//        return viewHolder(view)
//    }
//
//    override fun onBindViewHolder(
//        holder: viewHolder,
//        position: Int,
//    ) {
//        val realImage = position % imageList.size
//        Glide.with(holder.itemView.context)
//            .load(imageList[realImage])
//            .into(holder.image)
//    }
//}