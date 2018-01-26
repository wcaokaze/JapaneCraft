package com.wcaokaze.japanecraft

class Dictionary(map: Map<String, String>) {
  private val trie = map.toTrie()

  operator fun invoke(rawString: String): String {
    fun loop(prevNode: Trie<String>, index: Int): String? {
      if (index > rawString.lastIndex) return null
      val node = prevNode[rawString[index]] ?: return null

      return if (node.value == null) {
        loop(node, index + 1)
      } else {
        loop(node, index + 1) ?: node.value + rawString.substring(index + 1)
      }
    }

    return loop(trie, 0) ?: rawString
  }
}
