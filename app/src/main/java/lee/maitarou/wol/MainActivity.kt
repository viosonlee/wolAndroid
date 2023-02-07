package lee.maitarou.wol

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.edit
import cn.jiguang.api.utils.JCollectionAuth
import cn.jpush.android.api.JPushInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lee.maitarou.wol.push.PushRequest
import lee.maitarou.wol.ui.page.NoRoleUi
import lee.maitarou.wol.ui.page.ReceiverUi
import lee.maitarou.wol.ui.page.SenderUi
import lee.maitarou.wol.ui.theme.WolTheme
import lee.maitarou.wol.utils.PushConfig
import lee.maitarou.wol.utils.pushApiService
import lee.maitarou.wol.utils.sp
import lee.maitarou.wol.wol.WolSender

class MainActivity : ComponentActivity() {
    companion object {
        const val isSenderKey = "isSender"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JCollectionAuth.setAuth(this, true)
        JPushInterface.setDebugMode(BuildConfig.DEBUG)
        JPushInterface.init(this)
        setContent {
            WolTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val context = LocalContext.current
                    //是否已经选择了角色
                    var hasRole by remember { mutableStateOf(false) }
                    //发送端还是接收端
                    var isSender by remember { mutableStateOf<Boolean?>(null) }
                    LaunchedEffect(key1 = Unit, block = {
                        context.sp {
                            val isSenderCode = getInt(isSenderKey, -1)
                            hasRole = isSenderCode >= 0
                            if (isSenderCode == 0) {
                                isSender = false
                            } else if (isSenderCode == 1) {
                                isSender = true
                            }
                        }
                    })
                    if (hasRole) {
                        if (isSender == true) {
                            SenderUi()
                        } else {
                            ReceiverUi()
                        }
                    } else {
                        NoRoleUi {
                            hasRole = true
                            isSender = it
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(false)
    }
}