package br.com.mapsmarker.features.home

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.com.mapsmarker.R
import br.com.mapsmarker.base.BaseActivity
import br.com.mapsmarker.base.UiListeners
import br.com.mapsmarker.base.closeKeyboard
import br.com.mapsmarker.features.Constants.KEY_PLACE
import br.com.mapsmarker.features.Constants.STATUS_API_OK
import br.com.mapsmarker.features.Constants.STATUS_API_OVER_QUERY_LIMIT
import br.com.mapsmarker.features.map.MapsActivity
import br.com.mapsmarker.model.api.ApiResponse
import br.com.mapsmarker.model.api.StatusEnum
import br.com.mapsmarker.model.domain.ResultVO
import br.com.mapsmarker.model.domain.SearchResponseVO
import kotlinx.android.synthetic.main.activity_home.*
import org.parceler.Parcels
import javax.inject.Inject

class HomeActivity(override val layoutResId: Int = R.layout.activity_home) :
        BaseActivity(), SearchView.OnQueryTextListener, UiListeners.OnClickListener {

    @Inject lateinit var viewModel: HomeViewModel
    private var adapter : HomeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLoadingStatus(viewModel, rvResultSearch, loadingIndicator)
        observeSearchResponse()
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter(this)
        rvResultSearch.layoutManager = LinearLayoutManager(this)
        rvResultSearch.setHasFixedSize(true)
        rvResultSearch.adapter = adapter
    }

    private fun observeSearchResponse() {
        viewModel.getResponse().observe(this, Observer<ApiResponse<SearchResponseVO>> {
            response -> response?.let {
                if (adapter == null) setupRecyclerView()
                processResponse(it)
            }
        })
    }

    private fun processResponse(response: ApiResponse<*>) {
        when (response.status) {
            StatusEnum.SUCCESS -> {
                if (response.data is SearchResponseVO) {
                    when (response.data.status) {
                        STATUS_API_OK -> adapter?.refreshLocationList(response.data.results)
                        STATUS_API_OVER_QUERY_LIMIT ->
                            Toast.makeText(this,
                                    response.data.errorMessage.substring(IntRange(0, 56)),
                                    Toast.LENGTH_LONG).show()
                        else -> adapter?.refreshLocationList(hasNoResults = true)
                    }
                }
            }

            StatusEnum.ERROR -> {
                Log.e(localClassName, response.error?.message, response.error)
                Toast.makeText(this, response.error?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(placesList: List<ResultVO>) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.apply { putExtra(KEY_PLACE, Parcels.wrap(placesList)) }
              .run { startActivity(this) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                this@HomeActivity.closeKeyboard()
                processResponse(ApiResponse(StatusEnum.LOADING, data = null, isLoading = false))
                return true
            }
        })

        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        closeKeyboard()
        viewModel.searchByQuery(query)
        return true
    }

    override fun onQueryTextChange(query: String): Boolean {
        with (query) {
            if (length > 4 && length % 2 == 0)
                viewModel.searchByQuery(query)
        }
        return true
    }
}
