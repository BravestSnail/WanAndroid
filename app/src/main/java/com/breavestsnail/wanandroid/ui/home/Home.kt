package com.breavestsnail.wanandroid.ui.home

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.rememberImagePainter
import com.breavestsnail.wanandroid.AppViewModel
import com.breavestsnail.wanandroid.R
import com.breavestsnail.wanandroid.data.WebPage
import com.breavestsnail.wanandroid.logic.model.Article
import com.breavestsnail.wanandroid.logic.model.Banner
import com.breavestsnail.wanandroid.logic.model.Chapter
import com.breavestsnail.wanandroid.removeHtml
import com.breavestsnail.wanandroid.ui.HorizontalTabs
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

// TODO: 使用Navigation为Chapter导航 

@Composable
fun Home(onClickItem: (WebPage) -> Unit) {
    val model: AppViewModel = viewModel()
    //开始使用时从网络加载数据并存入SharedPreferece中，之后直接从SharedPreferece读取
    val chapters = if (model.isChaptersSaved("chapters")){
        model.getSavedChapters("chapters").toMutableList()
    }else{
        val tmp = model.chapters.observeAsState().value?.let {
            mutableListOf(model.firshChapter).apply {
                it.getOrNull()?.let { it1 -> addAll(it1) }
            }
        }
        if (tmp!=null){
            model.saveChapters(tmp,"chapters")
        }
        tmp
    }
    var currentChapter:Chapter by remember {
        mutableStateOf(model.firshChapter)
    }

    var searchText by remember {
        mutableStateOf("")
    }
    var isSearching by remember {
        mutableStateOf(false)
    }
    isSearching = !(searchText.isBlank())
    val searchList = model.getSearchArticles(searchText)
    val articles = model.getArticles(currentChapter.cid)

    val banners = model.banners

    Column(Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            val contentPaddingValues = PaddingValues(horizontal = 10.dp)
            TopBar(onSearch = {
                searchText = it
                isSearching = true
            })
            if (isSearching){
                SeachResultList(
                    modifier = Modifier.padding(contentPaddingValues),
                    searchList=searchList,
                    onClickItem = {
                        onClickItem(it)
                    })
            }else{
                chapters?.let {
                    Articles(
                        modifier = Modifier.padding(contentPaddingValues),
                        articles = articles,
                        banners = banners,
                        currentChapter = currentChapter,
                        chapters = it,
                        onClick = {
                            onClickItem(it)
                        },
                        onTabSelected = {chapter->
                            currentChapter = chapter
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(onSearch:(String)->Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.default_head_photo),
            contentDescription = "头像",
            Modifier
                .size(64.dp)
                .clip(CircleShape)
                .padding(5.dp)
        )
        SearchBar(
            Modifier
                .weight(1f)
                .padding(5.dp),
            onSearch = {onSearch(it)}
        )
        Box(
            Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colors.primaryVariant)
                .padding(10.dp)
                .size(30.dp)
                .align(Alignment.CenterVertically)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_message), contentDescription = "消息")
        }
    }
}

@Composable
fun Chapter(currentChapter: Chapter?, chapters: List<Chapter>, onTabSelected:(Chapter)->Unit){
    HorizontalTabs<Chapter>(currentTabData = currentChapter, tabDatas = chapters, onTabSelected = {onTabSelected(it)})
}

@OptIn(ExperimentalPagerApi::class, coil.annotation.ExperimentalCoilApi::class)
@Composable
fun Banner(
    modifier: Modifier = Modifier,
    banners: LiveData<Result<List<Banner>>>,
    clickedBanner: (banner: Banner) -> Unit
) {
    val mBanners = banners.observeAsState().value?.let {
        it.getOrNull()?: listOf()
    }?: listOf()
    val pagerState = rememberPagerState(
        pageCount = mBanners.size,
        initialOffscreenLimit = 1,
        infiniteLoop = true,
        initialPage = 0
    )
    //记录Banner是否被选中，选中改变底部点的颜色
    var selectedNum by remember {
        mutableStateOf(0)
    }
    selectedNum = pagerState.currentPage
    Card(elevation = 5.dp, shape = RoundedCornerShape(15.dp)) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier.fillMaxWidth()
        ) {
            HorizontalPager(state = pagerState) { page ->
                val painter = rememberImagePainter(
                    data = mBanners[page].imagePath,
                    builder = {
                        placeholder(R.drawable.ic_hold_place)
                        crossfade(true)
                    }
                )
                Image(
                    painter = painter,
                    contentDescription = mBanners[page].desc,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.8f)
                        .clickable {
                            clickedBanner(mBanners[page])
                        },
                    contentScale = ContentScale.Crop
                )
                LaunchedEffect(pagerState.currentPage) {
                    delay(5000)
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in mBanners.indices) {
                    Surface(
                        shape = CircleShape,
                        color = if (selectedNum == i) MaterialTheme.colors.primary else Color.LightGray,
                        modifier = Modifier
                            .size(if (selectedNum == i) 23.dp else 20.dp)
                            .padding(5.dp)
                    ) {
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Articles(
    modifier: Modifier = Modifier,
    articles: Flow<PagingData<Article>>,
    banners: LiveData<Result<List<Banner>>>,
    currentChapter: Chapter?,
    chapters: List<Chapter>,
    onClick: (WebPage) -> Unit,
    onTabSelected: (Chapter) -> Unit
) {
    val mArticles = articles.collectAsLazyPagingItems()
    LazyColumn {
        stickyHeader {
            Chapter(currentChapter,chapters,
                onTabSelected = {chapter->
                    onTabSelected(chapter)
                }
            )
        }
        item {
            Banner(
                modifier = modifier,
                clickedBanner = {
                Log.d("test", "Articles: 点到广告了")
                onClick(WebPage(it.title,it.url)) },
                banners = banners)

        }
        itemsIndexed(mArticles) { index, article ->
            article?.let {
                Article(
                    modifier = modifier
                    ,article = article
                ){
                    Log.d("test", "Articles: 点到文章了")
                    onClick(WebPage(it.title,it.link))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Article(modifier: Modifier = Modifier,article: Article, onClick:(Article)->Unit) {
    Card(
        onClick = {onClick(article)},
        modifier = modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp
    ) {
        Column(Modifier.padding(vertical = 6.dp,horizontal = 10.dp)) {
            val contentModifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp)
            val cardModifier = Modifier
                .padding(end = 5.dp)
                .border(1.dp, MaterialTheme.colors.secondary, RoundedCornerShape(7.dp))
                .padding(2.dp)
            //Text暂时不支持解析Html，故将Html标签去掉
            Text(text = article.title.removeHtml(), modifier = contentModifier,fontSize = 18.sp)
            LazyRow() {
                item {
                    Card(cardModifier) {
                        Text(
                            text = if (article.author.isNullOrBlank()) "分享人：${article.shareUser}" else "作者：${article.author}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
                item {
                    Card(cardModifier) {
                        Text(
                            text = article.chapterName,
                            fontSize = 14.sp,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
                item {
                    Card(cardModifier) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_time),
                                contentDescription = "时间",
                                modifier = Modifier.size(14.dp),
                                MaterialTheme.colors.secondary
                            )
                            Text(
                                text = article.niceDate,
                                fontSize = 14.sp,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }
                }
            }

        }
    }
}
