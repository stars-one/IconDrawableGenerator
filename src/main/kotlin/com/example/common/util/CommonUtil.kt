package com.example.common.util

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.File
import java.io.IOException

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