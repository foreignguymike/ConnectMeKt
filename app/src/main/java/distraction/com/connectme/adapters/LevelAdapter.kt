package distraction.com.connectme.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import distraction.com.connectme.R
import distraction.com.connectme.data.LevelData
import distraction.com.connectme.utils.getColorCompat
import distraction.com.connectme.utils.getScore
import kotlinx.android.synthetic.main.level_list_item.view.*

class LevelAdapter(private val context: Context, private var data: List<LevelData>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(data: LevelData, index: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LevelViewHolder {
        return LevelViewHolder(LayoutInflater.from(context).inflate(R.layout.level_list_item, parent, false))
    }

    fun setLevelData(data: List<LevelData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: LevelViewHolder?, position: Int) {
        val data = data[position]
        holder?.itemView?.apply {
            setBackgroundColor(if (position % 2 == 0) context.getColorCompat(R.color.white) else context.getColorCompat(R.color.gray))
            levelTextView.text = resources.getString(R.string.level_number, data.level)
            sizeTextView.text = resources.getString(R.string.sizexsize, data.numRows, data.numCols)
            targetTextView.text = resources.getString(R.string.target_number_2, data.target)

            with(getScore(context, data.level)) {
                starImage.setImageResource(if (this == data.target) R.drawable.star else if (this > 0) R.drawable.star_silver else R.drawable.star_empty)
            }
        }
    }

    inner class LevelViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(data[adapterPosition], adapterPosition)
        }
    }
}