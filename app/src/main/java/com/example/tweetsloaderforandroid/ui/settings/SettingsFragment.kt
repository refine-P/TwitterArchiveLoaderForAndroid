package com.example.tweetsloaderforandroid.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tweetsloaderforandroid.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        const val TAG = "SettingsFragment"
        const val LAST_OPENED_URI_KEY =
            "com.example.tweetsloaderforandroid.pref.LAST_OPENED_URI_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.loadedArchiveName
        settingsViewModel.archiveName.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // https://zenn.dev/t2low/articles/ea610398e29154e1a887
        val loadTweets = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                activity?.contentResolver?.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                activity?.getSharedPreferences(TAG, Context.MODE_PRIVATE)?.edit {
                    putString(LAST_OPENED_URI_KEY, uri.toString())
                }

                settingsViewModel.loadTweets()
            }
        }

        val button: Button = binding.loadTweetsButton
        button.setOnClickListener {
            // tweets.jsを読み込む
            // TODO: 大元のzipファイルを読み込めるようにする
            // （なぜか読み込めるzipと読み込めないzipがある）
            // Virtual Deviceはjavascriptを読み込めないので、
            // tweets.jsをtweets.htmlにrenameしてからhtmlとして読み込む
            // （これなら、多分変なファイルを読む可能性は減らせるはず）
            loadTweets.launch(arrayOf("text/javascript", "text/html"))
        }

        // Set currently loaded file name.
        val fileName = settingsViewModel.getTweetsFileName()
        settingsViewModel.setLoadedFileName(fileName)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}