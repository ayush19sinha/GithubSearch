package my.android.githubsearch.utils

fun formatCount(count: Int): String {
    return when {
        count < 1000 -> count.toString()
        count < 10000 -> String.format("%.1fK", count / 1000.0)
        else -> String.format("%dK", count / 1000)
    }
}