package site.starsone.drawablegenerator

import site.starsone.drawablegenerator.util.RemixIconDataUtil
import site.starsone.drawablegenerator.util.toUnitString
import java.io.File

object CacheUtil {
    /**
     * 缓存文件夹
     */
    val cacheDir by lazy {
        File(getCache(), "cache").apply {
            if (exists().not()) {
                mkdirs()
            }
        }
    }

    private fun getCache(): File {
        if (RemixIconDataUtil.resourceDir == null) {
            //当前项目路径,如D:\java-project\IconDrawableGenerator
            val projectRootDir = File("").absolutePath

            return File("${projectRootDir}/build/compose/binaries/main/app/IconDrawableGenerator/app/resources/")
        }
        return File(RemixIconDataUtil.resourceDir, RemixIconDataUtil.dirPath)
    }

    /**
     * 清除缓存
     */
    fun cleanCache() {
        cacheDir.deleteRecursively()
    }

    /**
     * 计算缓存总大小
     */
    fun getCacheSize(): String {
        return cacheDir.length().toUnitString()
    }


}