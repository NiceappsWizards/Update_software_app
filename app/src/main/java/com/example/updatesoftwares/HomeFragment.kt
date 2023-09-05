package com.example.updatesoftwares

import android.Manifest
import android.app.AppOpsManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.updatesoftwares.Inter_Splash.Inter_Splash.InterstitialClosedListenerImplementerMasterSplash
import com.example.updatesoftwares.Inter_Splash.Inter_Splash.InterstitialClosedListenerMasterSplash
import com.example.updatesoftwares.Inter_Splash.Inter_Splash.InterstitialMasterSplash
import com.example.updatesoftwares.databinding.FragmentHomeBinding
import com.google.android.gms.ads.*


open class HomeFragment : Fragment() {
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var binding: FragmentHomeBinding
    private var code = 0

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDrawerLayout = requireActivity().findViewById(R.id.drawer_layout) as DrawerLayout
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view: View = binding.root
        /*mDrawerLayout = this.findViewById<View>(R.id.app_drawer)
                as DrawerLayout*/
        val unInstall = binding.Uninstall
        val dBtn = binding.getDownloaded
        binding.imageView2.setOnClickListener {
            mDrawerLayout.openDrawer(GravityCompat.START)
        }

        val sBtn = binding.getSystemApp
        val diviceInfo = binding.diveceinfo
        val systemUpdate = binding.checkUpdates

        //..
        //Getting Device info..
        diviceInfo.setOnClickListener {
            val intent = Intent(requireActivity(), DiviceInfo::class.java)
            // start your next activity
            startActivity(intent)
        }


        //...
        //UninstallApps..
        unInstall.setOnClickListener(View.OnClickListener {
            if (InterstitialMasterSplash.isAlreadyLoaded) {
                InterstitialMasterSplash.show_insterstitial( requireActivity())
                InterstitialClosedListenerImplementerMasterSplash.setOnInterstitialClosedMaster(
                    object :
                        InterstitialClosedListenerMasterSplash {
                        override fun onInterstitialClosed() {

                            code = 1
                            switchToDetailsActivity("Uninstall Apps", code.toString())
                        }

                        override fun onInterstitialFailedToShow() {
                            code = 1
                            switchToDetailsActivity("Uninstall Apps", code.toString())
                        }
                    })
            } else {
                code = 1
                switchToDetailsActivity("Uninstall Apps", code.toString())
            }



        })
        //...
        // showing system apps on recycler.
        sBtn.setOnClickListener {
            code = 2

            switchToDetailsActivity("System Apps", code.toString())

        }
        //...
        // showing downloaded apps on recycler.
        dBtn.setOnClickListener {
            if (InterstitialMasterSplash.isAlreadyLoaded) {
                InterstitialMasterSplash.show_insterstitial( requireActivity())
                InterstitialClosedListenerImplementerMasterSplash.setOnInterstitialClosedMaster(
                    object :
                        InterstitialClosedListenerMasterSplash {
                        override fun onInterstitialClosed() {
                            code = 0
                            switchToDetailsActivity("Downloaded Apps", code.toString())
                        }

                        override fun onInterstitialFailedToShow() {
                            code = 0
                            switchToDetailsActivity("Downloaded Apps", code.toString())
                        }
                    })
            } else {
                code = 0
                switchToDetailsActivity("Downloaded Apps", code.toString())
            }

        }

        //..
        // System Update
        systemUpdate.setOnClickListener {
            //val upDateSetting = Intent("android.settings.SYSTEM_UPDATE_SETTINGS")
            try {
                requireActivity().startActivity(Intent("android.settings.SYSTEM_UPDATE_SETTINGS"))
            } catch (activityNotFound: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "No Client Found", Toast.LENGTH_SHORT).show()
            }


        }
        binding.usageAccess.setOnClickListener {

            if (isAccessGranted()) {
                if (InterstitialMasterSplash.isAlreadyLoaded) {
                    InterstitialMasterSplash.show_insterstitial( requireActivity())
                    InterstitialClosedListenerImplementerMasterSplash.setOnInterstitialClosedMaster(
                        object :
                            InterstitialClosedListenerMasterSplash {
                            override fun onInterstitialClosed() {
                                try {
                                    val intent = Intent(requireContext(), AppUsageActivity::class.java)
                                    startActivity(intent)
                                }
                                catch (e:ActivityNotFoundException){
                                    e.printStackTrace()
                                }
                            }
                            override fun onInterstitialFailedToShow() {
                                try {
                                    val intent = Intent(requireContext(), AppUsageActivity::class.java)
                                    startActivity(intent)
                                }
                                catch (e:ActivityNotFoundException){
                                    e.printStackTrace()
                                }

                            }
                        })
                } else {
                    try {
                        val intent = Intent(requireContext(), AppUsageActivity::class.java)
                        startActivity(intent)
                    }
                    catch (e:ActivityNotFoundException){
                        e.printStackTrace()
                    }
                }


            } else {
                try {
                    val i = Intent(
                        Settings.ACTION_USAGE_ACCESS_SETTINGS,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    )
                    startActivity(i)
                }
                catch (e:ActivityNotFoundException){

                   e.printStackTrace()
                }


            }

        }
        //...
        // Switching to scan activity..
        binding.scan.setOnClickListener {
            if (InterstitialMasterSplash.isAlreadyLoaded) {
                InterstitialMasterSplash.show_insterstitial( requireActivity())
                InterstitialClosedListenerImplementerMasterSplash.setOnInterstitialClosedMaster(
                    object :
                        InterstitialClosedListenerMasterSplash {
                        override fun onInterstitialClosed() {
                            val intent = Intent(requireContext(), ScanApps::class.java)
                            startActivity(intent)

                        }

                        override fun onInterstitialFailedToShow() {
                            val intent = Intent(requireContext(), ScanApps::class.java)
                            startActivity(intent)
                        }
                    })
            } else {
                val intent = Intent(requireContext(), ScanApps::class.java)
                startActivity(intent)

            }

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBannerAd()
        Log.e("onViewCreated", "onViewCreated")
    }



    fun switchToDetailsActivity(heading: String, code: String) {
        try {
            val intent = Intent(requireContext(), DetailsActivity::class.java)
            intent.putExtra("heading", heading)
            intent.putExtra("code", code)
            //intent.putParcelableArrayListExtra("AppInfoList", ArrayList(List))
            // start your next activity
            startActivity(intent)
        } catch (e: Exception) {
            Log.d("TAG", "exception ")
        }

    }


    //..
    //  systemAndDownloadedAppsSeparater.


    private fun loadBannerAd() {
        val mAdView = AdView(requireContext())
        mAdView.setAdSize(AdSize.BANNER)
        mAdView.adUnitId = getString(R.string.banner_ad)
        binding.bannerHome.addView(mAdView)
        val adRequest = AdRequest.Builder()
            .build()
        Log.e("logee", "loggg")
        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {

            override fun onAdLoaded() {
                super.onAdLoaded()
                binding.lnbanner.visibility = View.VISIBLE
                Log.e("logee1", "onAdLoaded")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.e("logee1", "onAdFailedToLoad")
                binding.lnbanner.visibility = View.INVISIBLE
            }
        }
    }



    //check permission
    private fun isAccessGranted(): Boolean {
        return try {
            val packageManager: PackageManager? = activity?.getPackageManager()
            val applicationInfo =
                packageManager?.getApplicationInfo(requireActivity().getPackageName(), 0)
            val appOpsManager = activity?.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            var mode = 0
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo!!.uid, applicationInfo.packageName
                )
            }
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}