package com.example.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.File
import java.io.IOException


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageEnvInstall() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(enabled = true, state = rememberScrollState())
            .padding(16.dp)
    ) {

        //val ffmpegFile = File(System.getProperty("compose.application.resources.dir")).resolve("unicode.html")
        //println(ffmpegFile.path)

        println(this::class.java.getResource("").path)

        MyCard("Arrows") {

            val file = File("E:\\download\\新建文件夹\\arrow-up-circle-fill.svg").inputStream()
            Image(
                loadSvgPainter(file, Density(24f)),
                contentDescription = null,
                modifier = Modifier.width(24.dp).height(24.dp).onClick {
                    val file = File("E:\\download\\新建文件夹\\arrow-up-circle-fill.svg")



                    println("复制成功")
                }
            )
        }
    }
}


