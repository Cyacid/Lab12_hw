package com.example.lab12_hw

class Data {
    lateinit var result: Result

    class Result {
        lateinit var results : Array<Results>

        class Results {
            var Station = "123"    //站名
            var Destination = "456"    //目的地
        }
    }
}