package com.example.cuhubapp.utils

class StringParser {

    fun removeSpaceFromEnd(string: String):String{
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

    fun toLowerCase(string:String):String{
        var str = ""
        string.forEach{
            str += it.lowercase()
        }
        return str
    }

    fun extractYearCourseSectionGroup(string:String): ArrayList<String>{
        val list = ArrayList<String>()
        var item = ""
        var count = 0
        for (s in string){
            count++
            if(s=='/') {
                list += item
                item = ""
                continue
            }else if (count >= string.length){
                item += s
                list += item
                item = ""
            }
            item += s
        }

        return  list
    }

}