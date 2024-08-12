package parser.composite

enum class ResultType {
  ASSIGNATION, // ej: variable = 4;
  DECLARATION, // ej: let variable: Number;
    ASSIGNATION_DECLARATION, // ej: let four : Number = 4; // (all is assignation declaration)
  LITERAL, // ej: let four : Number = 4; // (4 is the literal)
    VALUE, // ej 4, its the value of the literal per se.
    // let em; // VALUE its null
    // let num : Number = 3; // VALUE its 3
    KIND_VARIABLE_DECLARATION, // ej: const, let
  IDENTIFIER,
    // ej: let num : Number = 4; // (the id is num)
  DATA_TYPE, //

}