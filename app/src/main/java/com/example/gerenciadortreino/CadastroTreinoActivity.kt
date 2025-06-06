package com.example.gerenciadortreino

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class CadastroTreinoActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var edtData: EditText
    private lateinit var containerExercicios: LinearLayout
    private lateinit var btnIncluirExercicio: Button
    private lateinit var btnSalvarTreino: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_treino)

        dbHelper = DatabaseHelper(this)
        edtData = findViewById(R.id.edtData)
        containerExercicios = findViewById(R.id.containerExercicios)
        btnIncluirExercicio = findViewById(R.id.btnIncluirExercicio)
        btnSalvarTreino = findViewById(R.id.btnSalvarTreino)
        val btnVoltar: Button = findViewById(R.id.btnVoltar)

        edtData.inputType = InputType.TYPE_CLASS_DATETIME
        val dataAtual = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date())
        edtData.setText(dataAtual)

        btnIncluirExercicio.setOnClickListener {
            adicionarExercicioView()
        }

        btnSalvarTreino.setOnClickListener {
            salvarTreino()
        }

        btnVoltar.setOnClickListener { finish() }
    }

    private fun adicionarExercicioView() {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_exercicio, containerExercicios, false)
        containerExercicios.addView(view)
    }

    private fun salvarTreino() {
        val data = edtData.text.toString()
        if (data.isBlank()) {
            Toast.makeText(this, "Informe a data", Toast.LENGTH_SHORT).show()
            return
        }

        val db: SQLiteDatabase = dbHelper.writableDatabase
        val treinoValues = ContentValues().apply {
            put(DatabaseHelper.COL_DATA, data)
        }

        val treinoId = db.insert(DatabaseHelper.TABLE_TREINO, null, treinoValues).toInt()


        for (i in 0 until containerExercicios.childCount) {
            val exercicioView = containerExercicios.getChildAt(i)
            val edtNome: EditText = exercicioView.findViewById(R.id.edtNomeExercicio)
            val edtSerie: EditText = exercicioView.findViewById(R.id.edtSerie)
            val edtRepeticoes: EditText = exercicioView.findViewById(R.id.edtRepeticoes)

            val nome = edtNome.text.toString()
            val serie = edtSerie.text.toString().toIntOrNull() ?: 0
            val repeticoes = edtRepeticoes.text.toString().toIntOrNull() ?: 0

            if (nome.isNotBlank()) {
                val exValues = ContentValues().apply {
                    put(DatabaseHelper.COL_TREINO_ID, treinoId)
                    put(DatabaseHelper.COL_NOME, nome)
                    put(DatabaseHelper.COL_SERIE, serie)
                    put(DatabaseHelper.COL_REPETICOES, repeticoes)
                }
                db.insert(DatabaseHelper.TABLE_EXERCICIO, null, exValues)
            }
        }

        Toast.makeText(this, "Treino salvo com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
