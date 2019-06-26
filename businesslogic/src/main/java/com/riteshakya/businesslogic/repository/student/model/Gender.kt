package com.riteshakya.businesslogic.repository.student.model

import androidx.annotation.StringDef

@StringDef(
        NOT_SELECTED,
        MALE,
        FEMALE
)
annotation class Gender

const val NOT_SELECTED = "NotSelected"
const val MALE = "Male"
const val FEMALE = "Female"