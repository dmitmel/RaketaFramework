package org.willthisfly.raketaframework

package object routes {
    @inline implicit def methodBasedRouterExtension(methodBasedRouter: MethodBasedRouter): MethodBasedRouterExtension =
        new MethodBasedRouterExtension(methodBasedRouter)
    
    @inline implicit def blockBasedRouterExtension(blockBasedRouter: BlockBasedRouter): BlockBasedRouterExtension =
        new BlockBasedRouterExtension(blockBasedRouter)
}
