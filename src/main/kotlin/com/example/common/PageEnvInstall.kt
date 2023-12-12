package com.example.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.common.util.CommonUtil
import com.example.common.util.Icon
import com.example.common.util.RemixIconDataUtil
import java.io.File


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageEnvInstall() {

    val list = RemixIconDataUtil.initResource()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {

        list.forEach {
            MyCard(it.groupName) {
                val iconList = it.data
                LazyVerticalGrid(GridCells.Adaptive(minSize = 24.dp)) {
                    items(iconList.size) { index ->
                        RemixIconItemView(iconList[index])
                    }
                }

            }
        }

    }
}

@Composable
fun RemixIconItemView(icon: Icon) {

    val defaultIcon = icon.getSvgFileDefault()
    //如果是默认图标是存在的,则不区分fill还是line

    val list = if (defaultIcon.exists()) {
        listOf(defaultIcon, defaultIcon)
    } else {
        val fillSvgFile = icon.getSvgFileFill()
        val lineSvgFile = icon.getSvgFileLine()
        listOf(fillSvgFile, lineSvgFile)
    }

    Column {
        list.forEach {
            RemixIconSingleView(it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RemixIconSingleView(svgFile: File) {
    Image(
        loadSvgPainter(svgFile.inputStream(), Density(24f)),
        contentDescription = null,
        modifier = Modifier.width(24.dp).height(24.dp).onClick {
            val file = svgFile
            CommonUtil.copyFile(file)
            println("复制成功")
        }
    )
}