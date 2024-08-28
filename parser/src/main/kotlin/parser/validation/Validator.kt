package parser.validation


interface Validator<T> {
    fun validate(node: T, varTable: List<Node.Declaration>): ValidationResult
}
