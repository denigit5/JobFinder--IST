package com.example.job_finder_ist

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.OutlinedTextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        setContent {
            LoginScreen()
        }
    }

    @Composable
    fun LoginScreen() {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var showPassword by remember { mutableStateOf(false) }

        // State to control the visibility of the verification dialog
        var showVerificationDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.ist_logo),
                contentDescription = "School Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 50.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(100.dp))

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (showPassword) "Hide password" else "Show password"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this@LoginActivity, "Please enter your email and password", Toast.LENGTH_SHORT).show()
                    } else {
                        loginUser(email, password) { isVerified ->
                            if (isVerified) {
                                // Navigate to MainActivityJobFinder if email is verified
                                val intent = Intent(this@LoginActivity, MainActivityJobFinder::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // Show verification dialog
                                showVerificationDialog = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Link
            Text(
                text = "Don't have an account? Sign up here",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                        startActivity(intent)
                    }
                    .background(Color.Transparent)
            )

            // Forgot Password Link
            Text(
                text = "Forgot your password? Click here",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        val intent = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
                        startActivity(intent)
                    }
                    .background(Color.Transparent)
            )
        }

        // Verification Dialog
        if (showVerificationDialog) {
            VerificationDialog(
                onResendClick = {
                    resendVerificationEmail()
                    showVerificationDialog = false
                },
                onDismiss = {
                    showVerificationDialog = false
                }
            )
        }
    }

    /**
     * Function to handle user login and check email verification.
     * @param email User's email.
     * @param password User's password.
     * @param callback Callback to handle the result. Passes true if email is verified, else false.
     */
    private fun loginUser(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@LoginActivity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        user.reload().addOnCompleteListener { reloadTask ->
                            if (reloadTask.isSuccessful) {
                                Log.d("LoginActivity", "User email verified: ${user.isEmailVerified}")
                                if (user.isEmailVerified) {
                                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                                    callback(true)
                                } else {
                                    Toast.makeText(this@LoginActivity, "Email not verified. Please verify your email.", Toast.LENGTH_SHORT).show()
                                    callback(false)
                                }
                            } else {
                                Log.e("LoginActivity", "User reload failed: ${reloadTask.exception}")
                                Toast.makeText(this@LoginActivity, "Failed to reload user data.", Toast.LENGTH_SHORT).show()
                                callback(false)
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        callback(false)
                    }
                } else {
                    handleLoginError(task.exception)
                    callback(false)
                }
            }
    }

    /**
     * Function to handle different login error cases.
     */
    private fun handleLoginError(exception: Exception?) {
        when (exception) {
            is FirebaseAuthInvalidCredentialsException -> {
                Toast.makeText(this, "Invalid credentials. Please check your password.", Toast.LENGTH_SHORT).show()
            }
            is FirebaseAuthInvalidUserException -> {
                Toast.makeText(this, "No account found with this email.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Authentication failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Function to resend the verification email.
     */
    private fun resendVerificationEmail() {
        val user = auth.currentUser
        user?.let {
            it.sendEmailVerification()
                .addOnCompleteListener { emailTask ->
                    if (emailTask.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Verification email resent. Please check your inbox.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Failed to resend verification email: ${emailTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } ?: run {
            Toast.makeText(this@LoginActivity, "User not found.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Composable function for the verification dialog.
     */
    @Composable
    fun VerificationDialog(onResendClick: () -> Unit, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Email Not Verified") },
            text = { Text(text = "Your email address is not verified. Please check your inbox for a verification email.") },
            confirmButton = {
                Button(onClick = { onResendClick() }) {
                    Text("Resend Email")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Dismiss")
                }
            }
        )
    }
}
