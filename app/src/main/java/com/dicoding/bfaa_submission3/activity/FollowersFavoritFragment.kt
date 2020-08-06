package com.dicoding.bfaa_submission3.activity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.bfaa_submission3.R
import com.dicoding.bfaa_submission3.adapter.FollowersFavoritAdapter
import com.dicoding.bfaa_submission3.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_followers_favorit.*
import org.json.JSONArray


class FollowersFavoritFragment : Fragment() {
    var list2 = ArrayList<User>()

    companion object {
        private val ARG_USERNAME = "username"
        private val TAG = FollowersFavoritFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val username2 = arguments?.getString(FollowersFavoritFragment.ARG_USERNAME)
        getFollowers(username2)
        return inflater.inflate(R.layout.fragment_followers_favorit, container, false)
    }

    fun newInstance(username: String?): FollowersFavoritFragment {
        val fragment = FollowersFavoritFragment()
        val bundle = Bundle()
        bundle.putString(FollowersFavoritFragment.ARG_USERNAME, username)
        fragment.arguments = bundle
        return fragment
    }

    fun getFollowers(username1: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username1/followers"
        client.addHeader("Authorization", "token 9937a7686131207935f01626676ac2c529fba873")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    list2.clear()
                    val response = JSONArray(result)
                    if (response.length() != 0) {
                        for (i in 0 until response.length()) {
                            val response2 = response.getJSONObject(i)

                            val username = response2.getString("login")
                            val type = response2.getString("type")
                            val avatar = response2.getString("avatar_url")

                            val user = User(
                                username,
                                type,
                                avatar
                            )

                            list2.add(user)
                        }
                        Log.d(FollowersFavoritFragment.TAG, list2.toString())
                        if (list2.size != 0) {
                            showListUser()
                        }

                    }

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
                Log.d(FollowersFavoritFragment.TAG, "ADA YANG SALAH")
            }

        })
    }

    private fun showListUser() {
        rv_followers.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = FollowersFavoritAdapter(list2)
        rv_followers.adapter = listUserAdapter
    }
}