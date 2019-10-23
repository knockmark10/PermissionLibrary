package com.android.softpermissions

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.permissionlibrary.callbacks.PermissionCallback
import com.android.permissionlibrary.managers.PermissionManager

class MainActivity : AppCompatActivity(), PermissionCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permission = PermissionManager(this, this)
        Thread.sleep(3000)
        permission.requestSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onPermissionGranted(permissions: String) {
        Log.d(MainActivity::class.java.name, "GRANTED")
    }

    override fun onPermissionDenied(permission: String) {
        Log.e(MainActivity::class.java.name, "GRANTED")
    }
}
