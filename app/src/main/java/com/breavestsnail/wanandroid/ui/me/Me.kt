package com.breavestsnail.wanandroid.ui.me

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.breavestsnail.wanandroid.R
import com.breavestsnail.wanandroid.data.AppData
import com.breavestsnail.wanandroid.data.Function
import com.breavestsnail.wanandroid.logic.model.User

@Composable
fun Me(user: User,onClickLogin: () -> Unit){
    Column() {
        MeTopBar(user = user,{onClickLogin()})
        AccountFunction(funtionList = AppData.functions,modifier = Modifier.padding(20.dp))
    }
}

@Composable
fun MeTopBar(user:User,onClick:()->Unit){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colors.primaryVariant)
            .padding(horizontal = 15.dp, vertical = 40.dp)
            .fillMaxWidth()
    ) {
        val painter = if (user.id==-1) painterResource(id = R.drawable.default_head_photo) else
            rememberImagePainter(
                data = user.icon,
                builder = {
                    placeholder(R.drawable.default_head_photo)
                    crossfade(true)
                }
            )
        Image(
            painter = painter, 
            contentDescription = "头像",
            modifier = Modifier
                .clip(CircleShape)
                .size(80.dp)
        )
        Text(
            modifier = Modifier
                .padding(start = 15.dp)
                .clickable { onClick() },
            text = if (user.id==-1) "登录/注册" else user.nickname,
            fontSize = 20.sp
        )
    }
}

@Composable
fun AccountFunction(funtionList: List<Function>,modifier: Modifier = Modifier){
    Column(modifier = modifier) {
        funtionList.forEach {function->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = function.icon), 
                    contentDescription =function.title,
                    modifier = Modifier
                        .size(54.dp)
                        .padding(end = 20.dp, top = 10.dp, bottom = 10.dp)
                )
                Text(text = function.title)
            }
        }
    }
}

@Composable
fun LoginPage(onClickBackBtn: () -> Unit, onClickLoginBtn:(username:String,password:String)->Unit){
    val username = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = {},
            navigationIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = "返回",
                    modifier = Modifier
                        .clickable { onClickBackBtn() }
                        .size(36.dp)
                )
            },
        )
        Text(text = "登录后更精彩",modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),textAlign = TextAlign.Center,fontSize = 25.sp)
        TextField(
            value = username.value,
            onValueChange = {username.value = it},
            leadingIcon = {
              Text(text = "账号：")
            },
            label = { Text(text = "请输入用户名")},
            trailingIcon = {
               Icon(imageVector = Icons.Filled.Clear, contentDescription = "清除",modifier = Modifier.clickable { username.value="" })
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
        )
        TextField(
            value = password.value,
            onValueChange = {password.value = it },
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Text(text = "密码：")
            },
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Clear, contentDescription = "清除",modifier = Modifier.clickable { password.value="" })
            },
            label = { Text(text = "请输入密码")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
        )
        Button(onClick = { onClickLoginBtn(username.value,password.value) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = "登录")
        }
    }
}