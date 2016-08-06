package org.willthisfly.raketaframework.scala.routes

import java.util.regex.Pattern

import org.willthisfly.raketaframework.routes.{BlockBasedRouter, BlockRoute}

class BlockBasedRouterExtension(blockBasedRouter: BlockBasedRouter) extends RouterExtension(blockBasedRouter) {
    ///////////////////////// ADDING ROUTES WITH REGEXPS /////////////////////////
    def get[T](regexp: Pattern)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter = 
        blockBasedRouter.get(regexp, handlerBlock)
    
    def post[T](regexp: Pattern)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter =
        blockBasedRouter.post(regexp, handlerBlock)
    
    def put[T](regexp: Pattern)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter =
        blockBasedRouter.put(regexp, handlerBlock)
    
    def patch[T](regexp: Pattern)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter = 
        blockBasedRouter.patch(regexp, handlerBlock)
    
    def update[T](regexp: Pattern)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter =
        blockBasedRouter.update(regexp, handlerBlock)
    
    def delete[T](regexp: Pattern)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter =
        blockBasedRouter.delete(regexp, handlerBlock)
    
    ///////////////////////// ADDING ROUTES WITH GLOBS /////////////////////////
    def get[T](glob: String)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter =
        blockBasedRouter.get(glob, handlerBlock)
    
    def post[T](glob: String)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter =
        blockBasedRouter.post(glob, handlerBlock)
    
    def put[T](glob: String)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter =
        blockBasedRouter.put(glob, handlerBlock)
    
    def patch[T](glob: String)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter =
        blockBasedRouter.patch(glob, handlerBlock)
    
    def update[T](glob: String)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter =
        blockBasedRouter.update(glob, handlerBlock)
    
    def delete[T](glob: String)(handlerBlock: BlockRoute.Block[T]): BlockBasedRouter =
        blockBasedRouter.delete(glob, handlerBlock)
}
