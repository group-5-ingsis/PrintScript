package parser.composite

interface Node {
    fun addChild(node: Node)
    fun removeChild(node: Node) 
    fun solve()
}