package com.dicoding.bfaa_submission3.adapter

import com.dicoding.bfaa_submission3.model.User
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.bfaa_submission3.R
import kotlinx.android.synthetic.main.item_followers_user.view.*

class FollowersAdapter(val listUser: ArrayList<User>) :
    RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): FollowersViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_followers_user, viewGroup, false)
        return FollowersViewHolder(view)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    inner class FollowersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(img_photo)

                itemView.txt_name.text = user.username
                itemView.txt_locale.text = user.type

            }
        }
    }

}