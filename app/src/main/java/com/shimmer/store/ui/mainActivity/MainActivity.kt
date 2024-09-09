package com.shimmer.store.ui.mainActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.shimmer.store.R
import com.shimmer.store.databinding.MainActivityBinding
import com.shimmer.store.datastore.DataStoreKeys.ADMIN_TOKEN
import com.shimmer.store.datastore.DataStoreKeys.CUSTOMER_TOKEN
import com.shimmer.store.datastore.DataStoreKeys.LOGIN_DATA
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.datastore.DataStoreUtil.saveData
import com.shimmer.store.datastore.db.AppDatabase
import com.shimmer.store.datastore.db.CartModel
import com.shimmer.store.networking.ConnectivityManager
import com.shimmer.store.ui.enums.LoginType
import com.shimmer.store.ui.main.category.Category
import com.shimmer.store.ui.main.home.Home
import com.shimmer.store.ui.main.faq.Faq
import com.shimmer.store.ui.main.profile.Profile
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.cartItemCount
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.cartItemLiveData
import com.shimmer.store.ui.mainActivity.MainActivityVM.Companion.loginType
import com.shimmer.store.utils.getDensityName
import com.shimmer.store.utils.mainThread
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.lang.ref.WeakReference


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityVM by viewModels()

    companion object {
        @JvmStatic
        var PACKAGE_NAME = ""

        @JvmStatic
        var SIGNATURE_NAME = ""

        @JvmStatic
        var isBackApp = false

        @JvmStatic
        var isBackStack = false

        @JvmStatic
        var hideValueOff = 0

        @JvmStatic
        lateinit var context: WeakReference<Context>

        @JvmStatic
        lateinit var activity: WeakReference<Activity>

        @JvmStatic
        lateinit var mainActivity: WeakReference<MainActivity>

        var logoutAlert: AlertDialog? = null
        var deleteAlert: AlertDialog? = null

        @SuppressLint("StaticFieldLeak")
        var navHostFragment: NavHostFragment? = null

        private var _binding: MainActivityBinding? = null
        val binding get() = _binding!!

        @JvmStatic
        lateinit var isOpen: WeakReference<Boolean>

        @JvmStatic
        var scale10: Int = 0

        @JvmStatic
        var fontSize: Float = 0f

        @JvmStatic
        var networkFailed: Boolean = false

        lateinit var fragmentInFrame: Fragment

        @JvmStatic
        var typefacenunitosans_light: Typeface? = null

        @JvmStatic
        var typefacenunitosans_semibold: Typeface? = null

        @JvmStatic
        var db : AppDatabase?= null
    }

    private val connectivityManager by lazy { ConnectivityManager(this) }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        fragmentInFrame = navHostFragment!!.getChildFragmentManager().getFragments().get(0)
        context = WeakReference(this)
        activity = WeakReference(this)
        mainActivity = WeakReference(this)
        observeConnectivityManager()
        setupBottomNav()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "shimmer.db"
        ).build()

        typefacenunitosans_light = resources.getFont(R.font.nunitosans_light)
        typefacenunitosans_semibold = resources.getFont(R.font.nunitosans_semibold)

//        setBar()



        binding.apply {
            toolbar.appicon.singleClick {
                if (isBackStack) {
                    navHostFragment.findNavController().navigateUp()
                }
            }



            toolbar.editSearch.setOnClickListener {
                Log.e("TAG", "cardSearch")
                navHostFragment.findNavController().navigate(R.id.search)
            }
        }

//
//        mainThread {
////            var cart = CartModel(
////                product_id=174,
////                name="Posy Cluster Diamond Ring-G-18-YG-6",
////                price=29000.0,
////                quantity=1,
////                sku="SRI00010-G-18-YG-6",
////                color="19",
////                material_type="12",
////                purity="15",
////                size="4",
////                currentTime=1725617662842)
//
//            var cart = CartModel(
//                product_id=178,
//                name="Posy Cluster Diamond Ring-G-18-YG-6",
//                price=29000.0,
//              )
//
//            db?.cartDao()?.insertAll(cart)
//
//            Log.e("TAG", "newUser.toString() " + cart.toString())
//
//            val userList: List<CartModel>? = db?.cartDao()?.getAll()
//            Log.e("TAG", "metal_typeBBB " + userList?.size)
//        }



//        readData(CUSTOMER_TOKEN) { token ->
//            Log.e("TAG", "itAAAtoken " + token)
//            token?.let {
//                viewModel.getQuoteId(token, JSONObject()) {
//                    Log.e("TAG", "getQuoteId " + this)
//                    quoteId = this
//                }
//            }
//        }



//        val shipping_address = JSONObject().apply {
//            put("region", "arguments?.getString")
//            put("region_id", "arguments?.getString")
//            put("region_code", "arguments?.getString")
//            put("country_id", "IN")
//            put("street", "pending")
//            put("postcode", "pending")
//            put("city", "pending")
//            put("firstname", "pending")
//            put("lastname", "pending")
//            put("email", "pending")
//            put("telephone", "pending")
//        }
//
//        val billing_address = JSONObject().apply {
//            put("region", "arguments?.getString")
//            put("region_id", "arguments?.getString")
//            put("region_code", "arguments?.getString")
//            put("country_id", "IN")
//            put("street", "pending")
//            put("postcode", "pending")
//            put("city", "pending")
//            put("firstname", "pending")
//            put("lastname", "pending")
//            put("email", "pending")
//            put("telephone", "pending")
//        }
//
//        val addressInformation = JSONObject().apply {
//            put("addressInformation", JSONObject().apply {
//                put("shipping_address", shipping_address)
//                put("billing_address", billing_address)
//                put("shipping_carrier_code", "flatrate")
//                put("shipping_method_code", "flatrate")
//            })
//        }
//
//        Log.e("TAG", "jsonObjectaddressInformation "+addressInformation)

    }

    @SuppressLint("RestrictedApi")
    private fun setBar() {
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(false)
            it.setDisplayHomeAsUpEnabled(false)
            it.setDisplayShowTitleEnabled(false)
            it.setShowHideAnimationEnabled(false)
        }
    }


    private fun observeConnectivityManager() = try {
        connectivityManager.observe(this) {
            binding.tvInternet.isVisible = !it
            networkFailed = it
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }


    private fun setupBottomNav() {
        binding.apply {
            Companion.navHostFragment?.findNavController()
                ?.addOnDestinationChangedListener { _, destination, _ ->
                    if (
                        destination.id == R.id.home ||
                        destination.id == R.id.category ||
                        destination.id == R.id.faq ||
                        destination.id == R.id.profile
                    ) {
                        toolbar.appicon.setImageDrawable(
                            ContextCompat.getDrawable(
                                context.get()!!,
                                R.drawable.asl_drawer
                            )
                        )
                        toolbar.cardSearch.visibility = View.VISIBLE
                        toolbar.textViewTitle.visibility = View.GONE
                    } else {
                        toolbar.appicon.setImageDrawable(
                            ContextCompat.getDrawable(
                                context.get()!!,
                                R.drawable.baseline_west_24
                            )
                        )
                        toolbar.cardSearch.visibility = View.GONE
//                        toolbar.textViewTitle.visibility = View.VISIBLE
                    }

                    Log.e("TAG", "addOnDestinationChangedListener " + destination.id)

//                    bottomNavMain.getMenu().getItem(2).setChecked(true);
//                    bottomNavMain.itemIconTintList = null
                    when (destination.id) {
                        R.id.home -> {
                            bottomNavMain.getMenu().findItem(R.id.home).setChecked(true)
//                            bottomNavMain.getMenu().findItem(R.id.home).iconTintList = ColorStateList.valueOf(
//                                        ContextCompat.getColor(context.get()!!,R.color._28E0DE)
//                                    )
//                            bottomNavMain.itemIconTintList = null
//                            bottomNavMain.itemIconTintList =
//                                ColorStateList.valueOf(
//                                    ContextCompat.getColor(context.get()!!,R.color._28E0DE)
//                                )

                        }

                        R.id.category -> {
                            bottomNavMain.getMenu().findItem(R.id.category).setChecked(true)
                        }

                        R.id.faq -> {
                            bottomNavMain.getMenu().findItem(R.id.faq).setChecked(true)
                        }

                        R.id.profile -> {
                            bottomNavMain.getMenu().findItem(R.id.profile).setChecked(true)
                        }

                        R.id.products -> {
                            toolbar.textViewTitle.visibility = View.GONE
                        }

                        R.id.productDetail -> {
                            toolbar.textViewTitle.visibility = View.VISIBLE
                        }


                        else -> {
//                            toolbar.appicon.setTag("back")
                        }
                    }
                }





//            bottomNavMain.itemIconTintList = null


            bottomNavMain.setOnItemSelectedListener { item ->
                Log.e("TAG", "setOnItemSelectedListener ")
                when (item.itemId) {
                    R.id.home -> {
                        if (fragmentInFrame !is Home) {
                            navHostFragment.findNavController().navigate(R.id.home)
                        }
                    }

                    R.id.category -> {
                        if (fragmentInFrame !is Category) {
                            navHostFragment.findNavController().navigate(R.id.category)
                        }
                    }

                    R.id.faq -> {
                        if (fragmentInFrame !is Faq) {
                            navHostFragment.findNavController().navigate(R.id.faq)
                        }
                    }

                    R.id.profile -> {
                        if (fragmentInFrame !is Profile) {
                            navHostFragment.findNavController().navigate(R.id.profile)
                        }
                    }
                }
                true
            }

        }

    }


    override fun onStart() {
        super.onStart()
        val fontScale = resources.configuration.fontScale
        scale10 = when (fontScale) {
            0.8f -> 13
            0.9f -> 12
            1.0f -> 11
            1.1f -> 10
            1.2f -> 9
            1.3f -> 8
            1.5f -> 7
            1.7f -> 6
            2.0f -> 5
            else -> 4
        }

        val densityDpi = getDensityName()
//        Log.e("TAG", "densityDpiAA " + densityDpi)
        fontSize = when (densityDpi) {
            "xxxhdpi" -> 9f
            "xxhdpi" -> 9.5f
            "xhdpi" -> 10.5f
            "hdpi" -> 10.5f
            "mdpi" -> 11f
            "ldpi" -> 11.5f
            else -> 12f
        }
    }


    fun barHideShow() {
        readData(LOGIN_DATA) { loginUser ->
            if (loginUser == null) {
                binding.toolbar.root.visibility = View.GONE
                binding.bottomNavMain.visibility = View.GONE
            } else {
                binding.toolbar.root.visibility = View.VISIBLE
                binding.bottomNavMain.visibility = View.VISIBLE
            }
        }
    }


    fun callBack(hideValue: Int) {
//        setBar()
        binding.apply {
            when (hideValue) {
                0 -> {
//                    toolbar.root.visibility = View.GONE
                    bottomNavLayout?.visibility = View.GONE
                }

                1 -> {
//                    toolbar.root.visibility = View.VISIBLE
                    bottomNavLayout?.visibility = View.VISIBLE
                }

                2 -> {
//                    toolbar.root.visibility = View.VISIBLE
                    bottomNavLayout?.visibility = View.GONE
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.single_cart_item_badge, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (hideValueOff == 1) {
            menu?.findItem(R.id.item_search)?.isVisible = true
            menu?.findItem(R.id.item_cart_badge)?.isVisible = true
        } else if (hideValueOff == 2) {
            menu?.findItem(R.id.item_search)?.isVisible = false
            menu?.findItem(R.id.item_cart_badge)?.isVisible = true
        } else if (hideValueOff == 3) {
            menu?.findItem(R.id.item_search)?.isVisible = false
            menu?.findItem(R.id.item_cart_badge)?.isVisible = false
        } else {
            menu?.findItem(R.id.item_search)?.isVisible = false
            menu?.findItem(R.id.item_cart_badge)?.isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_search -> {
                Log.e("TAG", "onOptionsItemSelected: 1")
                navHostFragment?.findNavController()?.navigate(R.id.search)
            }

            R.id.item_cart_badge -> {
                Log.e("TAG", "onOptionsItemSelected: 2")
                navHostFragment?.findNavController()?.navigate(R.id.cart)
            }
        }
        return super.onOptionsItemSelected(item);
    }






    fun adminToken() {
        val obj: JSONObject = JSONObject().apply {
            put("username", "admin")
            put("password", "admin123")
        }
        viewModel.adminToken(obj){
            Log.e("TAG", "ADMIN_TOKENAAAA: "+this)
            saveData(ADMIN_TOKEN, this)

            val navOptions: NavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.navigation_bar, true)
                .build()
            runOnUiThread {
                navHostFragment?.navController?.navigate(R.id.login, null, navOptions)
            }
        }
    }





    fun callCartApi(){
        if (LoginType.CUSTOMER == loginType) {
            mainThread {
                val userList: List<CartModel>? = db?.cartDao()?.getAll()
                cartItemCount = 0
                userList?.forEach {
                    cartItemCount += it.quantity
                }
                cartItemLiveData.value = true
            }
        }

        if (LoginType.VENDOR == loginType) {
            readData(CUSTOMER_TOKEN) { token ->
                viewModel.getCart(token!!) {
                    val itemCart = this
                    Log.e("TAG", "getCart " + this.toString())
                    cartItemCount = 0
                    itemCart.items.forEach {
                        cartItemCount += it.qty
                    }
                    cartItemLiveData.value = true
                }
            }
        }

    }

}

