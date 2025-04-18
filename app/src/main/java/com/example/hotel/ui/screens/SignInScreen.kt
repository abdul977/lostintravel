package com.example.hotel.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotel.R
import com.example.hotel.ui.theme.Black
import com.example.hotel.ui.theme.BlackTransparent
import com.example.hotel.ui.theme.Blue
import com.example.hotel.ui.viewmodel.SignInUiState
import com.example.hotel.ui.viewmodel.SignInViewModel
import kotlinx.coroutines.launch

/**
 * Sign In Screen composable
 *
 * @param onSignUpClick Callback for when the user clicks on the sign up link
 * @param onSignInSuccess Callback for when the user successfully signs in
 * @param viewModel ViewModel for the sign in screen
 */
@Composable
fun SignInScreen(
    onSignUpClick: () -> Unit = {},
    onSignInSuccess: () -> Unit = {},
    viewModel: SignInViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application))
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Password visibility state
    var passwordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Get string resources
    val successMessage = stringResource(R.string.sign_in_success)

    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (uiState) {
            is SignInUiState.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(message = successMessage)
                }
                onSignInSuccess()
                viewModel.resetState()
            }
            is SignInUiState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = (uiState as SignInUiState.Error).message
                    )
                }
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Content
            // Using verticalScroll modifier to make the screen scrollable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()), // Add scrolling capability
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header image - full width
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.sign_in),
                        contentDescription = "Beach header",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                // Content with padding
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                Spacer(modifier = Modifier.height(24.dp))

                // Title
                Text(
                    text = stringResource(R.string.welcome_back),
                    color = Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.165).sp,
                    lineHeight = 25.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 0.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = stringResource(R.string.please_sign_into_account),
                    color = BlackTransparent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.165).sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 0.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Form fields container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp)
                ) {
                    Column {
                        // Email field
                        TextField(
                            value = email,
                            onValueChange = { viewModel.updateEmail(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.enter_email),
                                    color = Color(0xFFCECCCC),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = (-0.41).sp,
                                    lineHeight = 22.sp,
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_person),
                                    contentDescription = "Person icon",
                                    tint = Color(0xFFCECCCC),
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0x33000000),
                                unfocusedIndicatorColor = Color(0x33000000),
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        // Password field
                        TextField(
                            value = password,
                            onValueChange = { viewModel.updatePassword(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.password),
                                    color = Color(0xFFCECCCC),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = (-0.41).sp,
                                    lineHeight = 22.sp,
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_lock),
                                    contentDescription = "Lock icon",
                                    tint = Color(0xFFCECCCC),
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_visibility),
                                    contentDescription = "Toggle password visibility",
                                    tint = Color(0xFFCECCCC),
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { passwordVisible = !passwordVisible }
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0x33000000),
                                unfocusedIndicatorColor = Color(0x33000000),
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                        )
                    }
                }

                // Forgot password
                Text(
                    text = stringResource(R.string.forgot_password),
                    color = BlackTransparent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.165).sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp, end = 0.dp)
                        .clickable { /* Handle forgot password */ }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sign in button
                Box(
                    modifier = Modifier
                        .padding(horizontal = 7.dp)
                ) {
                    Button(
                        onClick = { viewModel.signIn() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.sign_in),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = (-0.165).sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Or sign in using
                Text(
                    text = stringResource(R.string.or_sign_in_using),
                    color = BlackTransparent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = (-0.165).sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Social sign in buttons
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 69.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Twitter
                        SocialButton(
                            iconResId = R.drawable.ic_twitter,
                            contentDescription = "Sign in with Twitter",
                            onClick = { /* Handle Twitter sign in */ },
                            backgroundColor = Color(0x19007AFF)
                        )

                        // Google
                        SocialButton(
                            iconResId = R.drawable.ic_google,
                            contentDescription = "Sign in with Google",
                            onClick = { /* Handle Google sign in */ },
                            backgroundColor = Color(0x19FF9790)
                        )

                        // Facebook
                        SocialButton(
                            iconResId = R.drawable.ic_facebook,
                            contentDescription = "Sign in with Facebook",
                            onClick = { /* Handle Facebook sign in */ },
                            backgroundColor = Color(0x19707ADA)
                        )
                    }
                }

                // Add fixed space instead of weight to ensure consistent spacing
                Spacer(modifier = Modifier.height(40.dp))

                // Don't have an account
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    // Regular text part
                    Text(
                        text = "Don't have an account? ",
                        color = Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = (-0.165).sp,
                        lineHeight = 18.sp
                    )

                    // Sign up text in blue
                    Text(
                        text = "Sign up.",
                        color = Blue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = (-0.165).sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.clickable(onClick = onSignUpClick)
                    )
                }
            }
        }
    }
}
}
