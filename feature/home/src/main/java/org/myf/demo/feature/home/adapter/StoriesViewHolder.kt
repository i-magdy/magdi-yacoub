package org.myf.demo.feature.home.adapter

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.myf.demo.core.common.model.StoryModel
import org.myf.demo.feature.home.databinding.StoryListItemBinding

class StoriesViewHolder(
    private val binding: StoryListItemBinding
): ViewHolder(binding.root) {

    init {
        binding.setStoryClickListener {
            binding.story?.let {
                try {
                    itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    fun onBind(story: StoryModel){
        binding.story = story
        binding.executePendingBindings()
    }

}