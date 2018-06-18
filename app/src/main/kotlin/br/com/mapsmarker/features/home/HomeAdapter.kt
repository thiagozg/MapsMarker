package br.com.mapsmarker.features.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.mapsmarker.R
import br.com.mapsmarker.base.UiListeners
import br.com.mapsmarker.base.clearText
import br.com.mapsmarker.base.display
import br.com.mapsmarker.model.domain.PlaceAdapterTypeEnum
import br.com.mapsmarker.model.domain.ResultVO
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdapter(private val listener: UiListeners.OnClickListener) :
        RecyclerView.Adapter<HomeAdapter.PlaceHolder>() {

    private var locations = mutableListOf<ResultVO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_home, parent, false)
        return PlaceHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        val location = locations[position]
        if (location.placeAdapterType != PlaceAdapterTypeEnum.NO_RESULTS) {
            val hasMoreThanOneItem = itemCount > 1
            if (location.placeAdapterType != PlaceAdapterTypeEnum.DISPLAY_ALL)
                holder.bindItemData(location, hasMoreThanOneItem)
            else
                holder.bindLastItemData()
        } else {
            holder.showNoResults()
        }
    }

    override fun getItemCount() = locations.size

    fun refreshLocationList(locations: List<ResultVO>? = null,
                            hasNoResults: Boolean = false) {
        if (hasNoResults) {
            with(this.locations) {
                clear()
                add(ResultVO(placeAdapterType = PlaceAdapterTypeEnum.NO_RESULTS))
            }
        } else {
            locations?.let {
                this.locations = it.toMutableList()
                if (it.size > 1)
                    this.locations.add(ResultVO(placeAdapterType = PlaceAdapterTypeEnum.DISPLAY_ALL))
            }
        }
        notifyDataSetChanged()
    }

    inner class PlaceHolder(view: View, val listener: UiListeners.OnClickListener) :
            RecyclerView.ViewHolder(view) {

        fun bindItemData(resultVO: ResultVO, shoulShowBottomLine: Boolean) {
            with(itemView) {
                setOnClickListener { handleClickItem(resultVO) }

                tvTitle.text = String.format(
                        context.getString(R.string.place),
                        resultVO.formattedAddress)

                tvDescription.text = String.format(
                        context.getString(R.string.location),
                        resultVO.geometry.location.lat,
                        resultVO.geometry.location.lng)

                vLine.display(shoulShowBottomLine)
            }
        }

        fun bindLastItemData() {
            with(itemView) {
                setOnClickListener { handleClickItem() }
                tvTitle.text = context.getString(R.string.display_all)
                tvDescription.clearText()
                vLine.display(false)
            }
        }

        fun showNoResults() {
            with(itemView) {
                setOnClickListener { }
                tvTitle.text = context.getString(R.string.no_results)
                tvDescription.clearText()
                vLine.display(false)
            }
        }

        private fun handleClickItem(resultVO: ResultVO ? = null) {
            val locationsFiltered =
                    locations.filter { it.placeAdapterType == PlaceAdapterTypeEnum.NORMAL }
            locationsFiltered.map { it.itemSelected = it.placeId.equals(resultVO?.placeId) }
            listener.onClick(locationsFiltered)
        }

    }
}