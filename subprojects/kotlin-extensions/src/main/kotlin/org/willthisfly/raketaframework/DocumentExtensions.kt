package org.willthisfly.raketaframework

operator fun Document.plus(byte: Byte): Document {
    val result = Document()
    result.writeAll(this)
    result += byte
    return result
}

operator fun Document.plus(bytes: Iterable<Byte>): Document {
    val result = Document()
    result.writeAll(this)
    result += bytes
    return result
}

operator fun Document.plus(document: Document): Document {
    val result = Document()
    result.writeAll(this)
    result += document
    return result
}

operator fun Document.plusAssign(byte: Byte) = write(byte)
operator fun Document.plusAssign(bytes: Iterable<Byte>) = bytes.forEach { b -> write(b) }
operator fun Document.plusAssign(document: Document) = writeAll(document)

