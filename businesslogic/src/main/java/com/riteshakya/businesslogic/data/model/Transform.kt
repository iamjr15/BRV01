package com.riteshakya.businesslogic.data.model

import com.riteshakya.businesslogic.data.datasource.student.model.ParentDto
import com.riteshakya.businesslogic.repository.student.model.ParentModel
import com.riteshakya.core.model.PhoneModel

fun PhoneDto.transform() = PhoneModel(
    dial_code,
    phone_no
)

fun PhoneModel.transform() = PhoneDto(
    dialCode,
    phoneNo
)