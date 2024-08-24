package com.shimmer.store.ui.main.nearetFranchise

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.shimmer.store.R
import com.shimmer.store.databinding.NearetFranchiseBinding
import com.shimmer.store.ui.mainActivity.MainActivity
import com.shimmer.store.utils.PermissionUtils
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NearetFranchise : Fragment(), OnMapReadyCallback {
    private val viewModel: NearetFranchiseVM by viewModels()
    private var _binding: NearetFranchiseBinding? = null
    private val binding get() = _binding!!
    protected var map: GoogleMap? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    lateinit var fusedLocation: FusedLocationProviderClient

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
            topBarBack.ivCartLayout.visibility = View.GONE
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


//            val isLocationEnabled = PermissionUtils.isLocationEnabled(requireContext())
//            Log.e("TAG", "isLocationEnabled "+isLocationEnabled)


//            setUpMapIfNeeded();
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
        map?.uiSettings?.isZoomControlsEnabled = true

        val array = ArrayList<LatLng>()
        array.add(LatLng(28.635324, 77.224944))
        array.add(LatLng(28.641529,  77.120918))
        array.add(LatLng(28.546536,  77.7647568))
        array.add(LatLng(28.99876529,  77.340918))

        array.forEach {
            val markerOptions = MarkerOptions().position(it)
                .title("First Pit Stop")
                .icon(
                    BitmapDescriptorFactory
                        .fromResource(R.drawable.marker)
                )
            map?.addMarker(markerOptions)
            map?.setInfoWindowAdapter(InfoWindowAdapter(requireContext(), it));
        }


//        val cameraPosition = CameraPosition.Builder()
//            .target(array[0]).zoom(5.0f).build()
//        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

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


    class InfoWindowAdapter(private val myContext: Context, huduma_gpo: LatLng) :
        GoogleMap.InfoWindowAdapter {
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


    private fun setUpLocationListener() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 100
        )
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocation?.let {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            it.lastLocation.addOnSuccessListener { location: Location? ->
                Log.e("TAG", "onViewCreated: " + location.toString())

                if (location == null){
                    setUpLocationListener()
                    return@addOnSuccessListener
                }

                map?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
                    map?.animateCamera(CameraUpdateFactory.zoomTo(15f))
                    map?.isMyLocationEnabled = true
            }

//            fusedLocation.lastLocation?.let{
//                fusedLocation.lastLocation.addOnSuccessListener { location: Location ->
////            val latLong = LatLng(location!!.latitude, location.longitude)
//                    Log.e("TAG", "onViewCreated: " + location.toString())
////            val markerOptions = MarkerOptions().position((LatLng(location.latitude, location.longitude)))
////                .title("Current Location")
////                .icon(
////                    BitmapDescriptorFactory
////                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)
////                )
////            map?.addMarker(markerOptions)
////            val cameraPosition = CameraPosition.Builder()
////                .target(LatLng(location.latitude, location.longitude)).zoom(5.0f).build()
////            map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//
////            val update = CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude))
////            val zoom = CameraUpdateFactory.zoomTo(5f)
//                    map?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
//                    map?.animateCamera(CameraUpdateFactory.zoomTo(15f))
//                    map?.isMyLocationEnabled = true
//                }
//            }

        }



    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(requireContext()) -> {
                when {
                    PermissionUtils.isLocationEnabled(requireContext()) -> {
                        setUpMapIfNeeded()
                        setUpLocationListener()
                    }

                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(requireContext())
                    }
                }
            }

            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    MainActivity.mainActivity.get()!!,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            LOCATION_PERMISSION_REQUEST_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    when {
//                        PermissionUtils.isLocationEnabled(this) -> {
//                            setUpLocationListener()
//                        }
//                        else -> {
//                            PermissionUtils.showGPSNotEnabledDialog(this)
//                        }
//                    }
//                } else {
//                    Toast.makeText(
//                        this,
//                        getString(R.string.location_permission_not_granted),
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
//    }
}