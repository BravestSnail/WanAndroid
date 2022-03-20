package com.breavestsnail.wanandroid

import java.util.regex.Matcher
import java.util.regex.Pattern


fun String.removeHtml(): String {
    val REGEX_HTML = "<[^>]+>"
    val p_html: Pattern = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE)
    val m_html: Matcher = p_html.matcher(this)
    return m_html.replaceAll("")
}