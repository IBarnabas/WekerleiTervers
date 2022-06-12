package hu.bme.aut.android.wekerleitervers.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.wekerleitervers.databinding.ActivityLocationsBinding
import androidx.recyclerview.widget.DividerItemDecoration
import hu.bme.aut.android.wekerleitervers.R
import hu.bme.aut.android.wekerleitervers.locations.LocationAdapter
import hu.bme.aut.android.wekerleitervers.locations.ModelRecycleLoc
import hu.bme.aut.android.wekerleitervers.navview.NavView


class LocationsActivity : AppCompatActivity(), LocationAdapter.OnLocationSelectedListener {
    private lateinit var binding: ActivityLocationsBinding
    private lateinit var adapter: LocationAdapter
    private lateinit var drawer: DrawerLayout
    private lateinit var navigation: NavView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)
        binding = ActivityLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar.toolbar
        setSupportActionBar(toolbar)
        drawer = binding.drawerLayout
        val id = 1
        navigation = NavView(this, drawer, toolbar, id, binding.nawview.navView)
        navigation.create()

        binding.mRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.mRecyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        initRecyclerView()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen((GravityCompat.START))) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun initRecyclerView() {
        binding.mRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LocationAdapter(this)

        var quote: String
        var text: String
        var model: ModelRecycleLoc
        for (i in 0..9) {
            quote = resources.getStringArray(R.array.loc_quotes)[i]
            text = resources.getStringArray(R.array.loc_texts)[i]
            model = ModelRecycleLoc(i, quote, text)
            adapter.newLocation(model)
        }

        binding.mRecyclerView.adapter = adapter
    }

    override fun onItemSelected(item: ModelRecycleLoc?) {
        val showActivityIntent = Intent()
        showActivityIntent.setClass(this, LocationDetailsActivity::class.java)
        showActivityIntent.putExtra(LocationDetailsActivity.id, item?.id.toString())
        startActivity(showActivityIntent)
    }
}