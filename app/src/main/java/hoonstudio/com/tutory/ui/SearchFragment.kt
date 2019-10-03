package hoonstudio.com.tutory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hoonstudio.com.tutory.R
import hoonstudio.com.tutory.data.network.response.Hit
import hoonstudio.com.tutory.data.network.response.SearchResponse
import hoonstudio.com.tutory.data.viewmodel.SongViewModel
import hoonstudio.com.tutory.ui.adapter.SearchAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SearchFragment : Fragment(), SearchAdapter.OnSearchItemClickListener {
    private lateinit var songViewModel: SongViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var songList: List<Hit>

    // Creates the view hierarchy controlled by the fragment
    // Activities use setContentView() to specify the XML file that defines their layouts, but
    // fragments create their view hierarchy in onCreateView()
    // Fragments don't need onCreate() since they are attached to an Activity that calls it
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initRecyclerView(view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                lifecycleScope.launchWhenCreated {
                    songViewModel.getSong(query ?: "Dancing Mellow Fellow").observe(this@SearchFragment, Observer {
                        adapter.setSongList(it.response.hits)
                        songList = it.response.hits
                    })
                }


                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        songViewModel = ViewModelProviders.of(this).get(SongViewModel::class.java)
    }

    override fun onSearchItemClick(position: Int) {
        val fragment = LyricRecordFragment.newInstance()
        var args = Bundle()
        var current = songList.get(position)
        var songArtUrl = current.result.songArtImageUrl
        var songTitle = current.result.title
        var songArtist = current.result.primaryArtist.name
        var lyricUrl = current.result.url
        args.putString("title", songTitle)
        args.putString("artist", songArtist)
        args.putString("songArtUrl", songArtUrl)
        args.putString("lyricUrl", lyricUrl)
        fragment.arguments = args
        startFragment(fragment)
    }

    private fun startFragment(fragment: Fragment) {
        fragmentManager!!
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun initRecyclerView(view: View){
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)

        adapter = SearchAdapter(this)
        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}