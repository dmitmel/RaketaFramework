package org.willthisfly.raketaframework

import org.willthisfly.raketaframework.RequestParamsExtension._
import scala.collection.Map

class RequestParamsExtension(requestParams: RequestParams) {
    def apply[T](key: Key): T =
        requestParams.get(key)
    
    def apply[T](key: Key, orElse: T): T =
        requestParams.getOrElse(key, orElse)
    
    def  +(entry: Entry): RequestParams =
        ++(Map(entry))
    
    def  +(entry: java.util.Map.Entry[Key, Value]): RequestParams =
        ++((entry.getKey, entry.getValue))
    
    def ++(entries: TraversableOnce[Entry]): RequestParams =
        ++(entries.toMap)
    
    def ++(entry: Entry, entries: Entry*): RequestParams =
        ++(Map(entries : _*) + entry)
    
    def ++(map: collection.Map[Key, Value]): RequestParams = {
        val result = new RequestParams(requestParams)
        for ((key, value) <- map)
            // Java generics support only objects, so casting value to AnyRef (it's for objects)
            result.put(key, value.asInstanceOf[AnyRef])
        result
    }
    
    def  +=(entry: Entry): RequestParams =
        ++=(Map(entry))
    
    def ++=(entries: TraversableOnce[Entry]): RequestParams =
        ++=(entries.toMap)
    
    def ++=(entry: Entry, entries: Entry*): RequestParams =
        ++=(Map(entries : _*) + entry)
    
    def ++=(map: collection.Map[Key, Value]): RequestParams = {
        for ((key, value) <- map)
            // Java generics support only objects, so casting value to AnyRef (it's for objects)
            requestParams.put(key, value.asInstanceOf[AnyRef])
        requestParams
    }
    
    def foreach[U](f: ((Key, Value)) => U): Unit = {
        val iterator = requestParams.keySet().iterator()
        while (iterator.hasNext) {
            val key = iterator.next()
            f(key, requestParams.get(key))
        }
    }
    
    def getOptional[T](key: Key): Option[T] =
        if (requestParams.containsKey(key)) Some(requestParams.get(key)) else None
    
    def -(key: Key): RequestParams = {
        val result = new RequestParams(requestParams)
        result.remove(key)
        result
    }
    
    def -=(key: Key): RequestParams = {
        requestParams.remove(key)
        requestParams
    }
    
    def scalaIterator: Iterator[(Key, Value)] =
        new RequestParamsScalaIterator(requestParams)
    
    class RequestParamsScalaIterator(requestParams: RequestParams) extends Iterator[(Key, Value)] {
        private val javaIterator = requestParams.entrySet.iterator
        
        override def hasNext: Boolean = javaIterator.hasNext
        
        override def next(): (Key, Value) = {
            val entry = javaIterator.next()
            (entry.getKey, entry.getValue)
        }
    }
}

object RequestParamsExtension {
    type Key = String
    type Value = Any
    type Entry = (Key, Value)
}
