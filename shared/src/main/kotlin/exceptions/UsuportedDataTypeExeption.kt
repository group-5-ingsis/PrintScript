package exceptions

class UsuportedDataTypeExeption(val dataType: String) : Exception("Unsuported data type: $dataType")
