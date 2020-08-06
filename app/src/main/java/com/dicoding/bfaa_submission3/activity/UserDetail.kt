package com.dicoding.bfaa_submission3.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.bfaa_submission3.R
import com.dicoding.bfaa_submission3.adapter.TablayoutAdapter
import com.dicoding.bfaa_submission3.db.DatabaseContract
import com.dicoding.bfaa_submission3.db.UserHelper
import com.dicoding.bfaa_submission3.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.json.JSONObject

class UserDetail : AppCompatActivity() {

    var list = ArrayList<User>()
    private lateinit var userHelper: UserHelper

    companion object {
        const val EXTRA_USER = "extra_user"
        private val TAG = UserDetail::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        setActionBarTitle()

        val user = intent.getParcelableExtra(EXTRA_USER) as User

        getDetailUser(user.username)

        val tablayoutAdapter = TablayoutAdapter(this, supportFragmentManager)
        tablayoutAdapter.username = user.username
        view_pager.adapter = tablayoutAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()


        //set button favorite
        val cek = userHelper.queryByUsername(user.username.toString())
        Log.d(TAG, cek.moveToFirst().toString())
        setButtonFavorite(cek.moveToFirst())


        //add or delete user from database
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
                values.put(DatabaseContract.UserColumns.IMAGE, user.avatar)

                val result = userHelper.insert(values)
                if (result > 0) {
                    btn_favorite.setImageResource(R.drawable.ic_favorite)
                    Toasty.success(this, "Success add to favorite", Toast.LENGTH_SHORT, true).show()
                }
            }
        }
    }

    fun setButtonFavorite(cek: Boolean) {
        if (cek) {
            btn_favorite.setImageResource(R.drawable.ic_favorite)
        } else {
            btn_favorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    fun getDetailUser(username1: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username1"
        client.addHeader("Authorization", "token 9937a7686131207935f01626676ac2c529fba873")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                list.clear()
                try {
                    val responseObject = JSONObject(result)
                    val name = responseObject["name"]
                    val username = responseObject["login"]
                    val photo = responseObject["avatar_url"]
                    val location = responseObject["location"]

                    tv_name.text = name.toString()
                    tv_username.text = username.toString()
                    tv_location.text = location.toString()
                    Glide.with(this@UserDetail)
                        .load(photo.toString())
                        .apply(RequestOptions().override(150, 150))
                        .into(img_photo)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray,
                error: Throwable
            ) {

            }

        })
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
            val IntentFavorite = Intent(this@UserDetail, UserFavorit::class.java)
            startActivity(IntentFavorite)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setActionBarTitle() {
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.action_bar_layout)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


}

