package com.zeoharlem.append.xtremecardz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.zeoharlem.append.xtremecardz.R
import com.zeoharlem.append.xtremecardz.models.CapturedImage

class CapturedImageAdapters(
        private val capturedImagesList: MutableList<CapturedImage>,
        private val onItemClickListener: OnItemClickListener
    ):
    RecyclerView.Adapter<CapturedImageAdapters.CapturedImageViewHolder>() {

    inner class CapturedImageViewHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener{
        val captureImageView: ImageView = v.findViewById(R.id.imgPhotoCaptured)
        lateinit var capturedImage: CapturedImage

        init {
            v.setOnClickListener(this)
        }

        fun setCapturedImageData(capturedImage: CapturedImage){
            this@CapturedImageViewHolder.capturedImage = capturedImage
        }

        override fun onClick(v: View?) {
            //onItemClickListener.onItemClicked(capturedImage, absoluteAdapterPosition)
            if(absoluteAdapterPosition != RecyclerView.NO_POSITION){
                onItemClickListener.onItemClicked(capturedImage, absoluteAdapterPosition)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClicked(capturedImage: CapturedImage, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CapturedImageViewHolder {
        val view    = LayoutInflater.from(parent.context).inflate(R.layout.item_image_captured, parent, false)
        return CapturedImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: CapturedImageViewHolder, position: Int) {
        val currentItem = capturedImagesList[position]

//        holder.captureImageView.load(currentItem.capturedItem){
//            crossfade(true)
//            transformations(CircleCropTransformation())
//        }
        //Glide.with(holder.captureImageView).load(currentItem.capturedItem).circleCrop().into(holder.captureImageView)
        //holder.captureImageView.setImageBitmap(currentItem.capturedItem)
        holder.capturedImage    = currentItem
    }

    fun removeItem(position: Int){
        capturedImagesList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getCapturedImagesList(): MutableList<CapturedImage> {
        return capturedImagesList
    }

    override fun getItemCount(): Int {
        return capturedImagesList.size
    }
}