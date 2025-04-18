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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hotel.R
import com.example.hotel.ui.theme.Black
import com.example.hotel.ui.theme.BlackTransparent
import com.example.hotel.ui.theme.Blue
import com.example.hotel.ui.viewmodel.SignUpUiState
import com.example.hotel.ui.viewmodel.SignUpViewModel
import kotlinx.coroutines.launch

/**
 * Sign Up Screen composable
 *
 * @param onSignInClick Callback for when the user clicks on the sign in link
 * @param onSignUpSuccess Callback for when the user successfully signs up
 * @param viewModel ViewModel for the sign up screen
 */
@Composable
fun SignUpScreen(
    onSignInClick: () -> Unit = {},
    onSignUpSuccess: () -> Unit = {},
    viewModel: SignUpViewModel = viewModel()
) {
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val location by viewModel.location.collectAsState()
    val password by viewModel.password.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Password visibility state
    var passwordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Get string resources
    val successMessage = stringResource(R.string.sign_up_success)

    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (uiState) {
            is SignUpUiState.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(message = successMessage)
                }
                onSignUpSuccess()
                viewModel.resetState()
            }
            is SignUpUiState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = (uiState as SignUpUiState.Error).message
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
                        painter = painterResource(id = R.drawable.sign_up_background),
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
                    text = stringResource(R.string.lets_get_started),
                    color = Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = (-0.165).sp,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 0.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = stringResource(R.string.please_create_account),
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
                        // Name field
                        TextField(
                            value = name,
                            onValueChange = { viewModel.updateName(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.enter_name),
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
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        // Email field
                        TextField(
                            value = email,
                            onValueChange = { viewModel.updateEmail(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.email),
                                    color = Color(0xFFCECCCC),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = (-0.41).sp,
                                    lineHeight = 22.sp,
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_email),
                                    contentDescription = "Email icon",
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

                        // Location field
                        TextField(
                            value = location,
                            onValueChange = { viewModel.updateLocation(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.location),
                                    color = Color(0xFFCECCCC),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = (-0.41).sp,
                                    lineHeight = 22.sp,
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_location),
                                    contentDescription = "Location icon",
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
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
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
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0x33000000),
                                unfocusedIndicatorColor = Color(0x33000000),
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Sign up button
                Box(
                    modifier = Modifier
                        .padding(horizontal = 7.dp) // Adjusted to positive padding
                ) {
                    Button(
                        onClick = { viewModel.signUp() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        enabled = uiState !is SignUpUiState.Loading,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)
                    ) {
                        if (uiState is SignUpUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.sign_up),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                letterSpacing = (-0.165).sp,
                                lineHeight = 24.sp,
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Or sign up using
                Text(
                    text = stringResource(R.string.or_sign_up_using),
                    color = BlackTransparent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = (-0.165).sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 0.dp, bottom = 0.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Social sign up buttons
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 69.dp) // Adjusted to positive padding
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Twitter
                        SocialButton(
                            iconResId = R.drawable.ic_twitter,
                            contentDescription = "Sign up with Twitter",
                            onClick = { /* Handle Twitter sign up */ },
                            backgroundColor = Color(0x19007AFF)
                        )

                        // Google
                        SocialButton(
                            iconResId = R.drawable.ic_google,
                            contentDescription = "Sign up with Google",
                            onClick = { /* Handle Google sign up */ },
                            backgroundColor = Color(0x19FF9790)
                        )

                        // Facebook
                        SocialButton(
                            iconResId = R.drawable.ic_facebook,
                            contentDescription = "Sign up with Facebook",
                            onClick = { /* Handle Facebook sign up */ },
                            backgroundColor = Color(0x19707ADA)
                        )
                    }
                }

                // Add fixed space instead of weight to ensure consistent spacing
                Spacer(modifier = Modifier.height(40.dp))

                // Already have an account
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    // Regular text part
                    Text(
                        text = "Already have an account? ",
                        color = Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = (-0.165).sp,
                        lineHeight = 18.sp
                    )

                    // Sign in text in blue
                    Text(
                        text = "Sign in.",
                        color = Blue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = (-0.165).sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.clickable(onClick = onSignInClick)
                    )
                }

                // Home indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .padding(bottom = 2.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .width(135.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .align(Alignment.BottomCenter)
                            .offset(y = (-8).dp),
                        color = Black
                    )
                }
                }
            }
        }
    }
}

/**
 * Social media button composable
 *
 * @param iconResId Resource ID for the icon
 * @param contentDescription Content description for accessibility
 * @param onClick Callback for when the button is clicked
 * @param backgroundColor Background color of the button
 */
@Composable
fun SocialButton(
    iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    backgroundColor: Color = Color.White
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified
        )
    }
}
