//package com.example.test
//
//import android.R
//import org.junit.Test
//
//import org.junit.Assert.*
//import kotlin.reflect.typeOf
//
///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// */
//class ExampleUnitTest {
//    //
//    data class CartItem(val name: String, val variation: List<Variation>)
//    data class Variation(val name: String, val option: List<Option>)
//    data class Option(val name: String, val price: Int)
////    //    val variations: List<Variation> = emptyList(),
////    //    val options: List<VariationOption> = emptyList()
////    //    data class VariationOption(
////
////    fun cartItem(): CartItem {
////        val cartItem = CartItem(
////            "Pho bo", listOf(
////                Variation(
////                    "topping", listOf(
////                        Option("trung tran", 5000),
////                        Option("thit them", 15000)
////                    )
////                ),
////                Variation(
////                    "Size", listOf(
////                        Option("nho", 25000),
////                    )
////                )
////            )
////        )
////
////
////        return cartItem
////    }
////
////    fun calculatePrice() {
////        val cartItem = cartItem()
////        val calculate = cartItem.variation.map { it.option.map { option -> option.price } }
////        val calculate2 = cartItem.variation.sumOf { it.option.sumOf { option -> option.price } }
////
////        println(calculate)
////        println(calculate2)
////        println("++++++++++")
////        println("day la: ${cartItem.variation.maxOfOrNull { it.option.maxOf { option -> option.price } }}")
////    }
////
////
////    fun transform() {
////        val cartItem = cartItem()
////        val transform = cartItem.variation.joinToString("--") {
////            it.option.joinToString("||")
////        }
////        println("x is string ${transform is String}")
////        println(transform)
////    }
////
////    @Test
////    fun check_isCorrect() {
////
////        assertEquals(cartItem(), cartItem())
////        val lst = listOf(1, 2, 3, 4, 5)
////        println("---------")
////        transform()
////        println("----------")
////
////        println(cartItem())
////        println("++++++")
////        println(calculatePrice())
////        println("++++++")
////    }
//
//
//    interface Animal {
//        var age: Int
//        fun sound(): String
//        fun doSomethings(): String {
//            age++
//            return "do something"
//        }
//
//        fun makeSomething(): String {
//            val a: Int = 12;
//            return "make something"
//        }
//    }
//
//    interface Dog : Animal {
//        override fun sound(): String {
//            return "ga gau"
//        }
//
//        fun doSomethingInDog(): String = ""
//
//    }
//
//    interface Cat : Animal {
//        override fun sound(): String {
//            return "meo meo"
//        }
//
//        fun doSomethingInCat(): String = ""
//    }
//
//    abstract class AnimalBase(
//        val name: String,
//        val age: Int,
//        val oos: String,
//        val oop: String,
//    ) {
//        val state: Int = 1;
//        fun getInfo(): String {
//            return "$name, $age, $oos, $oop"
//        }
//    }
//
//    class Implement : Dog, Cat {
//        val name: String = "bINH".uppercase()
//        override var age: Int
//            get() = TODO("Not yet implemented")
//            set(value) {}
//
//        override fun sound(): String {
//            val cat = super<Cat>.sound()
//            val dog = super<Dog>.sound()
//            return "$dog, $cat"
//        }
//
//        override fun doSomethingInCat(): String {
//            return super.doSomethingInCat()
//        }
//
//        override fun doSomethingInDog(): String {
//            return super.doSomethingInDog()
//        }
//    }
//
////    sealed class Result() {
////        data class Success(val data: String) : Result()
////        data class Error(val message: String) : Result()
////        object Loading : Result()
////    }
//
//    object AppConfig {
//        const val API_VERSION: String = "1.1"
//        const val APP_NAME: String = "Food App"
//    }
//
//    enum class FoodType(val type: String) {
//        DRINK("Drink"),
//        FOOD("Food")
//    }
//
//    enum class OrderStatus() {
//        CANCEL,
//        PENDING,
//    }
//
//
//    class GetSet() {
//        var name1: String = "mdde"
//            get() = field.uppercase()
//        var age1: Int = 1
//            set(value) {
//                field = if (value < 0) 0 else value
//            }
//        var name2: String = "mdde".uppercase()
//        var age2: Int = 1
//
//
//        init {
//            name1 = "change v1"
//            name2 = "change v2"
//            age1 = -10
//            age2 = -10
//            println("v1: name - ${name1}, age ${age1}")
//            println("v2: name - ${name2}, age ${age2}")
//            age1 = 10
//            age2 = 10
//
//
//        }
//
//
//    }
////    class AnimalFactory: Dog , Cat , Animal {
////        override fun sound(): String {
////            return super<Dog>.sound()
////        }
////
////
////        override fun doSomethings(): String {
////            return super<Animal>.doSomethings()
////        }
//
//    //    class Adapter(
////        val animal: Animal
////    )
////    fun handle(result: Result) {
////        when (result) {
////            is Result.Error -> "day ${result.message}"
////            is Result.Success -> result.data
////            is Result.Loading -> ""
////        }
////    }
//
//    fun handleType(type: FoodType) {
//
//
//        when (type) {
//            FoodType.DRINK -> "Drink"
//            FoodType.FOOD -> "Food"
//        }
//    }
//
//    enum class Value {
//        A(),
//        B(),
//        C(),
//        D();
//
//
//    }
//
//    fun call(value: Value) {
//        when (value) {
//            Value.A -> println("")
//            Value.B -> println("")
//            Value.C -> println("")
//            Value.D -> println("")
//        }
//    }
//
//    enum class FoodTypek(val text: String) {
//        DRINK("Drink"),
//        FOOD("Food");
//
//        fun isDrink(): Boolean = this == DRINK
//    }
//
//    fun checkF() {
//        val foodType = FoodTypek.FOOD.isDrink()
//
//    }
//
//    sealed class Result1<out T> {
//        data class Success<T>(val data: T) : Result1<T>()
//        data object Error : Result1<Nothing>()
//    }
//
//    fun <T, R> Result1<T>.mapSimple(transform: (T) -> R): Result1<R> {
//        return when (this) {
//            is Result1.Success -> Result1.Success(transform(data))
//            is Result1.Error -> Result1.Error
//        }
//    }
//
//    fun listTest() {
//        val option1 = listOf(Option("trung tran", 5000), Option("thit them", 10000))
//        val option2 = listOf(Option("m", 7000), Option("x", 12000))
//        val variation = listOf(Variation("topping", option1), Variation("Size", option2))
//        val cartItem = CartItem("Pho bo", variation)
//
//        val lst = cartItem.variation
//            .map { variation ->
//                val selected = variation.option
//                    .filter { option -> option.price > 5000 }
//                    .joinToString(", ") { option -> "${option.name}" }
//
//                if (selected.isNotEmpty()) {
//                    "${variation.name}: $selected"
//                } else ""
//            }.filter { it.isNotEmpty() }
//            .joinToString(", ")
//
//        println(lst)
//    }
//
//    fun lambda(a: Int, b: Int, doSomething: ((Int, Int) -> Int)? = null) {
//        doSomething?.invoke(a, b)
//    }
//
//    fun String.printUpper() {
//        println(this.uppercase())
//    }
//
//    fun loadData(onSuccess: ((String) -> Unit)? = null) {
//        onSuccess?.invoke("limit")
//    }
//
//    class Box<in T> {
//        fun set(value: T) {}
//    }
//
//    sealed class Api<out T> {
//        data class Success<T>(val data: T) : Api<T>()
//        data class Error<T>(
//            val message: String,
//            val throwable: Throwable? = null,
//            val code: CodeError = CodeError.UNKNOW
//        ): Api<T>()
//    }
//
////    fun <T, R> Api<T>.map(transform: (T) -> R): Api<R> {
////        return when (this) {
////            is Api.Success -> Api.Success(transform(this.data))
////        }
////    }
//
//    enum class CodeError {
//        UNKNOW,
//
//    }
//
//    fun <T> Api<T>.onError(action: (String) -> Unit): Api<T> {
//        if (this is Api.Error) action(message)
//        return this
//    }
//
//    enum class Payment(val isOnlinePayment: Boolean) {
//
//        CASH(isOnlinePayment = false),
//        BANK(true);
//
//
//        companion object {
//            fun getOnlinePayment(): List<Payment> {
//                println("list = ${entries.map { it.isOnlinePayment }}")
//                println("filter = ${entries.filter { it.isOnlinePayment  }}")
//                return entries.filter { it.isOnlinePayment  }
//            }
//        }
//    }
//    @Test
//    fun check_Correct() {
////        val r: Api<Int> = Api.Success(35)
////        r.map { it -> it.toDouble() }
//        println("---")
//        Payment.getOnlinePayment()
////        val box: Box<String> = Box<Any>()
////        box.set("fd")
////        println("--------------------------")
////        ExampleUnitTest.GetSet()
////
////        val dog =  Implement().sound()
////        val lst = listOf<String>(dog.sound(), dog.doSomethingInCat())
////        val adapter = Adapter(object : Animal{
////            override fun sound(): String {
////                TODO("Not yet implemented")
////            }
////        })
////        val result =  Result.Error("error")
////        when (result) {
////            is ExampleUnitTest.Result.Success -> result.data
////            is ExampleUnitTest.Result.Loading -> ""
////            is ExampleUnitTest.Result.Error -> result.message
////        }
//
//        println("LST------------------")
////        val result = Result1.Success("Hello")
////            .mapSimple { it ->
////                it.uppercase()
////            }
////        println(result)
////        lambda(1,2){x, y -> x+y}
////        var onClick: (() -> Unit)? = null
////        onClick = {
////            println("onClick")
////        }
////        onClick.invoke()
////        val upper = "hello".printUpper()
////        println(upper)
////        println("be-----------")
////        loadData() {name ->
////            println(name)
////        }
////        loadData { name ->
////            println(name)
////        }
////        val a = lambda(1, 2){x, y -> x + y}
////        val b = {a: Int, b: Int -> a + b}
////
////
////        assertEquals("ga gau, meo meo", dog)
//
////com.example.test.ExampleUnitTest$$Lambda/0x000000010019aad8@238d68ff
//
//
//    }
//}