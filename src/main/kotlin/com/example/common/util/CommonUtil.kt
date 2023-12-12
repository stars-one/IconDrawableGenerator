package com.example.common.util

import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import org.apache.batik.anim.dom.SAXSVGDocumentFactory
import org.apache.batik.svggen.SVGGraphics2D
import org.jetbrains.skia.svg.SVGDOM
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

object CommonUtil {

    fun copyText(cmd: String) {
        val systemClipboard = Toolkit.getDefaultToolkit().systemClipboard
        val selection = StringSelection(cmd)
        systemClipboard.setContents(selection, null)
    }

    fun copyFile(file: File){
        // 创建剪切板内容
        val systemClipboard = Toolkit.getDefaultToolkit().systemClipboard
        systemClipboard.setContents(FileTransferable(file), null)
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

    fun svgToPng(svgFile:File,pngFile:File){
        SvgToPngUtil().toPngFileFromString(svgFile.readText(),"white",pngFile.path,null)
    }
}

private class FileTransferable(private val file: File) : Transferable {
    override fun getTransferDataFlavors(): Array<DataFlavor> {
        return arrayOf(FILE_FLAVOR)
    }

    override fun isDataFlavorSupported(flavor: DataFlavor): Boolean {
        return flavor.equals(FILE_FLAVOR)
    }

    @Throws(UnsupportedFlavorException::class, IOException::class)
    override fun getTransferData(flavor: DataFlavor): Any {
        return if (flavor.equals(FILE_FLAVOR)) {
            ArrayList(listOf(file))
        } else {
            throw UnsupportedFlavorException(flavor)
        }
    }

    companion object {
        private val FILE_FLAVOR = DataFlavor.javaFileListFlavor
    }
}