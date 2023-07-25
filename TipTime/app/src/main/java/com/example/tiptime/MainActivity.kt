package com.example.tiptime

import android.icu.util.CurrencyAmount
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
//import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
///import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
///import androidx.compose.ui.tooling.data.EmptyGroup.name
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipTimeCalculator()
                }
            }
        }
    }
}

@Composable
fun TipTimeCalculator() {
    var amountInput by remember { mutableStateOf("") }
    val amount= amountInput.toDoubleOrNull() ?: 0.0
    var tipInput by remember { mutableStateOf("") }
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    var roundOn by remember { mutableStateOf(false) }
    val tip = calculateTip(amount,tipPercent, roundOn )
    val focusManager = LocalFocusManager.current
    Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(8.dp)){
        Text(
            text = stringResource(id = R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(
            label = R.string.bill_amount,
            value = amountInput,
            onValuechange = {amountInput =it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {focusManager.moveFocus(FocusDirection.Down)}
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        EditNumberField(
            label = R.string.how_was_service,
            value = tipInput,
            onValuechange = {tipInput =it},
            keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {focusManager.clearFocus()}
            )
        )
        RoundTheTipRow(roundOn = roundOn, onRoundOn = {roundOn= it})
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.tip_amount,tip),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RoundTheTipRow(
    roundOn: Boolean,
    onRoundOn: (Boolean)-> Unit,
    modifier: Modifier= Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.round_on_tip))
        Switch(checked = roundOn,
            onCheckedChange = onRoundOn,
            modifier= Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
        colors= SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
        )

    }
}
@Composable
fun EditNumberField(
    @StringRes label :Int,
    value: String,
    onValuechange : (String)-> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier =Modifier
    ){

    TextField(
        value = value,
        onValueChange = onValuechange,
        label = {
          Text(text = stringResource(id = label),
          modifier = Modifier.fillMaxWidth()
          )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true
    )

}
@VisibleForTesting
internal fun calculateTip(amount: Double,tipPercent: Double=15.0,roundOn: Boolean): String{
    var tip =tipPercent/100 * amount
    if (roundOn){
        tip=kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
        TipTimeCalculator()
    }
}