package com.hsf1002.sky.wanandroid.ui.activity

import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.AppCompatButton
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.gyf.barlibrary.ImmersionBar
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.base.BaseActivity
import com.hsf1002.sky.wanandroid.base.Preference
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.toast
import com.hsf1002.sky.wanandroid.ui.fragment.CommonUserFragment
import com.hsf1002.sky.wanandroid.ui.fragment.HomeFragment
import com.hsf1002.sky.wanandroid.ui.fragment.TypeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private var lastTime:Long = 0
    private var currentIndex = 0
    private var homeFragment: HomeFragment? = null
    private var typeFragment: TypeFragment? = null
    private var commonUserFragment: CommonUserFragment? = null

    private val isLogin:Boolean by Preference(Constant.LOGIN_KEY, false)
    private val username:String by Preference(Constant.USERNAME_KEY, "sky")
    private lateinit var tvUsername:TextView
    private lateinit var btnLogout:AppCompatButton

    private val fragmentManger by lazy {
        supportFragmentManager
    }

    override fun setLayoutId(): Int = R.layout.activity_main

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.main_toolbar).init()
    }

    override fun cancelRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        /****************************** toolbar ***********************/
        main_toolbar.run {
            setTitle(getString(R.string.app_name))
            setSupportActionBar(this)
        }

        /****************************** draw layout ***********************/
        drawerLayout.run {
            val toggle = ActionBarDrawerToggle(this@MainActivity, this, main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            addDrawerListener(toggle)
            toggle.syncState()
        }

        /****************************** navigation layout ***********************/
        navigationView.run {
            setNavigationItemSelectedListener (onDrawNavigationItemSelectedListener)
        }

        tvUsername = navigationView.getHeaderView(0).findViewById<TextView>(R.id.navigationViewUsername)
        btnLogout = navigationView.getHeaderView(0).findViewById<AppCompatButton>(R.id.navigationViewLogout)

        tvUsername.run {
            if (!isLogin)
            {
                setText(getString(R.string.not_login))
            }
            else
            {
                setText(username)
                //text = username
            }
        }

        btnLogout.run {
            if (!isLogin)
            {
                setText(getString(R.string.goto_login))
            }
            else{
                setText(getString(R.string.logout))
            }

            setOnClickListener{     ///////////////// the left bracket must adhere the function /////////////////
                if (!isLogin)
                {
                    Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivityForResult(this, Constant.MAIN_REQUEST_CODE)
                    }
                }
                else
                {
                    Preference.clear()
                    tvUsername.setText(getString(R.string.not_login))
                    setText(getString(R.string.goto_login))
                    //homeFragment?
                }
            }

        }

        /****************************** bottom view ***********************/
        bottomNavigation.run {
            setOnNavigationItemSelectedListener (onNavigationItemSelectedListener )
            selectedItemId = R.id.navigation_home
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuSearch)
        {
            Intent(this, SearchActivity::class.java).run {
                startActivity(this)
            }
            return true
        }

        when (item.itemId)
        {
            R.id.menuSearch ->
            {
                Intent(this, SearchActivity::class.java).run {
                    startActivity(this)
                }
                return true
            }
            R.id.menuHot ->
            {
                if (currentIndex == R.id.menuHot)
                {
                    //commonUserFragment?
                }
                setFragment(R.id.menuHot)
                currentIndex = R.id.menuHot
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setFragment(index:Int)
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        fragmentManager.beginTransaction().apply {
            homeFragment?:let{
                HomeFragment().let {
                    homeFragment = it
                    add(R.id.content, it)
                }
            }

            typeFragment?:let {
                TypeFragment().let {
                    typeFragment = it
                    add(R.id.content, it)
                }
            }

            commonUserFragment?:let {
                CommonUserFragment().let {
                    commonUserFragment = it
                    add(R.id.content, it)
                }
            }

            hideFragment(this)

            when (index)
            {
                R.id.navigation_home ->
                {
                    main_toolbar.setTitle(getString(R.string.app_name))
                    homeFragment?.let {
                        this.show(it)
                    }
                }
                R.id.navigation_type ->
                {
                    main_toolbar.setTitle(getString(R.string.title_dashboard))
                    typeFragment?.let {
                        this.show(it)
                    }
                }
                R.id.menuHot ->
                {
                    main_toolbar.setTitle(getString(R.string.hot_title))
                    commonUserFragment?.let {
                        this.show(it)
                    }
                }
                else ->
                {
                    // nothing here need to handle
                }
            }

        }.commit()
    }

    private fun hideFragment(transation:FragmentTransaction)
    {
        homeFragment?.let {
            transation.hide(it)
        }

        typeFragment?.let {
            transation.hide(it)
        }

        commonUserFragment?.let {
            transation.hide(it)
        }
    }

    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                setFragment(item.itemId)

                return@OnNavigationItemSelectedListener when(item.itemId)
                {
                    R.id.navigation_home ->
                    {
                        if (currentIndex == R.id.navigation_home)
                        {
                            //homeFragment?
                        }

                        currentIndex = R.id.navigation_home
                        true
                    }
                    R.id.navigation_type ->
                    {
                        if (currentIndex == R.id.navigation_type)
                        {
                            //typeFragment?
                        }

                        currentIndex = R.id.navigation_type
                        true
                    }
                    else ->
                    {
                        false
                    }
                }
            }

    private val onDrawNavigationItemSelectedListener =
            NavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId)
                {
                    R.id.nav_like ->
                    {
                        if (!isLogin)
                        {
                            Intent(this, LoginActivity::class.java).run {
                                startActivityForResult(this, Constant.MAIN_REQUEST_CODE)
                            }
                            toast(getString(R.string.login_please_login))
                            return@OnNavigationItemSelectedListener true
                        }
                        else{
                            Intent(this, SearchActivity::class.java).run {
                                putExtra(Constant.SEARCH_KEY, false)
                                startActivityForResult(this, Constant.MAIN_LIKE_REQUEST_CODE)
                            }
                        }
                    }
                    R.id.nav_about ->
                    {
                        Intent(this, AboutActivity::class.java).run {
                            startActivity(this)
                        }
                    }
                    else ->
                    {
                        // nothing here need to handle
                    }

                }
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }

}
