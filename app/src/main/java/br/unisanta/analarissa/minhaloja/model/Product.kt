package br.unisanta.analarissa.minhaloja.model

data class Product(
    val id : Int?,
    val title : String?,
    val price : Double?,
    val description : String?,
    val category : String?,
    val image : String?,
    val rate : String?,
    val erro : Boolean?
)
