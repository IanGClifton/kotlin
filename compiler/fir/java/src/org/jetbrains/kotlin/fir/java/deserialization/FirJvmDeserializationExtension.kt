/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.java.deserialization

import org.jetbrains.kotlin.builtins.jvm.JvmBuiltInsSignatures
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.builder.FirRegularClassBuilder
import org.jetbrains.kotlin.fir.deserialization.FirDeserializationExtension
import org.jetbrains.kotlin.fir.types.builder.buildResolvedTypeRef
import org.jetbrains.kotlin.fir.types.impl.ConeClassLikeTypeImpl
import org.jetbrains.kotlin.fir.types.toLookupTag
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

class FirJvmDeserializationExtension(session: FirSession) : FirDeserializationExtension(session) {
    override fun FirRegularClassBuilder.configureDeserializedClass(classId: ClassId) {
        addSerializableIfNeeded(classId)
    }

    private fun FirRegularClassBuilder.addSerializableIfNeeded(classId: ClassId) {
        if (!JvmBuiltInsSignatures.isSerializableInJava(classId.asSingleFqName().toUnsafe())) return
        superTypeRefs += buildResolvedTypeRef {
            type = ConeClassLikeTypeImpl(
                JAVA_IO_SERIALIZABLE.toLookupTag(),
                typeArguments = emptyArray(),
                isNullable = false
            )
        }
    }

    companion object {
        private val JAVA_IO_SERIALIZABLE = ClassId.topLevel(FqName("java.io.Serializable"))
    }
}
