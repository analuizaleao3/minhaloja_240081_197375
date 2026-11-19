package br.unisanta.analarissa.minhaloja.api

import br.unisanta.analarissa.minhaloja.model.Product
import retrofit2.http.GET

interface ProdutoService {

    @GET("products")
    suspend fun buscarProdutos():List<Product>
}