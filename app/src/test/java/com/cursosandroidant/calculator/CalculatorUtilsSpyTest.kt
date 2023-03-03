package com.cursosandroidant.calculator

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CalculatorUtilsSpyTest {

    @Spy
    lateinit var operations: Operations

    @Spy
    lateinit var listener: OnResolveListener


    lateinit var calculatorUtils: CalculatorUtils

    @Before
    fun setUp() {
        calculatorUtils = CalculatorUtils(operations, listener)
    }

    //Alt + 96 -> `
    @Test
    fun `checkOrResolve should call tryResolve`() {
        val operation = "1+1"
        val isFromResolve = true
        calculatorUtils.checkOrResolve(operation, isFromResolve)
        Mockito.verify(operations).tryResolve(operation, isFromResolve, listener)
    }

    @Test
    fun `addOperator should call callback`() {
        val operator = "-"
        val operation = "4+" // 4+-
        var callbackCalled = false
        calculatorUtils.addOperator(operator, operation) {
            callbackCalled = true
        }
        Assert.assertTrue(callbackCalled)
    }

    @Test
    fun `addOperator invalid Sub should not call callback`() {
        val operator = "-"
        val operation = "4-" // 4--
        var callbackCalled = false
        calculatorUtils.addOperator(operator, operation) {
            callbackCalled = true
        }
        Assert.assertFalse(callbackCalled)
    }

    @Test
    fun `addPoint validSecondPoint should call callback`() {
        val operation = "4.5x2"
        val operator = "x"
        var callbackCalled = false

        calculatorUtils.addPoint(operation) {
            callbackCalled = true
        }
        Assert.assertTrue(callbackCalled)
        Mockito.verify(operations).getOperator(operation) //Se llama a getOperator
        Mockito.verify(operations).divideOperation(operator, operation) //Se llama a divideOperation
    }

    @Test
    fun `addPoint invalidSecondPoint should not call callback`() {
        val operation = "4.5x2."
        val operator = "x"
        var callbackCalled = false

        calculatorUtils.addPoint(operation) {
            callbackCalled = true
        }
        Assert.assertFalse(callbackCalled)
        Mockito.verify(operations).getOperator(operation) //Se llama a getOperator
        Mockito.verify(operations).divideOperation(operator, operation) //Se llama a divideOperation
    }
}