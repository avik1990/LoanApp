package com.diageo.edge.utils

import android.util.Log

object LogUtil {

    var isDebug = true
    var tag = "EDGE_PLAY"
    val maxLogSize = 800
    fun init(isDebug: Boolean) {
        this.isDebug = isDebug
    }

    fun v(tag: String, msg: String) {
        if (isDebug) {
            Log.v(tag, msg)
        }
    }

    fun v(msg: String) {
        if (isDebug) {
            Log.v(tag, msg)
        }
    }

    fun debug(tag: String, msg: String) {
        if (isDebug) {
            Log.d(tag, msg)
        }
    }

    fun debug(msg: String) {
        for (index in 0..msg.length / maxLogSize) {
            val start = index * maxLogSize
            var end = (index + 1) * maxLogSize
            end = if (end > msg.length) msg.length else end
            if (isDebug) {
                Log.d(tag, msg.substring(start, end))
            }
        }
        //    Log.i(tag, msg)
    }

    fun info(tag: String, msg: String) {
        if (isDebug) {
            Log.i(tag, msg)
        }
    }

    fun info(msg: String) {
        if (isDebug) {
            Log.i(tag, msg)
        }
    }

    fun warning(tag: String, msg: String) {
        if (isDebug) {
            Log.w(tag, msg)
        }
    }

    fun warning(msg: String) {
        if (isDebug) {
            Log.w(tag, msg)
        }
    }

    fun error(tag: String, msg: String) {
        if (isDebug) {
            Log.e(tag, msg)
        }
    }

    fun error(msg: String) {
        if (isDebug) {
            Log.e(tag, msg)
        }
    }
}
