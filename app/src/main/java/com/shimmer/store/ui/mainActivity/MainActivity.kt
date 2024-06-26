package com.shimmer.store.ui.mainActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
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
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.shimmer.store.R
import com.shimmer.store.databinding.MainActivityBinding
import com.shimmer.store.datastore.DataStoreKeys.LOGIN_DATA
import com.shimmer.store.datastore.DataStoreUtil.readData
import com.shimmer.store.networking.ConnectivityManager
import com.shimmer.store.ui.main.category.Category
import com.shimmer.store.ui.main.home.Home
import com.shimmer.store.ui.main.faq.Faq
import com.shimmer.store.ui.main.profile.Profile
import com.shimmer.store.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
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
        var typefaceroboto_light: Typeface? = null

        @JvmStatic
        var typefaceroboto_medium: Typeface? = null
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

        typefaceroboto_light = resources.getFont(R.font.roboto_light)
        typefaceroboto_medium = resources.getFont(R.font.roboto_medium)

        setBar()



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

                    when (destination.id) {
                        R.id.home -> {
                            bottomNavMain.getMenu().findItem(R.id.home).setChecked(true)
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
//        barHideShow()
//        callBack()

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
                    bottomNavMain.visibility = View.GONE
                }

                1 -> {
//                    toolbar.root.visibility = View.VISIBLE
                    bottomNavMain.visibility = View.VISIBLE
                }

                2 -> {
//                    toolbar.root.visibility = View.VISIBLE
                    bottomNavMain.visibility = View.GONE
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


}

