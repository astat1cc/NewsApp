package com.example.newsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Source(
    val id: String? = null,
    val name: String
): Parcelable