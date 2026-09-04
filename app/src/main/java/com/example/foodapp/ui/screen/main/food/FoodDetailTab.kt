package com.example.foodapp.ui.screen.main.food

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Note
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodapp.R
 import com.example.foodapp.core.utils.toVND
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Variation
import com.example.foodapp.domain.model.Variation.VariationType
import com.example.foodapp.presentation.state.CartUiState
import com.example.foodapp.presentation.state.getTotalPrice
import com.example.foodapp.ui.fakeData.PreviewCartState
import com.example.foodapp.ui.fakeData.PreviewDataFood
import com.example.foodapp.ui.screen.main.restaurant.section.ConflictDialog
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Blue2
import com.example.foodapp.ui.theme.Gray65
import com.example.foodapp.ui.theme.Gray85
import com.example.foodapp.ui.theme.Gray95
import com.example.foodapp.ui.theme.MediumOrange

@Composable
fun FoodDetailTab(
    food: Food,
    cartState: CartUiState,
    onSelectVariation: (String, Variation, Boolean) -> Unit,
    onStartEditing: (Food) -> Unit,
    decreaseQtyDetail: () -> Unit,
    increaseQtyDetail: () -> Unit,
    toAddCart: () -> Unit,
    onDialogToClose: () -> Unit,
    onForceAddItem: () -> Unit,

    ) {
    //DEBUG_CART, DEBUG_TAB


    LaunchedEffect(food.foodId) {
        Log.d(
            "DEBUG_TAB",
            "LaunchedEffect: foodId=${food.foodId}, currentItem=${cartState.currentEditingItem == null}"
        ) //
        if (cartState.currentEditingItem == null) {
            onStartEditing(food)
        }
    }
    val currentItem = cartState.currentEditingItem
    Log.d("check_cart_state_editing", "food detail: $currentItem")
    Scaffold(
        bottomBar = {
            BottomBar(
                cartState = cartState,
                food = food,
                increaseQtyDetail = increaseQtyDetail,
                decreaseQtyDetail = decreaseQtyDetail,
                toAddCart = toAddCart
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 0.dp,
                bottom = paddingValues.calculateBottomPadding() + 20.dp
            ),
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {

            //icon header
            item() {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    //back
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    //share
                    Icon(
                        imageVector = Icons.Rounded.IosShare,
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(10.dp))

                    //favorite
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = null,
                    )
                }
            }

            //img food + price
            item() {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .height(300.dp)
                ) {
                    AsyncImage(
                        model = food.imgUrl,
                        placeholder = painterResource(R.drawable.pizza2),
                        error = painterResource(R.drawable.pizza2),
                        contentScale = ContentScale.Crop,
                        onError = {
                            Log.e("COIL_ERROR", "coil: ${it.result.throwable.message}")
                        },
                        modifier = Modifier.align(Alignment.Center),
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomStart)
                            .padding(horizontal = 15.dp, vertical = 15.dp)
                            .background(MediumOrange, RoundedCornerShape(30.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        text = "${food.price.toVND()}đ",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

            }

            //info food
            item() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    //food name
                    Text(
                        text = food.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )

                    //cal
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_fire),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(Modifier.width(1.dp))

                        //calories
                        Text(
                            text = "${food.calories} cal",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                //ingredient
                Text(
                    text = "Nguyên liệu",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = food.ingredient,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray
                )

                Spacer(Modifier.height(10.dp))
            }



            Log.d("SIZE VAR", "${food.variations.size}")
            food.variations.forEach { variation ->

                val isRequired = if (variation.required) {
                    "Bắt buộc"
                } else
                    "Tùy chọn"
                val variationType = if (variation.type == VariationType.SINGLE)
                    "chỉ chọn một"
                else
                    "có thể chọn nhiều"

                //variation
                item() {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(),
                        border = BorderStroke(1.dp, Gray85),
                        shape = RoundedCornerShape(25.dp),
                        color = Color.Unspecified
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp), // chỉ vertical ở đây
//                        verticalArrangement = Arrangement.SpaceBetween,
//                        horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            //text
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp), //padding rieng,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,

                                ) {

                                //text
                                Text(
                                    text = variation.name,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.SemiBold
                                )

                                //text
                                Text(
                                    text = "$isRequired - $variationType",
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .background(
                                            color = Gray85.copy(alpha = 0.50f),
                                            shape = RoundedCornerShape(30.dp)
                                        )
                                        .padding(vertical = 5.dp, horizontal = 10.dp)
                                )
                            }

                            variation.options.forEachIndexed { index, option ->

                                val isChecked = currentItem
                                    ?.variations
                                    ?.get(variation.id)
                                    ?.any { it.id == option.id } == true //co op id trong current
                                //tuong minh hon
                                //val selectedOptions = currentItem?.variations?.get(variation.id)
                                //val isChecked = if (selectedOptions != null) {
                                //    selectedOptions.any { it.id == option.id }
                                //} else {
                                //    false
                                //}
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 15.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (variation.type == Variation.VariationType.MULTI) {
                                        Checkbox(
                                            checked = isChecked, //ischecked
                                            onCheckedChange = { checked ->
                                                Log.d(
                                                    "DEBUG_TAB",
                                                    "Checkbox change: opt=${option.id}, checked=$checked"
                                                ) //
                                                onSelectVariation(option.id, variation, checked)
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = Blue1,
                                                uncheckedColor = Gray65,
                                                checkmarkColor = Color.White
                                            )
                                        )
                                    } else {
                                        //false, true,

                                        RadioButton(
                                            selected = isChecked,
                                            onClick = {
                                                Log.d(
                                                    "DEBUG_TAB",
                                                    "RadioButton click: opt=${option.id}, currentChecked=$isChecked"
                                                ) //
                                                onSelectVariation(option.id, variation, !isChecked)
                                            },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color.Cyan,

                                            )
                                        )
                                    }


                                    Text(
                                        text = option.name,
                                        fontSize = 14.sp
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Text(text = option.price.toVND(), fontSize = 14.sp)
                                }


                                if (index != (variation.options.size - 1)) {
                                    Spacer(
                                        Modifier
                                            .height(1.dp)
                                            .padding(start = 50.dp)
                                            .fillMaxWidth()
                                            .background(Gray85)
                                    )
                                }
                            }
                        }

                    }
                }
            }


            //note
            item() {
                Surface(
                    border = BorderStroke(1.dp, Gray85),
                    shape = RoundedCornerShape(25.dp),
                    color = Color.Unspecified,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Thêm lưu ý cho quán",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )

                            Text(
                                text = "Không bắt buộc",
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .background(Gray85.copy(0.30f), RoundedCornerShape(30.dp))
                                    .padding(vertical = 5.dp, horizontal = 10.dp)
                            )
                        }

                        Spacer(Modifier.height(20.dp))
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = "",
                            onValueChange = {},
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Note,
                                    contentDescription = null,
                                )
                            },
                            placeholder = {
                                Text(
                                    "Quán sẽ cố gắng đáp ứng yêu cầu",
                                    fontSize = 15.sp,
                                    color = Gray65
                                )
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedContainerColor = Gray95,
                                unfocusedContainerColor = Gray95,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            )
                        )
                    }
                }
            }


        }

    }


    ConflictDialog(
        showDialog = cartState.showConfirmDialog,
        onDialogToClose = onDialogToClose,
        onForceAddItem = onForceAddItem,
        title = "Thay thế giỏ hàng?",
        message = cartState.conflictData?.message ?: "..."

    )
}


@Composable
fun BottomBar(
    cartState: CartUiState,
    food: Food,
    decreaseQtyDetail: () -> Unit,
    increaseQtyDetail: () -> Unit,
    toAddCart: () -> Unit,
) {

    val context = LocalContext.current
//    cartState.currentEditingItem?.let { itemUi ->
//        if (itemUi.variations.isNotEmpty()) {

    val currentItem = cartState.currentEditingItem ?: return
    val price = currentItem.getTotalPrice()
    val isClickable = food.variations.all { variation -> //duyệt var
        val current = currentItem.variations[variation.id]?.isNotEmpty() ?: false
        (!variation.required || current)
        //nhánh variation.required = false -> phủ định thì luôn đúng
        //ngược lại varation.reuqured = true -> phủ định = false,
        //thì dựa vào trường hợp còn lại để quyết định đúng sai, là var có trống hay k
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .topShadow(height = 14.dp)
            .background(Color.White)
    )
    {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                //price
                Text(
                    text = price.toVND(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = Blue1
                )

                Spacer(Modifier.weight(1f))

                //header
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    //-
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null,
                        tint = if (isClickable)
                            Color.Black
                        else
                            Color.Black.copy(0.3f),
                        modifier = Modifier
                            .clickable(
                                enabled = isClickable
                            ) {
                                decreaseQtyDetail()
                            }
                            .background(
                                if (isClickable)
                                    Blue0.copy(alpha = 0.3f)
                                else
                                    Blue2.copy(0.3f),
                                CircleShape)
                            .padding(3.dp)
                            .size(30.dp)
                    )

                    //quantity
                    Text(
                        text = currentItem.quantity.toString(),
                        fontSize = 17.sp
                    )

                    // +
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .clickable(
                                enabled = isClickable
                            ) {
                                increaseQtyDetail()
                            }
                            .background(
                                if (isClickable)
                                    Blue1
                                else
                                    Blue1.copy(0.3f),
                                CircleShape
                            )
                            .padding(3.dp)
                            .size(30.dp)
                    )
                }
            }

            //add btn
            Box(
                modifier = Modifier
                    .clickable(enabled = isClickable, onClick = {
                        toAddCart()

                    })
                    .fillMaxWidth()
                    .background(
                        if (isClickable) {
                            Blue0
                        } else {
                            Blue0.copy(0.3f)
                        },
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(15.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Thêm vào giỏ hàng",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.White

                )
            }
        }

    }
}

//shadow top custom
fun Modifier.topShadow(
    height: Dp = 12.dp,
    color: Color = Color.Black
) = this.drawBehind {
    val heightPx = height.toPx()

    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                color.copy(alpha = 0f),      // trong suốt ở trên
                color.copy(alpha = 0.05f),
                color.copy(alpha = 0.12f)    // đậm nhất sát mép top của box
            ),
            startY = -heightPx,
            endY = 0f
        ),
        topLeft = Offset(0f, -heightPx),
        size = Size(size.width, heightPx)
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    FoodDetailTab(
        food = PreviewDataFood.food,
        cartState = PreviewCartState.previewCartState,
        onSelectVariation = { _, _, _ -> },
        onStartEditing = {},
        decreaseQtyDetail = {},
        increaseQtyDetail = {},
        toAddCart = {},
        onDialogToClose = {},
        onForceAddItem = {},
    )
}
