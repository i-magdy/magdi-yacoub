package org.myf.ahc.ui.registration.createPatient

import org.myf.ahc.api.CountriesService
import org.myf.ahc.util.VerifyUtil
import javax.inject.Inject

class CreatePatientRepo @Inject constructor(
    private val countryService: CountriesService,
    private val util: VerifyUtil
){

}