package com.example.cuhubapp.classes

import android.os.Parcel
import android.os.Parcelable

class FacultyUser : Parcelable {
    var active:Boolean? = true
    var name:String? = null
    var uid:String? = null
    var firebaseUid:String? = null
    var classes:ArrayList<String>? = null

    constructor(parcel: Parcel) : this() {
        active = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        uid = parcel.readString()
        firebaseUid = parcel.readString()
        name = parcel.readString()
    }

    constructor(){}

    constructor(
        active:Boolean?,
        uid:String?,
        name:String?,
        firebaseUid:String?,
    ){
        this.active = active
        this.uid = uid
        this.name = name
        this.firebaseUid = firebaseUid
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(active)
        parcel.writeString(uid)
        parcel.writeString(firebaseUid)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FacultyUser> {
        override fun createFromParcel(parcel: Parcel): FacultyUser {
            return FacultyUser(parcel)
        }

        override fun newArray(size: Int): Array<FacultyUser?> {
            return arrayOfNulls(size)
        }
    }


}