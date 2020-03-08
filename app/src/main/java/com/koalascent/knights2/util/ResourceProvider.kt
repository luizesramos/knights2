package com.koalascent.knights2.util

import android.content.Context
import android.content.res.Resources
import java.io.InputStream
import java.lang.ref.WeakReference

/**
 * (c) 2020 Luiz Ramos All Rights Reserved
 */

interface ResourceProvider {
    fun screenWidth(): Int
    fun screenHeight(): Int
    @Throws
    fun openAsset(name: String): InputStream
}

class ResourceProviderWrapper(context: Context): ResourceProvider {

    private val weakContext = WeakReference(context)

    override fun screenWidth(): Int {
        return weakContext.get()?.resources?.displayMetrics?.widthPixels ?: 0
    }

    override fun screenHeight(): Int {
        return weakContext.get()?.resources?.displayMetrics?.heightPixels ?: 0
    }

    override fun openAsset(name: String): InputStream {
        return weakContext.get()?.resources?.assets?.open(name)
            ?: throw Resources.NotFoundException("Unable to open $name")
    }
}