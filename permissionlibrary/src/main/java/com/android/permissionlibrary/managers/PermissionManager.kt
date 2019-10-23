package com.android.permissionlibrary.managers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.android.permissionlibrary.callbacks.PermissionCallback
import com.tbruyelle.rxpermissions2.RxPermissions

class PermissionManager(
    private val mActivity: Activity,
    private val mListener: PermissionCallback
) {

    private val rxPermission: RxPermissions by lazy { RxPermissions(this.mActivity) }

    /**
     * This function allows you to request single permission
     * without splitting the process in two different places.
     * You only need to specify:
     * @param permission must be of type [Manifest.permission]
     * @return boolean -> true if it needs to be requested
     *                    false ft it was already requested
     */
    @SuppressLint("CheckResult")
    fun requestSinglePermission(permission: String) {
        if (isPermissingPresentInManifest(permission)) {
            this.rxPermission.request(permission).subscribe { checkPermissionState(it, permission) }
        } else {
            this.mListener.onPermissionDenied(permission)
        }
    }

    /**
     * Check if some permission was request and granted
     * @param permission must be of type [Manifest.permission]
     */
    fun isPermissionGranted(permission: String): Boolean =
        if (permission == Manifest.permission.GET_ACCOUNTS) {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 ||
                    selfPermissionIsGranted(permission)
        } else {
            selfPermissionIsGranted(permission)
        }

    /**
     * Checks if some permission is declared in the manifest.
     * @param permission must be of type [Manifest.permission]
     */
    fun isPermissingPresentInManifest(permission: String): Boolean = with(this.mActivity) {
        val packageInfo =
            this.packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        packageInfo.requestedPermissions.firstOrNull { it.contains(permission, true) } != null
    }

    private fun selfPermissionIsGranted(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(
            mActivity,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun checkPermissionState(isGranted: Boolean, permission: String) {
        if (isGranted) {
            this.notifyGrantedPermission(permission)
        } else {
            this.notifyDeniedPermission(permission)
        }
    }

    private fun notifyGrantedPermission(permissions: String) {
        this.mListener.onPermissionGranted(permissions)
    }

    private fun notifyDeniedPermission(permission: String) {
        this.mListener.onPermissionDenied(permission)
    }

}