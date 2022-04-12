package com.example.cuhubapp.classes

import android.os.Parcel
import android.os.Parcelable

class User: Parcelable  {

    var active:Boolean? = false
    var uid:String? = null
    var firebaseUid:String? = null
    var name:String? = null
    var course:String? = null
    var section:Long? = null
    var group:String? = null
    var yer:Long? = null

    constructor(parcel: Parcel) : this() {
        active = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        uid = parcel.readString()
        firebaseUid = parcel.readString()
        name = parcel.readString()
        course = parcel.readString()
        section = parcel.readLong()
        group = parcel.readString()
        yer = parcel.readLong()
    }

    constructor(){}

    constructor(
        active: Boolean?,
        uid: String?,
        firebaseUid: String?,
        name: String?,
        course: String?,
        section: Long?,
        group: String?,
        yer:Long?
    ) {
        this.active = active
        this.uid = uid
        this.firebaseUid = firebaseUid
        this.name = name
        this.course = course
        this.section = section
        this.group = group
        this.yer = yer
    }

    constructor(name: String?) {
        this.name = name
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(active)
        parcel.writeString(uid)
        parcel.writeString(firebaseUid)
        parcel.writeString(name)
        parcel.writeString(course)
        parcel.writeLong(section!!)
        parcel.writeString(group)
        parcel.writeLong(yer!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}