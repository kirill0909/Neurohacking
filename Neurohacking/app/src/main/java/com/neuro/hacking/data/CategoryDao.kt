package com.neuro.hacking.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.neuro.hacking.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category_table ORDER BY category ASC")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM category_table ORDER BY category ASC")
    suspend fun getCategories(): List<Category>

    /*
    *This method remove word by category from word table
    * it call when user remove whole category
     */
    @Query("DELETE FROM word_table WHERE category = :category")
    suspend fun deleteWordByCategoryName(category: String)

    /*
    *This method update row category inside word table
    * it call when user update category name
     */
    @Query("UPDATE word_table SET category = :newCategory WHERE category = :oldCategory")
    suspend fun updateWordByCategoryName(newCategory: String , oldCategory: String)

    @Query("SELECT * FROM category_table WHERE category LIKE :category")
    fun searchCategory(category: String): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

}