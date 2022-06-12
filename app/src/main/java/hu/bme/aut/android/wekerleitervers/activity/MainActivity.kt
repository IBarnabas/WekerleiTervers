package hu.bme.aut.android.wekerleitervers.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import hu.bme.aut.android.wekerleitervers.R
import hu.bme.aut.android.wekerleitervers.databinding.ActivityMainBinding
import hu.bme.aut.android.wekerleitervers.navview.NavView

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigation: NavView
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1500)
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar.toolbar
        setSupportActionBar(toolbar)
        drawer = binding.drawerLayout
        val id = 0
        navigation = NavView(this, drawer, toolbar, id, binding.nawview.navView)
        navigation.create()
    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen((GravityCompat.START))){
            drawer.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

}