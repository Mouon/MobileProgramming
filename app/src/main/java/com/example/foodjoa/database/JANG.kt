// JANG 데이터 클래스입니다 저희가 통일한 변수대로 해둿습니다.

package com.example.foodjoa.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bag")
data class JANG(
    @PrimaryKey(autoGenerate = true) val itemId: Int,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "productname") val productname: String?,
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "countunit") val countunit: Int,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "week") val week: Int,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "weekend") val weekend: String?) : java.io.Serializable,Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(itemId)
        parcel.writeString(category)
        parcel.writeString(productname)
        parcel.writeInt(count)
        parcel.writeInt(countunit)
        parcel.writeInt(price)
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeInt(day)
        parcel.writeInt(week)
        parcel.writeString(weekend)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JANG> {
        override fun createFromParcel(parcel: Parcel): JANG {
            return JANG(parcel)
        }

        override fun newArray(size: Int): Array<JANG?> {
            return arrayOfNulls(size)
        }
    }
}

