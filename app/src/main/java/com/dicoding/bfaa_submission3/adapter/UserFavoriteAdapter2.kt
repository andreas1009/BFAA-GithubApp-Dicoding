package com.dicoding.bfaa_submission3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.bfaa_submission3.R
import com.dicoding.bfaa_submission3.model.UserEntity
import kotlinx.android.synthetic.main.item_favorite_user.view.*
import java.util.ArrayList

class UserFavoriteAdapter2(val listUserFavorite: ArrayList<UserEntity>) :
    RecyclerView.Adapter<UserFavoriteAdapter2.UserFavoriteViewHolder>() {

    private var onItemClickCallback: UserFavoriteAdapter2.OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: UserFavoriteAdapter2.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): UserFavoriteAdapter2.UserFavoriteViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_favorite_user, viewGroup, false)
        return UserFavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = listUserFavorite.size

    override fun onBindViewHolder(
        holder: UserFavoriteViewHolder,
        position: Int
    ) {
        holder.bind(listUserFavorite[position])
    }

    inner class UserFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userEntity: UserEntity) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(userEntity.image)
                    .apply(RequestOptions().override(100, 100))
                    .into(img_photo)

                itemView.txt_name.text = userEntity.name
                itemView.txt_username.text = userEntity.username

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(userEntity) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity)
    }
}