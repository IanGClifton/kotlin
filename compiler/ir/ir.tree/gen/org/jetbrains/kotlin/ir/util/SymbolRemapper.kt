/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

// This file was generated automatically. See compiler/ir/ir.tree/tree-generator/ReadMe.md.
// DO NOT MODIFY IT MANUALLY.

package org.jetbrains.kotlin.ir.util

import org.jetbrains.kotlin.ir.symbols.*

/**
 * Auto-generated by [org.jetbrains.kotlin.ir.generator.print.symbol.SymbolRemapperInterfacePrinter]
 */
interface SymbolRemapper : DeclaredSymbolRemapper, ReferencedSymbolRemapper {

    /**
     * The default implementation of [SymbolRemapper]
     * that just keeps the old symbols everywhere.
     *
     * Auto-generated by [org.jetbrains.kotlin.ir.generator.print.symbol.EmptySymbolRemapperPrinter]
     */
    open class Empty : SymbolRemapper {

        override fun getDeclaredValueParameter(symbol: IrValueParameterSymbol): IrValueParameterSymbol = symbol

        override fun getDeclaredClass(symbol: IrClassSymbol): IrClassSymbol = symbol

        override fun getDeclaredAnonymousInitializer(symbol: IrAnonymousInitializerSymbol): IrAnonymousInitializerSymbol = symbol

        override fun getDeclaredTypeParameter(symbol: IrTypeParameterSymbol): IrTypeParameterSymbol = symbol

        override fun getDeclaredConstructor(symbol: IrConstructorSymbol): IrConstructorSymbol = symbol

        override fun getDeclaredEnumEntry(symbol: IrEnumEntrySymbol): IrEnumEntrySymbol = symbol

        override fun getDeclaredSimpleFunction(symbol: IrSimpleFunctionSymbol): IrSimpleFunctionSymbol = symbol

        override fun getDeclaredProperty(symbol: IrPropertySymbol): IrPropertySymbol = symbol

        override fun getDeclaredField(symbol: IrFieldSymbol): IrFieldSymbol = symbol

        override fun getDeclaredLocalDelegatedProperty(symbol: IrLocalDelegatedPropertySymbol): IrLocalDelegatedPropertySymbol = symbol

        override fun getDeclaredScript(symbol: IrScriptSymbol): IrScriptSymbol = symbol

        override fun getDeclaredTypeAlias(symbol: IrTypeAliasSymbol): IrTypeAliasSymbol = symbol

        override fun getDeclaredVariable(symbol: IrVariableSymbol): IrVariableSymbol = symbol

        override fun getDeclaredExternalPackageFragment(symbol: IrExternalPackageFragmentSymbol): IrExternalPackageFragmentSymbol = symbol

        override fun getDeclaredFile(symbol: IrFileSymbol): IrFileSymbol = symbol

        override fun getDeclaredReturnableBlock(symbol: IrReturnableBlockSymbol): IrReturnableBlockSymbol = symbol

        override fun getReferencedClass(symbol: IrClassSymbol): IrClassSymbol = symbol

        override fun getReferencedProperty(symbol: IrPropertySymbol): IrPropertySymbol = symbol

        override fun getReferencedScript(symbol: IrScriptSymbol): IrScriptSymbol = symbol

        override fun getReferencedConstructor(symbol: IrConstructorSymbol): IrConstructorSymbol = symbol

        override fun getReferencedEnumEntry(symbol: IrEnumEntrySymbol): IrEnumEntrySymbol = symbol

        override fun getReferencedFunction(symbol: IrFunctionSymbol): IrFunctionSymbol = symbol

        override fun getReferencedSimpleFunction(symbol: IrSimpleFunctionSymbol): IrSimpleFunctionSymbol = symbol

        override fun getReferencedField(symbol: IrFieldSymbol): IrFieldSymbol = symbol

        override fun getReferencedLocalDelegatedProperty(symbol: IrLocalDelegatedPropertySymbol): IrLocalDelegatedPropertySymbol = symbol

        override fun getReferencedVariable(symbol: IrVariableSymbol): IrVariableSymbol = symbol

        override fun getReferencedClassifier(symbol: IrClassifierSymbol): IrClassifierSymbol = symbol

        override fun getReferencedReturnTarget(symbol: IrReturnTargetSymbol): IrReturnTargetSymbol = symbol

        override fun getReferencedValue(symbol: IrValueSymbol): IrValueSymbol = symbol

        override fun getReferencedTypeAlias(symbol: IrTypeAliasSymbol): IrTypeAliasSymbol = symbol
    }

    companion object {
        val EMPTY: SymbolRemapper = Empty()
    }

    // This method is left for compatibility with Compose. Do not use it, it will be removed soon.
    fun getReferencedClassOrNull(symbol: IrClassSymbol?): IrClassSymbol? =
        symbol?.let { getReferencedClass(it) }
}
