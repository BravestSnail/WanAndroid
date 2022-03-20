package com.breavestsnail.wanandroid.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.breavestsnail.wanandroid.R
import com.breavestsnail.wanandroid.data.WebPage
import com.breavestsnail.wanandroid.logic.model.Article
import com.breavestsnail.wanandroid.ui.theme.Gray100
import kotlinx.coroutines.flow.Flow

@Composable
fun SearchBar(modifier: Modifier,onSearch:(String)->Unit) {
    var searchText by remember {
        mutableStateOf("")
    }
    Row(
        modifier
            .padding(12.dp, 2.dp, 12.dp, 6.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Gray100),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchText, onValueChange = {
                searchText = it
                onSearch(searchText)
            },
            modifier = Modifier
                .padding(start = 24.dp)
                .weight(1f),
            textStyle = TextStyle(fontSize = 15.sp),
            singleLine = true
        ) {
            if (searchText.isEmpty()) {
                Text(text = "搜一下", fontSize = 15.sp, color = Color.Gray)
            }
            it()
        }
        Button(
            {onSearch(searchText)},
            Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search), contentDescription = "搜索",
                Modifier.size(36.dp), tint = Color.White
            )
        }
    }
}

@Composable
fun SeachResultList(modifier: Modifier = Modifier,searchList: Flow<PagingData<Article>>,onClickItem:(WebPage)->Unit){
    val mSearchList = searchList.collectAsLazyPagingItems()
    LazyColumn(modifier){
        items(mSearchList){article->
            article?.let {
                Article(article = article){
                    Log.d("test", "Articles: 点到文章了")
                    onClickItem(WebPage(it.title,it.link))
                }
            }
        }
    }
}