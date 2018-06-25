package br.com.mapsmarker.features.map

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.com.mapsmarker.R
import br.com.mapsmarker.base.BaseActivity
import br.com.mapsmarker.features.Constants.KEY_PLACE
import br.com.mapsmarker.model.domain.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.parceler.Parcels
import javax.inject.Inject

class MapsActivity(override val layoutResId: Int = R.layout.activity_maps) :
        BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, MenuItem.OnMenuItemClickListener {

    @Inject lateinit var viewModel: MapsViewModel
    private lateinit var googleMap: GoogleMap
    private lateinit var actionItem: MenuItem
    private var location: ResultVO? = null
    private var mapLocation = hashMapOf<String, ResultVO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.setOnMarkerClickListener(this)

        val locationList = Parcels.unwrap<List<ResultVO>>(intent.getParcelableExtra<Parcelable>(KEY_PLACE))
        handleMarkers(locationList)
    }

    private fun handleMarkers(locationList: List<ResultVO>?) {
        locationList?.let {
            var hasCenteredMap = false
            var position: LatLng
            val maxPosition = LatLngBO(LAT_MIN_VALUE, LNG_MIN_VALUE)
            val minPosition = LatLngBO(LAT_MAX_VALUE, LNG_MAX_VALUE)

            it.forEach {
                position = LatLng(it.geometry.location.lat, it.geometry.location.lng)

                maxPosition.latitude = viewModel.getClosestToCriterion(
                        maxPosition.latitude, position.latitude, LAT_MAX_VALUE)

                maxPosition.longitude = viewModel.getClosestToCriterion(
                        maxPosition.longitude, position.longitude, LNG_MAX_VALUE)

                minPosition.latitude = viewModel.getClosestToCriterion(
                        minPosition.latitude, position.latitude, LAT_MIN_VALUE)

                minPosition.longitude = viewModel.getClosestToCriterion(
                        minPosition.longitude, position.longitude, LNG_MIN_VALUE)

                googleMap.addMarker(
                        MarkerOptions()
                                .position(position)
                                .title("${it.formattedAddress}: ${position.latitude} / ${position.longitude}")
                ).tag = it.placeId

                mapLocation.put(it.placeId, it)

                if (it.itemSelected) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 8.0F))
                    location = it
                    hasCenteredMap = true
                }
            }

            if (!hasCenteredMap) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        viewModel.getLatLngAverage(maxPosition, minPosition)))
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        location = mapLocation[marker.tag]
        observeLocationStored()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_maps, menu)
        actionItem = menu.findItem(R.id.action)
        actionItem.setOnMenuItemClickListener(this)
        observeLocationStored()
        return true
    }

    private fun observeLocationStored() {
        location?.let {
            viewModel.getLocationStored(location!!).observe(this, Observer<LocationDTO> {
                if (it != null) {
                    actionItem.title = getString(R.string.delete)
                    actionItem.icon = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_delete)
                } else {
                    actionItem.title = getString(R.string.save)
                    actionItem.icon = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_save)
                }
            })
        }
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.title) {
            getString(R.string.save) -> {
                viewModel.storeLocation(location, true)
                Toast.makeText(this, R.string.location_saved, Toast.LENGTH_LONG).show()
            }
            getString(R.string.delete) -> {
                AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_alert))
                        .setMessage(getString(R.string.dialog_confirmation))
                        .setPositiveButton(R.string.delete) {
                            _, _ -> viewModel.storeLocation(location, false)
                        }
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show()
            }
        }

        return true
    }

}
