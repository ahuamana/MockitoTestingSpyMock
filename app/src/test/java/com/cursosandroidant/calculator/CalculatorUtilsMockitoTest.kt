package com.cursosandroidant.calculator

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CalculatorUtilsMockitoTest {

    @Mock
    lateinit var operations: Operations

    @Mock
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
        verify(operations).tryResolve(operation, isFromResolve, listener)
    }

    @Test
    fun `addOperator should call callback`() {
        val operator = "-"
        val operation = "4+" // 4+-
        var callbackCalled = false
        calculatorUtils.addOperator(operator, operation) {
            callbackCalled = true
        }
        assertTrue(callbackCalled)
    }

    @Test
    fun `addOperator invalid Sub should not call callback`() {
        val operator = "-"
        val operation = "4-" // 4--
        var callbackCalled = false
        calculatorUtils.addOperator(operator, operation) {
            callbackCalled = true
        }
        assertFalse(callbackCalled)
    }

    @Test
    fun `addPoint firstPoint should call callback`() {
        val operation = "4+"
        var callbackCalled = false
        calculatorUtils.addPoint(operation) {
            callbackCalled = true
        }
        assertTrue(callbackCalled)
        verifyNoInteractions(operations) //No se llama a ninguna función de operations
    }

    @Test
    fun `addPoint secondPoint should not call callback`() {
        val operation = "4.5x2"
        val operator = "x"
        var callbackCalled = false

        `when`(operations.getOperator(operation)).thenReturn(operator)
        `when`(operations.divideOperation(operator, operation)).thenReturn(arrayOf("4.5", "2"))

        calculatorUtils.addPoint(operation) {
            callbackCalled = true
        }
        assertTrue(callbackCalled)
        verify(operations).getOperator(operation) //Se llama a getOperator
        verify(operations).divideOperation(operator, operation) //Se llama a divideOperation
    }

}