package com.photosharingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.photosharingapp.databinding.RecyclerfeedRowBinding
import com.photosharingapp.model.Post
import com.squareup.picasso.Picasso

class PostAdapter  (private val PostList : ArrayList<Post>): RecyclerView.Adapter<PostAdapter.PhotoHolder>() {
    class PhotoHolder ( val binding: RecyclerfeedRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
val binding = RecyclerfeedRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PhotoHolder(binding)

    }

    override fun getItemCount(): Int {
        return PostList.size


    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
         holder.binding.recyclerTextView.text= PostList.get(position).email
        holder.binding.recyclerYorumText.text = PostList.get(position).comment
        Picasso.get().load(PostList[position].dowlandUrl).into(holder.binding.recyclerImageView)

    }
}