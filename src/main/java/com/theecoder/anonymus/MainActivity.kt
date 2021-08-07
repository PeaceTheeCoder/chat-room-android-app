package com.theecoder.anonymus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.theecoder.anonymus.Session.LoginPref
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL ="https://strrikecode-chatroom-api.herokuapp.com/"

class MainActivity : AppCompatActivity() {

    lateinit var session: LoginPref
    lateinit var adapter : RecyclerAdapter
    lateinit var userName: String
    lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.splash_theme)
        session = LoginPref(this)
        if(session.checkLogin()) {
            setContentView(R.layout.activity_main)
            this.supportActionBar?.hide()

            var user: HashMap<String, String> = session.getUserDetail()
            userName = user.get(LoginPref.KEY_NAME).toString()

            rv = findViewById<RecyclerView>(R.id.recycler_view)
            rv.layoutManager = LinearLayoutManager(this).apply {
                stackFromEnd = true
            }


            findViewById<ImageButton>(R.id.send_button).setOnClickListener {
                var text = findViewById<EditText>(R.id.text_message)
                if (!text.text.toString().isEmpty()) {
                    rawJSON(userName, text.text.toString())
                    text.setText("")
                }
            }
            findViewById<ImageButton>(R.id.refresh_button).setOnClickListener {
                geMyData()
            }
            geMyData()
        }
    }

    private fun rawJSON(userName: String,text: String) {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()

        // Create Service
        val service = retrofit.create(ApiInterface::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("username", userName)
        jsonObject.put("text", text)

        Log.d("Pretty Printed  :",jsonObject.toString())
       val jsonArray = JSONArray()
        jsonArray.put(jsonObject)
        Log.d("Pretty Printed JSON :",jsonArray.toString())
        // Convert JSONObject to String
        val jsonArrayString = jsonArray.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonArrayString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.sendMessage(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

                    Log.d("Pretty Printed JSON :", prettyJson)
                    geMyData()

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }

    private fun geMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()
        retrofitData.enqueue(object : Callback<List<MyDataItem>?> {
            override fun onResponse(
                call: Call<List<MyDataItem>?>,
                response: Response<List<MyDataItem>?>
            ) {
                val responseBody = response.body()!!

                adapter = RecyclerAdapter(responseBody,userName)
                adapter.notifyDataSetChanged()
                rv.adapter = adapter
            }

            override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Network Error, restart the app",Toast.LENGTH_SHORT).show()
            }
        })

    }

}

/*val retrofitData = retrofitBuilder.getData()
        retrofitData.enqueue(object : Callback<List<MyDataItem>?> {
           override fun onResponse(
               call: Call<List<MyDataItem>?>,
               response: Response<List<MyDataItem>?>
           ) {
               val responseBody = response.body()!!

               adapter = RecyclerAdapter(responseBody,userName)
               adapter.notifyDataSetChanged()
               rv.adapter = adapter
           }

           override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {
               Toast.makeText(this@MainActivity,"Error",Toast.LENGTH_SHORT).show()
           }
        })*/