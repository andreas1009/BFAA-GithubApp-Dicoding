package com.dicoding.bfaa_submission3.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.bfaa_submission3.R
import com.dicoding.bfaa_submission3.adapter.TablayoutFavoritAdapter
import com.dicoding.bfaa_submission3.db.DatabaseContract
import com.dicoding.bfaa_submission3.db.UserHelper
import com.dicoding.bfaa_submission3.model.UserEntity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_user_detail_favorite.*

class UserDetailFavorite : AppCompatActivity() {

    private lateinit var userHelper: UserHelper

    companion object {
        const val EXTRA_USER = "extra_user"
        private val TAG = UserDetailFavorite::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail_favorite)

        setActionBarTitle()

        val user = intent.getParcelableExtra(EXTRA_USER) as UserEntity

        Glide.with(this)
            .load(user.image.toString())
            .apply(RequestOptions().override(150, 150))
            .into(img_photo)
        tv_name.text = user.name
        tv_username.text = user.username
        tv_location.text = user.location

        val tablayoutFavoritAdapter = TablayoutFavoritAdapter(this, supportFragmentManager)
        tablayoutFavoritAdapter.username = user.username
        view_pager.adapter = tablayoutFavoritAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        //open database
        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        btn_favorite.setOnClickListener {
            val check = userHelper.queryByUsername(user.username.toString())
            if (check.moveToFirst()) {
                //action delete
                var result = userHelper.deleteUser(user.username.toString())
                if (result > 0) {
                    Toasty.success(this, "Success delete from favorite", Toast.LENGTH_SHORT, true)
                        .show()
                    //change state button favorite
                    btn_favorite.setImageResource(R.drawable.ic_favorite_border)
                }
            } else {
                //action add
                val values = ContentValues()
                values.put(DatabaseContract.UserColumns.NAME, tv_name.text.toString())
                values.put(DatabaseContract.UserColumns.USERNAME, tv_username.text.toString())
                values.put(DatabaseContract.UserColumns.LOCATION, tv_location.text.toString())
                values.put(DatabaseContract.UserColumns.IMAGE, user.image)

                val result = userHelper.insert(values)
                if (result > 0) {
                    btn_favorite.setImageResource(R.drawable.ic_favorite)
                    Toasty.success(this, "Success add to favorite", Toast.LENGTH_SHORT, true).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        } else if (item.itemId == R.id.favorite) {
            val IntentFavorite = Intent(this@UserDetailFavorite, UserFavorit::class.java)
            startActivity(IntentFavorite)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setActionBarTitle() {
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.action_bar_favorite_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}