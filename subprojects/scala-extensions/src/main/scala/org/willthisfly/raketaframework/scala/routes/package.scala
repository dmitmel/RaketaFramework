package org.willthisfly.raketaframework.scala

import org.willthisfly.raketaframework.routes.{BlockBasedRouter, MethodBasedRouter}

package object routes {
    @inline implicit def methodBasedRouterExtension(methodBasedRouter: MethodBasedRouter): MethodBasedRouterExtension =
        new MethodBasedRouterExtension(methodBasedRouter)
    
    @inline implicit def blockBasedRouterExtension(blockBasedRouter: BlockBasedRouter): BlockBasedRouterExtension =
        new BlockBasedRouterExtension(blockBasedRouter)
}
