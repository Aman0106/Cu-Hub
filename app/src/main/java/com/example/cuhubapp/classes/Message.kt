package com.example.cuhubapp.classes

class Message {
    var message:String? = null
    var senderUid:String? = null
    var timeStamp:String? = null
    constructor(){ }

    constructor(message: String?, senderUid: String?, timeStamp: String?) {
        this.message = message
        this.senderUid = senderUid
        this.timeStamp = timeStamp
    }

}