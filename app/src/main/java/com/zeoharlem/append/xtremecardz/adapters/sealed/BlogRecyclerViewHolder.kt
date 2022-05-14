package com.zeoharlem.append.xtremecardz.adapters.sealed

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import com.zeoharlem.append.xtremecardz.databinding.BlogComplexViewBinding
import com.zeoharlem.append.xtremecardz.databinding.BlogHeaderItemBinding
import com.zeoharlem.append.xtremecardz.databinding.BlogSimpleViewBinding
import com.zeoharlem.append.xtremecardz.models.sealed.BlogRecyclerViewSealed
import ng.com.zeoharlem.swopit.utils.MyCustomExtUtils.capitalizeWords
import ng.com.zeoharlem.swopit.utils.TrackingUtility

sealed class BlogRecyclerViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root){

    var viewClickedListener: ((view: View, item: BlogRecyclerViewSealed, p0: Int) -> Unit)? = null

    class BlogHeaderViewHolder(private val blogHeaderItemBinding: BlogHeaderItemBinding): BlogRecyclerViewHolder(blogHeaderItemBinding){

        fun bind(headerTitle: BlogRecyclerViewSealed.HeaderTitle){
            blogHeaderItemBinding.titleHeader.text  = headerTitle.title
        }
    }

    class BlogViewHolder(private val blogComplexViewBinding: BlogComplexViewBinding): BlogRecyclerViewHolder(blogComplexViewBinding){

        fun bind(blog: BlogRecyclerViewSealed.Blog){
            blogComplexViewBinding.blogTitle.text   = blog.blogTitle.capitalizeWords()
            blogComplexViewBinding.blogDate.text    = TrackingUtility.formatDateTimeAgo(blog.blogDate)
            blogComplexViewBinding.blogImage.load(blog.blogImage)

            blogComplexViewBinding.blogCardView.setOnClickListener {
                viewClickedListener?.invoke(it, blog, bindingAdapterPosition)
            }
        }
    }

    class TipsViewHolder(private val blogSimpleViewBinding: BlogSimpleViewBinding): BlogRecyclerViewHolder(blogSimpleViewBinding){
        fun bind(tips: BlogRecyclerViewSealed.Blog){
//            blogSimpleViewBinding.tipsTitle.text   = tips.blogTitle.capitalizeWords()
            blogSimpleViewBinding.tipsDate.text    = TrackingUtility.formatDateTimeAgo(tips.blogDate)
//            blogSimpleViewBinding.tipsImage.load(tips.blogImage)

            blogSimpleViewBinding.root.setOnClickListener {
                viewClickedListener?.invoke(it, tips, bindingAdapterPosition)
            }
        }
    }
}
