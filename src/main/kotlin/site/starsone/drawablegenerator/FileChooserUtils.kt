package site.starsone.drawablegenerator

import java.io.File
import javax.swing.JFileChooser
import javax.swing.JFrame

object FileChooserUtils {
    fun chooseFile(): File? {
        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "选择文件"
        val result = fileChooser.showOpenDialog(JFrame())
        return if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.selectedFile
        } else {
            null
        }
    }

    fun chooseFolder(): File? {
        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "选择文件夹"
        fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        val result = fileChooser.showOpenDialog(JFrame())
        return if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.selectedFile
        } else {
            null
        }
    }
}