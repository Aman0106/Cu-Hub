package com.example.cuhubapp.classes

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference

class FacultyUser : Parcelable {
    var active:Boolean? = true
    var name:String? = null
    var uid:String? = null
    var firebaseUid:String? = null
    var classes:ArrayList<String>? = null
    var path:String? = null

    constructor(parcel: Parcel) : this() {
        active = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        uid = parcel.readString()
        firebaseUid = parcel.readString()
        name = parcel.readString()
        path = parcel.readString()
    }

    constructor(){}

    constructor(
        active:Boolean?,
        uid:String?,
        name:String?,
        firebaseUid:String?,
        path:String?
    ){
        this.active = active
        this.uid = uid
        this.name = name
        this.firebaseUid = firebaseUid
        this.path = path
    }
    constructor(
        active:Boolean?,
        uid:String?,
        name:String?,
        firebaseUid:String?,
        classes:ArrayList<String>?
    ){
        this.active = active
        this.uid = uid
        this.name = name
        this.firebaseUid = firebaseUid
        this.classes = classes
    }
    constructor(
        active:Boolean?,
        uid:String?,
        name:String?,
        firebaseUid:String?,
        classes:ArrayList<String>?,
        path:String?
    ){
        this.active = active
        this.uid = uid
        this.name = name
        this.firebaseUid = firebaseUid
        this.classes = classes
        this.path = path
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(active)
        parcel.writeString(uid)
        parcel.writeString(firebaseUid)
        parcel.writeString(name)
        parcel.writeString(path)
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