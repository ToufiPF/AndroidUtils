package ch.epfl.toufi.android_utils.permissions

import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.PERMISSION_DENIED_APP_OP
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.res.ResourcesCompat.ID_NULL


open class MockPermissionsActivity @JvmOverloads constructor(
    @LayoutRes layoutRes: Int = ID_NULL
) : AppCompatActivity(layoutRes) {

    companion object {
        @JvmStatic
        private val TAG = MockPermissionsActivity::class.simpleName!!

        /**
         * Allows to configure which permissions are granted.
         * Map values are one of [PERMISSION_GRANTED],
         * [PERMISSION_DENIED] or [PERMISSION_DENIED_APP_OP].
         *
         * Not configured permissions are forwarded to original implementation
         * (e.g. to be able to run the app in debug mode).
         */
        val configuredSelfPermissions = HashMap<String, Int>()

        /**
         * Allows to configure the returned values of [shouldShowRequestPermissionRationale].
         *
         * Not configured permissions are forwarded to original implementation
         * (e.g. to be able to run the app in debug mode).
         */
        val configuredShouldShowRationale = HashMap<String, Boolean>()

        private fun flagToString(flag: Int?): String? = when (flag) {
            PERMISSION_GRANTED -> "granted"
            PERMISSION_DENIED -> "denied"
            PERMISSION_DENIED_APP_OP -> "denied_app_op"
            else -> null
        }
    }

    // called by ContextCompat.checkSelfPermission
    override fun checkPermission(permission: String, pid: Int, uid: Int): Int {
        val flag = configuredSelfPermissions[permission]
        val id = baseContext!!.applicationInfo.uid
        Log.i(
            TAG, "checkPermission($permission, $pid, $uid) (self=$id) : ${flagToString(flag)}"
        )
        return if (uid == id && flag != null) flag
        else super.checkPermission(permission, pid, uid)
    }

    // called by AppCompatActivity.checkSelfPermission
    override fun checkSelfPermission(permission: String): Int {
        val flag = configuredSelfPermissions[permission]
        Log.i(TAG, "checkSelfPermission($permission) : ${flagToString(flag)}")
        return flag ?: super.checkSelfPermission(permission)
    }

    // called by AppCompatActivity.shouldShowRequestPermissionRationale
    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        val flag = configuredShouldShowRationale[permission]
        Log.i(TAG, "shouldShowRequestPermissionRationale($permission) : $flag")
        return flag ?: super.shouldShowRequestPermissionRationale(permission)
    }
}
