package distraction.com.connectme.utils

import android.content.Context
import com.google.gson.Gson
import distraction.com.connectme.data.LevelData

object Res {

    var levelData: List<LevelData>? = null

    fun init(context: Context) {
        val stream = context.assets.open("leveldata.json")
        val buffer = ByteArray(stream.available())
        stream.read(buffer)
        val str = String(buffer)
        levelData = Gson().fromJson(str, Array<LevelData>::class.java).toList()
    }

}