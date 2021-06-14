package com.openclassrooms.realestatemanager.utils

import android.Manifest
import android.content.Context
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions


object PermissionsUtils {

    fun hasLocationPermissions(context: Context) =
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )

}