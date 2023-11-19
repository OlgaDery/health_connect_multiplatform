package com.example.health_multiplstform.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import com.chrynan.parcelable.compose.rememberSaveable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HealthDataEntryScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: RecordEntryViewModel) {

    var steps by rememberSaveable { mutableStateOf("") }
    var temperature by rememberSaveable { mutableStateOf("") }
    var heartRate by rememberSaveable { mutableStateOf("") }
    var displaySubmissionState by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier
    ) {
        if (displaySubmissionState) {
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 2f,
                    targetValue = 7f,
                    animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
                    label = "scale"
                )
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Done!",
                        color = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                transformOrigin = TransformOrigin.Center
                            }
                            .align(Alignment.Center),
                        // Text composable does not take TextMotion as a parameter.
                        // Provide it via style argument but make sure that we are copying from current theme
                        style = LocalTextStyle.current.copy(textMotion = TextMotion.Animated)
                    )
                }

            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 30.dp)
            ) {

                item {
                    Text(
                        modifier = Modifier
                            .padding(20.dp)
                            .wrapContentHeight(),
                        text = "Please enter your data:"
                    )
                }

                item {
                    NumbersInputField(
                        steps.toString(),
                        "Enter your steps number",
                        onValueChange = {
                            steps = it
                        })
                }

                item {
                    NumbersInputField(
                        heartRate.toString(),
                        "Enter your heart rate",
                        onValueChange = {
                            heartRate = it

                        })
                }

                item {
                    NumbersInputField(
                        temperature.toString(),
                        "Enter your body temperature",
                        onValueChange = {
                            temperature = it
                        })
                }

                item {
                    Text(
                        modifier = Modifier
                            .padding(20.dp)
                            .wrapContentHeight(),
                        text = "Select your location on the map:"
                    )
                }

                item {
                    MapToEnterLocation(
                        modifier = Modifier.requiredHeight(300.dp),
                        size = 300.dp,
                        viewModel = viewModel
                    )
                }

                item {
                    OutlinedButton(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(15.dp),
                        onClick = {
                            scope.launch {
                                displaySubmissionState = true

                                viewModel.constructAndInsertRecord(
                                    temperature.toInt(),
                                    heartRate.toInt(),
                                    steps.toInt()
                                )
                                delay(500)
                                steps = ""
                                temperature = ""
                                heartRate = ""
                                displaySubmissionState = false
                            }
                        }
                    ) {
                        Text("Submit data")
                    }
                }
            }
        }
    }
}

@Composable
fun NumbersInputField(
    initialValue: String,
    label: String,
    modifier: Modifier = Modifier.wrapContentHeight().padding(15.dp).fillMaxWidth(),
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = initialValue,
        onValueChange = { userInput ->
            try {
                userInput.toInt()
                onValueChange(userInput)
            } catch (e: NumberFormatException) { }
        },
        label = { Text(label) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

}
