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
import distraction.com.connectme.utils.getScore
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
    private val completeList by lazy {
        with(resources) {
            listOf(all, finished, notBest, unfinished)
        }
    }

    private val all by lazy { resources.getString(R.string.all) }
    private val finished by lazy { resources.getString(R.string.finished) }
    private val notBest by lazy { resources.getString(R.string.not_best) }
    private val unfinished by lazy { resources.getString(R.string.unfinished) }

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

        gridSizeList = Res.levelData!!.distinctBy { "${it.numRows}x${it.numCols}" }.map { "${it.numRows}x${it.numCols}" }.toMutableList().apply { add(0, all) }
        targetList = Res.levelData!!.distinctBy { it.target }.map { it.target.toString() }.toMutableList().apply { add(0, all) }

        gridSizeSpinner.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, gridSizeList).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        targetSpinner.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, targetList).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        completeSpinner.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, completeList).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        gridSizeSpinner.onItemSelectedListener = this
        targetSpinner.onItemSelectedListener = this
        completeSpinner.onItemSelectedListener = this

        levelRecyclerView.adapter = LevelAdapter(context, Res.levelData!!, this)
        levelRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClick(data: LevelData, index: Int) {
        fragmentListener?.changeFragment(LevelFragment.newInstance(data.level))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val gridSize = gridSizeSpinner.selectedItem as String?
        val target = targetSpinner.selectedItem as String?
        val complete = completeSpinner.selectedItem as String?

        levelLiveData.value = Res.levelData!!.multiFilter(
                {
                    when (gridSize) {
                        null, all -> true
                        else -> {
                            val (row, col) = parsePair(gridSize)
                            it.numRows == row && it.numCols == col
                        }
                    }
                },
                {
                    when (target) {
                        null, all -> true
                        else -> it.target == target.toInt()
                    }
                },
                {
                    when (complete) {
                        null, all -> true
                        finished -> getScore(context, it.level) == it.target
                        notBest -> with(getScore(context, it.level)) { this > 0 && this != it.target }
                        unfinished -> getScore(context, it.level) == 0
                        else -> false
                    }
                })
    }

    private fun parsePair(str: String): Pair<Int, Int> {
        val t = str.split("x")
        return Pair(t[0].toInt(), t[1].toInt())
    }
}