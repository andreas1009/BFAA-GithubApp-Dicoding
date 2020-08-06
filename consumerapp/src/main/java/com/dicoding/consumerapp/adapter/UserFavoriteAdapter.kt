package com.dicoding.consumerapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.consumerapp.R
import com.dicoding.consumerapp.model.UserEntity
import kotlinx.android.synthetic.main.item_favorite_user.view.*
import java.util.*

class UserFavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<UserFavoriteAdapter.UserFavoriteViewHolder>() {

    private var onItemClickCallback: UserFavoriteAdapter.OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: UserFavoriteAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    var listUsers = ArrayList<UserEntity>()
        set(listUsers) {
            if (listUsers.size > 0) {
                this.listUsers.clear()
            }
            this.listUsers.addAll(listUsers)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserFavoriteAdapter.UserFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_user, parent, false)
        return UserFavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listUsers.size

    override fun onBindViewHolder(
        holder: UserFavoriteAdapter.UserFavoriteViewHolder,
        position: Int
    ) {
        holder.bind(listUsers[position])
    }

    inner class UserFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userEntity: UserEntity){
            with(itemView){
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