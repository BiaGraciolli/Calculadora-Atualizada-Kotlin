package com.biabelaraica.calculadora

// Colegio Técnico Antônio Teixeira Fernandes (Univap)
// Curso Técnico em Informática - Data de Entrega: 04/11/2026
// Autoras do Projeto: Beatriz Graciolli Nascimento
                        //Isabela Bueno Ferreira
                        //Raica Vieira Uejo
// Turma: 3I

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Calculadora()
            }
        }
    }
}

@Composable
fun Calculadora() {
    var num1 by rememberSaveable {
        mutableStateOf("")
    }
    var num2 by rememberSaveable {
        mutableStateOf("")
    }
    var operacao by rememberSaveable {
        mutableStateOf("")
    }
    var resultado by rememberSaveable {
        mutableStateOf("")
    }
    var finalizado by rememberSaveable {
        mutableStateOf(false)
    }
    var erro by rememberSaveable {
        mutableStateOf(false)
    }

    fun limpar() {
        num1 = ""
        num2 = ""
        operacao = ""
        resultado = ""
        finalizado = false
        erro = false
    }

    fun digitar(d: String) {
        if (finalizado) {
            limpar()
        }

        if (operacao == "") {
            if (num1.length < 9) {
                if (num1 == "0") {
                    num1 = d
                } else {
                    num1 = num1 + d
                }
            }
        } else {
            if (num2.length < 9) {
                if (num2 == "0") {
                    num2 = d
                } else {
                    num2 = num2 + d
                }
            }
        }
    }

    fun colocarPonto() {
        if (finalizado) {
            limpar()
        }

        if (operacao == "") {
            if (!num1.contains(".")) {
                if (num1 == "") {
                    num1 = "0."
                } else {
                    num1 = num1 + "."
                }
            }
        } else {
            if (!num2.contains(".")) {
                if (num2 == "") {
                    num2 = "0."
                } else {
                    num2 = num2 + "."
                }
            }
        }
    }

    fun escolherOperacao(op: String) {
        if (erro) {
            return
        }

        if (finalizado) {
            num1 = resultado
            num2 = ""
            resultado = ""
            finalizado = false
        }

        if (num1 != "" && num2 == "") {
            num1 = num1.trimEnd('.')
            operacao = op
        }
    }

    fun voltarParaEdicao() {
        if (finalizado) {
            resultado = ""
            finalizado = false
        }
    }

    fun apagarUltimo(texto: String): String {
        var novo = ""
        for (i in 0 until texto.length - 1) {
            novo = novo + texto[i]
        }
        return novo
    }

    fun apagar() {
        if (erro) {
            limpar()
            return
        }

        voltarParaEdicao()

        if (num2 != "") {
            num2 = apagarUltimo(num2)
        } else if (operacao != "") {
            operacao = ""
        } else if (num1 != "") {
            num1 = apagarUltimo(num1)
        }
    }

    fun calcular() {
        if (num1 == "" || num2 == "" || operacao == "" || finalizado) {
            return
        }

        val n1 = num1.toDouble()
        val n2 = num2.toDouble()
        var conta = 0.0

        if (operacao == "+") {
            conta = n1 + n2
        } else if (operacao == "-") {
            conta = n1 - n2
        } else if (operacao == "*") {
            conta = n1 * n2
        } else if (operacao == "/") {
            if (n2 == 0.0) {
                resultado = "Erro: Divisão por zero"
                erro = true
            } else {
                conta = n1 / n2
            }
        }

        if (!erro) {
            resultado = String.format(Locale.US, "%.4f", conta).trimEnd('0').trimEnd('.')
            if (resultado == "-0") {
                resultado = "0"
            }
        }
        finalizado = true
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2EED0))
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = num1 + operacao + num2,
                fontSize = if ((num1 + operacao + num2).length > 10) 22.sp else 45.sp,
                maxLines = 1
            )
        }

        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = resultado,
                fontSize = if (resultado.length > 10) 22.sp else 45.sp,
                maxLines = 1
            )
        }

        Row(modifier = Modifier.fillMaxWidth().weight(2f)) {
            Button(
                onClick = { limpar() },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9BC997), contentColor = Color.White)
            ) {
                Text("C", fontSize = 25.sp)
            }
            Button(
                onClick = { apagar() },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9BC997), contentColor = Color.White)
            ) {
                Text("⌫", fontSize = 25.sp)
            }
            Button(
                onClick = { escolherOperacao("/") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9BC997), contentColor = Color.White)
            ) {
                Text("/", fontSize = 30.sp)
            }
        }

        Row(modifier = Modifier.fillMaxWidth().weight(2f)) {
            Button(
                onClick = { digitar("7") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6819D), contentColor = Color.White)
            ) {
                Text("7", fontSize = 25.sp)
            }
            Button(
                onClick = { digitar("8") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6819D), contentColor = Color.White)
            ) {
                Text("8", fontSize = 25.sp)
            }
            Button(
                onClick = { digitar("9") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6819D), contentColor = Color.White)
            ) {
                Text("9", fontSize = 25.sp)
            }
            Button(
                onClick = { escolherOperacao("*") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9BC997), contentColor = Color.White)
            ) {
                Text("*", fontSize = 30.sp)
            }
        }

        Row(modifier = Modifier.fillMaxWidth().weight(2f)) {
            Button(
                onClick = { digitar("4") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6819D), contentColor = Color.White)
            ) {
                Text("4", fontSize = 25.sp)
            }
            Button(
                onClick = { digitar("5") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6819D), contentColor = Color.White)
            ) {
                Text("5", fontSize = 25.sp)
            }
            Button(
                onClick = { digitar("6") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6819D), contentColor = Color.White)
            ) {
                Text("6", fontSize = 25.sp)
            }
            Button(
                onClick = { escolherOperacao("-") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9BC997), contentColor = Color.White)
            ) {
                Text("-", fontSize = 30.sp)
            }
        }

        Row(modifier = Modifier.fillMaxWidth().weight(2f)) {
            Button(
                onClick = { digitar("1") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6819D), contentColor = Color.White)
            ) {
                Text("1", fontSize = 25.sp)
            }
            Button(
                onClick = { digitar("2") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6819D), contentColor = Color.White)
            ) {
                Text("2", fontSize = 25.sp)
            }
            Button(
                onClick = { digitar("3") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6819D), contentColor = Color.White)
            ) {
                Text("3", fontSize = 25.sp)
            }
            Button(
                onClick = { escolherOperacao("+") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9BC997), contentColor = Color.White)
            ) {
                Text("+", fontSize = 30.sp)
            }
        }

        Row(modifier = Modifier.fillMaxWidth().weight(2f)) {
            Button(
                onClick = { digitar("0") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6819D), contentColor = Color.White)
            ) {
                Text("0", fontSize = 25.sp)
            }
            Button(
                onClick = { colocarPonto() },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6819D), contentColor = Color.White)
            ) {
                Text(".", fontSize = 30.sp)
            }
            Button(
                onClick = { calcular() },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9BC997), contentColor = Color.White)
            ) {
                Text("=", fontSize = 30.sp)
            }
        }
    }
}
