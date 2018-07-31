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
import distraction.com.connectme.utils.getScore
import kotlinx.android.synthetic.main.fragment_level_select.*
import kotlinx.android.synthetic.main.level_list_item.view.*

class LevelSelectFragment : BaseFragment(), LevelAdapter.ItemClickListener {

    companion object {
        fun newInstance() = LevelSelectFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_level_select, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Res.init(context)

        levelRecyclerView.adapter = LevelAdapter(context, Res.levelData!!, this)
        levelRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClick(data: LevelData, index: Int) {
        fragmentListener?.changeFragment(LevelFragment.newInstance(data.level))
    }

}

class LevelAdapter(private val context: Context, private val data: List<LevelData>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(data: LevelData, index: Int)
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
            setBackgroundColor(if (position % 2 == 0) context.getColorCompat(R.color.white) else context.getColorCompat(R.color.gray))
            levelTextView.text = resources.getString(R.string.level_number, data.level)
            sizeTextView.text = resources.getString(R.string.sizexsize, data.numRows, data.numCols)
            targetTextView.text = resources.getString(R.string.target_number_2, data.target)

            with(getScore(context, data.level)) {
                starImage2.visibility = if (this > 0) View.VISIBLE else View.INVISIBLE
                starImage1.visibility = if (this == data.target) View.VISIBLE else View.INVISIBLE
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