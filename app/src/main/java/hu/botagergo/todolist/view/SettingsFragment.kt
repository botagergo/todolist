package hu.botagergo.todolist.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import hu.botagergo.todolist.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}