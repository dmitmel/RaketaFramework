package org.willthisfly.raketaframework.scala

import org.willthisfly.raketaframework.Document

import scala.collection.IndexedSeq
import scala.collection.mutable.{ArrayLike, Builder}

class DocumentExtension(document: Document) extends ArrayLike[Byte, Document] {
    def  +(byte: Byte): Document =
        ++(Array(byte))
    
    def ++(bytes: Byte*): Document =
        ++(bytes.toArray)
    
    def ++(bytes: TraversableOnce[Byte]): Document =
        ++(bytes.toArray)
    
    def ++(document: Document): Document =
        ++(document.getBytes)
    
    def ++(newBytes: Array[Byte]): Document = {
        val bytesCopy = Array[Byte]()
        val bytes = document.getBytes
        System.arraycopy(bytes, 0, bytesCopy, 0, bytes.length)
        
        new Document(bytesCopy ++ newBytes)
    }
    
    def ++=(byte: Byte, bytes: Byte*): Document =
        ++=(Array(byte) ++ bytes)
    
    def ++=(bytes: TraversableOnce[Byte]): Document =
        ++=(bytes.toArray)
    
    def ++=(document: Document): Document =
        ++=(document.getBytes)
    
    def ++=(bytes: Array[Byte]): Document = {
        document.write(bytes)
        document
    }
    
    override def apply(index: Int): Byte =
        document.getBytes()(index)
    
    override def length: Int =
        document.length()
    
    override def seq: IndexedSeq[Byte] =
        document.getBytes.toSeq.asInstanceOf[IndexedSeq[Byte]]
    
    override def update(index: Int, elem: Byte): Unit =
        document.getBytes.update(index, elem)
    
    override protected[this] def newBuilder: Builder[Byte, Document] =
        new DocumentBuilder(new Document)
    
    
    class DocumentBuilder(document: Document) extends Builder[Byte, Document] {
        override def +=(elem: Byte): this.type = {
            document.write(elem)
            this
        }
    
        override def clear(): Unit =
            throw new UnsupportedOperationException("DocumentExtension.DocumentBuilder.clear")
    
        override def result(): Document =
            document
    }
}
