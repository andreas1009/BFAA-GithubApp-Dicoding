package com.dicoding.bfaa_submission3.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.bfaa_submission3.R
import com.dicoding.bfaa_submission3.service.Reminder

class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var reminder: Reminder

    private lateinit var REMINDER: String
    private lateinit var LANGUAGE: String

    private lateinit var isReminder: SwitchPreference
    private lateinit var languagePreference: Preference

    companion object {
        private const val DEFAULT_VALUE = "Tidak Ada"
    }

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
        setReminder()
    }

    private fun init() {
        REMINDER = resources.getString(R.string.key_reminder)
        LANGUAGE = resources.getString(R.string.key_language)

        isReminder = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
        languagePreference = findPreference<Preference>(LANGUAGE) as Preference


    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == REMINDER) {
            isReminder.isChecked = sharedPreferences.getBoolean(REMINDER, false)
            setReminder()
        }
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        isReminder.isChecked = sh.getBoolean(REMINDER, false)

        languagePreference.setOnPreferenceClickListener(object :
            Preference.OnPreferenceClickListener {
            override fun onPreferenceClick(preference: Preference?): Boolean {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
                return true
            }
        })
    }

    fun setReminder() {
        val sh = preferenceManager.sharedPreferences
        if (sh.getBoolean(REMINDER, false) == true) {
            //set alarm
            reminder = Reminder()
            activity?.let {
                reminder.setRepeatingReminder(
                    it,
                    Reminder.TYPE_REPEATING,
                    "08:04",
                    "Let's find popular user on Github!"
                )
            }
        } else {
            //delete alarm
            reminder = Reminder()
            activity?.let { reminder.cancelAlarm(it, Reminder.TYPE_REPEATING) }

        }
    }


}