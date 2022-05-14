package com.zeoharlem.append.xtremecardz.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.zeoharlem.append.xtremecardz.R
import com.zeoharlem.append.xtremecardz.adapters.sealed.BlogRecyclerViewHolder
import com.zeoharlem.append.xtremecardz.databinding.BlogComplexViewBinding
import com.zeoharlem.append.xtremecardz.databinding.BlogHeaderItemBinding
import com.zeoharlem.append.xtremecardz.databinding.BlogSimpleViewBinding
import com.zeoharlem.append.xtremecardz.models.HotDeals
import com.zeoharlem.append.xtremecardz.models.sealed.BlogRecyclerViewSealed

class BlogRecyclerViewAdapter: PagingDataAdapter<BlogRecyclerViewSealed, BlogRecyclerViewHolder>(BlogDiffUtilItem) {

    var viewClickedListener: ((view: View, item: BlogRecyclerViewSealed, p0: Int) -> Unit)? = null

    companion object BlogDiffUtilItem: DiffUtil.ItemCallback<BlogRecyclerViewSealed>(){
        @SuppressLint("LogNotTimber")
        override fun areItemsTheSame(oldItem: BlogRecyclerViewSealed, newItem: BlogRecyclerViewSealed): Boolean {
            return when {
                oldItem is BlogRecyclerViewSealed.Blog && newItem is BlogRecyclerViewSealed.Blog -> {
                    oldItem.blogId == newItem.blogId
                }
                else -> {
                    false
                }
            }
        }

        override fun areContentsTheSame(oldItem: BlogRecyclerViewSealed, newItem: BlogRecyclerViewSealed): Boolean {
            return when {
                oldItem is BlogRecyclerViewSealed.Blog && newItem is BlogRecyclerViewSealed.Blog -> {
                    oldItem == newItem
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BlogRecyclerViewHolder, position: Int) {
        holder.viewClickedListener  = viewClickedListener
        when(holder){
            is BlogRecyclerViewHolder.BlogViewHolder -> holder.bind(getItem(position) as BlogRecyclerViewSealed.Blog)
            is BlogRecyclerViewHolder.TipsViewHolder -> holder.bind(getItem(position) as BlogRecyclerViewSealed.Blog)
        }
    }

    @SuppressLint("LogNotTimber")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogRecyclerViewHolder {
        return when(viewType){
            R.layout.blog_complex_view ->
                BlogRecyclerViewHolder.BlogViewHolder(
                    BlogComplexViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                )
            R.layout.blog_simple_view ->
                BlogRecyclerViewHolder.TipsViewHolder(
                    BlogSimpleViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                )
            else -> {
                throw IllegalAccessException("Invalid ViewType Provided")
            }
        }
    }

    @SuppressLint("LogNotTimber")
    override fun getItemViewType(position: Int): Int {
        ///Log.e("BlogRecyclerAdapter", "getItemViewType: ${getItem(position)?.javaClass}")
//        return when(getItem(position)){
//            is BlogRecyclerViewSealed.Tips -> R.layout.blog_simple_view
//            is BlogRecyclerViewSealed.Blog -> R.layout.blog_complex_view
//            is BlogRecyclerViewSealed.HeaderTitle -> R.layout.blog_header_item
//            else -> R.layout.blog_simple_view
//        }
        val blogRecyclerViewSeal    = getItem(position) as BlogRecyclerViewSealed.Blog
        return when(blogRecyclerViewSeal.blogId % 2){
            0 -> R.layout.blog_complex_view
            else -> R.layout.blog_simple_view
        }
    }
}