package distraction.com.connectme.fragments

import android.content.Context
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import distraction.com.connectme.R
import distraction.com.connectme.data.LevelData
import kotlinx.android.synthetic.main.fragment_level_select.*
import kotlinx.android.synthetic.main.level_list_item.view.*

class LevelSelectFragment : BaseFragment(), LevelAdapter.ItemClickListener<String> {

    companion object {
        @JvmStatic
        fun newInstance() = LevelSelectFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_level_select, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data: MutableList<String> = MutableList(200, {
            "Level ${it + 1}"
        })


        levelRecyclerView.adapter = LevelAdapter(context, data, this)
        levelRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClick(data: String, index: Int) {
        fragmentListener?.changeFragment(LevelFragment.newInstance(createLevelData(index + 1)))
    }

    fun createLevelData(level: Int) = LevelData(level, 3, 3, intArrayOf(2, 0, 0, 1, 2, 0, 1, 1, 1), 3, 0)

}

class LevelAdapter(private val context: Context, private val data: List<String>, private val itemClickListener: ItemClickListener<String>) : RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {
    interface ItemClickListener<in T> {
        fun onItemClick(data: T, index: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LevelViewHolder {
        return LevelViewHolder(LayoutInflater.from(context).inflate(R.layout.level_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: LevelViewHolder?, position: Int) {
        val data = data[position]
        holder?.itemView?.apply {
            levelTextView?.text = data
            setBackgroundColor(if (position % 2 == 0) context.getColorCompat(R.color.white) else context.getColorCompat(R.color.gray))
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

fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)