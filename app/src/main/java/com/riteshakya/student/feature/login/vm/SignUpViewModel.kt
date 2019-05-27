package com.riteshakya.student.feature.login.vm

import androidx.lifecycle.MutableLiveData
import com.riteshakya.businesslogic.interactor.school.GetSchoolsInteractor
import com.riteshakya.businesslogic.interactor.signup.SignUpSchoolInteractor
import com.riteshakya.businesslogic.interactor.signup.SignUpStudentInteractor
import com.riteshakya.businesslogic.interactor.signup.SignUpTeacherInteractor
import com.riteshakya.businesslogic.repository.school.model.SchoolModel
import com.riteshakya.businesslogic.repository.student.model.Gender
import com.riteshakya.businesslogic.repository.student.model.MALE
import com.riteshakya.businesslogic.repository.student.model.ParentModel
import com.riteshakya.businesslogic.repository.student.model.StudentModel
import com.riteshakya.businesslogic.repository.teacher.model.TeacherModel
import com.riteshakya.core.exception.FailureMessageMapper
import com.riteshakya.core.model.PhoneModel
import com.riteshakya.core.model.STUDENT
import com.riteshakya.core.platform.BaseViewModel
import com.riteshakya.core.platform.ResultState
import com.riteshakya.student.interactor.geocode.GetCityNameInteractor
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


class SignUpViewModel
@Inject constructor(
    val getSchoolInteractor: GetSchoolsInteractor,
    val getCityNameInteractor: GetCityNameInteractor,
    val signUpStudentInteractor: SignUpStudentInteractor,
    val signUpTeacherInteractor: SignUpTeacherInteractor,
    val signUpSchoolInteractor: SignUpSchoolInteractor,
    val failureMessageMapper: FailureMessageMapper

) : BaseViewModel() {

    // Default user role
    var userRole = STUDENT

    private val schoolsSubject by lazy {
        PublishSubject.create<Boolean>()
    }

    private val postalCodeSubject by lazy {
        PublishSubject.create<String>()
    }

    // SignUp
    // Common
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val school = MutableLiveData<String>()

    // Student/Parent
    val gender = MutableLiveData<@Gender String>().also {
        it.value = MALE
    }
    val classValue = MutableLiveData<String>()
    val sectionValue = MutableLiveData<String>()
    val parent = MutableLiveData<ParentModel>()

    // Teacher
    val subject = MutableLiveData<String>()
    val teacher = MutableLiveData<Boolean>()

    // School
    val authorityPosition = MutableLiveData<String>()
    val schoolName = MutableLiveData<String>()
    val schoolEmail = MutableLiveData<String>()
    val schoolAreaCode = MutableLiveData<String>()
    val schoolCity = MutableLiveData<String>()

    val cityNameState: BehaviorSubject<ResultState> = BehaviorSubject.create()

    // Password Screen
    val password = MutableLiveData<String>()

    // Phone
    val phoneNo = MutableLiveData<PhoneModel>()

    // Profile Photo
    val profilePhoto = MutableLiveData<String>()

    // School logo
    val schoolLogo = MutableLiveData<String>()

    val schools = schoolsSubject
        .startWith(true)
        .flatMapSingle { getSchoolInteractor() }
        .replay()
        .autoConnect(1)

    val cityNameObserver = postalCodeSubject
        .filter { it.length == 5 }
        .flatMapSingle {
            getCityNameInteractor(it)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { e ->
                    cityNameState.onNext(ResultState.Error(failureMessageMapper(e)))
                    ""
                }
        }
        .replay()
        .autoConnect(1)

    fun getCityFromPostalCode(postalCode: String) {
        postalCodeSubject.onNext(postalCode)
    }

    fun signUpStudent(): Completable {
        val student = StudentModel(
            firstName.value ?: "",
            lastName.value ?: "",
            school.value ?: "",
            gender.value ?: MALE,
            classValue.value ?: "",
            sectionValue.value ?: "",
            parent.value ?: ParentModel(),
            phoneNo.value ?: PhoneModel(),
            profilePhoto.value ?: "",
            password.value ?: ""
        )

        return signUpStudentInteractor(student)
    }

    fun signUpTeacher(): Completable {
        val teacherModel = TeacherModel(
            firstName.value ?: "",
            lastName.value ?: "",
            school.value ?: "",
            subject.value ?: "",
            teacher.value ?: false,
            classValue.value ?: "",
            sectionValue.value ?: "",
            phoneNo.value ?: PhoneModel(),
            profilePhoto.value ?: "",
            password.value ?: ""
        )
        return signUpTeacherInteractor(
            teacherModel
        )
    }

    fun signUpSchool(): Completable {
        val schoolModel = SchoolModel(
            authorityPosition.value ?: "",
            firstName.value ?: "",
            lastName.value ?: "",
            schoolName.value ?: "",
            schoolEmail.value ?: "",
            schoolAreaCode.value ?: "",
            schoolCity.value ?: "",
            phoneNo.value ?: PhoneModel(),
            profilePhoto.value ?: "",
            schoolLogo.value ?: "",
            password.value ?: ""
        )
        return signUpSchoolInteractor(
            schoolModel
        )
    }
}