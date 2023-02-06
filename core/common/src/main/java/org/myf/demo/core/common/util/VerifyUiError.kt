package org.myf.demo.core.common.util

object PhoneAuthErrorMessage{
    const val invalid_phone: String = "The format of the phone number provided is incorrect. Please enter the phone number in a format that can be parsed into E.164 format. E.164 phone numbers are written in the format [+][country code][subscriber number including area code]. [ TOO_SHORT ]"
    const val invalid_code: String = " The sms verification code used to create the phone auth credential is invalid. Please resend the verification code sms and be sure use the verification code provided by the user."
}
enum class VerifyUiError {
    NONE,SELECT_COUNTRY,INVALID_PHONE,INVALID_CODE
}