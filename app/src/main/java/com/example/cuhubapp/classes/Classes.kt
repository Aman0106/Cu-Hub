package com.example.cuhubapp.classes

class Classes {
    var name:String? = null
    var rawName:String? = null
    constructor()

    constructor(name:String?){
        this.name = name
    }
    constructor(name:String?, rawName:String?){
        this.name = name
        this.rawName = rawName
    }
}