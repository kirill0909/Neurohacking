package com.neuro.hacking.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.neuro.hacking.model.Category
import com.neuro.hacking.model.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAllWords(): LiveData<List<Word>>

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    suspend fun getWords(): List<Word>

    @Query("SELECT * FROM word_table WHERE category = :category ORDER BY word ASC")
    fun getWordsByCategory(category: String): LiveData<List<Word>>

    @Query("SELECT * FROM word_table WHERE word LIKE :word")
    fun searchWord(word: String): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWord(word: Word)

    @Update
    suspend fun updateWord(word: Word)

    @Delete
    suspend fun deleteWord(word: Word)

}