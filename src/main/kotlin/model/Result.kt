package ege.bot.model

data class Result(
    val subject: String,
    val testResult: Boolean?,
    val pointsResult: Int?,
    var studentId: Long?
) {
    override fun toString(): String {
        return "$subject : ${ getResultText() }"
    }

    fun getResultText(): String {
        testResult?.also { return if (it) "Зачет" else "Незачет" }
        return pointsResult!!.toString()
    }
}
