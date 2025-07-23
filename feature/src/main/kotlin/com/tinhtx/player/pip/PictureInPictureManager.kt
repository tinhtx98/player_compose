package com.tinhtx.player.pip

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Rational
import androidx.annotation.RequiresApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PictureInPictureManager @Inject constructor(
    private val context: Context
) {

    fun isPictureInPictureSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun enterPictureInPictureMode(activity: Activity): Boolean {
        return if (isPictureInPictureSupported()) {
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(Rational(16, 9))
                .build()

            activity.enterPictureInPictureMode(params)
        } else {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updatePictureInPictureParams(
        activity: Activity,
        aspectRatio: Rational = Rational(16, 9)
    ) {
        if (isPictureInPictureSupported() && activity.isInPictureInPictureMode) {
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()

            activity.setPictureInPictureParams(params)
        }
    }

    fun isInPictureInPictureMode(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.isInPictureInPictureMode
        } else {
            false
        }
    }
}
