package com.example.wisielec

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.wisielec.databinding.ActivityMainBinding
import com.example.wisielec.databinding.ActivitySettingsBinding
import java.util.*

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val lang = Language.valueOf(intent?.getStringExtra("language") ?: Language.ENGLISH.name)
        val diff = Difficulty.valueOf(intent?.getStringExtra("difficulty") ?: Difficulty.MEDIUM.name)
        binding.difficultySpinner.setSelection(diff.ordinal)
        binding.languageSpinner.setSelection(lang.ordinal)
        binding.confirmSettingsBtn.setOnClickListener {
            val langSelection = binding.languageSpinner.selectedItemPosition
            val diffSelection = binding.difficultySpinner.selectedItemPosition
            val result = Intent().apply {
                putExtra("language", Language.values()[langSelection].name)
                putExtra("difficulty", Difficulty.values()[diffSelection].name)
            }
            setResult(RESULT_OK, result)
            finish()
        }
    }
}