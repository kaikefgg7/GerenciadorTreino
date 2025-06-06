package com.example.gerenciadortreino

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "gerenciadortreino.db"
        private const val DATABASE_VERSION = 1

        // Tabela Treino
        const val TABLE_TREINO = "treino"
        const val COL_ID = "id"
        const val COL_DATA = "data"

        // Tabela Exercício
        const val TABLE_EXERCICIO = "exercicio"
        const val COL_EX_ID = "id"
        const val COL_TREINO_ID = "treino_id"
        const val COL_NOME = "nome"
        const val COL_SERIE = "serie"
        const val COL_REPETICOES = "repeticoes"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTreinoTable = "CREATE TABLE $TABLE_TREINO (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_DATA TEXT)"
        db?.execSQL(createTreinoTable)

        val createExercicioTable = "CREATE TABLE $TABLE_EXERCICIO (" +
                "$COL_EX_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_TREINO_ID INTEGER, " +
                "$COL_NOME TEXT, " +
                "$COL_SERIE INTEGER, " +
                "$COL_REPETICOES INTEGER, " +
                "FOREIGN KEY($COL_TREINO_ID) REFERENCES $TABLE_TREINO($COL_ID))"
        db?.execSQL(createExercicioTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCICIO")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TREINO")
        onCreate(db)
    }
}
