package my.android.githubsearch.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import my.android.githubsearch.data.model.RepositoryOwner

class Converters {

    @TypeConverter
    fun fromRepositoryOwner(owner: RepositoryOwner): String {
        return Gson().toJson(owner)
    }

    @TypeConverter
    fun toRepositoryOwner(ownerString: String): RepositoryOwner {
        val type = object : TypeToken<RepositoryOwner>() {}.type
        return Gson().fromJson(ownerString, type)
    }
}
