package ch.epfl.toufi.android_utils.permissions

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.PERMISSION_DENIED_APP_OP
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.res.ResourcesCompat.ID_NULL


/**
 * Activity that allows mocking permissions for testing.
 * In Release mode, it does not redefine any method of [AppCompatActivity].
 */
open class MockPermissionsActivity @JvmOverloads constructor(
    @LayoutRes layoutRes: Int = ID_NULL
) : AppCompatActivity(layoutRes)
