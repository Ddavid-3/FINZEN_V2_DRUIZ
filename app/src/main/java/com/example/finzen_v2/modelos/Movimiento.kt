package com.example.finzen_v2.modelos

// Modelo para Movimiento optimizado para Firebase


// movimientos aqui en andoid la llame y en firebase se llama transacciones,
// pero en si no es para traferir dinero , me refiero a por ejemplo cuando
// comrpas un cafe pues haces un movimento de dinero en tu cuenta , SE PODRIA
// HABER LLAMADO DE OTRA FORMA EN LA BBDD Y AQUI PUEDE SER UN POCO LIOSO
// PERO ES ESO SIRVE PARA CUANDO COMOPRAS ALGO O AS PILLADO DINERO DE ALGUN
// SITIO PUES HACES UN MOVIEMTO Y LUEGO TODO ESO SE VA USANDO PARA LLEVAR UN
//  CONTROL DE TU DINERO Y VER COMO SE VA GASTANDO O COMO SE VA GANANDO



// TAMBIEN AQUI TENGO ID POR Q ES NECESARIO , EN FIREBASE NOTENGO EN SI UN ATRIBUTO ID PQ NO SE VAN A REPETIR
// CUANDO CREE UNO EL FIREBASE LE PONE UNO AUTOMATICO EL POR COMO FUNCIOAN EN SI ,COMO NO ES UNA BBDD NORMAL


data class Movimiento(
    val id: String = "",
    val id_usuario: String = "",
    val cantidad: Double = 0.0,
    val categoria: String = "",
    val fecha: String = "", // Cambiado a String para evitar errores de deserialización
    val descripcion: String = "",
    val tipo: TipoMovimiento = TipoMovimiento.INGRESO
)

enum class TipoMovimiento {
    INGRESO,
    GASTO
}
