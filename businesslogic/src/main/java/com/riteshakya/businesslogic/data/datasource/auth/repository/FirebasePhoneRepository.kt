package com.riteshakya.businesslogic.data.datasource.auth.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.riteshakya.businesslogic.repository.auth.PhoneRepository
import com.riteshakya.businesslogic.repository.auth.PhoneRepository.Companion.VERIFIED
import com.riteshakya.businesslogic.repository.auth.PhoneRepository.Companion.WAITING_CODE
import io.reactivex.Single
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirebasePhoneRepository
@Inject constructor(
) : PhoneRepository {

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var storedVerificationId: String
    override var smsToken: String = ""

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun requestCode(phoneNumber: String): Single<Int> {
        return requestCode(phoneNumber, null)
    }


    private fun requestCode(
        phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken?
    ): Single<Int> {
        Log.d(FirebasePhoneRepository::class.java.simpleName, "phone: $phoneNumber")
        return Single.create { emitter ->
            PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                    phoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    UiThreadExecutor(),
                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        override fun onVerificationCompleted(
                            credential: PhoneAuthCredential
                        ) {
                            var code = credential.smsCode;
                            if (code != null) {
                                smsToken = code
                            }
                            emitter.onSuccess(WAITING_CODE)
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            emitter.onError(e)
                        }

                        override fun onCodeAutoRetrievalTimeOut(p0: String?) {
                            super.onCodeAutoRetrievalTimeOut(p0)
                            smsToken = ""
                            emitter.onSuccess(WAITING_CODE)
                        }

                        override fun onCodeSent(
                            verificationId: String?,
                            token: PhoneAuthProvider.ForceResendingToken
                        ) {
                            // Save verification ID and resending token so we can use them later
                            storedVerificationId = verificationId!!
                            resendToken = token
                        }
                    }, token
                ) // OnVerificationStateChangedCallbacks
        }
    }


    override fun resendToken(phoneNumber: String): Single<Int> {
        Log.d(FirebasePhoneRepository::class.java.simpleName, "resendToken, phone: $phoneNumber")
        return requestCode(phoneNumber, resendToken)
    }

    override fun submitCode(code: String): Single<Int> {
        Log.d(FirebasePhoneRepository::class.java.simpleName, "submitCode, $code")
        return Single.create { emitter ->
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(
                    UiThreadExecutor(), OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            auth.signOut()
                            emitter.onSuccess(VERIFIED)
                        } else {
                            emitter.onError(task.exception!!)
                        }
                    }
                )
        }
    }

    private class UiThreadExecutor : Executor {
        private val mHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mHandler.post(command)
        }
    }

}