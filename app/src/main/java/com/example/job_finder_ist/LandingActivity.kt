package com.example.job_finder_ist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.job_finder_ist.ui.theme.JobFinderTheme
import androidx.compose.ui.Modifier as Modifier2

class LandingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view using Jetpack Compose
        setContent {
            JobFinderTheme {
                // Call the LandingScreen composable with click handlers for buttons
                LandingScreen(
                    onSignUpClick = {
                        val intent = Intent(this, SignUpActivity::class.java)
                        startActivity(intent)
                    },
                    onLoginClick = {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    },
                    onAllJobsClick = {
                        val intent = Intent(this, AddJobsActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun LandingScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onAllJobsClick: () -> Unit
) {
    // Background layout with Box and Images
    Box(
        modifier = Modifier2.fillMaxSize() // Fill the entire screen
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.backrun3),
            contentDescription = stringResource(R.string.school_logo_desc),
            modifier = Modifier2.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay with semi-transparent background
        Box(
            modifier = Modifier2
                .fillMaxSize()
                .background(Color(0x99000000))
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ist_logo),
                contentDescription = stringResource(R.string.school_logo_desc),
                modifier = Modifier
                    .size(150.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStartPercent = 50,
                            bottomEndPercent = 50
                        )
                    )
                    .align(Alignment.TopCenter)
            )
        }

        // Main content with Column layout
        Column(
            modifier = Modifier2
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier2.height(40.dp))

            // Welcome Box
            Box(
                modifier = Modifier2
                    .fillMaxWidth()
                    .background(Color(0x50FFFFFF))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier2.padding(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.welcome_message),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier2
                            .padding(5.dp)
                            .align(Alignment.CenterHorizontally),
                        style = TextStyle(
                            fontSize = 27.sp,
                            lineHeight = 35.sp
                        )
                    )

                    Spacer(modifier = Modifier2.height(18.dp))

                    Text(
                        text = stringResource(R.string.create_account),
                        color = Color.White,
                        modifier = Modifier2
                            .background(Color(0x90AF0404))
                            .padding(10.dp)
                            .clickable { onSignUpClick() }
                    )

                    Spacer(modifier = Modifier2.height(18.dp))

                    // Sign Up and Log In buttons inside a Row
                    Row(
                        modifier = Modifier2
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        Button(
                            onClick = onSignUpClick,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier2
                                .padding(10.dp)
                                .width(116.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.signup),
                                color = Color(0xFFE61C1C)
                            )
                        }

                        Spacer(modifier = Modifier2.width(16.dp))

                        Button(
                            onClick = onLoginClick,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier2
                                .padding(8.dp)
                                .width(176.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.login),
                                color = Color(0xFFE61C1C)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier2.height(55.dp))

        // Footer with icons and clickable labels
        Row(
            modifier = Modifier2
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0x90AF0404))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.jobimg1),
                contentDescription = stringResource(R.string.todo),
                modifier = Modifier2.size(25.dp)
            )

            // Clickable "All Jobs" text
            Text(
                text = stringResource(R.string.all_jobs),
                color = Color.White,
                modifier = Modifier2.clickable { onAllJobsClick() }
            )

            Image(
                painter = painterResource(id = R.drawable.jobimg2),
                contentDescription = stringResource(R.string.tododo),
                modifier = Modifier2.size(26.dp)
            )

            // Clickable "Applied Jobs" text
            Text(
                text = stringResource(R.string.applied_jobs),
                color = Color.White,
                modifier = Modifier2.clickable { onLoginClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLandingScreen() {
    JobFinderTheme {
        LandingScreen(onSignUpClick = {}, onLoginClick = {}, onAllJobsClick = {})
    }
}
