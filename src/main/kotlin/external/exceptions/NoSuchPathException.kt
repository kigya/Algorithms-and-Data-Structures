package external.exceptions

class NoSuchPathException(s: String?) : Exception(s) {
    constructor() : this(null)
}