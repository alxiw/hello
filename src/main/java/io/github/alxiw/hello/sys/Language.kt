package io.github.alxiw.hello.sys

enum class Language(val code: String) {
    EN("en"),
    UK("en-GB"),
    US("en-US"),
    RU("ru"),
    DE("de"),
    FR("fr"),
    ES("es"),
    IT("it"),
    PT("pt"),
    NL("nl"),
    SV("sv"),
    PL("pl"),
    CS("cs"),
    AR("ar"),
    TR("tr"),
    ZH("zh"),
    JA("ja");

    override fun toString(): String {
        return code
    }

    companion object {
        fun fromCode(code: String): Language? {
            return Language.entries.filter {
                it.code != "en-GB" && it.code != "en-US"
            }.find {
                it.code.equals(code, ignoreCase = true)
            }
        }
    }
}
