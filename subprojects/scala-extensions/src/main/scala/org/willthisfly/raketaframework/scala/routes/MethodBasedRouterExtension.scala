package org.willthisfly.raketaframework.scala.routes

import org.willthisfly.raketaframework.routes.{MethodBasedRouter}

class MethodBasedRouterExtension(methodBasedRouter: MethodBasedRouter) extends RouterExtension(methodBasedRouter) {
    def +=(declaringClass: Class[Any], handlerMethodName: String): MethodBasedRouter =
        methodBasedRouter.addMethod(declaringClass, handlerMethodName)
}
