package hu.bme.aut.android.wekerleitervers.navview
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.wekerleitervers.R
import hu.bme.aut.android.wekerleitervers.activity.DetailsProjectActivity
import hu.bme.aut.android.wekerleitervers.activity.LocationsActivity
import hu.bme.aut.android.wekerleitervers.activity.MainActivity
import hu.bme.aut.android.wekerleitervers.activity.MapsActivity

class NavView(private val activity: Activity?, private var drawer: DrawerLayout, private var toolbar: androidx.appcompat.widget.Toolbar, private val id: Int, private val navigationView: NavigationView): NavigationView.OnNavigationItemSelectedListener {
    fun create(){
        if (id != 4)
            navigationView.menu.getItem(id).isChecked = true
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(activity, drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val showActivityIntent = Intent()
        when (item.itemId){
            R.id.nav_mainpage->{
                if(id == 0){
                    drawer.closeDrawer(GravityCompat.START)
                    return true
                }
                showActivityIntent.setClass(activity!!, MainActivity::class.java)
                activity.startActivity(showActivityIntent)
            }
            R.id.nav_locations -> {
                if(id == 1){
                    drawer.closeDrawer(GravityCompat.START)
                    return true
                }
                showActivityIntent.setClass(activity!!, LocationsActivity::class.java)
                activity.startActivity(showActivityIntent)
            }
            R.id.nav_map -> {
                if(id == 2){
                    drawer.closeDrawer(GravityCompat.START)
                    return true
                }
                showActivityIntent.setClass(activity!!, MapsActivity::class.java)
                activity.startActivity(showActivityIntent)
            }
            R.id.nav_detail -> {
                if(id == 3){
                    drawer.closeDrawer(GravityCompat.START)
                    return true
                }
                showActivityIntent.setClass(activity!!, DetailsProjectActivity::class.java)
                activity.startActivity(showActivityIntent)
            }
            R.id.nav_fb -> {
                val webpage: Uri = Uri.parse("https://www.facebook.com/tervers")
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                if (intent.resolveActivity(activity!!.packageManager) != null) {
                    activity.startActivity(intent)
                }
            }
            R.id.nav_web -> {
                val webpage: Uri = Uri.parse("http://wekerleitervers.hu/")
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                if (intent.resolveActivity(activity!!.packageManager) != null) {
                    activity.startActivity(intent)
                }
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}