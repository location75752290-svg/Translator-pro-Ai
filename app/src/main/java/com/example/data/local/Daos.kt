package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {
    @Query("SELECT * FROM translation_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translation_history WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteHistory(): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translation_history WHERE sourceText LIKE '%' || :query || '%' OR translatedText LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchHistory(query: String): Flow<List<TranslationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationEntity): Long

    @Query("UPDATE translation_history SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM translation_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM translation_history")
    suspend fun clearAll()
}

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM saved_dictionary ORDER BY timestamp DESC")
    fun getAllSavedWords(): Flow<List<DictionaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWord(entity: DictionaryEntity)

    @Query("DELETE FROM saved_dictionary WHERE word = :word")
    suspend fun deleteWord(word: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_dictionary WHERE word = :word)")
    suspend fun isWordSaved(word: String): Boolean
}

@Dao
interface TutorChatDao {
    @Query("SELECT * FROM tutor_chat_history WHERE scenarioId = :scenarioId ORDER BY timestamp ASC")
    fun getChatMessages(scenarioId: String): Flow<List<TutorChatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: TutorChatEntity): Long

    @Query("DELETE FROM tutor_chat_history WHERE scenarioId = :scenarioId")
    suspend fun clearScenarioChat(scenarioId: String)
}

@Dao
interface TenseProgressDao {
    @Query("SELECT * FROM tense_progress")
    fun getAllProgress(): Flow<List<TenseProgressEntity>>

    @Query("SELECT * FROM tense_progress WHERE tenseId = :tenseId")
    suspend fun getProgressForTense(tenseId: String): TenseProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: TenseProgressEntity)
}
