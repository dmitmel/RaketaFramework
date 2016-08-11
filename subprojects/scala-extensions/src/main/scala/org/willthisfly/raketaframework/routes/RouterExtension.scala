package org.willthisfly.raketaframework.routes

class RouterExtension(router: Router) {
    def ++=(other: Router) =
        router.addAll(other)
}
