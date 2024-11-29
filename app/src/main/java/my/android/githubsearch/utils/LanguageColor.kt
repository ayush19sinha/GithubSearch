import android.content.Context
import android.graphics.Color
import my.android.githubsearch.utils.loadColorsFromJson

fun getLanguageColor(context: Context, language: String): Int {
    val languageMap = loadColorsFromJson(context)
    val languageInfo = languageMap[language]
    val colorCode = languageInfo?.color ?: "#FFFFFF"
    return Color.parseColor(colorCode)
}
