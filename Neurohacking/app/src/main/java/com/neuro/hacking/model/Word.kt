package com.neuro.hacking.model

import androidx.room.Entity
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import androidx.room.PrimaryKey

@Parcelize
@Entity(tableName = "word_table")
data class Word(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val word: String,
    var translation: String,
    val category: String
) : Parcelable