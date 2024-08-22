package com.shimmer.store.ui.main.nearetFranchise

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.shimmer.store.R
import com.shimmer.store.databinding.NearetFranchiseBinding
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NearetFranchise : Fragment(), OnMapReadyCallback {
    private val viewModel: NearetFranchiseVM by viewModels()
    private var _binding: NearetFranchiseBinding? = null
    private val binding get() = _binding!!
    protected var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NearetFranchiseBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()!!.callBack(0)
        binding.apply {
            topBarBack.includeBackButton.apply {
                layoutBack.singleClick {
                    findNavController().navigateUp()
                }
            }

            topBarBack.ivCart.singleClick {
                findNavController().navigate(R.id.action_nearetFranchise_to_cart)
            }




//            val mapFragment: SupportMapFragment =
//                activity?.supportFragmentManager
//                    ?.findFragmentById(R.id.map) as SupportMapFragment
//
//            mapFragment.getMapAsync(this);


//            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//            mapFragment!!.getMapAsync(this::onMapReady)

//            val googleMap = (childFragmentManager.findFragmentById(
//                R.id.map
//            ) as SupportMapFragment?)!!.getMapAsync(this)

            setUpMapIfNeeded();


        }
    }


    private fun setUpMapIfNeeded() {
//        if (map == null) {
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?

//                mapFragment?.getMapAsync(OnMapReadyCallback { googleMap ->
//                    map = googleMap
//                    //do load your map and other map stuffs here...
//                })

//            map = mapFragment!!.getMapAsync(this)
              //map = mapFragment?.view?.findViewById(R.id.map) as GoogleMap
            mapFragment?.getMapAsync(this)

//            if (map != null) {
//                //setUpMap();
//                MarkerTask().execute()
//            }
//        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        //setUpMap()
//        setUpMapIfNeeded();

        Log.e("onMapReady", "onMapReady")
//        map!!.setMyLocationEnabled(true);
//        map.apply {
//            if (ActivityCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//
//                return
//            }
//        }
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL
//        map?.isMyLocationEnabled = true

        val array = ArrayList<LatLng>()
        array.add(LatLng(31.037933, 31.381523))
        array.add(LatLng(33.037933, 37.381523))
        array.add(LatLng(36.037933, 39.381523))
        array.add(LatLng(37.037933, 34.381523))

        array.forEach {
            val markerOptions = MarkerOptions().position(it)
                .title("First Pit Stop")
                .icon(
                    BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
        map?.addMarker(markerOptions)
            map?.setInfoWindowAdapter(InfoWindowAdapter(requireContext(), it));
        }


        val cameraPosition = CameraPosition.Builder()
            .target(array[0]).zoom(5.0f).build()
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

//        map?.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
//            // which is clicked and displaying it in a toast message.
//            val markerName: String = "" + marker.title
//            Toast.makeText(requireActivity(), "Clicked location is $markerName", Toast.LENGTH_SHORT)
//                .show()
//            false
//        })


//        val huduma_gpo = LatLng(-1.280694, 36.818277)
//        map?.addMarker(MarkerOptions().position(huduma_gpo).title("Huduma center GPO"))
//            ?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//
//
//        // For zooming automatically to the location of the marker
//        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(huduma_gpo, 12.0f))


    }


    class InfoWindowAdapter(private val myContext: Context, huduma_gpo: LatLng) : GoogleMap.InfoWindowAdapter{
        private val view: View

        init {
            val inflater =
                myContext.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            view = inflater.inflate(
                R.layout.map_info_window,
                null
            )
        }

        override fun getInfoContents(marker: Marker): View {
            if (marker != null
                && marker.isInfoWindowShown()
            ) {
                marker.hideInfoWindow()
                marker.showInfoWindow()
            }
            return view
        }

        override fun getInfoWindow(marker: Marker): View {
//            val title: String = ""+marker.getTitle()
//            val titleUi = (view.findViewById<View>(R.id.title) as TextView)
//            if (title != null) {
//                titleUi.text = title
//            } else {
//                titleUi.text = ""
//                titleUi.visibility = View.GONE
//            }
//
//            val snippet: String = marker.getSnippet()
//            val snippetUi = (view
//                .findViewById<View>(R.id.snippet) as TextView)
//
//            if (snippet != null) {
//                val SnippetArray: Array<String> =
//                    snippet.split(SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
//                        .toTypedArray()
//
//
//                snippetUi.text = SnippetArray[0]
//            } else {
//                snippetUi.text = ""
//            }

            return view
        }
    }

}