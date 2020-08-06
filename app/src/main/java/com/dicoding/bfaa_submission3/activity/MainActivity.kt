package com.dicoding.bfaa_submission3.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.bfaa_submission3.R
import com.dicoding.bfaa_submission3.adapter.UserAdapter
import com.dicoding.bfaa_submission3.model.User
import com.dicoding.bfaa_submission3.service.Reminder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var list = ArrayList<User>()
    private var title = "Github User"
    private lateinit var preferenceFragment: PreferenceFragment
    private lateinit var reminder: Reminder

    companion object {
        private const val STATE_TITLE = "state_string"
        private const val STATE_LIST = "state_list"
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_user.setHasFixedSize(true)

        if (savedInstanceState == null) {
            progressBar.visibility = View.INVISIBLE
            setActionBarTitle(title)
        } else {
            progressBar.visibility = View.INVISIBLE
            search22.visibility = View.INVISIBLE
            pencarian.visibility = View.INVISIBLE
            hasil.visibility = View.INVISIBLE
            title = savedInstanceState.getString(STATE_TITLE).toString()
            val stateList = savedInstanceState.getParcelableArrayList<User>(STATE_LIST)

            setActionBarTitle(title)
            if (stateList != null) {
                list.addAll(stateList)
                showListUserAfterRotation(list)
            }
        }

        preferenceFragment = PreferenceFragment()
        //  preferenceFragment.setReminder()
        reminder = Reminder()
    }

    fun getGithubUser(username1: String) {
        progressBar.visibility = View.VISIBLE
        search22.visibility = View.INVISIBLE
        pencarian.visibility = View.INVISIBLE
        hasil.visibility = View.INVISIBLE

        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username1"
        client.addHeader("Authorization", "token 9937a7686131207935f01626676ac2c529fba873")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                list.clear()
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val type = item.getString("type")
                        val avatar = item.getString("avatar_url")

                        val user = User(
                            username,
                            type,
                            avatar
                        )
                        list.add(user)
                    }

                    showListUser()

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
                progressBar.visibility = View.INVISIBLE
            }

        })
    }

    private fun showListUser() {
        rv_user.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = UserAdapter(list)
        rv_user.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showListUserAfterRotation(list2: ArrayList<User>) {
        rv_user.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = UserAdapter(list2)
        rv_user.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_TITLE, title)
        outState.putParcelableArrayList(STATE_LIST, list)
    }

    private fun setActionBarTitle(title: String?) {
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /*
            Gunakan method ini ketika search selesai atau OK
             */
            override fun onQueryTextSubmit(query: String): Boolean {
                //Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                if (!query.equals("")) {
                    getGithubUser(query)
                }
                return true
            }

            /*
            Gunakan method ini untuk merespon tiap perubahan huruf pada searchView
             */
            override fun onQueryTextChange(newText: String): Boolean {
                if (!newText.equals("")) {
                    getGithubUser(newText)
                }

                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            val mIntent = Intent(this@MainActivity, Setting::class.java)
            startActivity(mIntent)
        } else if (item.itemId == R.id.favorite) {
            val IntentFavorite = Intent(this@MainActivity, UserFavorit::class.java)
            startActivity(IntentFavorite)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSelectedUser(user: User) {

        val moveWithObjectIntent = Intent(this@MainActivity, UserDetail::class.java)
        moveWithObjectIntent.putExtra(UserDetail.EXTRA_USER, user)
        startActivity(moveWithObjectIntent)
    }


}
