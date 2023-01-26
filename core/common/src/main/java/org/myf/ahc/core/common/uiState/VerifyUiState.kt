package org.myf.ahc.core.common.uiState

import org.myf.ahc.core.common.util.VerifyUiError
import org.myf.ahc.core.model.countries.CountryCodeModel


data class VerifyUiState(
    val phone: String = "",
    val isRequestingPhone: Boolean = false,
    val isCodeRequested: Boolean = false,
    val isCodeSent: Boolean = false,
    val isVerifying: Boolean = false,
    val isSuccess: Boolean = false,
    val selectedCountry: CountryCodeModel = CountryCodeModel(),
    val error: VerifyUiError = VerifyUiError.NONE,
    val shouldLogin: Boolean = false,
    val countries: List<String> = emptyList()
)
