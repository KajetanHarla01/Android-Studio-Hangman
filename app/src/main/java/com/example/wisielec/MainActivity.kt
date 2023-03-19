package com.example.wisielec

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.indices
import com.example.wisielec.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {
    private var language: Language = Language.ENGLISH
    private var difficulty: Difficulty = Difficulty.MEDIUM
    private lateinit var binding: ActivityMainBinding
    private var currentWord: Word = Word()
    private var wrongAttempts: Int = 0
    private val launchSettingsActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result?.data.let { data ->
                language = Language.valueOf(data?.getStringExtra("language") ?: Language.ENGLISH.name)
                difficulty = Difficulty.valueOf(data?.getStringExtra("difficulty") ?: Difficulty.MEDIUM.name)
            }
        }
        updateKeyboard()
        resetGame()
        Snackbar.make(
            binding.root,
            "Current settings: language: ${language.name}, difficulty: ${difficulty.name}",
            Snackbar.LENGTH_SHORT
        ).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        resetGame()
        updateKeyboard()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.settings -> startSettingsActivity()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun startSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java).apply {
            putExtra("difficulty", difficulty.name)
            putExtra("language", language.name)
        }
        launchSettingsActivity.launch(intent)
    }
    private fun updateKeyboard() {
        when (language) {
            Language.POLISH -> {
                binding.keyboardPL.visibility = View.VISIBLE
                binding.keyboardENG.visibility = View.GONE
            }
            else -> {
                binding.keyboardPL.visibility = View.GONE
                binding.keyboardENG.visibility = View.VISIBLE
            }
        }
    }
    private fun resetGame() {
        wrongAttempts = 0
        currentWord.generateWord(language, difficulty, this)
        binding.wordLabel.text = currentWord.showingWord
        enableAllButtons()
        updateHangmanImage()
    }
    private fun updateHangmanImage() {
        val image = when(wrongAttempts){
            0 -> R.drawable.hangman0
            1 -> R.drawable.hangman1
            2 -> R.drawable.hangman2
            3 -> R.drawable.hangman3
            4 -> R.drawable.hangman4
            5 -> R.drawable.hangman5
            6 -> R.drawable.hangman6
            7 -> R.drawable.hangman7
            8 -> R.drawable.hangman8
            9 -> R.drawable.hangman9
            else -> R.drawable.hangman10
        }
        binding.hangmanImageView.setImageResource(image)
    }
    private fun enableAllButtons() {
        for(i in binding.root.indices){
            if (binding.root[i] is Button) {
                binding.root[i].isEnabled = true
            }
        }
    }
    private fun disableAllButtons() {
        for(i in binding.root.indices){
            if (binding.root[i] is Button) {
                binding.root[i].isEnabled = false
            }
        }
    }
    private fun endGame(win: Boolean) {
        val endText = if (win) "You win" else "You lose | Right answer is: ${currentWord.word}"
        disableAllButtons()
        Snackbar.make(
            binding.root,
            endText,
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Start over"){
            resetGame()
        }.show()
    }
    fun onLetterButtonClick(view: View) {
        val letter = (view as Button).text.toString().single()
        if (currentWord.checkLetter(letter)) {
            binding.wordLabel.text = currentWord.showingWord
            if (!currentWord.showingWord.contains('_'))
                endGame(true)
        } else {
            wrongAttempts++
            updateHangmanImage()
            if(wrongAttempts == 10)
                endGame(false)
        }
        view.isEnabled = false
    }
}