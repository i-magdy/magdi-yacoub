package org.myf.demo.core.common.util

import androidx.core.text.isDigitsOnly
import org.myf.demo.core.common.util.EgyptCities.cities

object CreatePatientUtil {
    private val emailRegex = Regex("(?:[a-z\\d!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z\\d!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z\\d](?:[a-z\\d-]*[a-z\\d])?\\.)+[a-z\\d](?:[a-z\\d-]*[a-z\\d])?|\\[(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?|[a-z\\d-]*[a-z\\d]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")

    fun isNameValid(
        name: String
    ): CreatePatientUiError = when{
        name.isBlank() -> CreatePatientUiError.EMPTY_NAME
        name.isDigitsOnly() -> CreatePatientUiError.INVALID_PATIENT_NAME
        !isNameFormatValid(name) -> CreatePatientUiError.INVALID_NAME_FORMAT
        else -> CreatePatientUiError.NONE_NAME
    }

    fun isEgyptianIdValid(
        id: String,
        currentYearDigit: Int,
        currentMonthDigit: Int
    ): CreatePatientUiError = when{
        id.isBlank() -> CreatePatientUiError.EMPTY_ID
        id.length < 14 -> CreatePatientUiError.INVALID_NATIONAL_ID
        !id.isDigitsOnly() -> CreatePatientUiError.INVALID_NATIONAL_ID
        !validateEgyptianId(
            id = id,
            currentYearDigit = currentYearDigit,
            currentMonthDigit = currentMonthDigit
        ) ->  CreatePatientUiError.INVALID_ID_FORMAT
        else -> CreatePatientUiError.NONE_ID
    }

    fun isEmailValid(
        email: String
    ): CreatePatientUiError = when{
        email.isBlank() -> CreatePatientUiError.NONE_EMAIL
        !email.matches(emailRegex)  -> CreatePatientUiError.INVALID_EMAIL
        else -> CreatePatientUiError.NONE_EMAIL
    }
    private fun isNameFormatValid(
        name: String
    ): Boolean = if(name.matches("\\b[a-zA-Z]+\\D[^\\u0621-\\u064A]\\s[a-zA-Z]+\\D[^\\u0621-\\u064A]\\s[a-zA-Z]+\\D[^\\u0621-\\u064A]".toRegex())){
        true
    }else name.matches("\\b[\\u0621-\\u064A]+\\D[^a-zA-Z]\\s[\\u0621-\\u064A]+\\D[^a-zA-Z]\\s[\\u0621-\\u064A]+\\D[^a-zA-Z]".toRegex())

    private fun validateEgyptianId(
        id: String,
        currentYearDigit: Int,
        currentMonthDigit: Int
    ): Boolean = isEgyptianIdValidYear(
        id = id,
        currentYearDigit = currentYearDigit
    ) && isEgyptianIdValidMonth(
        id = id,
        currentMonthDigit = currentMonthDigit,
        currentYearDigit = currentYearDigit
    ) && validateEgyptianIdDay(
        id = id
    ) && validateEgyptianCity(
        id = id
    )

    private fun isEgyptianIdValidYear(
        id: String,
        currentYearDigit: Int = 3
    ): Boolean = id.substring(0,3).matches("2\\d{2}|30\\d|31\\d|32[0-$currentYearDigit]".toRegex())

    private fun isEgyptianIdValidMonth(
        id: String,
        currentYearDigit: Int = 3,
        currentMonthDigit: Int = 1
    ): Boolean = when(id.subSequence(0,1)[0]){
        '2' -> id.substring(1,5).matches("\\d{2}0[1-9]|\\d{2}1[0-2]".toRegex())
        '3' -> if (id[1] == '2' && id[2].digitToInt() == currentYearDigit){
            if (currentMonthDigit < 10){
                id.substring(3,5).matches("0[0-$currentMonthDigit]".toRegex())
            }else{
                id.substring(3,5).matches("1[0-2]".toRegex())
            }
        }else{
            id.substring(3,5).matches("0[1-9]|1[0-2]".toRegex())
        }
        else -> false
    }

    private fun validateEgyptianIdDay(
        id: String
    ): Boolean = id.substring(5,7).matches("0[1-9]|1\\d|2\\d|3[0-1]".toRegex())

    private fun validateEgyptianCity(
        id: String
    ): Boolean = cities.containsKey(id.substring(7,9))
}