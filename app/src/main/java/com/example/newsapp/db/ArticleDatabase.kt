package com.example.newsapp.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.newsapp.model.Article

@Database(
    version = 1,
    entities = [Article::class]
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDAO

    companion object {
        private var instance: ArticleDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            createInstance(context).also { instance = it }
        }

        private fun createInstance(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_database"
            ).build()
    }
}