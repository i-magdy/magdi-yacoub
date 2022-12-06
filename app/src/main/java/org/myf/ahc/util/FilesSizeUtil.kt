package org.myf.ahc.util


object FilesSizeUtil {
    private const val total_size: Double = 20000000.0
    fun calculateSizePercentage(size: Double): String {
        val v = size/ total_size
        val p = v * 100
        return String.format("%.2f%c",p,'%')
    }

    fun getSize(size: Long): String {
        return if (size < 1000000){
             String.format("%d KB",(size/1000).toInt())
        }else{
             String.format("%d MB",(size/1000000).toInt())
        }
    }
}