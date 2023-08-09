package org.myf.demo.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.myf.demo.core.common.model.HomeModel

interface HomeRepository {
    fun getData(): Flow<HomeModel>
    fun push()
    fun cancelJob()
}