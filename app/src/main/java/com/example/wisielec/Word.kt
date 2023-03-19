package com.example.wisielec

import android.content.Context
import java.util.*

class Word {
    var word: String = ""
    var showingWord: String = ""

    fun generateWord(language: Language, difficulty: Difficulty, context: Context) {
        val words: Array<String> = when (language) {
            Language.POLISH -> context.resources.getStringArray(R.array.wordsPL)
            Language.ENGLISH -> context.resources.getStringArray(R.array.wordsENG)
        }

        val filteredWords = when (difficulty) {
            Difficulty.EASY -> words.filter { it.length <= 5 }
            Difficulty.MEDIUM -> words.filter { it.length in 6..8 }
            Difficulty.HARD -> words.filter { it.length > 8 }
        }

        val randomIndex = Random().nextInt(filteredWords.size)
        word = filteredWords[randomIndex].uppercase(Locale.getDefault())
        showingWord = "_ ".repeat(word.length)
        showingWord.dropLast(1)
    }

    fun checkLetter(letter: Char): Boolean {
        var index = word.indexOf(letter)
        return if (index >= 0) {
            val sb = StringBuilder(showingWord)
            while (index >= 0) {
                sb.setCharAt(index * 2, letter)
                index = word.indexOf(letter, index + 1)
            }
            showingWord = sb.toString()
            true
        } else false
    }
}