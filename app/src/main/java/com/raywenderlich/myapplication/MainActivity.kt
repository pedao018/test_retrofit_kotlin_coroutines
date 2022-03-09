package com.raywenderlich.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.raywenderlich.myapplication.databinding.ActivityMainBinding
import com.raywenderlich.myapplication.repository.ItunesRepo
import com.raywenderlich.myapplication.service.ItunesService
import kotlinx.coroutines.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    companion object {
        val list_text_search =
            listOf<String>("Android", "iOS", "Java", "Ukraine", "American", "News")

        fun getTextSearch() = list_text_search.get(Random.nextInt(0, list_text_search.size - 1))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.textViewResult.setText("Haha")
        binding.btnGetapi.setOnClickListener { callAPI(getTextSearch()) }
        binding.btnGetmutilapi.setOnClickListener { callMutilAPI() }
        binding.btnGetmutilParallellapi.setOnClickListener { callParalleAPI() }

        viewModel = ViewModelProvider(this, MainViewModelFactory(0)).get(MainViewModel::class.java)
        setupViewModels()
    }

    private fun setupViewModels() {
        val service = ItunesService.instance
        viewModel.iTunesRepo = ItunesRepo(service)
    }

    //Gọi api bằng cách retrofit
    private fun callAPI(term: String) {
        showProgess()
        binding.btnGetapi.isEnabled = false
        //Background thread
        GlobalScope.launch {
            val listPodcast = viewModel.searchPodcasts(term)
            var s = ""
            for (item in listPodcast) {
                s += item.name + "\n"
            }
            //Main thread
            withContext(Dispatchers.Main) {
                binding.textViewResult.text = "${s}"
                hideProgess()
                binding.btnGetapi.isEnabled = true
            }
        }
    }

    //Gọi api bằng cách retrofit -> goi tuần tự
    private fun callMutilAPI() {
        showProgess()
        binding.btnGetmutilapi.isEnabled = false
        GlobalScope.launch {
            var s = ""
            var listPodcast: List<MainViewModel.PodcastSummaryViewData>
            repeat(10) { number ->
                listPodcast = viewModel.searchPodcasts_Mutil("Android", "$number + $s")
                for (item in listPodcast) {
                    s += item.name
                }
                s += "\n\n"
            }
            withContext(Dispatchers.Main) {
                binding.textViewResult.text = "${s}"
                hideProgess()
                binding.btnGetmutilapi.isEnabled = true
            }
        }
    }

    private fun callParalleAPI() {
        binding.btnGetmutilParallellapi.isEnabled = false
        showProgess()
        GlobalScope.launch {
            var s = ""
            /*val listPodcast1 = async { viewModel.searchPodcasts_Mutil(getTextSearch(), "") }
            val listPodcast2 = async { viewModel.searchPodcasts_Mutil(getTextSearch(), "") }

            s += "${listPodcast1.await().size} + ${listPodcast2.await().size}"*/

            /*repeat(10) { number ->
                val listPodcast =
                    async { viewModel.searchPodcasts_Mutil("Android", "$number + $s") }
                for (item in listPodcast.await()) {
                    s += item.name
                }
                s += "\n\n"
            }*/
            async {
                Log.e("hahaha", "Lauchn")
            }
            Log.e("hahaha", "haha")

            withContext(Dispatchers.Main) {
                binding.textViewResult.text = "${s}"
                hideProgess()
                binding.btnGetmutilParallellapi.isEnabled = true
            }
        }

    }

    private fun showProgess() {
        binding.textViewProgress.text = "Progressing"
        Log.e("hahaha", "Progressing")
    }

    private fun hideProgess() {
        binding.textViewProgress.text = "Complete!"
        Log.e("hahaha", "Complete!")
    }
}