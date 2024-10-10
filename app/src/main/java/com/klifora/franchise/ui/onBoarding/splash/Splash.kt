package com.klifora.franchise.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.klifora.franchise.R
import com.klifora.franchise.databinding.SplashBinding
import com.klifora.franchise.datastore.DataStoreKeys.LOGIN_DATA
import com.klifora.franchise.datastore.DataStoreKeys.WEBSITE_ID
import com.klifora.franchise.datastore.DataStoreUtil.readData
import com.klifora.franchise.ui.enums.LoginType
import com.klifora.franchise.ui.mainActivity.MainActivity
import com.klifora.franchise.ui.mainActivity.MainActivity.Companion.navHostFragment
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.loginType
import com.klifora.franchise.ui.mainActivity.MainActivityVM.Companion.storeWebUrl
import com.klifora.franchise.ui.onBoarding.splash.SplashVM
import com.klifora.franchise.utils.ioThread
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class Splash : Fragment() {
    private var _binding: SplashBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SplashVM by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        MainActivity.mainActivity.get()?.callFragment(0)


        //  handleSplashTime()

//        binding.avFromCode.setAnimation("drink.json")
//        binding.avFromCode.playAnimation()
//        binding.avFromCode.addAnimatorListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator) {
//                Log.e("TAG", "onAnimationStart")
//            }
//
//            override fun onAnimationEnd(animation: Animator) {
//                Log.e("TAG", "onAnimationEnd")
//                handleSplashTime()
//            }
//
//            override fun onAnimationCancel(animation: Animator) {
//                Log.e("TAG", "onAnimationCancel")
//            }
//
//            override fun onAnimationRepeat(animation: Animator) {
//                Log.e("TAG", "onAnimationRepeat")
//            }
//        })


        readData(WEBSITE_ID) {
            storeWebUrl = it.toString()
        }


//        val obj: JSONObject = JSONObject().apply {
//            put("username", "admin")
//            put("password", "Admin@1234")
//        }
//        viewModel.adminToken(obj) {
//            Log.e("TAG", "ADMIN_TOKENAAAA: " + this)
//            saveData(ADMIN_TOKEN, this)
            handleSplashTime()
//        }
//        readData(ADMIN_TOKEN) { savedToken ->
//            if(savedToken == null){
//                viewModel.adminToken(obj){
//                    Log.e("TAG", "ADMIN_TOKENAAAA: "+this)
//                    saveData(ADMIN_TOKEN, this)
//                    handleSplashTime()
//                }
//            } else {
//                Log.e("TAG", "ADMIN_TOKENBBBB: "+savedToken)
//                handleSplashTime()
//            }
//        }
    }


    override fun onResume() {
        super.onResume()
    }

    private fun handleSplashTime() {
        ioThread {
            delay(1000)
            readData(LOGIN_DATA) { loginUser ->

//                val fragmentInFrame = navHostFragment!!.getChildFragmentManager().getFragments().get(0)
                if (loginUser == null) {
//                    if (fragmentInFrame !is Start){
//                    navHostFragment?.navController?.navigate(R.id.action_splash_to_login)
                    navHostFragment?.navController?.navigate(R.id.action_splash_to_loginOptions)
                    //isHide.value = false
                    MainActivity.mainActivity.get()!!.callBack(0)
//                    }
                } else {
//                    if (fragmentInFrame !is Dashboard){
//                        if(!MainActivity.isBackStack){
                    loginType = LoginType.VENDOR
//                    navHostFragment?.navController?.navigate(R.id.action_splash_to_home)
                    navHostFragment?.navController?.navigate(R.id.action_splash_to_home)
//                        }
                    //isHide.value = true
                    MainActivity.mainActivity.get()!!.callBack(1)
//                   }
                }
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


//    <com.google.android.material.appbar.MaterialToolbar
//    android:id="@+id/toolbar"
//    android:layout_width="match_parent"
//    android:layout_height="?attr/actionBarSize"
//    app:navigationIcon="?attr/homeAsUpIndicator"
//    app:layout_constraintStart_toStartOf="parent"
//    app:layout_constraintEnd_toEndOf="parent"
//    app:layout_constraintTop_toTopOf="parent"
//    android:backgroundTint="@color/_999999"
//    app:title="@string/app_name"
//    app:titleTextColor="@color/_000000"
//    android:background="@color/_999999"
//    app:menu="@menu/single_cart_item_badge"/>
}