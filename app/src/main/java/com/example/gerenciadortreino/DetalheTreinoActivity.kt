package com.example.gerenciadortreino

import android.app.AlertDialog
import android.database.Cursor
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DetalheTreinoActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var txtData: TextView
    private lateinit var containerExercicios: LinearLayout
    private lateinit var btnExcluir: Button
    private lateinit var btnVoltar: Button

    private var treinoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_treino)

        dbHelper = DatabaseHelper(this)
        txtData = findViewById(R.id.txtData)
        containerExercicios = findViewById(R.id.containerExerciciosDetalhe)
        btnExcluir = findViewById(R.id.btnExcluir)
        btnVoltar = findViewById(R.id.btnVoltar)

        treinoId = intent.getIntExtra("treinoId", 0)
        carregarDetalhesTreino()

        btnExcluir.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Excluir Treino")
                .setMessage("Deseja realmente excluir esse treino?")
                .setPositiveButton("Sim") { _, _ -> excluirTreino() }
                .setNegativeButton("Não", null)
                .show()
        }

        btnVoltar.setOnClickListener { finish() }
    }

    private fun carregarDetalhesTreino() {
        val db = dbHelper.readableDatabase
        val cursorTreino: Cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_TREINO} WHERE ${DatabaseHelper.COL_ID} = ?",
            arrayOf(treinoId.toString())
        )
        if (cursorTreino.moveToFirst()) {
            val data = cursorTreino.getString(cursorTreino.getColumnIndexOrThrow(DatabaseHelper.COL_DATA))
            txtData.text = "Data: $data"
        }
        cursorTreino.close()

        val cursorEx: Cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_EXERCICIO} WHERE ${DatabaseHelper.COL_TREINO_ID} = ?",
            arrayOf(treinoId.toString())
        )
        containerExercicios.removeAllViews()
        if (cursorEx.moveToFirst()) {
            do {
                val nome = cursorEx.getString(cursorEx.getColumnIndexOrThrow(DatabaseHelper.COL_NOME))
                val serie = cursorEx.getInt(cursorEx.getColumnIndexOrThrow(DatabaseHelper.COL_SERIE))
                val repeticoes = cursorEx.getInt(cursorEx.getColumnIndexOrThrow(DatabaseHelper.COL_REPETICOES))

                val textView = TextView(this)
                textView.text = "Exercício: $nome | Séries: $serie | Repetições: $repeticoes"
                containerExercicios.addView(textView)
            } while (cursorEx.moveToNext())
        }
        cursorEx.close()
    }

    private fun excluirTreino() {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_EXERCICIO, "${DatabaseHelper.COL_TREINO_ID} = ?", arrayOf(treinoId.toString()))
        db.delete(DatabaseHelper.TABLE_TREINO, "${DatabaseHelper.COL_ID} = ?", arrayOf(treinoId.toString()))
        Toast.makeText(this, "Treino excluído!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
