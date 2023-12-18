package com.example.common.util

import com.google.gson.Gson
import com.google.gson.JsonParser
import java.io.File

object RemixIconDataUtil {

    val resourceDir = System.getProperty("compose.application.resources.dir")

    const val dirPath = "remixicon-data"

    const val jsonName = "remixicon_v4.0.0.json"

    fun initResource(): List<IconGroup> {


        val pair = if (resourceDir == null) {
            //如果是直接运行MainKt就会进入此逻辑
            //例: D:\java-project\IconDrawableGenerator
            val projectRootDir = File("").absolutePath

            val tagJson = File(projectRootDir,"resources/common/remixicon_tags.json").readText()
            val remixIconJson =
                File(projectRootDir,"/resources/common/${jsonName}").readText()
            Pair(tagJson, remixIconJson)

        } else {
            //打包成对应系统的二进制文件(exe等)会进入此逻辑
            val tagJson = File(resourceDir).resolve("remixicon_tags.json").readText()
            val remixIconJson = File(resourceDir).resolve(jsonName).readText()
            Pair(tagJson, remixIconJson)
        }

        val tagJson = pair.first
        val remixIconJson = pair.second


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
        if (resourceDir == null) {
            //当前项目路径,如D:\java-project\IconDrawableGenerator
            val projectRootDir = File("").absolutePath
            
            return File("${projectRootDir}/build/compose/binaries/main/app/IconDrawableGenerator/app/resources/remixicon-data")
        }
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