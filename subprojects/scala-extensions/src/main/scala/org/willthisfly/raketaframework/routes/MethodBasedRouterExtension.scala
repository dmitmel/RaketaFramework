package org.willthisfly.raketaframework.routes

class MethodBasedRouterExtension(methodBasedRouter: MethodBasedRouter) extends RouterExtension(methodBasedRouter) {
    def +=(declaringClass: Class[Any], handlerMethodName: String): MethodBasedRouter =
        methodBasedRouter.addMethod(declaringClass, handlerMethodName)
}
