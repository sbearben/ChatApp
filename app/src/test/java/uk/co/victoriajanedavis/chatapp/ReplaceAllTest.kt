package uk.co.victoriajanedavis.chatapp

import org.junit.Before
import org.junit.Test

import org.junit.Assert.assertEquals
import java.util.concurrent.ConcurrentHashMap

class ReplaceAllTest {

    private val cache = ConcurrentHashMap<Int, Model>()

    @Before
    fun setup() {
        cache.clear()
        (0..9).forEach { id -> cache[id] = Model(id, (id % 2) + 10) }  // parentId either 10 or 11
    }

    @Test
    fun testReplaceAll() {
        assert(cache.size == 10)
        replaceAll(11, newList())
        assert(cache.size == 7)
        assert(!cache.containsKey(1))
    }


    private fun replaceAll(parentId: Int?, valuesList: List<Model>) {
        val oldValues = cache.values.toList().filter { value -> value.parentId != parentId }

        cache.clear()
        cache.putAll(oldValues.associateBy { value -> value.id })
        cache.putAll(valuesList.associateBy { value -> value.id })
    }

    private fun newList(): List<Model> {
        return ArrayList<Model>().apply {
            add(Model(12, 11, "World"))
            add(Model(13, 11, "World"))
        }
    }

    class Model(
        val id: Int,
        val parentId: Int,
        val text: String = "Hello"
    )
}
