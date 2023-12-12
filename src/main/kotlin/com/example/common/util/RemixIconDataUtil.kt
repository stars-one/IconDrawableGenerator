package com.example.common.util

import com.google.gson.Gson
import com.google.gson.JsonParser
import java.io.File

object RemixIconDataUtil {

    val resourceDir = System.getProperty("compose.application.resources.dir")

    const val dirPath = "remixicon-data"
    fun initResource(): List<IconGroup> {

        val tagJson = File(resourceDir).resolve("remixicon_tags.json").readText()
        val remixIconJson = File(resourceDir).resolve("remixicon_v3.7.0.json").readText()


        val remixIconJsonObject = JsonParser.parseString(remixIconJson)

        val map = hashMapOf<String, String>()

        val svgDirFile = getSvgDirFile()

        remixIconJsonObject.asJsonObject.entrySet().forEach {
            val name = it.key
            val svgPath = it.value.asJsonObject.getAsJsonArray("path").first().asString

            //生成svg文件
            val svgFile = File(svgDirFile, "${name}.svg").apply {
                if (parentFile.exists().not()) {
                    parentFile.mkdirs()
                }
                writeText(generateSvgStr(svgPath))
            }

            map[name] = svgFile.absolutePath
        }

        val entrySet = JsonParser.parseString(tagJson).asJsonObject.entrySet()
        entrySet.removeIf { it.key == "_comment" }

        val iconGroupList = entrySet.map {
            val groupName = it.key

            val iconList = it.value.asJsonObject.entrySet().map {
                val iconName = it.key
                val keywordList = it.value.asString.split(",").toList()
                Icon(iconName, keywordList.toList())
            }
            IconGroup(groupName, iconList)
        }

        //File("D:/tempxx.json").writeText(Gson().toJson(iconGroupList))

        return iconGroupList
    }

    fun getSvgDirFile(): File {
        return File(resourceDir, dirPath)
    }
}

data class IconGroup(val groupName: String, val data: List<Icon>)

/**
 * @param name vip-diamond
 * @param keyword vip-diamond
 *
 */
data class Icon(val name: String, val keyword: List<String>) {
    fun getSvgFileLine(): File {
        val file = RemixIconDataUtil.getSvgDirFile()
        return File(file, "$name-line.svg")
    }

    fun getSvgFileFill(): File {
        val file = RemixIconDataUtil.getSvgDirFile()
        return File(file, "$name-fill.svg")
    }

    fun getSvgFileDefault(): File {
        val file = RemixIconDataUtil.getSvgDirFile()
        return File(file, "$name.svg")
    }
}

fun generateSvgStr(path: String): String {
    val svgStr = """
        <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
        	<path
        		d="$path" />
        </svg>
    """.trimIndent()
    return svgStr
}