package org.willthisfly.raketaframework.routes

operator fun MethodBasedRouter.plusAssign(other: Router) {
    addAll(other)
}

operator fun MethodBasedRouter.plus(declaringMethod: NamedMethod) {
    addMethod(declaringMethod.declaringClass, declaringMethod.methodName)
}

data class NamedMethod(val declaringClass: Class<Any>, val methodName: String)
