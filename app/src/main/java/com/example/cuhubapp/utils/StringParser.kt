package com.example.cuhubapp.utils

class StringParser {

    public fun removeSpaceFromEnd(string: String):String{
        var str:String = string
        var pos = str.length-1
        if(str.endsWith(' ') || str.endsWith('\n')){
            str.forEach {
                if (it == ' ' || it == '\n')
                    pos--
            }
            return str.dropLast((str.length - pos)-1)
        }
        return str
    }

    public fun toLowerCase(string:String):String{
        var str = ""
        string.forEach{
            str += it.lowercase()
        }

        return str
    }
}