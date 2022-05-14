package com.zeoharlem.append.xtremecardz.adapters.sealed

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.zeoharlem.append.xtremecardz.models.Messages

open class MessageRecyclerViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {

    var viewClickedListener: ((view: View, item: Messages, p0: Int) -> Unit)? = null

}