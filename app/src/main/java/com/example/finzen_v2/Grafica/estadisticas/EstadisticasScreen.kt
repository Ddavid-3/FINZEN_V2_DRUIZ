package com.example.finzen_v2.Grafica.estadisticas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finzen_v2.modelos.Movimiento
import com.example.finzen_v2.modelos.TipoMovimiento
import com.example.finzen_v2.utilidades.ConfiguracionApp
import com.example.finzen_v2.utilidades.Rutas
import com.example.finzen_v2.viewmodels.ViewModelMovimientos
import com.example.finzen_v2.viewmodels.ViewModelPresupuestos

@Composable
fun EstadisticasScreen(
    navController: NavController,
    viewModel: ViewModelMovimientos = viewModel(),
    presupuestosViewModel: ViewModelPresupuestos = viewModel()
) {
    // vauirables
    val movimientos = viewModel.movimientos.observeAsState(emptyList()).value
    val presupuestos by presupuestosViewModel.presupuestos.collectAsState()

    val gastosPorCategoria = calcularTotalesPorCategoria(movimientos, TipoMovimiento.GASTO)
    val ingresosPorCategoria = calcularTotalesPorCategoria(movimientos, TipoMovimiento.INGRESO)
    val totalGastos = movimientos
        .filter { it.tipo == TipoMovimiento.GASTO }
        .sumOf { it.cantidad }
    val totalIngresos = movimientos
        .filter { it.tipo == TipoMovimiento.INGRESO }
        .sumOf { it.cantidad }
    val limitePresupuesto = presupuestos.sumOf { it.limite }













    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ConfiguracionApp.colorFondo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Estadisticas", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            ResumenGeneral(
                totalIngresos = totalIngresos,
                totalGastos = totalGastos
            )

            Spacer(modifier = Modifier.height(12.dp))

            GraficaBarrasCategorias(
                titulo = "Gastos por categoria",
                datos = gastosPorCategoria,
                colorBarra = MaterialTheme.colorScheme.error,
                textoVacio = "No hay gastos registrados."
            )

            Spacer(modifier = Modifier.height(12.dp))

            GraficaBarrasCategorias(
                titulo = "Ingresos por categoria",
                datos = ingresosPorCategoria,
                colorBarra = MaterialTheme.colorScheme.primary,
                textoVacio = "No hay ingresos registrados."
            )

            Spacer(modifier = Modifier.height(12.dp))

            GraficaComparativaIngresosGastos(
                totalIngresos = totalIngresos,
                totalGastos = totalGastos
            )

            Spacer(modifier = Modifier.height(12.dp))

            GraficaPresupuesto(
                totalGastos = totalGastos,
                limitePresupuesto = limitePresupuesto
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate(Rutas.TABLERO) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Tablero")
            }
        }
    }
}



private fun calcularTotalesPorCategoria(
    movimientos: List<Movimiento>,
    tipo: TipoMovimiento
): Map<String, Double> {
    val totales = mutableMapOf<String, Double>()
    for (movimiento in movimientos) {
        if (movimiento.tipo == tipo) {
            val categoria = movimiento.categoria.ifBlank { "Sin categoria" }
            totales[categoria] = (totales[categoria] ?: 0.0) + movimiento.cantidad
        }
    }
    return totales.toList()
        .sortedByDescending { it.second }
        .toMap()
}












@Composable
private fun ResumenGeneral(totalIngresos: Double, totalGastos: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Resumen", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                DatoResumen(
                    titulo = "Ingresos",
                    cantidad = totalIngresos,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                DatoResumen(
                    titulo = "Gastos",
                    cantidad = totalGastos,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}












@Composable
private fun DatoResumen(
    titulo: String,
    cantidad: Double,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(titulo, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "$${"%.2f".format(cantidad)}",
            color = color,
            style = MaterialTheme.typography.titleLarge
        )
    }
}








@Composable
private fun GraficaBarrasCategorias(
    titulo: String,
    datos: Map<String, Double>,
    colorBarra: Color,
    textoVacio: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            if (datos.isEmpty()) {
                Text(textoVacio, style = MaterialTheme.typography.bodyMedium)
            } else {
                val maximo = datos.values.maxOrNull() ?: 1.0
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    for ((categoria, cantidad) in datos) {
                        BarraConTexto(
                            etiqueta = categoria,
                            cantidad = cantidad,
                            porcentaje = (cantidad / maximo).toFloat(),
                            color = colorBarra
                        )
                    }
                }
            }
        }
    }
}


















@Composable
private fun GraficaComparativaIngresosGastos(
    totalIngresos: Double,
    totalGastos: Double
) {
    val maximo = maxOf(totalIngresos, totalGastos, 1.0)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Comparativa ingresos y gastos", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
            BarraConTexto(
                etiqueta = "Ingresos",
                cantidad = totalIngresos,
                porcentaje = (totalIngresos / maximo).toFloat(),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            BarraConTexto(
                etiqueta = "Gastos",
                cantidad = totalGastos,
                porcentaje = (totalGastos / maximo).toFloat(),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}














@Composable
private fun GraficaPresupuesto(
    totalGastos: Double,
    limitePresupuesto: Double
) {
    val progreso = if (limitePresupuesto > 0.0) {
        (totalGastos / limitePresupuesto).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }
    val presupuestoSuperado = limitePresupuesto > 0.0 && totalGastos > limitePresupuesto

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Gastos contra presupuesto", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))

            if (limitePresupuesto <= 0.0) {
                Text("Crea un presupuesto para ver esta estadistica.")
            } else {
                Text("Gastado: $${"%.2f".format(totalGastos)}")
                Text("Presupuesto: $${"%.2f".format(limitePresupuesto)}")
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = if (presupuestoSuperado) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (presupuestoSuperado) {
                        "Has superado el presupuesto."
                    } else {
                        "Te quedan $${"%.2f".format(limitePresupuesto - totalGastos)}."
                    },
                    color = if (presupuestoSuperado) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}












@Composable
private fun BarraConTexto(
    etiqueta: String,
    cantidad: Double,
    porcentaje: Float,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(etiqueta, style = MaterialTheme.typography.bodyMedium)
            Text("$${"%.2f".format(cantidad)}", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(porcentaje.coerceIn(0.05f, 1f))
                    .height(20.dp)
                    .background(color)
            )
        }
    }
}
