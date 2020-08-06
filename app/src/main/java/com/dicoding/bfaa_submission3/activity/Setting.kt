package com.dicoding.bfaa_submission3.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.bfaa_submission3.R

class Setting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setActionBarTitle()

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, PreferenceFragment())
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         if (item.itemId == R.id.favorite) {
            val IntentFavorite = Intent(this@Setting, UserFavorit::class.java)
            startActivity(IntentFavorite)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setActionBarTitle() {
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.action_bar_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}