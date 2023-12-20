package site.starsone.drawablegenerator

import java.io.File
import java.util.*

object ConfigSave {
    val configFile by lazy {
        File(File("").absoluteFile, "localConfig.properties").apply {
            if (!exists()) {
                this.createNewFile()
            }
        }
    }

    private val properties by lazy {
        Properties().apply {
            load(configFile.inputStream())
        }
    }

    fun save(key: String, value: String) {
        properties.setProperty(key, value)

        configFile.outputStream().use {
            properties.store(it, "Configuration File")
        }
    }

    fun get(key: String,defaultValue:String=""): String {
        val value = properties[key] ?: return defaultValue
        return value.toString()
    }


}