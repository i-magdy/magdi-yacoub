package org.myf.ahc.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object PatientSerializer : Serializer<PatientData> {
    override val defaultValue: PatientData
        get() = PatientData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PatientData {
        try {
            return PatientData.parseFrom(input)
        }catch (exception: InvalidProtocolBufferException){
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: PatientData, output: OutputStream) {
        t.writeTo(output)
    }
}