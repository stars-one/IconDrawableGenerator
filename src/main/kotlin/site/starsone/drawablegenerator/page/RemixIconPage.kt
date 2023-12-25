package site.starsone.drawablegenerator.page

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CursorDropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.starsone.drawablegenerator.view.ColorPicker
import site.starsone.drawablegenerator.toast.ComposeToast
import site.starsone.drawablegenerator.util.CommonUtil
import site.starsone.drawablegenerator.util.Icon
import site.starsone.drawablegenerator.util.IconGroup
import site.starsone.drawablegenerator.util.RemixIconDataUtil
import site.starsone.drawablegenerator.view.MyCard
import java.io.File


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RemixIconPage() {

    var widthDp by remember { mutableStateOf(200.dp) }

    val allListData = RemixIconDataUtil.initResource()

    var list by remember { mutableStateOf(allListData) }

    var keyWord by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            OutlinedTextField(keyWord, onValueChange = {
                keyWord = it
                list = if (keyWord.isNotBlank()) {
                    val newList = allListData.map {
                        val newIconList = it.data
                        it.copy(data = newIconList.filter { it.keyword.any { it.contains(keyWord) } })
                    }
                    newList
                } else {
                    allListData
                }
            }, placeholder = {Text("输入关键字搜索")}, leadingIcon = { Icon(Icons.Default.Search,"") })
        }


        Box(Modifier.fillMaxSize()) {

            val state = rememberLazyListState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { size ->
                        // 获取组件的宽度
                        widthDp = size.width.dp
                    }
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                //注意这里
                state = state
            ) {


                items(list.size) {
                    val item = list[it]
                    if (item.data.isNotEmpty()) {//数据不为空才展示

                        MyCard(item.groupName + "( ${item.data.size}个)") {
                            Spacer(Modifier.height(16.dp))

                            val iconList = item.data

                            //计算每行多少个(每个item宽度为120),动态变化

                            val spanCount = widthDp.div(120).value.toInt()

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


}

@Composable
fun RemixIconItemViewRow(list: List<Icon>) {
    Row {
        list.forEach {
            RemixIconItemView(it)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
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
        modifier = Modifier
            .wrapContentWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        list.forEach {
            RemixIconSingleView(it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RemixIconSingleView(svgFile: File) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var xmlSizeText by remember { mutableStateOf("24") }
    var pngSizeText by remember { mutableStateOf("200") }

    var selectColor by remember { mutableStateOf(Color.Black) }
    var isShowColorPicker by remember { mutableStateOf(false) }

    if (isDialogOpen) {
        AlertDialog(
            //点击外层空白处关闭
            onDismissRequest = { isDialogOpen = false }
        ) {
            Box {

                Column(modifier = Modifier.background(Color.White, RoundedCornerShape(24.dp)).padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.border(1.dp, Color(0xFFe1e6f0)).padding(8.dp)) {
                            Image(
                                loadSvgPainter(svgFile.inputStream(), Density(24f)),
                                contentDescription = null,
                                modifier = Modifier.width(24.dp).height(24.dp)
                            )
                        }

                        Spacer(Modifier.width(8.dp))

                        Text(svgFile.nameWithoutExtension)

                        IconButton(onClick = {
                            CommonUtil.copyText(svgFile.nameWithoutExtension)
                            ComposeToast.show("复制图标名成功!")
                            isDialogOpen = false
                        }) {
                            Icon(
                                painter = painterResource("file-copy-fill.svg"),
                                contentDescription = "Favorite Icon",
                                tint = Color(0xff1d5ef5),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            xmlSizeText,
                            onValueChange = { xmlSizeText = it },
                            label = { Text("输出XML文件的宽高(单位dp)") })
                        Spacer(Modifier.width(16.dp))

                        Button(onClick = {
                            isDialogOpen = false

                            val outputFile = CommonUtil.svgToXml(svgFile, xmlSizeText.toInt())
                            CommonUtil.copyFiles(outputFile)

                            ComposeToast.show("导出xml文件成功,已复制文件到剪切板,可以直接粘贴!")

                        }) {
                            Icon(
                                painter = painterResource("arrow-down-fill.svg"),
                                contentDescription = "Favorite Icon",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Text("XML下载")
                        }
                    }

                    Spacer(Modifier.height(8.dp))


                    Row(verticalAlignment = Alignment.CenterVertically) {

                        //选择颜色
                        Box(Modifier.size(50.dp, 50.dp).background(selectColor).onClick {
                            isShowColorPicker = true
                        }) {

                        }

                        Spacer(Modifier.width(8.dp))

                        OutlinedTextField(
                            pngSizeText,
                            onValueChange = { pngSizeText = it },
                            label = { Text("输出PNG文件的宽高(单位px)") })
                        Spacer(Modifier.width(16.dp))


                        Button(onClick = {
                            val outputFile = CommonUtil.svgToPng(svgFile, Color.White, pngSizeText.toInt())
                            CommonUtil.copyFiles(outputFile)
                            ComposeToast.show("导出png文件成功,已复制文件到剪切板,可以直接粘贴!")
                            isDialogOpen = false
                        }) {
                            Icon(
                                painter = painterResource("arrow-down-fill.svg"),
                                contentDescription = "Favorite Icon",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Text("下载PNG")
                        }
                    }
                }

                CursorDropdownMenu(isShowColorPicker, onDismissRequest = { isShowColorPicker = false }) {
                    ColorPicker(selectColor, onColorChanged = {
                        selectColor = it
                    }, onConfirm = { isShowColorPicker = it })
                }
            }

        }
    }

    var active by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .size(120.dp, 100.dp)
            .background(color = if (active) MaterialTheme.colorScheme.background else Color.White)
            .border(width = 1.dp, color = if (active) MaterialTheme.colorScheme.outline else Color.White)
            .onPointerEvent(PointerEventType.Enter) { active = true }
            .onPointerEvent(PointerEventType.Exit) { active = false }
            .onClick { //显示对话框
                isDialogOpen = true
            },

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            loadSvgPainter(svgFile.inputStream(), Density(24f)),
            contentDescription = null,
            modifier = Modifier.width(24.dp).height(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(svgFile.nameWithoutExtension, fontSize = 12.sp)
    }

}