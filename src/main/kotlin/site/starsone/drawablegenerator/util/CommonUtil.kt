package site.starsone.drawablegenerator.util

import androidx.compose.ui.graphics.toArgb
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import site.starsone.svg2vector.SvgUtils
import java.awt.Color
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.File

object CommonUtil {

    fun copyText(cmd: String) {
        val systemClipboard = Toolkit.getDefaultToolkit().systemClipboard
        val selection = StringSelection(cmd)
        systemClipboard.setContents(selection, null)
    }

    /**
     * 复制单个文件
     */
    fun copyFile(file: File) {
        // 创建剪切板内容
        val systemClipboard = Toolkit.getDefaultToolkit().systemClipboard
        systemClipboard.setContents(FileTransferable(listOf(file)), null)
    }


    /**
     * 复制多个文件
     */
    fun copyFile(files: List<File>) {
        // 创建剪切板内容
        val systemClipboard = Toolkit.getDefaultToolkit().systemClipboard
        systemClipboard.setContents(FileTransferable(files), null)
    }



    /**
     * 将json数据转为List<T>
     *
     * @param T 数据类型
     * @return
     */
    fun <T> String.parseJsonToList(clazz: Class<T>): List<T> {
        val gson = Gson()
        val type = `$Gson$Types`.newParameterizedTypeWithOwner(null, ArrayList::class.java, clazz)
        val data: List<T> = gson.fromJson(this, type)
        return data
    }

    /**
     * 将json字符串数据转为某个类
     *
     * @param T
     * @return
     */
    inline fun <reified T> String.parseJsonToObject(): T {
        val gson = Gson()
        val result = gson.fromJson(this, T::class.java)
        return result
    }

    /**
     * svg转为png文件
     * @param color 颜色
     * @param size 宽度或高度(生成的png文件宽高一致)
     */
    fun svgToPng(svgFile: File,color:androidx.compose.ui.graphics.Color,size:Int):File {
        val outputFile = File(svgFile.parentFile,"cache/${svgFile.nameWithoutExtension.replace("-","_")}.png").apply {
            if (parentFile.exists().not()) {
                parentFile.mkdirs()
            }
        }
        SvgToPngUtil().toPngFileFromString(svgFile.readText(), color.toAwtColor(), outputFile,size,size)
        return outputFile
    }

    fun svgToXml(svgFile:File,size:Int): File {
        val outputFile = File(svgFile.parentFile,"cache/${svgFile.nameWithoutExtension.replace("-","_")}.xml").apply {
            if (parentFile.exists().not()) {
                parentFile.mkdirs()
            }
        }
        SvgUtils.toXmlFile(svgFile,outputFile,size)
        return outputFile
    }
}


class FileTransferable(private val fileList: List<File>) : Transferable {
    override fun getTransferDataFlavors(): Array<DataFlavor> {
        return arrayOf(DataFlavor.javaFileListFlavor)
    }

    override fun isDataFlavorSupported(flavor: DataFlavor): Boolean {
        return flavor.equals(DataFlavor.javaFileListFlavor)
    }

    @Throws(UnsupportedFlavorException::class)
    override fun getTransferData(flavor: DataFlavor): Any {
        return if (flavor.equals(DataFlavor.javaFileListFlavor)) {
            fileList
        } else {
            throw UnsupportedFlavorException(flavor)
        }
    }
}

/**
 * 将compose的color转为awt包中的color
 */
fun androidx.compose.ui.graphics.Color.toAwtColor(): Color {
    val color = this
    val awtColor = Color(color.red,color.green,color.blue,color.alpha)
    return awtColor
}
fun androidx.compose.ui.graphics.Color.toHexString(): String {
    val color = this
    val argb = color.toArgb()
    val alpha = (argb shr 24 and 0xFF).toString(16).padStart(2, '0')
    val red = (argb shr 16 and 0xFF).toString(16).padStart(2, '0')
    val green = (argb shr 8 and 0xFF).toString(16).padStart(2, '0')
    val blue = (argb and 0xFF).toString(16).padStart(2, '0')
    return "#$red$green$blue$alpha"
}

fun String.toColor(): androidx.compose.ui.graphics.Color {
    val awtColor = Color.decode(this)
    return androidx.compose.ui.graphics.Color(awtColor.red,awtColor.green,awtColor.blue,awtColor.alpha)
}
