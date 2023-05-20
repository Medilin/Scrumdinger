package com.example.scrumister

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class StartMeeting : AppCompatActivity() {

    var database: DatabaseReference = Firebase.database.reference
    private val RECORD_AUDIO_REQUEST_CODE = 101
    private val RECORDING_DURATION_MS = 5000 // Recording duration in milliseconds
    private lateinit var progressBar: ProgressBar
    private lateinit var startButton: Button
    private lateinit var speechRecognizer: SpeechRecognizer
    var dailyScrum:DailyScrum?=null
    public var saveScript:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_meeting)

        progressBar = findViewById(R.id.progressBar)
        startButton = findViewById(R.id.startButton)

        //Get Scrum info

        var myObject = intent.getSerializableExtra ("scrum") as DailyScrum?
        dailyScrum=myObject

        // Request runtime permission for RECORD_AUDIO
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE
            )
        } else {
            initializeSpeechRecognizer()
        }

        startButton.setOnClickListener { startRecording() }
    }

    private fun initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {}
                override fun onResults(results: Bundle?) {
                    val speechToTextResult =
                        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                    if (speechToTextResult != null && speechToTextResult.isNotEmpty()) {
                        val recognizedText = speechToTextResult[0]
                        val date = Calendar.getInstance().time
                        val formatter = SimpleDateFormat("yyyy.MM.dd") //or use getDateInstance()
                        val formatedDate = formatter.format(date)
                        var meeting=Meeting(formatedDate,recognizedText)
                        updateHistory(meeting)



                    }
                }
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }
    }

    fun updateHistory(meeting: Meeting)
    {
        val scrumRef =database.child("scrum-list").child(dailyScrum!!.id!!)
        var list2= ArrayList<Meeting>()
        list2.add(dailyScrum!!.history!![0])
        list2.add(meeting)
        val updates: MutableMap<String, Any> = HashMap()
        updates["history"]=list2
        scrumRef.updateChildren(updates)
            .addOnSuccessListener {
                // Update successful

            }
            .addOnFailureListener { e ->

            }


    }


    private fun startRecording() {
        startButton.isEnabled = false

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer.startListening(intent)

        // Start a countdown timer to stop recording after the specified duration
        val timer = object : CountDownTimer(RECORDING_DURATION_MS.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = ((RECORDING_DURATION_MS - millisUntilFinished) / RECORDING_DURATION_MS.toDouble() * 100).toInt()
                progressBar.progress = progress
            }

            override fun onFinish() {

                stopRecording()
            }
        }
        timer.start()
    }

    private fun stopRecording() {
        startButton.isEnabled = true

        progressBar.progress = 0

        Toast.makeText(this," Meeting  recorded!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)


    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()

    }
}
