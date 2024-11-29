package my.android.githubsearch.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class LanguageInfo(
    val color: String,
    val url: String
)

fun loadColorsFromJson(context: Context): Map<String, LanguageInfo> {
    return try {

        val jsonString = context.assets.open("colors.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<Map<String, LanguageInfo>>() {}.type
        Gson().fromJson(jsonString, type)

    } catch (e: Exception) {
        e.printStackTrace()
        emptyMap()
    }
}
