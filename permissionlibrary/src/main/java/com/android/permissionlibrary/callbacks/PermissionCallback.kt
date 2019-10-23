package com.android.permissionlibrary.callbacks

interface PermissionCallback {
    fun onPermissionGranted(permissions: String)
    fun onPermissionDenied(permission: String)
}