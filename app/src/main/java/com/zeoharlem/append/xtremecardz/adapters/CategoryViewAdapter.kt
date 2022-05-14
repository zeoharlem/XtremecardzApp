package com.zeoharlem.append.xtremecardz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeoharlem.append.xtremecardz.databinding.HomeCategoryItemBinding
import com.zeoharlem.append.xtremecardz.models.Categories
import com.zeoharlem.append.xtremecardz.models.Category
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class CategoryViewAdapter: ListAdapter<Category, CategoryViewAdapter.HomeCategoryViewHolder>(CategoryDiffUtilItem) {

    private lateinit var categoryItemBinding: HomeCategoryItemBinding

    var categoryItemClicked: ((Category) -> Unit)?  = null

    companion object CategoryDiffUtilItem: DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    inner class HomeCategoryViewHolder(private val itemBinding: HomeCategoryItemBinding): RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemBinding.categoryClickFragment.setOnClickListener {
                categoryItemClicked!!.invoke(getItem(bindingAdapterPosition))
            }
        }

        fun bind(category: Category){
            itemBinding.categoryTextButton.text = category.name
        }
    }
//
//    override fun submitList(list: List<Category>?) {
//        super.submitList(list?.let { ArrayList(it) })
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryViewHolder {
        val layoutInflater  = LayoutInflater.from(parent.context)
        categoryItemBinding = HomeCategoryItemBinding.inflate(layoutInflater, parent, false)
        return HomeCategoryViewHolder(categoryItemBinding)
    }

    override fun onBindViewHolder(holder: HomeCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}