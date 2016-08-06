package org.willthisfly.raketaframework.scala.routes

import org.willthisfly.raketaframework.routes.Router

class RouterExtension(router: Router) {
    def ++=(other: Router) =
        router.addAll(other)
}
