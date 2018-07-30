package distraction.com.connectme.fragments

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import distraction.com.connectme.R
import distraction.com.connectme.data.LevelData
import distraction.com.connectme.utils.Res
import distraction.com.connectme.utils.getColorCompat
import kotlinx.android.synthetic.main.fragment_level_select.*
import kotlinx.android.synthetic.main.level_list_item.view.*

class LevelSelectFragment : BaseFragment(), LevelAdapter.ItemClickListener<String> {

    companion object {
        fun newInstance() = LevelSelectFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_level_select, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Res.init(context)

        val data: MutableList<String> = MutableList(Res.levelData!!.size, {
            "Level ${it + 1}"
        })

        levelRecyclerView.adapter = LevelAdapter(context, data, this)
        levelRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClick(data: String, index: Int) {
        fragmentListener?.changeFragment(LevelFragment.newInstance(createLevelData(index + 1)))
    }

    fun createLevelData(level: Int): LevelData {
        with (Res.levelData!![level]) {
            return LevelData(level, numRows, numCols, grid, target, 0)
        }
    }

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