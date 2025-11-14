package com.clickretina.android.clickretinasocialprofile.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

object LinkUtils {


    // to open Social Medias
    fun openSocial(context: Context, platform: String, rawUrl: String) {
        val normalized = normalizeSocialLink(rawUrl, platform)
        when (platform.lowercase().trim()) {
            "instagram" -> openInstagram(context, normalized)
            "facebook" -> openFacebook(context, normalized)
            else -> openInCustomTab(context, normalized)
        }
    }

    // Open website in custom tab or new tab
    fun openInCustomTab(context: Context, rawUrl: String) {
        val url = ensureScheme(rawUrl)
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }


    // open instagram -) first check if App is installed or not
    private fun openInstagram(context: Context, url: String) {
        val pm = context.packageManager
        val packageName = "com.instagram.android"

        // try deep link with username if possible
        val username = extractInstagramUsername(url)
        val appUri = if (!username.isNullOrBlank()) {
            Uri.parse("instagram://user?username=$username")
        } else {
            // try the normal https url but force package
            Uri.parse(url)
        }

        // attempt to open in Instagram app
        try {
            val intent = Intent(Intent.ACTION_VIEW, appUri).apply {
                setPackage(packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if (intent.resolveActivity(pm) != null) {
                context.startActivity(intent)
                return
            }
        } catch (e: Exception) {
            // error
        }

        // As a fallback, try opening the https url with package (some apps handle it)
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                setPackage(packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if (intent.resolveActivity(pm) != null) {
                context.startActivity(intent)
                return
            }
        } catch (e: Exception) {
            // error
        }

        // final fallback -> open in Custom Tab
        openInCustomTab(context, url)
    }

    private fun openFacebook(context: Context, url: String) {
        val pm = context.packageManager
        val packageName = "com.facebook.katana"

        // Try Facebook native "faceWeb" handler for web URLs
        val fbFaceWeb = try {
            Uri.parse("fb://facewebmodal/f?href=$url")
        } catch (e: Exception) {
            null
        }

        // 1) Try faceWeb deep link
        try {
            if (fbFaceWeb != null) {
                val intent = Intent(Intent.ACTION_VIEW, fbFaceWeb).apply {
                    setPackage(packageName)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                if (intent.resolveActivity(pm) != null) {
                    context.startActivity(intent)
                    return
                }
            }
        } catch (e: Exception) {
            // error
        }

        // 2) Try opening the URL within Facebook package (if it handles it)
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                setPackage(packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if (intent.resolveActivity(pm) != null) {
                context.startActivity(intent)
                return
            }
        } catch (e: Exception) {
            // error
        }

        // 3) Final fallback: open in Custom Tab
        openInCustomTab(context, url)
    }



    // to check scheme for url
    private fun ensureScheme(raw: String): String {
        val u = raw.trim()
        return if (u.startsWith("http://") || u.startsWith("https://")) u else "https://$u"
    }

    private fun normalizeSocialLink(raw: String, platform: String = ""): String {
        var r = raw.trim()

        // remove leading @ for e.g. @brunopham
        if (r.startsWith("@")) r = r.removePrefix("@")

        // if it's already an http/https url, return it (ensure scheme)
        if (r.startsWith("http://") || r.startsWith("https://")) return r

        // build platform-specific urls for common platforms
        return when (platform.lowercase().trim()) {
            "instagram" -> "https://instagram.com/${r.trim().trimStart('/')}"
            "facebook" -> "https://facebook.com/${r.trim().trimStart('/')}"
            else -> ensureScheme(r)
        }
    }


    private fun extractInstagramUsername(url: String): String? {
        // Examples:
        // https://instagram.com/brunopham
        // https://www.instagram.com/brunopham/
        return try {
            val uri = Uri.parse(url)
            // lastPathSegment usually holds username for profile URLs
            val seg = uri.lastPathSegment
            if (!seg.isNullOrBlank()) seg.trim().trim('/') else null
        } catch (e: Exception) {
            null
        }
    }
}
