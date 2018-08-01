package distraction.com.connectme.fragments

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import distraction.com.connectme.R
import distraction.com.connectme.adapters.LevelAdapter
import distraction.com.connectme.data.LevelData
import distraction.com.connectme.utils.Res
import distraction.com.connectme.utils.multiFilter
import kotlinx.android.synthetic.main.fragment_level_select.*

interface OnItemSelectedListener : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = Unit
    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
}

class LevelSelectFragment : BaseFragment(), LevelAdapter.ItemClickListener, OnItemSelectedListener {

    private val levelLiveData = MutableLiveData<List<LevelData>>()
    private lateinit var gridSizeList: MutableList<String>
    private lateinit var targetList: MutableList<String>

    companion object {
        fun newInstance() = LevelSelectFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_level_select, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Res.init(context)

        levelLiveData.observe(this, Observer {
            it?.let { (levelRecyclerView.adapter as LevelAdapter).setLevelData(it) }
        })

        gridSizeList = Res.levelData!!.distinctBy { "${it.numRows}x${it.numCols}" }.map { "${it.numRows}x${it.numCols}" }.toMutableList().apply { add(0, "All") }
        targetList = Res.levelData!!.distinctBy { it.target }.map { it.target.toString() }.toMutableList().apply { add(0, "All") }

        gridSizeSpinner.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, gridSizeList).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        targetSpinner.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, targetList).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        gridSizeSpinner.onItemSelectedListener = this
        targetSpinner.onItemSelectedListener = this

        levelRecyclerView.adapter = LevelAdapter(context, Res.levelData!!, this)
        levelRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClick(data: LevelData, index: Int) {
        fragmentListener?.changeFragment(LevelFragment.newInstance(data.level))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val gridSize = gridSizeSpinner.selectedItem as String?
        val target = targetSpinner.selectedItem as String?

        levelLiveData.value = Res.levelData!!.multiFilter(
                {
                    when (gridSize) {
                        null -> true
                        "All" -> true
                        else -> {
                            val (row, col) = parsePair(gridSize)
                            it.numRows == row && it.numCols == col
                        }
                    }
                },
                {
                    when (target) {
                        null -> true
                        "All" -> true
                        else -> it.target == target.toInt()
                    }
                })
    }

    private fun parsePair(str: String): Pair<Int, Int> {
        val t = str.split("x")
        return Pair(t[0].toInt(), t[1].toInt())
    }

}