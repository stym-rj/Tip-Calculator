package com.example.billcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billcalculator.ui.theme.BillCalculatorTheme
import java.text.NumberFormat
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BillCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipTimeScreen()
                }
            }
        }
    }
}

@Composable
fun TipTimeScreen() {

    var amountInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0

    var percentInput by remember { mutableStateOf("") }
    val percent = percentInput.toDoubleOrNull() ?: 0.0

    var roundUp by remember { mutableStateOf(false) }

    val tip = calculateTip(amount, percent, roundUp)

    val focusManager = LocalFocusManager.current



    Column(
        modifier = Modifier
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.calculate_tip),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        EnterTextField(
            label = R.string.bill_amount,
            value = amountInput,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            onValueChange = {amountInput = it}
        )

        Spacer(modifier = Modifier.height(16.dp))

        EnterTextField(
            label = R.string.how_was_the_service,
            value = percentInput,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            onValueChange = {percentInput = it}
        )

        Spacer(modifier = Modifier.height(16.dp))

        RoundTipArea(roundUp = roundUp, onRoundUpChange = { roundUp = it })

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.tip_amount, tip),
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )


    }
}

@Composable
fun RoundTipArea (
    roundUp : Boolean,
    onRoundUpChange : (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row( modifier = modifier
        .fillMaxWidth()
        .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        Text(
            text = stringResource(id = R.string.round_up_tip)
        )
        Switch(checked = roundUp,
            onCheckedChange = onRoundUpChange,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
            )

    }
}

@Composable
fun EnterTextField(
    @StringRes label : Int,
    value : String,
    keyboardOptions : KeyboardOptions,
    keyboardActions: KeyboardActions,
    onValueChange : (String) -> Unit
) {

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
            ){
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = stringResource(id = label)
                )
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

private fun calculateTip(
    amount : Double,
    tipPercentage : Double = 15.0,
    roundUp: Boolean
) : String {
    var tip = (tipPercentage / 100) * amount
    if ( roundUp ) {
        val temp = tip.roundToInt()
        tip = temp.toDouble()
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BillCalculatorTheme {
        TipTimeScreen()
    }
}