package hu.bme.aut.android.wekerleitervers.locations

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.wekerleitervers.R
import hu.bme.aut.android.wekerleitervers.databinding.ItemLocBinding

class LocationAdapter(private val listener: OnLocationSelectedListener) : RecyclerView.Adapter<LocationAdapter.LocViewHolder>() {
    private val locations: MutableList<ModelRecycleLoc> = ArrayList()

    interface OnLocationSelectedListener{
        fun onItemSelected(item: ModelRecycleLoc?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loc, parent, false)
        return LocViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocViewHolder, pos: Int) {
        val item = locations[pos]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    inner class LocViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var binding = ItemLocBinding.bind(itemView)
        private var item: ModelRecycleLoc? = null

        init{
            binding.root.setOnClickListener{listener.onItemSelected(item)}
        }

        @SuppressLint("SetTextI18n")
        fun bind(newLoc: ModelRecycleLoc){
            item = newLoc
            binding.LocQuoteTV.text = (item?.id!! + 1).toString() + ". " + item?.quote.toString()
        }
    }

    fun newLocation(newLoc: ModelRecycleLoc){
        locations.add(newLoc)
        notifyItemInserted(locations.size - 1)
    }
}