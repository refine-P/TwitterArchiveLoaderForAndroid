package com.example.tweetsloaderforandroid.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tweetsloaderforandroid.databinding.FragmentHomeBinding
import com.example.tweetsloaderforandroid.model.Tweet
import com.example.tweetsloaderforandroid.ui.TweetsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tweets: RecyclerView = binding.tweets
        tweets.layoutManager = LinearLayoutManager(context)
        tweets.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val tweetsAdapter = TweetsAdapter()
        tweets.adapter = tweetsAdapter

        homeViewModel.tweets.observe(viewLifecycleOwner) {
            // 最後の要素が表示されることを保証するために番兵を追加
            // ただし、これはtemporary workaroundなので要改善
            // 番兵がないと最後まで表示されない理由がわからない
            // TODO: 番兵追加の処理をAdapterに移動する
            // https://ameblo.jp/highcommunicate/entry-12650107077.html
            val tweetsWithSentinel = it.plus(
                Tweet(
                    "sentinel",
                    "",
                    if (it.isEmpty()) {
                        "Settingsからツイートのアーカイブを読み込んでください"
                    } else {
                        ""
                    }
                )
            )
            // 前回読んだツイートの位置を保持するためにスクロールしない
            tweetsAdapter.submitList(tweetsWithSentinel)
        }
        return root
    }

    override fun onResume() {
        super.onResume()

        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.initTweets()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}