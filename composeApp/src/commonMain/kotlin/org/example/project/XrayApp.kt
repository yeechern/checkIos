package org.example.project

import checkios.composeapp.generated.resources.Res
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import checkios.composeapp.generated.resources.arrow
import checkios.composeapp.generated.resources.backgroud1
import checkios.composeapp.generated.resources.circle
import checkios.composeapp.generated.resources.circlewaze1
import checkios.composeapp.generated.resources.circlewaze2
import checkios.composeapp.generated.resources.global
import checkios.composeapp.generated.resources.logo
import checkios.composeapp.generated.resources.oval
import checkios.composeapp.generated.resources.power
import checkios.composeapp.generated.resources.signal
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.Font


@Composable
@Preview
fun XrayApp(
    onStartStopClick: (Boolean) -> Unit,
    onConfigChangeClick: () -> Unit,
    isConnected: Boolean,
    xrayOutput: String
){

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF8F5))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(Res.drawable.backgroud1),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize()
            )
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp, start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.logo),
                        contentDescription = "Logo",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(50.dp)
                            .weight(1f)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(45.dp)
                        .fillMaxSize()
                )


                Row(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp)
                        .background(Color.White)
                        .fillMaxWidth()
                        .height(64.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Global Icon at the start
                    Icon(
                        painter = painterResource(Res.drawable.global),
                        contentDescription = "global",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(60.dp) // Adjust size as needed
                            .padding(12.dp)
                    )

                    // Text Column to the right of Global Icon
                    Column(
                        modifier = Modifier
                            .weight(1f) // Takes remaining space between global and signal icons
                            .padding(start = 8.dp), // Space between icon and text
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Best Location",
                            //fontFamily = poppinsfamily,
                            fontSize = 13.sp,
                            color = Color(0xFFFF9900)
                        )
                        Text(
                            text = "Fastest Server",
                            //fontFamily = poppinsfamily,
                            fontSize = 13.sp,
                        )
                    }

                    // Signal Icon at the end
                    Icon(
                        painter = painterResource(Res.drawable.signal),
                        contentDescription = "signal",
                        tint = Color.LightGray,
                        modifier = Modifier
                            .size(30.dp) // Adjust size as needed
                            .padding(end = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row() {
                    Button(onClick = {
                        onConfigChangeClick()
                    },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Color(0xFFFFA500)),) {
                        Text(text = "Change Location", color = Color(0xFFFF9900))
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            painter = painterResource(Res.drawable.arrow),
                            contentDescription = "Arrow Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Image(
                    painter = painterResource(Res.drawable.oval),
                    contentDescription = "Orange Oval",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )

                // This box will handle the toggle and update text based on state
                Box(contentAlignment = Alignment.Center, modifier = Modifier.align(Alignment.TopCenter)) {
                    Icon(
                        painter = painterResource(Res.drawable.circlewaze1),
                        contentDescription = "circle1",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(200.dp)
                    )
                    Icon(
                        painter = painterResource(Res.drawable.circlewaze2),
                        contentDescription = "circle2",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(160.dp)
                    )
                    Icon(
                        painter = painterResource(Res.drawable.circle),
                        contentDescription = "circle",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(150.dp)
                    )

                    // Power icon is now clickable to toggle the connection state
                    Icon(
                        painter = painterResource(Res.drawable.power),
                        contentDescription = "power",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                onStartStopClick(!isConnected)
                            }
                    )

                    // Display the connection status text
                    Text(
                        text = if (isConnected) "Connected" else "Disconnected",
                        color = Color.Black,
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Text(text = xrayOutput, modifier = Modifier.align(Alignment.BottomStart), color = Color.Black)
        }
    }


}

//fun formatTime(milliseconds: Long): String {
//    val seconds = milliseconds / 1000 % 60
//    val minutes = milliseconds / 1000 / 60 % 60
//    val hours = milliseconds / 1000 / 60 / 60
//    return String.format("%02d : %02d : %02d", hours, minutes, seconds)
//}