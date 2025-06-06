package com.example.gerenciadortreino

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


data class Treino(val id: Int, val data: String)

class MainActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var listView: ListView
    var treinos = mutableListOf<Treino>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.listViewTreinos)
        val btnNovoTreino: Button = findViewById(R.id.btnNovoTreino)

        btnNovoTreino.setOnClickListener {
            startActivity(Intent(this, CadastroTreinoActivity::class.java))
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val treino = treinos[position]
            val intent = Intent(this, DetalheTreinoActivity::class.java)
            intent.putExtra("treinoId", treino.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        carregarTreinos()
    }

    private fun carregarTreinos() {
        treinos.clear()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_TREINO}", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID))
                val data = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATA))
                treinos.add(Treino(id, data))
            } while (cursor.moveToNext())
        }
        cursor.close()

        val treinoList = treinos.map { treino: Treino -> "Treino em: ${treino.data}" }
        listView.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            treinoList.toTypedArray()
        )
    }
}
