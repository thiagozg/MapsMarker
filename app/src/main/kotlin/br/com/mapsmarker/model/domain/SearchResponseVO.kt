package br.com.mapsmarker.model.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class SearchResponseVO(
        @SerializedName("results")
        val results: List<ResultVO> = listOf(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("error_message")
        val errorMessage: String = ""
)

@Parcel
data class ResultVO(
        @SerializedName("formatted_address")
        val formattedAddress: String = "",
        @SerializedName("geometry")
        val geometry: GeometryVO = GeometryVO(),
        @SerializedName("place_id")
        val placeId: String = "",
        @SerializedName("types")
        val types: List<String> = listOf(),
        @Expose
        var itemSelected: Boolean = false,
        @Expose
        val placeAdapterType: PlaceAdapterTypeEnum = PlaceAdapterTypeEnum.NORMAL
)

@Parcel
data class GeometryVO(
        @SerializedName("location")
        val location: LocationVO = LocationVO(),
        @SerializedName("location_type")
        val locationType: String = ""
)

@Parcel
data class LocationVO(
        @SerializedName("lat")
        val lat: Double = 0.0,
        @SerializedName("lng")
        val lng: Double = 0.0
)
