package org.willthisfly

import org.willthisfly.raketaframework.RequestParamsExtension.{Key => RequestParamsKey, Value => RequestParamsValue}

import _root_.scala.collection.JavaConverters

package object raketaframework {
    @inline implicit def documentExtension(document: Document): DocumentExtension =
        new DocumentExtension(document)
    
    @inline implicit def requestParamsExtension(requestParams: RequestParams): RequestParamsExtension =
        new RequestParamsExtension(requestParams)
    
    @inline implicit def mapFromRequestParams(requestParams: RequestParams): Map[RequestParamsKey, RequestParamsValue] =
        JavaConverters.mapAsScalaMap(requestParams).toMap
}
