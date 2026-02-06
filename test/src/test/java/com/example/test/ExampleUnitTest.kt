package com.example.test

import android.R
import android.view.View
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Test

import org.junit.Assert.*
import kotlin.reflect.typeOf

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest
    : ViewModel() {

//    private val _foodState = MutableStateFlow<UiState<Food>>(UiState.Idle)
//    val foodState = _foodState.asStateFlow()
    private val _selectedVariations = MutableStateFlow<Map<String, Set<String>>>(emptyMap())
    val selectedVariations: StateFlow<Map<String, Set<String>>> = _selectedVariations.asStateFlow()
    @Test
    fun `ApiResponse Success converts to UiState Success`() {

        _selectedVariations.value = mapOf(
            "size" to setOf("size_l", "size_xl")
        )
        val sizeVariation = Variation(
            id = "size",
            options = listOf(
                VariationOption(id = "size_m", price = 0),
                VariationOption(id = "size_l", price = 10),
                VariationOption(id = "size_xl", price = 20)
            )
        )
        val optionMap = sizeVariation.options.associateBy { it.id }
        println(optionMap[sizeVariation.options[0].id])

        fun calculateVariation(variation: Variation): Set<String> {
//          return _selectedVariations.value[variation.id] ?: emptyList()
            println(_selectedVariations.value[variation.id])
            println(variation.id)
            return _selectedVariations.value[variation.id] ?: emptySet()

        }

        println("day la ${calculateVariation(sizeVariation)}")




        val apiRespone = ApiResponse.Error(
            message = "error",
            ErrorCode.NOT_FOUND,
        )
        val uiState = apiRespone.toUiState()
        println(apiRespone)
        println(uiState)
        val user: Map<String, Set<String>> = mapOf(
            "VAN A" to setOf("lop a", "15 tuoi"),
            "VAN B" to setOf("lop B", "15 tuoi"),)
val current = user.toMutableMap()
        current.remove("")
        val value = current["VAN B"]?.toMutableSet() ?: mutableSetOf()
        value.add("lop c")
        current["VAN B"] = value
        println(current)
        println(value)

    }
}







data class Variation(
    val id: String,
    val options: List<VariationOption>
) {
    fun calculatePrice(selectedIds: List<String>): Int {
        return options
            .filter { it.id in selectedIds }
            .sumOf { it.price }
    }
}

data class VariationOption(
    val id: String,
    val price: Int
)
