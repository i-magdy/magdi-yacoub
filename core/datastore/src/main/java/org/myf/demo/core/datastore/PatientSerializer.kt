package org.myf.demo.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class PatientSerializer @Inject constructor(): Serializer<PatientData> {
    override val defaultValue: PatientData = PatientData.getDefaultInstance()

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