package lee.maitarou.wol.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import lee.maitarou.wol.MainActivity
import lee.maitarou.wol.R
import lee.maitarou.wol.utils.sp

@Composable
fun NoRoleUi(chooseFeedback: ((isSender: Boolean) -> Unit)? = null) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.pls_choose_role))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            context.sp {
                edit {
                    putInt(MainActivity.isSenderKey, 1)
                }
            }
            chooseFeedback?.invoke(true)
        }) {
            Text(text = stringResource(id = R.string.sender))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            context.sp {
                edit {
                    putInt(MainActivity.isSenderKey, 0)
                }
            }
            chooseFeedback?.invoke(false)
        }) {
            Text(text = stringResource(id = R.string.recevier))
        }
    }
}