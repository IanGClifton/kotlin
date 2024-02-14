/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.generators.tests

import com.intellij.openapi.util.io.FileUtil
import org.jetbrains.kotlin.generators.tests.GenerateSteppedRangesCodegenTestData.Function
import org.jetbrains.kotlin.generators.tests.GenerateSteppedRangesCodegenTestData.Function.*
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

object GenerateInRangeExpressionTestData {
    private val TEST_DATA_DIR = File("compiler/testData/codegen/box/ranges/contains")
    private val GENERATED_DIR = File(TEST_DATA_DIR, "generated")

    private val PREAMBLE_MESSAGE = "Auto-generated by ${GenerateInRangeExpressionTestData::class.java.simpleName}. Do not edit!"

    private val FILES_MUTED_IN_WASM = listOf<String>()

    private fun generateMatrixTestCase(
        fileName: String,
        rangeExpressions: List<String>,
        elementExpressions: List<String>,
        header: String = ""
    ) {
        PrintWriter(File(GENERATED_DIR, fileName)).use {
            it.generateTestCaseBody(
                fileName, header, rangeExpressions, elementExpressions
            )
        }
    }

    private fun PrintWriter.generateTestCaseBody(
        fileName: String,
        header: String,
        rangeExpressions: List<String>,
        elementExpressions: List<String>
    ) {
        if (fileName in FILES_MUTED_IN_WASM) {
            println("// IGNORE_BACKEND: WASM")
        }
        println("// $PREAMBLE_MESSAGE")
        println("// WITH_STDLIB")
        if (rangeExpressions.any { "..<" in it }) {
            println("// DONT_TARGET_EXACT_BACKEND: JVM")
            println("// !LANGUAGE: +RangeUntilOperator")
            println("@file:OptIn(ExperimentalStdlibApi::class)")
        }
        println()
        println(header)
        println()

        val rangeValNames = generateGlobalValDefinitions(rangeExpressions, "range")

        val elementValNames = generateGlobalValDefinitions(elementExpressions, "element")

        val testFunctions = StringWriter()
        val testFunctionsWriter = PrintWriter(testFunctions)

        println("fun box(): String {")
        rangeValNames.zip(rangeExpressions).forEachIndexed { i, (rangeValName, rangeExpression) ->
            elementValNames.zip(elementExpressions).forEachIndexed { j, (elementValName, elementExpression) ->
                val functionName = "testR${i}xE${j}"

                println("    $functionName()")

                testFunctionsWriter.generateTestCaseFunction(functionName, rangeValName, rangeExpression, elementValName, elementExpression)
            }
        }
        println("    return \"OK\"")
        println("}")
        println()
        println(testFunctions.toString())
    }

    private fun PrintWriter.generateGlobalValDefinitions(expressions: List<String>, prefix: String): List<String> {
        val valNames = expressions.indices.map { "$prefix$it" }
        valNames.zip(expressions).forEach { (name, expression) -> println("val $name = $expression") }
        println()
        return valNames
    }

    private fun PrintWriter.generateTestCaseFunction(
        functionName: String,
        rangeValName: String,
        rangeExpression: String,
        elementValName: String,
        elementExpression: String
    ) {
        println("fun $functionName() {")
        println("    // with possible local optimizations")
        println("    if ($elementExpression in $rangeExpression != $rangeValName.contains($elementExpression)) throw AssertionError()")
        println("    if ($elementExpression !in $rangeExpression != !$rangeValName.contains($elementExpression)) throw AssertionError()")
        println("    if (!($elementExpression in $rangeExpression) != !$rangeValName.contains($elementExpression)) throw AssertionError()")
        println("    if (!($elementExpression !in $rangeExpression) != $rangeValName.contains($elementExpression)) throw AssertionError()")
        println("    // no local optimizations")
        println("    if ($elementValName in $rangeExpression != $rangeValName.contains($elementValName)) throw AssertionError()")
        println("    if ($elementValName !in $rangeExpression != !$rangeValName.contains($elementValName)) throw AssertionError()")
        println("    if (!($elementValName in $rangeExpression) != !$rangeValName.contains($elementValName)) throw AssertionError()")
        println("    if (!($elementValName !in $rangeExpression) != $rangeValName.contains($elementValName)) throw AssertionError()")
        println("}")
        println()
    }

    private fun generateRangeOperatorTestCases(
        namePrefix: String,
        functions: List<Function>,
        bounds: Pair<String, String>,
        elementExpressions: List<String>
    ) {
        val (aExpression, bExpression) = bounds
        for (function in functions) {
            generateMatrixTestCase(
                "$namePrefix${function.subdir.replaceFirstChar(Char::uppercase)}.kt",
                listOf(
                    "$aExpression${function.infixFunctionName}$bExpression",
                    "$bExpression${function.infixFunctionName}$aExpression"
                ),
                elementExpressions
            )
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        if (!TEST_DATA_DIR.exists()) throw AssertionError("${TEST_DATA_DIR.path} doesn't exist")

        FileUtil.delete(GENERATED_DIR)
        GENERATED_DIR.mkdirs()

        val charLiterals = listOf("'0'", "'1'", "'2'", "'3'", "'4'")

        val numbers = listOf("-1", "0", "1", "2", "3", "4")
        fun String.wrapNegative() = if (this.startsWith("-")) "($this)" else this

        val integerLiterals =
            numbers.flatMap {
                listOf("${it.wrapNegative()}.toByte()", "${it.wrapNegative()}.toShort()", it, it + "L")
            }
        val floatingPointLiterals =
            numbers.flatMap {
                listOf(it + "F", it + ".0")
            }

        val unsignedNumbers = numbers.drop(1).map { it + "u" }

        val allFunctions = Function.entries
        val rangeFunctions = allFunctions - DOWN_TO

        generateRangeOperatorTestCases("char", allFunctions, "'1'" to "'3'", charLiterals)

        val intBounds = "1" to "3"
        generateRangeOperatorTestCases("int", rangeFunctions, intBounds, integerLiterals)
        generateRangeOperatorTestCases("int", listOf(DOWN_TO), intBounds, numbers)

        val longBounds = "1L" to "3L"
        generateRangeOperatorTestCases("long", rangeFunctions, longBounds, integerLiterals)
        generateRangeOperatorTestCases("long", listOf(DOWN_TO), longBounds, numbers.map { it + "L" })

        generateRangeOperatorTestCases("uint", allFunctions, "1u" to "3u", unsignedNumbers)
        generateRangeOperatorTestCases("ulong", allFunctions, "1uL" to "3uL", unsignedNumbers.map { it + "L" })

        generateRangeOperatorTestCases("double", listOf(RANGE_TO, RANGE_UNTIL), "1.0" to "3.0", floatingPointLiterals)

        val floatBounds = "1.0F" to "3.0F"
        generateRangeOperatorTestCases("float", listOf(RANGE_TO), floatBounds, floatingPointLiterals)
        // only Float in OpenEndRange<Float> operation is supported
        generateRangeOperatorTestCases("float", listOf(RANGE_UNTIL), floatBounds, floatingPointLiterals.filter { "F" in it })

        generateMatrixTestCase(
            "arrayIndices.kt",
            listOf("intArray.indices", "objectArray.indices", "emptyIntArray.indices", "emptyObjectArray.indices"),
            integerLiterals,
            """val intArray = intArrayOf(1, 2, 3)
                    |val objectArray = arrayOf(1, 2, 3)
                    |val emptyIntArray = intArrayOf()
                    |val emptyObjectArray = arrayOf<Any>()
                """.trimMargin()
        )

        generateMatrixTestCase(
            "collectionIndices.kt",
            listOf("collection.indices", "emptyCollection.indices"),
            integerLiterals,
            """val collection = listOf(1, 2, 3)
                    |val emptyCollection = listOf<Any>()
                """.trimMargin()
        )

        generateMatrixTestCase(
            "charSequenceIndices.kt",
            listOf("charSequence.indices", "emptyCharSequence.indices"),
            integerLiterals,
            """val charSequence: CharSequence = "123"
                    |val emptyCharSequence: CharSequence = ""
                """.trimMargin()
        )
    }
}
