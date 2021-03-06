package node.util

import java.net.URLDecoder
import java.util.HashMap
import java.text.ParseException
import java.util.Random
import java.net.URLEncoder

/**
 * Some string utility functions
 */
fun String.extension(): String? {
  val idx = this.lastIndexOf('.')
  return if (idx >= 0) {
    this.substring(idx + 1)
  } else {
    null
  }
}

/**
 * Get a version of this string encoded for use as a component of a URI
 */
fun String.encodeUriComponent(): String {
  return URLEncoder.encode(this, "UTF-8")
          .replace("\\+".toRegex(), "%20")
          .replace("\\%21".toRegex(), "!")
          .replace("\\%27".toRegex(), "'")
          .replace("\\%28".toRegex(), "(")
          .replace("\\%29".toRegex(), ")")
          .replace("\\%7E".toRegex(), "~");
}

/**
 * Get a version of the string suitable for use in an HTML attribute
 */
fun String.toHTMLAttribute(): String {
  return this.replace("\"".toRegex(), "&quot;").replace("'".toRegex(), "&#39;")
}

fun String.decodeUriComponent() = URLDecoder.decode(this, "UTF-8")

fun Array<String>.toMap(vararg keys: String): Map<String, String> {
  val result = HashMap<String, String>()
  var i = 0
  for (key in keys) {
    if (this.size() > i) {
      result[key] = this[i]
    } else {
      throw ParseException("No enough values in string to match keys", 0)
    }
    i++
  }
  return result
}

/**
 * Split a string into a map with provided keys. After splitting a string, each token is
 * added to a map with the provided key of the same index.
 */
fun String.splitToMap(delimiter: String, vararg keys: String): Map<String, String> {
  return this.split(delimiter.toRegex()).toTypedArray().toMap(*keys)
}

// stores symbols used by the random string generator
private val randomSymbols: CharArray = {
  val symbols = CharArray(36)
  for (idx in 0..10 - 1) symbols[idx] = ('0' + idx).toChar()
  for (idx in 10..36 - 1) symbols[idx] = ('a' + idx - 10).toChar()
  symbols
}()

/**
 * Generates random strings of a given length using numbers and lowercase letters.
 */
public class RandomStringGenerator(val length: Int) {
  private val random = Random()

  fun next(): String {
    val buf = CharArray(length)
    for (idx in 0..length-1) buf[idx] = randomSymbols[random.nextInt(randomSymbols.size())]
    return String(buf)
  }
}

/**
 * Get a substring of the string that occurs after the given sequence. If the sequence isn't found,
 * returns an empty string
 */
fun String.after(seq: String): String {
  val index = this.indexOf(seq)
  if (index < 0) {
    return ""
  }
  return this.substring(index + seq.length())
}

/**
 * Get a substring of the string up until the given sequence. If the sequence isn't found,
 * returns teh full string
 */
fun String.until(seq: String): String {
  val index = this.indexOf(seq)
  if (index < 0) {
    return this
  }
  return this.substring(0, index)
}