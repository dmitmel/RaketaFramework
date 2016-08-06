package org.willthisfly.raketaframework.scala.util

import org.willthisfly.raketaframework.util.{JSONJavaSerializer, Strings}

import scala.collection.JavaConverters

object JSONScalaSerializer {
    private val DEFAULT_INDENT = " " * 4
    
    
    def toMultilineJSON[K, V](map: Map[K, V]): String =
        toJSONWithIndent(DEFAULT_INDENT, map)
    
    def toSingleLineJSON[K, V](map: Map[K, V]): String =
        toJSONWithIndent(Strings.EMPTY_STRING, map)
    
    def toJSONWithIndent[K, V](indentBase: String, scalaMap: Map[K, V]): String =
        JSONJavaSerializer.toJSONWithIndent(indentBase,
            JavaConverters.mapAsJavaMapConverter(scalaMap).asJava)
}
