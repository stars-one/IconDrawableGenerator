package com.example.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.toast.ComposeToast
import com.example.common.util.CommonUtil
import com.example.common.util.Icon
import com.example.common.util.RemixIconDataUtil
import java.io.File


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageEnvInstall() {


    val list = RemixIconDataUtil.initResource()


    Box(Modifier.fillMaxSize()) {

        val state = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            //注意这里
            state = state
        ) {

            items(list.size) {
                val item = list[it]
                MyCard(item.groupName + "( ${item.data.size}个)") {
                    Spacer(Modifier.height(16.dp))

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
                }
            }
        }

        //右侧的滚动条
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )
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

    Column(
        modifier = Modifier.wrapContentWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        list.forEach {
            RemixIconSingleView(it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RemixIconSingleView(svgFile: File) {
    var isDialogOpen by remember { mutableStateOf(false) }

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = { isDialogOpen = false }) {
                    Text("OK")
                }
            },
            title = { Text("Alert Dialog") },
            text = { Text("Lore ipsum") },
        )
    }


    Column(
        modifier = Modifier.size(120.dp, 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            loadSvgPainter(svgFile.inputStream(), Density(24f)),
            contentDescription = null,
            modifier = Modifier.width(24.dp).height(24.dp).onClick {
                val file = svgFile
                CommonUtil.copyFile(file)

                ComposeToast.show("复制成功!")

                CommonUtil.svgToPng(file,File("D:/jkkk.png"))
                //isDialogOpen = true
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(svgFile.nameWithoutExtension, fontSize = 12.sp)
    }

}