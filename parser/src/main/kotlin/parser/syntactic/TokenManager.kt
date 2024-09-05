package parser.syntactic

import exceptions.BadSyntacticException
import lexer.Lexer
import position.Position
import token.Token
import java.util.*

/**
 * TokenManager se encarga de gestionar los tokens provenientes de un StreamingLexer.
 * Proporciona métodos para avanzar, mirar, y validar tokens, y lanzar excepciones
 * cuando no se cumplen las expectativas de valor o tipo de token.
 */
object TokenManager {

    // Instancia de StreamingLexer que procesa el texto de entrada
    private var lexerStreamer = Lexer("")

    /**
     * Inicializa el texto que será procesado por el lexer.
     * @param text Texto a analizar en busca de tokens.
     */
    fun setTextToLex(text: String) {
        lexerStreamer = Lexer(text)
    }

    /**
     * Verifica si el valor del siguiente token en el lexer coincide con el valor dado.
     * @param value Valor esperado del siguiente token.
     * @return `true` si el valor del siguiente token coincide, `false` en caso contrario.
     */
    fun isValue(value: String): Boolean {
        return lexerStreamer.peek().value == value
    }

    /**
     * Consume y devuelve el siguiente token del lexer.
     * @return El token consumido.
     * @throws NoSuchElementException si no hay más tokens disponibles.
     */
    fun advance(): Token {
        return lexerStreamer.next()
    }

    /**
     * Mira el siguiente token sin consumirlo.
     * @return El siguiente token en la secuencia.
     * @throws NoSuchElementException si no hay más tokens disponibles.
     */
    fun peek(): Token {
        return lexerStreamer.peek()
    }

    /**
     * Verifica si hay más tokens por procesar.
     * @return `true` si hay más tokens, `false` si no.
     */
    fun hasMoreTokens(): Boolean {
        return lexerStreamer.hasNext()
    }

    /**
     * Verifica si el valor del siguiente token coincide con el valor especificado.
     * @param value Valor esperado del siguiente token.
     * @return `true` si el valor coincide, `false` en caso contrario.
     */
    fun checkNextTokenValue(value: String): Boolean {
        val token = lexerStreamer.peek()
        return token.value == value
    }

    /**
     * Verifica si el tipo del siguiente token coincide con el tipo especificado.
     * @param type Tipo esperado del siguiente token.
     * @return `true` si el tipo coincide, `false` en caso contrario.
     */
    fun checkNextTokenType(type: String): Boolean {
        val peek = lexerStreamer.peek()
        return peek.type == type
    }

    /**
     * Obtiene la posición del siguiente token.
     * @return La posición del siguiente token.
     * @throws NoSuchElementException si no hay más tokens disponibles.
     */
    fun getPosition(): Position {
        return lexerStreamer.peek().position ?: throw NoSuchElementException("No tokens to get position from.")
    }

    /**
     * Consume el siguiente token si su valor coincide con el valor especificado.
     * @param value Valor esperado del siguiente token.
     * @return El token consumido si el valor coincide.
     * @throws BadSyntacticException si el valor no coincide.
     */
    fun consumeTokenValue(value: String): Token {
        if (peek().value == value) return advance()
        throw BadSyntacticException("Expected value: '$value' but found: '${peek().value}'.")
    }

    /**
     * Consume el siguiente token si su tipo coincide con el tipo especificado.
     * @param type Tipo esperado del siguiente token.
     * @return El token consumido si el tipo coincide.
     * @throws BadSyntacticException si el tipo no coincide.
     */
    fun consumeTokenType(type: String): Token {
        val b = peek().type == type
        if (b) return advance()
        throw BadSyntacticException("Expected token type: '$type' but found: '${peek().type}'.")
    }

    /**
     * Verifica si no se ha llegado al final de la secuencia de tokens.
     * @return `true` si no es el final, `false` si lo es.
     */
    fun isNotTheEndOfTokens(): Boolean {
        return lexerStreamer.hasNext()
    }

    /**
     * Verifica si el tipo del siguiente token pertenece a una lista de tipos especificados.
     * @param types Lista de tipos permitidos.
     * @return `true` si el tipo del siguiente token está en la lista, `false` en caso contrario.
     */
    fun checkTokensAreFromSomeTypes(types: List<String>): Boolean {
        val nextTokenType = peek().type
        return types.contains(nextTokenType)
    }
}
