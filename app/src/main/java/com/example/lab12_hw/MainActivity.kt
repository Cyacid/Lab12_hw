package com.example.lab12_hw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn).setOnClickListener {
            val url =
                "https://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=55ec6d6e-dc5c-4268-a725-d04cc262172b"
            val req = Request.Builder().url(url).build()

            OkHttpClient().newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("查詢失敗", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code == 200) {
                        if (response.body == null) return
                        // val json = response.toString()
                        //Toast.makeText(this@MainActivity,json,Toast.LENGTH_SHORT).show()
                        val data = Gson().fromJson(response.body?.string(), Data::class.java)
                        val items = arrayOfNulls<String>(data.result.results.size)
                        for (i in 0 until items.size)
                            items[i] = "\n列車即將進入 :${data.result.results[i].Station}" +
                                    "\n列車行駛目的地 :${data.result.results[i].Destination}"
                        runOnUiThread {
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("台北捷運列車到站站名")
                                .setItems(items, null)
                                .show()
                        }
                    } else if (!response.isSuccessful)
                        Log.e("伺服器錯誤", "${response.code} ${response.message}")
                    else
                        Log.e("其他錯誤", "${response.code} ${response.message}")
                }
            })
        }
    }
}
