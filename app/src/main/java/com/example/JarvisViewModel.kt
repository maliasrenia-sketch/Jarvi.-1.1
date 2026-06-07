package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

data class Message(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class Diagnostics(
    val arcReactorOutput: Float, // e.g. 98.7%
    val suitIntegrity: Float,    // e.g. 100%
    val thrusterStatus: String,  // e.g. "OPTIMAL"
    val coolingSystems: String,  // e.g. "NOMINAL"
    val securityLevel: String,   // e.g. "MAX SECURE"
    val mainframeTemperature: Float // e.g. 34.2 °C
)

data class JarvisUiState(
    val messages: List<Message> = listOf(
        Message(
            id = "welcome",
            text = "Console online. Good morning, Sir. I am J.A.R.V.I.S., standing by to assist with your operational requests. Mainframe diagnostics are currently optimal.",
            isUser = false
        )
    ),
    val isReplying: Boolean = false,
    val diagnostics: Diagnostics = Diagnostics(
        arcReactorOutput = 98.4f,
        suitIntegrity = 100f,
        thrusterStatus = "OPTIMAL",
        coolingSystems = "STABLE",
        securityLevel = "ENCRYPTED LEVEL 5",
        mainframeTemperature = 32.5f
    ),
    val energyGridTrend: List<Float> = List(15) { 95f + Random.nextFloat() * 4f }
)

class JarvisViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(JarvisUiState())
    val uiState: StateFlow<JarvisUiState> = _uiState.asStateFlow()

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMessage = Message(
            id = System.currentTimeMillis().toString() + "_user",
            text = text,
            isUser = true
        )

        val updatedMessages = _uiState.value.messages + userMessage
        _uiState.value = _uiState.value.copy(
            messages = updatedMessages,
            isReplying = true
        )

        // Map messages for conversation history
        val history = _uiState.value.messages.take(updatedMessages.size - 1).map {
            Pair(it.text, it.isUser)
        }

        viewModelScope.launch {
            val reply = JarvisBrain.askJarvis(text, history)
            
            // Randomly update diagnostics for rich HUD feeling
            val currentDiag = _uiState.value.diagnostics
            val newDiag = currentDiag.copy(
                arcReactorOutput = (95f + Random.nextFloat() * 4.9f).coerceIn(0f, 100f),
                mainframeTemperature = (30f + Random.nextFloat() * 8f).coerceIn(15f, 45f)
            )

            // Randomly shift energy grid trend values to look alive
            val newGrid = _uiState.value.energyGridTrend.toMutableList().apply {
                removeAt(0)
                add(95f + Random.nextFloat() * 4.9f)
            }

            val jarvisMessage = Message(
                id = System.currentTimeMillis().toString() + "_jarvis",
                text = reply,
                isUser = false
            )

            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + jarvisMessage,
                isReplying = false,
                diagnostics = newDiag,
                energyGridTrend = newGrid
            )
        }
    }

    fun runFullDiagnosticsCheck() {
        val userActionMessage = Message(
            id = System.currentTimeMillis().toString() + "_user_diag",
            text = "J.A.R.V.I.S., execute full systems diagnostics check.",
            isUser = true
        )

        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userActionMessage,
            isReplying = true
        )

        viewModelScope.launch {
            val currentDiag = _uiState.value.diagnostics
            val updatedDiag = currentDiag.copy(
                arcReactorOutput = 99.9f,
                suitIntegrity = 100f,
                thrusterStatus = "OVERCHARGED",
                coolingSystems = "MAXIMUM VENTILATION",
                mainframeTemperature = 28.1f
            )

            val replyText = "Sir, I have executed the requested high-energy diagnostic sequence. Arc Reactor output has reached $99.9% efficiency. Thermal cooling is operating under full ventilation modes, and global quantum firewall structures are absolute. Ready when you are."

            val jarvisMessage = Message(
                id = System.currentTimeMillis().toString() + "_jarvis_diag",
                text = replyText,
                isUser = false
            )

            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + jarvisMessage,
                isReplying = false,
                diagnostics = updatedDiag
            )
        }
    }

    fun optimizeMainframeFlow() {
         val userMessage = Message(
            id = System.currentTimeMillis().toString() + "_user_opt",
            text = "Redirect aux power, balance the neural net.",
            isUser = true
        )

        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userMessage,
            isReplying = true
        )

        viewModelScope.launch {
            val replyText = "Understood, Sir. Rerouting aux power cells from sub-grids B through F directly to central processing. Balancing synaptic weights in the neural net. Computational latency reduced by 42%. We are fully optimized."

            val jarvisMessage = Message(
                id = System.currentTimeMillis().toString() + "_jarvis_opt",
                text = replyText,
                isUser = false
            )

            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + jarvisMessage,
                isReplying = false,
                diagnostics = _uiState.value.diagnostics.copy(
                    arcReactorOutput = 99.1f,
                    thrusterStatus = "OPTIMIZED",
                    coolingSystems = "RE-REGULATED"
                )
            )
        }
    }

    fun triggerAlarmMode() {
        val userMessage = Message(
            id = System.currentTimeMillis().toString() + "_user_alert",
            text = "House party protocol, engage security protocols.",
            isUser = true
        )

        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userMessage,
            isReplying = true
        )

        viewModelScope.launch {
            val replyText = "Sir! The 'House Party Protocol' is classified under highest authorization. Initializing regional defense lattices, engaging auxiliary Mark-armor units, and sealing the perimeter. Satellites are tracking and armed. Let's make an entrance."

            val jarvisMessage = Message(
                id = System.currentTimeMillis().toString() + "_jarvis_alert",
                text = replyText,
                isUser = false
            )

            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + jarvisMessage,
                isReplying = false,
                diagnostics = _uiState.value.diagnostics.copy(
                    suitIntegrity = 100f,
                    thrusterStatus = "ARMED & ENGAGED",
                    coolingSystems = "EMERGENCY VENT",
                    securityLevel = "RED ALERT ACTIVE"
                )
            )
        }
    }

    fun clearHistory() {
        _uiState.value = JarvisUiState(
            messages = listOf(
                Message(
                    id = "welcome_reset",
                    text = "System session re-initialized, Sir. Core memory channels flush. What are your instructions?",
                    isUser = false
                )
            )
        )
    }
}
