
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.AddShoppingCart
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.ShoppingBasket
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.core.utils.toVND
import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.presentation.state.CartUiState
import com.example.foodapp.ui.preview.PreviewCartState
import com.example.foodapp.ui.preview.PreviewDataFood.food
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Brow0
import com.example.foodapp.ui.theme.Gray65
import com.example.foodapp.ui.theme.Gray85
import com.example.foodapp.ui.theme.Yellow0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTab(
    cartState: CartUiState,
    onClickClearCart: () -> Unit,
    onClickGetBack: () -> Unit,
    onClickNavCheckOut: () -> Unit,
) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
    //    }
    var onClickSetting by remember { mutableStateOf(false) }
    var onClickItem by remember { mutableStateOf(false) }
    var onClickItems by remember { mutableStateOf(false) }
    var onClickHasItem = remember { mutableStateListOf<String>() }

    val isPreview = LocalInspectionMode.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        contentColor = Color.White,
        topBar = {
            MyTopAppBar(
                onClickSetting = onClickSetting,
                onToggleSetting = { onClickSetting = !onClickSetting },
                cart = cartState.cart,
                onClickGetBack = onClickGetBack
            )
        },
        bottomBar = {
            if (onClickSetting && cartState.cart != null) CartTabBotBar(
                checkedItems = onClickItems,
                onClickItems = {
                    onClickItems = it
                    onClickItem = it
                },
                checkedItem = onClickItem,
                onClickClearCart = onClickClearCart,
                cart = cartState.cart
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val cart = cartState.cart
            val cartSize = cart?.cartItems?.size ?: 0
            val restaurant = cartState.restaurant ?: Restaurant()
            Log.d("Crt state", restaurant.toString())
//            val food = cartState.currentEditingItem.food
            val display = if (isPreview) {
                10
            } else if (cart != null) {
                1
            } else {
                0
            }
//            if (false) {

            if (display != 0) {
                items(display) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Gray65.copy(0.7f)),
                        tonalElevation = 2.dp,
                        color = Color.Unspecified
                    ) {

                        //box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min)
                            ) {

                                // image
                                AsyncImage(
                                    model = restaurant.imageUrl,
                                    contentDescription = null,
                                    fallback = painterResource(R.drawable.pizza2),
                                    placeholder = painterResource(R.drawable.pizza2),
                                    modifier = Modifier
                                        .align(Alignment.Top)
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(15.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                //res info
                                Column(
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                ) {

                                    //preview
                                    if (display == 10) {
                                        Text(
                                            text = "Mì cay Seoul - Lê Trọng Tấn",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "7.69km",
                                            color = Color.Black.copy(alpha = 0.6f)
                                        )
                                        Text(
                                            text = "123.000đ",
                                            color = Color.Black,

                                            )


                                    }
                                    //res info + price + km
                                    else {
                                        if (!onClickSetting) {
    //                                        if (false) {
                                            Text(
                                                text = restaurant.restaurantName,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                            Text(
                                                text = "7.65 km",
                                                color = Color.Black.copy(alpha = 0.5f)
                                            )
                                            Text(
                                                text = food.price.toVND(),
                                                color = Color.Black.copy(alpha = 0.6f),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        } else {
                                            Text(
                                                text = restaurant.restaurantName,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )

                                            //danh gia + luot mua
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                                ) {
                                                    Icon (
                                                        imageVector = Icons.Rounded.Star,
                                                        contentDescription = null,
                                                        tint = Yellow0,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    //luot danh gia
                                                    Text(
                                                        text = "5 (1)",
                                                        color = Gray65
                                                    )
                                                }

                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.ShoppingBasket,
                                                        contentDescription = null,
                                                        tint = Brow0.copy(),
                                                        modifier = Modifier.size(18.dp)
                                                    )

                                                    //luot mua
                                                    Text(
                                                        text = "900+",
                                                        color = Gray65
                                                    )
                                                }


                                            }
                                            //dia chi
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.LocationOn,
                                                    contentDescription = null,
                                                    tint = Color.Red.copy(alpha = 0.4f),
                                                    modifier = Modifier.size(18.dp)
                                                )

                                                Text(
                                                    text = "6.2 km",
                                                    color = Gray65
                                                )
                                            }
                                        }
                                    }
                                }

                                //checkbox 1 item
                                if (onClickSetting) {
//                                if (true) {

                                    Row(
                                        modifier = Modifier.fillMaxHeight(),
                                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    ) {

                                        //price + quantity
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(5.dp),
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            //price
                                            Text(
                                                "35.000d",
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.Black
                                            )

                                            //quantity
                                            Text(
                                                text = "2 món",
                                                color = Gray65
                                            )
                                        }

                                        //checkbox + border
                                        //sao cho nay toi ko dung padding ma box to qua vay
//                                        Box(
//                                            modifier = Modifier
//                                                .border(
//                                                    1.dp,
//                                                    color = if (onClickItem) Blue1 else Gray65,
//                                                    RoundedCornerShape(7.dp)
//                                                )
////                                                .padding(horizontal = 0.dp)
//                                        ) {

//                                        CompositionLocalProvider(
//                                            LocalMinimumInteractiveComponentEnforcement provides false
//                                        ) {
//                                            Checkbox(
//
//                                                modifier = Modifier
//                                                    .border(
//                                                        width = 1.dp,
//                                                        color = Color.Red,
//                                                        RoundedCornerShape(4.dp)
//                                                    ),
//                                                checked = onClickItem,
//                                                onCheckedChange = {},
//                                                colors = CheckboxDefaults.colors(
//                                                    checkedColor = Color.Red,
//                                                    uncheckedColor = Color.White,
//                                                    checkmarkColor = Blue1
//                                                )
//                                            )
//                                        }
                                    }


                                    Checkbox(
                                        checked = onClickItem,
                                        onCheckedChange = {
                                            onClickItem = it
                                                          },
                                        modifier = Modifier
                                            .border(
                                                width = 1.dp,
                                                color = if (onClickItem) Blue1 else Gray65,
                                                RoundedCornerShape(4.dp),
                                            )
                                            .size(24.dp),
                                        colors = CheckboxDefaults.colors(
                                            checkmarkColor = Blue1,
                                            checkedColor = Color.Transparent,
                                            uncheckedColor = Color.Transparent,
                                        )
                                    )


//                                    }
                                } else {
                                    val context = LocalContext.current
//                                    if (true) {
                                    //onclick checkout
                                    Row(
                                        horizontalArrangement = Arrangement
                                            .spacedBy(10.dp, Alignment.CenterHorizontally),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .clickable{
                                                showToast(context = context, "clicked")
                                                onClickNavCheckOut()
                                            }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .defaultMinSize(minWidth = 24.dp, minHeight = 24.dp)
                                                .background(Blue1, CircleShape)
                                                .padding(5.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = cartSize.toString(),
                                                modifier = Modifier,
                                                color = Color.White
                                            )
                                        }

                                        Icon(
                                            Icons.Rounded.ArrowForwardIos,
                                            contentDescription = null,
                                            tint = Gray65
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                //gio hang trong'
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_empty_cart1),
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                        Spacer(Modifier.height(20.dp))

                        Text(
                            text = "Giỏ hàng hiện đang trống",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 26.sp,
                            color = Gray65
                        )


                    }
                }


            }
        }
    }
}


//top bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    onClickSetting: Boolean,
    onToggleSetting: () -> Unit,
    cart: Cart?,
    onClickGetBack:() -> Unit,
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
            title = {
                Text(
                    "Giỏ hàng của tôi ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier
                )
            },

            //back
            navigationIcon = {
                IconButton(onClick = {
                    showToast(context, "on clicked")
                    onClickGetBack()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            },

            //edit mode
            actions = {
                TextButton(
                    onClick = {
                        onToggleSetting()
                    }) {
                    //cart == null false, !onclick false -> quan ly,
                    if (!onClickSetting || cart == null) {
                        Text(
                            text = "Quản lý",
                            color = Blue1,
                            fontSize = 16.sp
                        )
                    } else {
                        Text(
                            text = "Hủy",
                            color = Blue1,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        )
        HorizontalDivider(thickness = 1.dp)
    }

}

@Preview(showBackground = true)
@Composable
fun Preview() {

    CartTab(
        cartState = PreviewCartState.previewCartState,
        onClickClearCart =  {},
        onClickGetBack = {},
        onClickNavCheckOut = {},
        )
}

@Composable
fun CartTabBotBar(
    checkedItem: Boolean,
    checkedItems: Boolean,
    onClickItems: (Boolean) -> Unit,
    onClickClearCart: () -> Unit,
    cart: Cart?,

    ) {

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
    ) {
        //divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray85)
        )

        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 30.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            //box delete
            Box(
                modifier = Modifier
                    .background(Color.White)
            ) {

                Box(
                    modifier = Modifier
                        .border(
                            1.dp,
                            if (checkedItems) Blue1 else Gray65,
                            RoundedCornerShape(4.dp)
                        )
                ) {
                    Checkbox(
                        checked = checkedItems, //toi ghi nhu vay dc ko, toi ko muon dung 2 param chi de nhan gia tri va set
                        onCheckedChange = { onClickItems(it) },
                        modifier = Modifier.size(24.dp),
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = Blue1,
                            checkedColor = Color.White,
                            uncheckedColor = Color.White,
                        )
                    )
                }
            }

            //chon tat ca
            Text(
                text = "Chọn tất cả",
//                color = if (checkedItems) Color.Black else Gray65
                color =  Color.Black

            )

            Spacer(Modifier.weight(1f))

            //xoa
            Box(
                modifier = Modifier
                    .background(
                        if (checkedItem || checkedItems) Blue1 else Gray85.copy(alpha = 0.5f),
                        RoundedCornerShape(10.dp)
                    )
                    .clickable(
                        enabled = checkedItem || checkedItems,
                        onClick = {
                            showToast(context = context, "clicked!")
                            onClickClearCart()
                        }
                    )
                    .padding(vertical = 15.dp, horizontal = 70.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (cart == null) "Xóa (0)" else "Xóa (1)",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = if (checkedItem || checkedItems) Color.White else Color.Black.copy(alpha = 0.4f),
                )
            }
        }


    }
}