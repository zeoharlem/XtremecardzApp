package com.zeoharlem.append.xtremecardz.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.zeoharlem.append.xtremecardz.databinding.ProjectItemRowBinding
import com.zeoharlem.append.xtremecardz.databinding.ProjectViewItemBinding
import com.zeoharlem.append.xtremecardz.models.ProjectInfoX
import com.zeoharlem.append.xtremecardz.utils.Constants
import com.zeoharlem.append.xtremecardz.utils.MyCustomExtUtils.capitalizeWords

class ProjectInfoAdapter:PagingDataAdapter<ProjectInfoX, ProjectInfoAdapter.MyProjectViewHolder>(ProjectInfoDiffUtilItem) {

    private var viewBinding: ProjectItemRowBinding? = null
    var projectItemClickListener: ((v: ProjectInfoX) -> Unit)? = null

    companion object ProjectInfoDiffUtilItem: DiffUtil.ItemCallback<ProjectInfoX>(){
        override fun areItemsTheSame(oldItem: ProjectInfoX, newItem: ProjectInfoX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProjectInfoX, newItem: ProjectInfoX): Boolean {
            return oldItem == newItem
        }

    }

    inner class MyProjectViewHolder(private val viewItemBinding: ProjectItemRowBinding): RecyclerView.ViewHolder(viewItemBinding.root){

        init {
            viewItemBinding.root.setOnClickListener {
                projectItemClickListener!!.invoke(getItem(bindingAdapterPosition)!!)
            }
        }

        fun binding(projectInfoX: ProjectInfoX){
            viewItemBinding.fullname.text   = projectInfoX.fullname?.capitalizeWords()
            viewItemBinding.designation.text= projectInfoX.designation?.capitalizeWords()
            viewItemBinding.companyName.text= projectInfoX.companyName?.capitalizeWords()
            //viewItemBinding.companyAddress.text = projectInfoX.companyAddress
            //viewItemBinding.tipsDate.text   = projectInfoX.createdAt
            //val imageUrlLink    = Constants.BASE_URL.split("/api")[0]+"/"+projectInfoX.image
            val imageUrlLink    = Constants.BASE_URL+projectInfoX.image
            Log.e("Project", "binding: $imageUrlLink", )
            viewItemBinding.profileImage.load(imageUrlLink){
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun onBindViewHolder(holder: MyProjectViewHolder, position: Int) {
        holder.binding(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProjectViewHolder {
        val layoutInflater  = LayoutInflater.from(parent.context)
        viewBinding         = ProjectItemRowBinding.inflate(layoutInflater, parent, false)
        return MyProjectViewHolder(viewBinding!!)
    }
}