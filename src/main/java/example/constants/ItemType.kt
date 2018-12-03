package example.constants

enum class ItemType(val parameter: String) {
    COMMIT("commit"),
    PULL_REQUEST("pr"),
    ISSUE("issue");

    companion object {
        fun getByName(parameter: String): ItemType? = ItemType.values().find { it.parameter == parameter }
    }
}