package com.sergioteso.conecta4.activities

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.preference.RingtonePreference
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.Round

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 * See [Android Design: Settings](http://developer.android.com/design/patterns/settings.html)
 * for design guidelines and the [Settings API Guide](http://developer.android.com/guide/topics/ui/settings.html)
 * for more information on developing a Settings UI.
 */
class SettingsActivityC4 : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
        fragmentManager.beginTransaction().replace(android.R.id.content,
            GeneralPreferenceFragment()).commit()
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * {@inheritDoc}
     */
    override fun onIsMultiPane(): Boolean {
        return isXLargeTablet(this)
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onBuildHeaders(target: List<PreferenceActivity.Header>) {
        //loadHeadersFromResource(R.xml.pref_headers, target)

    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    override fun isValidFragment(fragmentName: String): Boolean {
        return PreferenceFragment::class.java.name == fragmentName
                || GeneralPreferenceFragment::class.java.name == fragmentName
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class GeneralPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)
            setHasOptionsMenu(true)

            val preferences = findPreference("prueba")
            preferences.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                Toast.makeText(activity, "Preferecia pulsada", Toast.LENGTH_SHORT).show()
                true
            }

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("board_name"))
            bindPreferenceSummaryToValue(findPreference("board_rows_list"))
            bindPreferenceSummaryToValue(findPreference("board_columns_list"))
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, SettingsActivityC4::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }
    }

    companion object {


        val BOARD_NAME_KEY = "board_name"
        val BOARD_ROWS_KEY = "board_rows_list"
        val BOARD_COLUMNS_KEY = "board_columns_list"
        val BOARD_NAME_DEFAULT = "Tablero C4 Default"
        val BOARD_ROWS_DEFAULT = "4"
        val BOARD_COLUMNS_DEFAULT = "4"
        val PLAYER_UUID_KEY = "board_player_uuid"
        val PLAYER_NAME_KEY = "board_player_name"
        val PLAYER_UUID_DEFAULT = "1234"
        val PLAYER_NAME_DEFAULT = "Segismundo"

        fun getName(context: Context): String {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(BOARD_NAME_KEY, BOARD_NAME_DEFAULT)
        }
        fun getRows(context: Context): Int {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(BOARD_ROWS_KEY, BOARD_ROWS_DEFAULT).toInt()
        }

        fun getColumns(context: Context): Int {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(BOARD_COLUMNS_KEY, BOARD_COLUMNS_DEFAULT).toInt()
        }

        fun getPlayerUUID(context: Context?): String{
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PLAYER_UUID_KEY, PLAYER_UUID_DEFAULT)
        }

        fun getPlayerName(context: Context?): String{
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PLAYER_NAME_KEY, PLAYER_NAME_DEFAULT)
        }

        fun setRowsSize(context: Context, size: Int) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putInt(SettingsActivityC4.BOARD_ROWS_KEY, size)
            editor.commit()
        }

        fun setColumnsSize(context: Context, size: Int) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putInt(SettingsActivityC4.BOARD_COLUMNS_KEY, size)
            editor.commit()
        }

        fun setPlayerUUID(context: Context, playerUuid: String) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(SettingsActivityC4.PLAYER_UUID_KEY, playerUuid)
            editor.commit()
        }

        fun setPlayerName(context: Context, name: String) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(SettingsActivityC4.PLAYER_NAME_KEY, name)
            editor.commit()
        }

        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()

            if (preference is ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                val listPreference = preference
                val index = listPreference.findIndexOfValue(stringValue)

                // Set the summary to reflect the new value.
                preference.setSummary(
                    if (index >= 0)
                        listPreference.entries[index]
                    else
                        null
                )

            }

            else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.summary = stringValue
            }
            true
        }

        /**
         * Helper method to determine if the device has an extra-large screen. For
         * example, 10" tablets are extra-large.
         */
        private fun isXLargeTablet(context: Context): Boolean {
            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
        }

        /**
         * Binds a preference's summary to its value. More specifically, when the
         * preference's value is changed, its summary (line of text below the
         * preference title) is updated to reflect the value. The summary is also
         * immediately updated upon calling this method. The exact display format is
         * dependent on the type of preference.

         * @see .sBindPreferenceSummaryToValueListener
         */
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set the listener to watch for value changes.
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                preference,
                PreferenceManager
                    .getDefaultSharedPreferences(preference.context)
                    .getString(preference.key, "")
            )
        }

    }
}
