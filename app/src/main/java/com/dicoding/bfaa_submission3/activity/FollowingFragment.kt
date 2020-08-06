package com.dicoding.bfaa_submission3.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.bfaa_submission3.R
import com.dicoding.bfaa_submission3.adapter.FollowingAdapter
import com.dicoding.bfaa_submission3.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray


class FollowingFragment : Fragment() {
    var list2 = ArrayList<User>()

    companion object {
        private val ARG_USERNAME = "username"
        private val TAG = FollowingFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val username2 = arguments?.getString(ARG_USERNAME)
        getFollowing(username2)
        return inflater.inflate(R.layout.fragment_following, container, false)
    }


    fun newInstance(username: String?): FollowingFragment {
        val fragment = FollowingFragment()
        val bundle = Bundle()
        bundle.putString(ARG_USERNAME, username)
        fragment.arguments = bundle
        return fragment
    }

    fun getFollowing(username1: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username1/following"
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
                        Log.d(TAG, list2.toString())
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
                Log.d(TAG, "ADA YANG SALAH")
            }

        })
    }

    private fun showListUser() {
        rv_following.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = FollowingAdapter(list2)
        rv_following.adapter = listUserAdapter
    }

}