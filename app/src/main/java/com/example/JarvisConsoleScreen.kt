package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArcBlue
import com.example.ui.theme.ArcBlueDeep
import com.example.ui.theme.ArcBlueGlow
import com.example.ui.theme.DeepSpace
import com.example.ui.theme.HologramBorder
import com.example.ui.theme.HologramDark
import com.example.ui.theme.HologramText
import com.example.ui.theme.StarkGold
import com.example.ui.theme.StarkRed
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JarvisConsoleScreen(
    viewModel: JarvisViewModel,
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }

    // Scroll to latest message automatically when new logs arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "STARK INDUSTRIES",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArcBlueGlow.copy(alpha = 0.6f),
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "J.A.R.V.I.S.",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = ArcBlue,
                            letterSpacing = 1.sp
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Mainframe telemetry navigation",
                        tint = ArcBlueGlow.copy(alpha = 0.8f),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(20.dp)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.clearHistory() },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("clear_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset terminal system channels",
                            tint = ArcBlueGlow.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DeepSpace
                )
            )
        },
        containerColor = DeepSpace
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DeepSpace)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Concentric Active Arc Reactor Blueprint Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background radial / cross diagonal grids drawn within
                ArcReactorDrawing(
                    efficiency = uiState.diagnostics.arcReactorOutput,
                    isReplying = uiState.isReplying,
                    modifier = Modifier.size(140.dp)
                )

                // Top Floating Badge: ACTIVE CORE
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 4.dp)
                        .border(1.dp, HologramBorder, RoundedCornerShape(50))
                        .background(DeepSpace)
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "ACTIVE CORE",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArcBlueGlow,
                        letterSpacing = 1.5.sp
                    )
                }

                // Bottom Floating Badge: MK-85 PROTOCOL
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-4).dp)
                        .border(1.dp, HologramBorder, RoundedCornerShape(50))
                        .background(DeepSpace)
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "MK-85 PROTOCOL",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = StarkGold,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2x2 Metric Stats Grid - Recreating pure Tailwind styling & structures
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricStatCard(
                        category = "Power Output",
                        value = "${"%.1f".format(uiState.diagnostics.arcReactorOutput)}%",
                        icon = Icons.Default.Info,
                        accentColor = ArcBlue,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        category = "Shield integrity",
                        value = "${"%.1f".format(uiState.diagnostics.suitIntegrity)}%",
                        icon = Icons.Default.Warning,
                        accentColor = StarkRed,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricStatCard(
                        category = "Internal Temp",
                        value = "${"%.1f".format(uiState.diagnostics.mainframeTemperature)}°C",
                        icon = Icons.Default.Settings,
                        accentColor = ArcBlueGlow,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        category = "Network Threat",
                        value = if (uiState.diagnostics.securityLevel.contains("RED ALERT")) "ALERT TRIGGER" else "ZERO ACTIVE",
                        icon = Icons.Default.Warning,
                        accentColor = if (uiState.diagnostics.securityLevel.contains("RED ALERT")) StarkRed else StarkGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Reroute actions overrides row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.runFullDiagnosticsCheck() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HologramDark,
                        contentColor = ArcBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ArcBlueDeep.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("diagnostic_check_btn")
                ) {
                    Text(
                        "DIAGNOSTIC",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { viewModel.optimizeMainframeFlow() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HologramDark,
                        contentColor = StarkGold
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, StarkGold.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("optimization_btn")
                ) {
                    Text(
                        "SYS REROUTE",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { viewModel.triggerAlarmMode() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HologramDark,
                        contentColor = StarkRed
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, StarkRed.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("alarm_btn")
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = StarkRed,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "ALERT SETUP",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Energy Level Oscilloscope Display Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .border(1.dp, HologramBorder, RoundedCornerShape(12.dp))
                    .background(HologramDark, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "AUX WAVELENGTH STABILIZER",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            color = ArcBlueGlow,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "GRID SYNC: ACTIVE",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            color = StarkGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OscilloscopeGraph(
                        points = uiState.energyGridTrend,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Chat & Terminal Transcript Output Panel
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(1.dp, HologramBorder, RoundedCornerShape(16.dp))
                    .background(HologramDark, RoundedCornerShape(16.dp))
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "TERMINAL TRANSCRIPT",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArcBlueGlow,
                        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp, top = 4.dp)
                    )

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(uiState.messages) { message ->
                            TerminalMessageBubble(message)
                        }
                        
                        if (uiState.isReplying) {
                            item {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(ArcBlue)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Connecting to Jarvis mainframe...",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = ArcBlueGlow,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Input Text Field Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            "Enter override vocal parameters...",
                            color = ArcBlueGlow.copy(alpha = 0.5f),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("terminal_input"),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = HologramText,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp
                    ),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendMessage(inputText)
                                inputText = ""
                            }
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = HologramDark,
                        unfocusedContainerColor = HologramDark.copy(alpha = 0.5f),
                        focusedBorderColor = ArcBlue,
                        unfocusedBorderColor = HologramBorder
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendMessage(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .border(1.dp, HologramBorder, RoundedCornerShape(12.dp))
                        .background(HologramDark)
                        .testTag("send_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send vocal diagnostics override key",
                        tint = ArcBlue
                    )
                }
            }
        }
    }
}

// Geometric balanced metrics cards styling helper
@Composable
fun MetricStatCard(
    category: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(1.dp, HologramBorder, RoundedCornerShape(16.dp))
            .background(HologramDark, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor.copy(alpha = 0.8f),
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = category.uppercase(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = HologramText.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
            }
            Text(
                text = value,
                fontFamily = FontFamily.Monospace,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }
    }
}

// Re-engineered concentric geometric blueprint Arc Reactor Drawing
@Composable
fun ArcReactorDrawing(
    efficiency: Float,
    isReplying: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val outerRadius = size.width * 0.48f
        val middleRadius = outerRadius * 0.85f
        val innerCircleRadius = outerRadius * 0.70f
        val centralCoreRadius = outerRadius * 0.16f

        // 1. Orthogonal crosshairs structure
        drawLine(
            color = ArcBlue.copy(alpha = 0.08f),
            start = Offset(center.x, center.y - outerRadius - 10f),
            end = Offset(center.x, center.y + outerRadius + 10f),
            strokeWidth = 1f
        )
        drawLine(
            color = ArcBlue.copy(alpha = 0.08f),
            start = Offset(center.x - outerRadius - 10f, center.y),
            end = Offset(center.x + outerRadius + 10f, center.y),
            strokeWidth = 1f
        )

        // 2. Intersecting Diagonal lines rotate-45 and -rotate-45
        drawLine(
            color = ArcBlue.copy(alpha = 0.12f),
            start = Offset(center.x - outerRadius, center.y - outerRadius),
            end = Offset(center.x + outerRadius, center.y + outerRadius),
            strokeWidth = 1f
        )
        drawLine(
            color = ArcBlue.copy(alpha = 0.12f),
            start = Offset(center.x - outerRadius, center.y + outerRadius),
            end = Offset(center.x + outerRadius, center.y - outerRadius),
            strokeWidth = 1f
        )

        // 3. Outer Ring: border border-cyan-500/20 (20% opacity)
        drawCircle(
            color = ArcBlue.copy(alpha = 0.20f),
            radius = outerRadius,
            center = center,
            style = Stroke(width = 1.dp.toPx())
        )

        // 4. Middle Ring: border-2 border-cyan-500/40 (40% opacity)
        drawCircle(
            color = ArcBlue.copy(alpha = 0.40f),
            radius = middleRadius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // 5. Inner Ring: border border-cyan-400/60 (60% opacity)
        drawCircle(
            color = ArcBlueGlow.copy(alpha = 0.60f),
            radius = innerCircleRadius,
            center = center,
            style = Stroke(width = 1.dp.toPx())
        )

        // 6. Central Glow Core dot shadow
        drawCircle(
            color = if (efficiency > 99f) StarkGold.copy(alpha = 0.35f) else ArcBlueGlow.copy(alpha = 0.35f),
            radius = centralCoreRadius + 5.dp.toPx(),
            center = center
        )
        // Main Core Pinpoint
        drawCircle(
            color = if (efficiency > 99f) StarkGold else ArcBlueGlow,
            radius = centralCoreRadius,
            center = center
        )

        // 7. Interactive orbit sweep ticks
        val timeStep = System.currentTimeMillis() / 4
        val orbitSteps = 6
        for (i in 0 until orbitSteps) {
            val angle = timeStep + (i * (360 / orbitSteps))
            val angleRad = Math.toRadians((angle % 360).toDouble())
            val px = center.x + (middleRadius) * cos(angleRad).toFloat()
            val py = center.y + (middleRadius) * sin(angleRad).toFloat()

            drawCircle(
                color = ArcBlue.copy(alpha = if (isReplying) 0.9f else 0.4f),
                radius = 2.5.dp.toPx(),
                center = Offset(px, py)
            )
        }
    }
}

// Oscilloscope style visualizer of energy levels, moving waves
@Composable
fun OscilloscopeGraph(
    points: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val stepX = width / (points.size - 1).coerceAtLeast(1)

        val path = Path()
        points.forEachIndexed { index, value ->
            // Scale and map to vertical height
            val ratio = (value - 90f) / 15f
            val y = height - (ratio * height).coerceIn(0f, height)
            val x = index * stepX

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = ArcBlue,
            style = Stroke(width = 2.dp.toPx())
        )

        // Draw horizontal grid center-line
        drawLine(
            color = HologramBorder.copy(alpha = 0.2f),
            start = Offset(0f, height / 2f),
            end = Offset(width, height / 2f),
            strokeWidth = 1.dp.toPx()
        )
    }
}

// Beautiful glowing sci-fi terminals for conversation bubbles
@Composable
fun TerminalMessageBubble(message: Message) {
    val containerColor = if (message.isUser) {
        HologramDark.copy(alpha = 0.4f)
    } else {
        DeepSpace.copy(alpha = 0.8f)
    }

    val borderColor = if (message.isUser) {
        StarkGold.copy(alpha = 0.5f)
    } else {
        ArcBlue.copy(alpha = 0.6f)
    }

    val systemTag = if (message.isUser) "[STARK_OVERRIDE]" else "[JARVIS_REPLY]"
    val tagColor = if (message.isUser) StarkGold else ArcBlue

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        // Tag identity line
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(bottom = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(tagColor)
            )
            Text(
                text = systemTag,
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = tagColor
            )
        }

        // Output bubble with glowing cyber limits
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 12.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 4.dp
                    )
                )
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 12.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 4.dp
                    )
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                color = if (message.isUser) HologramText else ArcBlueGlow,
                lineHeight = 18.sp
            )
        }
    }
}
