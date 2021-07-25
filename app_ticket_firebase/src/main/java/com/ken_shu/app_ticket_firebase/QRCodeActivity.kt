package com.ken_shu.app_ticket_firebase

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.ken_shu.app_ticket_firebase.models.Order
import kotlinx.android.synthetic.main.activity_qrcode.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class QRCodeActivity : AppCompatActivity() {
    private val database = Firebase.database
    private val myRef = database.getReference("ticketsStock")
    private val PERMISSION_REQUEST_CODE = 200
    private lateinit var context : Context
    private lateinit var codeScanner:CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        context = this

        //check permission
        if (checkPermission()){
            // 執行QRCode程式
            runProgram()
        }else {
            //啟動動態核准對話框
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),PERMISSION_REQUEST_CODE)
        }
    }

    //取得使用者在動態核准對話框的決定
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_CODE){
            Toast.makeText(context , grantResults[0].toString(),Toast.LENGTH_SHORT).show()
            //執行QRCode程式
            //grantResults[0] = 0 -> Accept
            //grantResults[0] = -1 -> Deny
                if (grantResults[0] == 0){
                    runProgram()
                }else if(grantResults[0] == -1){
                    finish()
                }
        }
    }


    //執行QRCode程式
    fun runProgram(){
        title = "執行QRCode程式..."
        codeScanner = CodeScanner(context , scanner_view)
        //設定 codescanner 參數
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        //解碼回傳 Callback
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val result_txt = it.text //解碼內容
                AlertDialog.Builder(context)
                    .setTitle("QRCode內容")
                    .setMessage("${result_txt}")
                    .setPositiveButton("使用",{
                        dialogInterface , i ->
                        val order = Gson().fromJson<Order>(result_txt , Order::class.java)
                        //取的該票路徑
                        val path = order.Ticket.userName+ "/" + order.key
                        val transPath = "transaction/" + path
                        //刪除該票
                        myRef.child(transPath).removeValue()
                        //建立 transaction_history
                        val ts = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()).toString()
                        myRef.child("transaction_history/${path}/ts").setValue(ts)
                        myRef.child("transaction_history/${path}/key").setValue(order.key)
                        myRef.child("transaction_history/${path}/userName").setValue(order.Ticket.userName)
                        myRef.child("transaction_history/${path}/allTickets").setValue(order.Ticket.allTickets)
                        myRef.child("transaction_history/${path}/oneWay").setValue(order.Ticket.oneWay)
                        myRef.child("transaction_history/${path}/roundTrip").setValue(order.Ticket.roundTrip)
                        myRef.child("transaction_history/${path}/total").setValue(order.Ticket.total)
                        myRef.child("transaction_history/${path}/json").setValue(result_txt)
                        //結束
                        finish()
                    })
                    .setNegativeButton("取消" , DialogInterface.OnClickListener{
                        dialogInterface , i -> finish()
                    })
                    .create()
                    .show()
            }
        }
        //解碼錯誤
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                val result_txt = it.message //解碼內容
                AlertDialog.Builder(context)
                    .setTitle("錯誤內容")
                    .setMessage("${result_txt}")
                    .setNegativeButton("取消" , DialogInterface.OnClickListener{
                            dialogInterface , i -> finish()
                    })
                    .create()
                    .show()
            }
        }

        //執行
        codeScanner.startPreview()
    }

    override fun onStop() {
        super.onStop()
        try {
            codeScanner.releaseResources()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    //使用者是否有同意使用權限 ex : Camara
    private fun checkPermission() : Boolean{
        val check = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val result = (check == PackageManager.PERMISSION_GRANTED)
        if (result){
            Toast.makeText(context , "Permission is not OK" , Toast.LENGTH_SHORT).show()
            return true
        }else {
            Toast.makeText(context , "Permission is not granted" , Toast.LENGTH_SHORT).show()
            return false
        }
    }
}