package com.breavestsnail.wanandroid.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.breavestsnail.wanandroid.AppViewModel
import com.breavestsnail.wanandroid.R
import com.breavestsnail.wanandroid.data.WebPage
import com.breavestsnail.wanandroid.logic.model.Article
import com.breavestsnail.wanandroid.logic.model.Chapter
import com.breavestsnail.wanandroid.removeHtml
import com.breavestsnail.wanandroid.ui.theme.WanAndroidTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun Project(onClickItem: (WebPage) -> Unit){
    val model:AppViewModel = viewModel()
    //获取开始从网络获取chapter信息并存入SharedPreferece中，之后从SharedPreferece中读取，提高加载速度
    val chapters = if (model.isChaptersSaved("project_chapters")){
        model.getSavedChapters("project_chapters").toMutableList()
    }else{
        val tmp = model.projectChapters.observeAsState().value?.let {
            it.getOrNull()
        }
        if (tmp!=null){
            model.saveChapters(tmp,"project_chapters")
        }
        tmp
    }

    Column() {
        chapters?.let {
            val currentChapter = remember {
                mutableStateOf(it[0])
            }
            val projectArticles = currentChapter.value.cid?.let { cid ->
                model.getProjectActicles(cid)
            }
            ProjectChapter(currentChapter = currentChapter.value, chapters = it, onTabSelected ={currentChapter.value = it} )
            if (projectArticles != null) {
                ProjectContent(articles = projectArticles,onClickItem = {onClickItem(it)})
            }
        }
    }

}

@Composable
fun ProjectChapter(currentChapter: Chapter?, chapters: List<Chapter>, onTabSelected:(Chapter)->Unit){
    HorizontalTabs<Chapter>(
        modifier = Modifier.padding(vertical = 10.dp),
        currentTabData = currentChapter,
        tabDatas = chapters,
        onTabSelected = {onTabSelected(it)}
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectContent(modifier: Modifier = Modifier,articles: Flow<PagingData<Article>>,onClickItem: (WebPage) -> Unit){
    val mArticles = articles.collectAsLazyPagingItems()
    LazyVerticalGrid(
        modifier = modifier,
        cells = GridCells.Fixed(2),
        content = {
            items(mArticles){article->
                if (article != null) {
                    ProjectItem(
                        article =article ,
                        modifier = Modifier.padding(6.dp),
                        onClick ={onClickItem(WebPage(article.title,article.link))})
                }
            }
        })

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProjectItem(modifier: Modifier = Modifier,article: Article, onClick:(Article)->Unit){
    Card(
        onClick = {onClick(article)},
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column {
            val painter = rememberImagePainter(
                data = article.envelopePic,
                builder = {
                    crossfade(true)
                }
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.0f),
                painter = painter,
                contentDescription = article.title,
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = article.title.removeHtml(),
                modifier = Modifier.padding(10.dp,3.dp),
                fontSize = 16.sp,
                maxLines = 2
            )
            Card(modifier = Modifier
                .padding(10.dp, 3.dp)
                .border(1.dp, MaterialTheme.colors.secondary, RoundedCornerShape(7.dp))
                .padding(2.dp)
            ) {
                Text(
                    text = "作者：${article.author}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.secondary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
@Preview
fun Preview(){
    WanAndroidTheme {
        ProjectItem(
            article = Article("me","基础","https://www.wanandroid.com/resources/image/pc/default_project_img.jpg","","https://www.wanandroid.com/blog/show/3019","2021-06-15 22:50","开源项目主Tab","基于Jetpack Compose实现的一款集新闻、视频、美图、天气等功能的资讯App,持续完善中..."),
            onClick = {}
        )
    }
}