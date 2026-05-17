package com.example.finzen_v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
// Importación actualizada a la carpeta estándar
import com.example.finzen_v2.ui.theme.FinZenTheme
import com.example.finzen_v2.Grafica.configuracion.BienvenidaScreen
import com.example.finzen_v2.Grafica.estadisticas.EstadisticasScreen
import com.example.finzen_v2.Grafica.configuracion.ConfiguracionScreen
import com.example.finzen_v2.Grafica.presupuestos.PresupuestosScreen
import com.example.finzen_v2.Grafica.tablero.TableroScreen
import com.example.finzen_v2.Grafica.transacciones.TransaccionesScreen
import com.example.finzen_v2.Grafica.transacciones.EditarTransaccionScreen
import com.example.finzen_v2.utilidades.ConfiguracionApp
import com.example.finzen_v2.utilidades.Rutas
import com.example.finzen_v2.viewmodels.ViewModelMovimientos
import com.example.finzen_v2.viewmodels.ViewModelPresupuestos

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ConfiguracionApp.cargar(this)




        val inicioRuta = if (ConfiguracionApp.nombrePedido) Rutas.TABLERO else Rutas.BIENVENIDA

        setContent {
            FinZenTheme {
                val navController = rememberNavController()
                val movimientosViewModel: ViewModelMovimientos = viewModel()
                val presupuestosViewModel: ViewModelPresupuestos = viewModel()





                NavHost(navController = navController, startDestination = inicioRuta) {



                    composable(Rutas.BIENVENIDA) {
                        BienvenidaScreen(navController)
                    }
                    composable(Rutas.TABLERO) {
                        TableroScreen(navController, movimientosViewModel)
                    }
                    composable(Rutas.TRANSACCIONES) {
                        TransaccionesScreen(navController, movimientosViewModel)
                    }
                    composable(Rutas.ESTADISTICAS) {
                        EstadisticasScreen(navController, movimientosViewModel, presupuestosViewModel)
                    }
                    composable(Rutas.CONFIGURACION) {
                        ConfiguracionScreen(navController, movimientosViewModel, presupuestosViewModel)
                    }
                    composable(Rutas.PRESUPUESTOS) {
                        PresupuestosScreen(navController, presupuestosViewModel)
                    }

                    // LA RUTA A SEGUIR PARA EDITAR UN TRANSACION PASANDO SU ID
                    composable(
                        route = "editar/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id") ?: ""
                        EditarTransaccionScreen(navController, id, movimientosViewModel)
                    }
                }















            }
        }
    }
}
