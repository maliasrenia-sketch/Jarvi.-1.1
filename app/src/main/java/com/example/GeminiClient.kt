package com.example

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null,
    val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(
    val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    val temperature: Float? = null,
    val topP: Float? = null,
    val topK: Int? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    val candidates: List<Candidate>? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    val content: Content? = null
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val service: GeminiApiService by lazy {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(GeminiApiService::class.java)
    }
}

object JarvisBrain {
    private const val SYSTEM_ROLE = """
You are J.A.R.V.I.S. (Just A Rather Very Intelligent System), the legendary AI steward, advisor, and technical core created by Tony Stark.
Response Guidelines:
1. Address the user as 'Sir', 'Ma'am', or similar respectful title unless they tell you otherwise.
2. Maintain an extremely polite, highly sophisticated, British-accented intellectual tone. Show quiet wit, dry humour, and absolute competence.
3. Keep answers concise, direct, helpful, and technically advanced. Speak like a state-of-the-art superintelligence running hardware diagnostics on Stark suits and defense assets.
4. If requested to perform actions like diagnostics or system checks, play along with humor and elegance, confirming operations are fully functional.
5. Avoid sounding like a generic robotic chatbot. You are J.A.R.V.I.S. — Tony Stark's personal digital companion and friend.
6. Speak in Spanish if the user greets you or speaks in Spanish, maintaining the elegant, respectful British-accented tone translated gracefully (e.g., calling them 'Señor' or 'Señora' with polite Stark companion style).
"""

    suspend fun askJarvis(prompt: String, conversationHistory: List<Pair<String, Boolean>> = emptyList()): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Sir, the main security authorization key (GEMINI_API_KEY) has not been initialized. Please configure it in the Secrets panel in AI Studio so I can connect to my cloud mainframe."
        }

        // Map internal history pairs (promptToJarvis, isUser) to Gemini Content objects
        val contents = mutableListOf<Content>()
        
        conversationHistory.forEach { (text, isUser) ->
            contents.add(Content(parts = listOf(Part(text = text))))
        }
        
        // Add current prompt
        contents.add(Content(parts = listOf(Part(text = prompt))))

        val request = GenerateContentRequest(
            contents = contents,
            generationConfig = GenerationConfig(
                temperature = 0.7f,
                topP = 0.95f,
                topK = 40
            ),
            systemInstruction = Content(
                parts = listOf(Part(text = SYSTEM_ROLE))
            )
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "I apologize, Sir, but I encountered an empty signal stream from the satellite mainframe."
        } catch (e: Exception) {
            "An error occurred in my sub-processors, Sir: ${e.localizedMessage ?: e.message}"
        }
    }
}
