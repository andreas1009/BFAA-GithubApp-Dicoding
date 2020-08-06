package com.dicoding.bfaa_submission3.activity

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.bfaa_submission3.R
import com.dicoding.bfaa_submission3.adapter.UserFavoriteAdapter
import com.dicoding.bfaa_submission3.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.bfaa_submission3.helper.MappingHelper
import com.dicoding.bfaa_submission3.model.UserEntity
import kotlinx.android.synthetic.main.activity_user_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavorit : AppCompatActivity() {

    private lateinit var adapter: UserFavoriteAdapter

    companion object{
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_favorite)

        setActionBarTitle()

        rv_user_favorite.layoutManager = LinearLayoutManager(this)
        rv_user_favorite.setHasFixedSize(true)
        adapter = UserFavoriteAdapter(this)
        rv_user_favorite.adapter = adapter

        adapter.setOnItemClickCallback(object :
            UserFavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                val moveWithObjectIntent = Intent(this@UserFavorit, UserDetailFavorite::class.java)
                moveWithObjectIntent.putExtra(UserDetailFavorite.EXTRA_USER, data)
                startActivity(moveWithObjectIntent)
            }
        })


        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        loadUserAsync()

//        if (savedInstanceState == null) {
//            loadUserAsync()
//        } else {
//            val list = savedInstanceState.getParcelableArrayList<UserEntity>(EXTRA_STATE)
//            if (list != null) {
//                adapter.listUsers = list
//            }
//        }
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = deferredNotes.await()
            if (users.size > 0) {
                adapter.listUsers = users
            } else {
                adapter.listUsers = ArrayList()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listUsers)
    }

    private fun setActionBarTitle() {
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.action_bar_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            val mIntent = Intent(this@UserFavorit, Setting::class.java)
            startActivity(mIntent)
        } else if (item.itemId == R.id.favorite) {
            val IntentFavorite = Intent(this@UserFavorit, UserFavorit::class.java)
            startActivity(IntentFavorite)
        }
        return super.onOptionsItemSelected(item)
    }
}