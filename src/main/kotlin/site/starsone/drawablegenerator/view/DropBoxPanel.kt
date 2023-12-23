package site.starsone.drawablegenerator.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.dp
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.SystemColor.text
import java.awt.SystemColor.window
import java.awt.TextArea
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.io.File
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.SwingConstants
import javax.swing.TransferHandler
import kotlin.math.roundToInt


@Composable
fun DropBoxPanel(
    modifier: Modifier,
    onFilesDrop: (file:List<File>) -> Unit
) {
    SwingPanel(modifier = modifier,
        factory = {
            JPanel().apply {
                background = java.awt.Color.decode("#f6f8fa")
                layout = BorderLayout()

                val label =  JLabel("拖动文件放在这里").apply {
                    verticalAlignment = SwingConstants.CENTER
                    horizontalAlignment = SwingConstants.CENTER
                }

                add(label,BorderLayout.CENTER)

                transferHandler = object : TransferHandler() {
                    override fun importData(comp: JComponent, t: Transferable): Boolean {
                        val transferData = t.getTransferData(DataFlavor.javaFileListFlavor)
                        val list = transferData as List<*>
                        val files  = list.map { it as File }

                        //val fileText = files.joinToString(",")
                        onFilesDrop.invoke(files)
                        return true
                    }

                    override fun canImport(
                        comp: JComponent,
                        transferFlavors: Array<out DataFlavor>
                    ): Boolean {
                        return true
                    }
                }
            }
        }
    )
}