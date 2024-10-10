package com.klifora.franchise.ui.mainActivity

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
import com.google.firebase.FirebaseOptions
import com.klifora.franchise.R
import com.klifora.franchise.databinding.MainActivityBinding
import com.klifora.franchise.datastore.DataStoreKeys.ADMIN_TOKEN
import com.klifora.franchise.datastore.DataStoreKeys.CUSTOMER_TOKEN
import com.klifora.franchise.datastore.DataStoreKeys.LOGIN_DATA
import com.klifora.franchise.datastore.DataStoreUtil.readData
import com.klifora.franchise.datastore.DataStoreUtil.saveData
import com.klifora.franchise.datastore.db.AppDatabase
import com.klifora.franchise.datastore.db.CartModel
import com.klifora.franchise.networking.ConnectivityManager
import com.klifora.franchise.networking.password
import com.klifora.franchise.networking.username
import com.klifora.franchise.ui.enums.LoginType
import com.klifora.franchise.ui.main.category.Category
import com.klifora.franchise.ui.main.home.Home
import com.klifora.franchise.ui.main.faq.Faq
import com.klifora.franchise.ui.main.profile.Profile
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.cartItemCount
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.cartItemLiveData
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.loginType
import com.klifora.franchise.utils.getDensityName
import com.klifora.franchise.utils.getToken
import com.klifora.franchise.utils.mainThread
import com.klifora.franchise.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import org.jsoup.internal.StringUtil.isNumeric
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
            AppDatabase::class.java, "klifora.db"
        ).build()

        typefacenunitosans_light = resources.getFont(R.font.nunitosans_light)
        typefacenunitosans_semibold = resources.getFont(R.font.nunitosans_semibold)

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


        getToken() {
            Log.e("TAG", "thisgetToken "+this)
        }

//        val dd = FirebaseOptions.fromResource(context.get()!!)?.getApiKey()
//        Log.e("TAG", "thisgetTokendd "+dd)
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
            put("username", username)
            put("password", password)
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
