package distraction.com.connectme.data

import android.os.Parcel
import android.os.Parcelable

data class LevelData(val level: Int, val target: Int, val numRows: Int, val numCols: Int, val grid: IntArray) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.createIntArray())

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeInt(level)
        parcel?.writeInt(target)
        parcel?.writeInt(numRows)
        parcel?.writeInt(numCols)
        parcel?.writeIntArray(grid)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<LevelData> {
        override fun createFromParcel(parcel: Parcel): LevelData {
            return LevelData(parcel)
        }

        override fun newArray(size: Int): Array<LevelData?> {
            return arrayOfNulls(size)
        }
    }
}