package com.riteshakya.businesslogic.data.datasource.student.model

import com.riteshakya.businesslogic.repository.student.model.ParentModel
import com.riteshakya.businesslogic.data.model.transform
import com.riteshakya.businesslogic.repository.student.model.StudentModel

fun StudentModel.transform(): StudentDto = StudentDto(
    firstName,
    lastName,
    school,
    gender,
    className,
    section,
    parent.parentId,
    phoneNo.transform(),
    profilePicture
)

fun StudentDto.transform(): StudentModel = StudentModel(
    first_name,
    last_name,
    school,
    gender,
    class_name,
    section,
    ParentModel(parent_id),
    phone_no.transform(),
    profile_picture
)

fun StudentDto.transform(userId: String, teacherId: String) = StudentCollectionDto(
    school,
    userId,
    teacherId
)

fun ParentDto.transform() = ParentModel(
    parent_id,
    first_name,
    last_name
)

fun ParentModel.transform() = ParentDto(
    parentId,
    firstName,
    lastName
)