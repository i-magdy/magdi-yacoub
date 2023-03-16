package org.myf.demo.core.common.model

import androidx.annotation.Keep

@Keep
class CurrentPatientModel {
    constructor()
    constructor(
        id: String,
        uid: String,
        name: String,
        email: String,
        primaryPhone: String,
        secondaryPhone: String
    ) {
        this.id = id
        this.uid = uid
        this.name = name
        this.email = email
        this.primaryPhone = primaryPhone
        this.secondaryPhone = secondaryPhone
    }

    var id: String = ""
    var uid: String = ""
    var name: String = ""
    var email: String = ""
    var primaryPhone: String = ""
    var secondaryPhone: String = ""


}