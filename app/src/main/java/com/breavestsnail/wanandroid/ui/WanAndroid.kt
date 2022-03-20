package com.breavestsnail.wanandroid.ui

import android.os.Build
import android.text.Html
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.paging.compose.LazyPagingItems
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.breavestsnail.wanandroid.R
import com.breavestsnail.wanandroid.data.TabData
import com.breavestsnail.wanandroid.data.WebPage
import com.breavestsnail.wanandroid.removeHtml

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun WebViewPage(webPage: WebPage,onClickBackBtn:()->Unit){
    Scaffold(
        topBar = {
            Row(
                Modifier.padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onClickBackBtn()},
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24), contentDescription = "返回",modifier = Modifier.size(30.dp))
                }
                Text(text = webPage.title.removeHtml(),Modifier.weight(1f),maxLines = 1,fontSize = 18.sp)
            }
        }
    ) { innerpadding->
        AndroidView(factory = {
            val apply = WebView(it).apply {
                //设置WebView的大小为父布局的尺寸
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                //实现WebView的深色模式,需要系统为Android Q
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                    WebSettingsCompat.setForceDark(
                        this.settings,
                        WebSettingsCompat.FORCE_DARK_AUTO
                    )
                }
                webViewClient = WebViewClient()
                loadUrl(webPage.url)
            }
            apply
        },update = {
            it.loadUrl(webPage.url)
        },
        modifier = Modifier.padding(innerpadding))
    }
}

@Composable
fun <T:TabData>HorizontalTabs(modifier: Modifier = Modifier,currentTabData: TabData?, tabDatas: List<TabData>, onTabSelected:(T)->Unit){
    var selected by remember {
        mutableStateOf(currentTabData)
    }
    LazyRow(modifier.background(MaterialTheme.colors.background)){
        itemsIndexed(tabDatas){index, tabData ->
            Column (
                Modifier
                    .padding(12.dp, 4.dp)
                    .width(IntrinsicSize.Max)
                    .clickable {
                        if (currentTabData != tabData) {
                            onTabSelected(tabData as T)
                            selected = tabData
                        }
                    }
            ) {
                Text(
                    text = tabData.name,
                    fontSize = 18.sp,
                    color = if (tabData==selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(if (tabData == selected) MaterialTheme.colors.primary else Color.Transparent)
                )
            }
        }
    }
}

@ExperimentalFoundationApi
fun <T: Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}

//@RequiresApi(Build.VERSION_CODES.N)
//@Composable
//fun StyledText(
//    text:String,
//    modifier: Modifier=Modifier,
//    textColor: Color =MaterialTheme.colors.onSurface,
//    fontSize: TextUnit = 16.sp,
//    maxLines:Int = 0
//){
//    AndroidView(
//        modifier = modifier,
//        factory = {context ->
//            TextView(context).apply {
//                layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//                setText(Html.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY))
//                setTextColor(textColor.toArgb())
//                setTextSize(fontSize.value)
//                setMaxLines(maxLines)
//            }
//        }
//    )
//}

