package org.myf.demo.core.common.util


object FilesSizeUtil {

    const val REPORTS_SIZE: Long = 20000000
    private const val total_size: Double = 20000000.0

    fun calculateSizePercentage(size: Double): String {
        val v = size/ total_size
        val p = v * 100
        return String.format("%.2f%c",p,'%')
    }

    fun getSize(
        size: Long,
        kb: String,
        mb: String
    ): String {
        return if (size < 1000000){
             String.format("%d $kb",(size/1000).toInt())
        }else{
             String.format("%d $mb",(size/1000000).toInt())
        }
    }
}