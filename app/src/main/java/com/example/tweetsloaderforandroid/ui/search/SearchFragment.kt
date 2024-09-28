package com.example.tweetsloaderforandroid.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tweetsloaderforandroid.databinding.FragmentSearchBinding
import com.example.tweetsloaderforandroid.model.Tweet
import com.example.tweetsloaderforandroid.ui.TweetsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewModel =
            ViewModelProvider(this)[SearchViewModel::class.java]

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tweets: RecyclerView = binding.searchedTweets
        tweets.layoutManager = LinearLayoutManager(context)
        tweets.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val tweetsAdapter = TweetsAdapter()
        tweets.adapter = tweetsAdapter

        val searchView: SearchView = binding.searchBox
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if (query != null) {
                    searchViewModel.searchTweets(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        searchViewModel.tweets.observe(viewLifecycleOwner) {
            // 最後の要素が表示されることを保証するために番兵を追加
            // ただし、これはtemporary workaroundなので要改善
            // 番兵がないと最後まで表示されない理由がわからない
            // TODO: 番兵追加の処理をAdapterに移動する
            // https://ameblo.jp/highcommunicate/entry-12650107077.html
            val tweetsWithSentinel = it.plus(Tweet(
                "sentinel",
                "",
                if (it.isEmpty()) {
                    "ツイートが見つかりませんでした"
                } else {
                    ""
                }
            ))
            // 検索する度に一番上の位置に戻るようにする
            tweetsAdapter.submitList(tweetsWithSentinel) {
                tweets.scrollToPosition(0)
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}