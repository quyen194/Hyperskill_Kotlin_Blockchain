package blockchain

import java.security.MessageDigest

fun applySha256(input: String): String {
    return try {
        val digest = MessageDigest.getInstance("SHA-256")
        /* Applies sha256 to our input */
        val hash = digest.digest(input.toByteArray(charset("UTF-8")))
        val hexString = StringBuilder()
        for (elem in hash) {
            val hex = Integer.toHexString(0xff and elem.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        hexString.toString()
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}

class BlockChain {
    val blocks = mutableListOf<Block>()

    class Block(val prevBlockHash: String,
                val id: Int,
                val timeStamp: Long = System.currentTimeMillis()) {
        val hash: String = applySha256("$prevBlockHash$id$timeStamp")

        override fun toString(): String {
            return """
                Block:
                Id: $id
                Timestamp: $timeStamp
                Hash of the previous block:
                $prevBlockHash
                Hash of the block:
                $hash

            """.trimIndent()
        }
    }

    fun add() {
        val prevBlockHash = if (blocks.isEmpty()) "0" else blocks.last().hash
        val id = blocks.size + 1
        blocks.add(Block(prevBlockHash, id))
    }

    fun validate(): Boolean {
        // first block
        var block = blocks.first()
        var hash = applySha256("${block.prevBlockHash}${block.id}${block.timeStamp}")
        if (hash != block.hash) {
            return false
        }
        for (i in 1..blocks.lastIndex) {
            val prevHash = hash
            block = blocks[i]
            hash = applySha256("${block.prevBlockHash}${block.id}${block.timeStamp}")
            if (hash != block.hash) {
                return false
            }
        }
        return true
    }
}

fun main() {
    val blockChain = BlockChain()
    repeat(5) {
        blockChain.add()
    }

    for (block in blockChain.blocks) {
        println(block)
    }
}