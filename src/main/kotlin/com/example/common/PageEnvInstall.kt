package com.example.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.google.gson.annotations.Until
import java.io.File


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageEnvInstall() {

    val list = RemixIconDataUtil.initResource()


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        items(list.size) {
            val item = list[it]
            MyCard(item.groupName + "( ${item.data.size}个)") {
                val iconList = item.data

                val spanCount = 10
                val size = iconList.size
                val rows = (size / spanCount) + 1

                for (i in 0 until rows) {
                    val start = i * spanCount
                    var end = (i + 1) * spanCount
                    if (end > size) {
                        end = size
                    }

                    val rowData = iconList.subList(start, end)
                    RemixIconItemViewRow(rowData)
                }

                //LazyVerticalGrid(GridCells.Adaptive(minSize = 24.dp), modifier = Modifier.wrapContentHeight()) {
                //    items(iconList.size) { index ->
                //        RemixIconItemView(iconList[index])
                //    }
                //}
            }
        }
    }
}

@Composable
fun RemixIconItemViewRow(list: List<Icon>) {
    Row {
        list.forEach {
            RemixIconItemView(it)
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