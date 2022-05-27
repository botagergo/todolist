package hu.botagergo.todolist.feature_task_view.data.filter.predicate

enum class PredicateKind(val value: String) {

    EXISTS("Exists"),
    EQUAL("="),
    LESS("<"),
    LESS_EQUAL("<="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    IN("In"),
    CONTAINS("Contains"),
    BETWEEN("Between"),
    LIKE("Like"),
    REGEX("Regex");

    override fun toString(): String = value

}
