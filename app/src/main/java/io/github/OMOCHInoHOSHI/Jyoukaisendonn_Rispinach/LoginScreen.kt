package io.github.OMOCHInoHOSHI.Jyoukaisendonn_Rispinach

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(): Boolean {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    val sharedPreferences: SharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    var email by remember { mutableStateOf(sharedPreferences.getString("email", "") ?: "") }
    var password by remember { mutableStateOf(sharedPreferences.getString("password", "") ?: "") }
    var rememberMe by remember { mutableStateOf(sharedPreferences.getBoolean("rememberMe", false)) }
    var isLoading by remember { mutableStateOf(false) }
    var isTextFieldClicked by remember { mutableStateOf(false) }

    var signSuccess by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Scaffold(
//        topBar = {
//            TopAppBar(
//                modifier = Modifier
//                    .padding(top = 110.dp)
//                    .fillMaxWidth(),
//                title = {
//                    Text(
//                        modifier = Modifier.offset(x = -15.dp),
//                        text = "ようこそRispinachへ",
//                        fontSize = 30.sp
//                    )
//                }
//            )
//        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.risrisris),
                    contentDescription = "背景画像",
                    contentScale = ContentScale.FillHeight, // 画面全体に引き伸ばす
                    modifier = Modifier.fillMaxSize()
                )



                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "メールアドレスを入力",
                        style = TextStyle(
                            color = Color.White, // テキストの色を赤色に設定
                            fontSize = 14.sp // フォントサイズを18スケールポイントに設定
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (it.isNotEmpty()) emailError = false
                        },
                        label = { Text("メールアドレス") },
                        isError = emailError,
                        modifier = Modifier.fillMaxWidth().clickable {
                            isTextFieldClicked = true
                        }
                    )
                    if (emailError) {
                        Text(
                            text = "メールアドレスを入力してください。",
                            color = Color.Red,
                            style = TextStyle(fontSize = 12.sp),
                            modifier = Modifier.align(Alignment.Start).padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "パスワードは6文字以上",
                        style = TextStyle(
                            color = Color.White, // テキストの色を赤色に設定
                            fontSize = 14.sp // フォントサイズを18スケールポイントに設定
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (it.isNotEmpty()) passwordError = false
                        },
                        label = { Text("パスワード") },
                        isError = passwordError,
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    if (passwordError) {
                        Text(
                            text = "パスワードを入力してください。",
                            color = Color.Red,
                            style = TextStyle(fontSize = 12.sp),
                            modifier = Modifier.align(Alignment.Start).padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (email.isEmpty()) {
                                emailError = true
                            }
                            if (password.isEmpty()) {
                                passwordError = true
                            }
                            if (!emailError && !passwordError) {
                                isLoading = true
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        signSuccess = task.isSuccessful
                                    }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ログインする", fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (email.isEmpty()) {
                                emailError = true
                            }
                            if (password.isEmpty()) {
                                passwordError = true
                            }
                            if (!emailError && !passwordError) {
                                isLoading = true
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        signSuccess = task.isSuccessful
                                    }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("新規登録", fontSize = 18.sp)
                    }

                    if (isLoading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }
                }
            }
        }
    )

    return signSuccess
}














/**
 * 例: Firestore や Realtime Database にユーザー情報を保存する場合の関数。
 * 実際に使う時は必要なパラメータを調整し、マルチラインコメントを正しく閉じること。
 */

/*
fun saveUserDataToDB(uid: String?, email: String?) {
    if (uid == null || email == null) return

    // --- Firestore例 ---
    // val db = Firebase.firestore
    // val userMap = mapOf(
    //     "email" to email,
    //     "registeredAt" to System.currentTimeMillis()
    // )
    // db.collection("users")
    //     .document(uid)
    //     .set(userMap)
    //     .addOnSuccessListener {
    //         Log.d("Firestore", "User data saved successfully.")
    //     }
    //     .addOnFailureListener {
    //         Log.e("Firestore", "Error saving user data: ${it.message}")
    //     }

    // --- Realtime Database例 ---
    // val rtdb = Firebase.database.reference
    // val userRef = rtdb.child("users").child(uid)
    // userRef.setValue(userMap)
    //     .addOnSuccessListener {
    //         Log.d("RTDB", "User data saved successfully.")
    //     }
    //     .addOnFailureListener {
    //         Log.e("RTDB", "Error saving user data: ${it.message}")
    //     }
}
*/
