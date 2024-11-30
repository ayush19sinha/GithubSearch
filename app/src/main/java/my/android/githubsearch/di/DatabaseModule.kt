package my.android.githubsearch.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import my.android.githubsearch.data.local.AppDatabase
import my.android.githubsearch.data.local.RepositoryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "github_repository_database"
        ).build()
    }

    @Provides
    fun provideRepositoryDao(appDatabase: AppDatabase): RepositoryDao {
        return appDatabase.repositoryDao()
    }
}