package com.addcn.flutterperferences

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class JsonUtil private constructor() {
    companion object {
        private val newGson: Gson by lazy {
            GsonBuilder()
                // 这里对获取到的json字符串和解析的对象进行类型检验，避免类型不一致而导致类型转换失败
                .registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
                .registerTypeAdapter(Int::class.java, IntTypeAdapter())
                .registerTypeAdapter(Long::class.java, LongTypeAdapter())
                .registerTypeAdapter(Float::class.java, FloatTypeAdapter())
                .registerTypeAdapter(Double::class.java, DoubleTypeAdapter())
                .registerTypeAdapter(String::class.java, StringTypeAdapter())
                .registerTypeAdapterFactory(ArrayAdapterFactory())
                .registerTypeAdapterFactory(ObjectAdapterFactory())
                .create()
        }

        fun <T> fromJson(json: String?, clazz: Class<T>): T {
            return newGson.fromJson(json, clazz)
        }

        fun <T> toJson(obj: T): String {
            return newGson.toJson(obj)
        }
    }
}

class BooleanTypeAdapter : TypeAdapter<Boolean>() {
    override fun read(`in`: JsonReader?): Boolean {
        when (`in`?.peek()) {
            JsonToken.NULL -> {
                `in`.nextNull()
                return false
            }
            JsonToken.BOOLEAN -> {
                if (`in`.nextBoolean()) return true
                return false
            }
            JsonToken.STRING, JsonToken.NUMBER -> {
                var tmp = `in`.nextString()
                tmp = correctNumber(tmp)
                return when (tmp) {
                    "1" -> true
                    else -> try {
                        tmp.toBoolean()
                    } catch (e: NumberFormatException) {
                        false
                    }
                }
            }
            JsonToken.BEGIN_OBJECT -> {
                skipValue(`in`)
                `in`.endObject()
                return false
            }
            JsonToken.BEGIN_ARRAY -> {
                // 如果读取到的是数组则做处理
                skipValue(`in`)
                `in`.endArray()
                return false
            }
            else -> `in`?.skipValue()
        }
        return false
    }

    override fun write(out: JsonWriter?, value: Boolean?) {
        if (value == null) out?.value("")
        else out?.value(value)
    }
}

class IntTypeAdapter : TypeAdapter<Int>() {
    override fun read(`in`: JsonReader?): Int {
        when (`in`?.peek()) {
            JsonToken.NULL -> {
                `in`.nextNull()
                return 0
            }
            JsonToken.BOOLEAN -> {
                if (`in`.nextBoolean()) return 1
                return 0
            }
            JsonToken.STRING, JsonToken.NUMBER -> {
                var tmp = `in`.nextString()
                tmp = correctNumber(tmp)
                return try {
                    tmp.toInt()
                } catch (e: NumberFormatException) {
                    0
                }
            }
            JsonToken.BEGIN_OBJECT -> {
                skipValue(`in`)
                `in`.endObject()
                return 0
            }
            JsonToken.BEGIN_ARRAY -> {
                skipValue(`in`)
                `in`.endArray()
                return 0
            }
            else -> `in`?.skipValue()
        }
        return 0
    }

    override fun write(out: JsonWriter?, value: Int?) {
        if (value == null) out?.value("")
        else out?.value(value)
    }
}

class LongTypeAdapter : TypeAdapter<Long>() {
    override fun read(`in`: JsonReader?): Long {
        when (`in`?.peek()) {
            JsonToken.NULL -> {
                `in`.nextNull()
                return 0L
            }
            JsonToken.BOOLEAN -> {
                if (`in`.nextBoolean()) return 1L
                return 0L
            }
            JsonToken.STRING, JsonToken.NUMBER -> {
                var tmp = `in`.nextString()
                tmp = correctNumber(tmp)
                return try {
                    tmp.toLong()
                } catch (e: NumberFormatException) {
                    0L
                }
            }
            JsonToken.BEGIN_OBJECT -> {
                skipValue(`in`)
                `in`.endObject()
                return 0L
            }
            JsonToken.BEGIN_ARRAY -> {
                skipValue(`in`)
                `in`.endArray()
                return 0L
            }
            else -> `in`?.skipValue()
        }
        return 0L
    }

    override fun write(out: JsonWriter?, value: Long?) {
        if (value == null) out?.value("")
        else out?.value(value)
    }
}

class FloatTypeAdapter : TypeAdapter<Float>() {
    override fun read(`in`: JsonReader?): Float {
        when (`in`?.peek()) {
            JsonToken.NULL -> {
                `in`.nextNull()
                return 0f
            }
            JsonToken.BOOLEAN -> {
                if (`in`.nextBoolean()) return 1f
                return 0f
            }
            JsonToken.STRING, JsonToken.NUMBER -> {
                var tmp = `in`.nextString()
                if (tmp.contains('L'))
                    tmp = tmp.replace('L', ' ').trim()
                return try {
                    tmp.toFloat()
                } catch (e: NumberFormatException) {
                    0f
                }
            }
            JsonToken.BEGIN_OBJECT -> {
                skipValue(`in`)
                `in`.endObject()
                return 0f
            }
            JsonToken.BEGIN_ARRAY -> {
                skipValue(`in`)
                `in`.endArray()
                return 0f
            }
            else -> `in`?.skipValue()
        }
        return 0f
    }

    override fun write(out: JsonWriter?, value: Float?) {
        if (value == null) out?.value("")
        else out?.value(value)
    }
}

class DoubleTypeAdapter : TypeAdapter<Double>() {
    override fun read(`in`: JsonReader?): Double {
        when (`in`?.peek()) {
            JsonToken.NULL -> {
                `in`.nextNull()
                return 0.0
            }
            JsonToken.BOOLEAN -> {
                if (`in`.nextBoolean()) return 1.0
                return 0.0
            }
            JsonToken.STRING, JsonToken.NUMBER -> {
                var tmp = `in`.nextString()
                if (tmp.contains('L'))
                    tmp = tmp.replace('L', ' ').trim()
                return try {
                    tmp.toDouble()
                } catch (e: NumberFormatException) {
                    0.0
                }
            }
            JsonToken.BEGIN_OBJECT -> {
                skipValue(`in`)
                `in`.endObject()
                return 0.0
            }
            JsonToken.BEGIN_ARRAY -> {
                skipValue(`in`)
                `in`.endArray()
                return 0.0
            }
            else -> `in`?.skipValue()
        }
        return 0.0
    }

    override fun write(out: JsonWriter?, value: Double?) {
        if (value == null) out?.value("")
        else out?.value(value)
    }
}

/**
 * String类型数据校验
 */
class StringTypeAdapter : TypeAdapter<String>() {
    override fun read(`in`: JsonReader?): String {
        when (`in`?.peek()) {
            JsonToken.NULL -> {
                `in`.nextNull()
                return ""
            }
            JsonToken.BOOLEAN -> {
                if (`in`.nextBoolean()) return "true"
                return "false"
            }
            JsonToken.STRING, JsonToken.NUMBER -> {
                return `in`.nextString()
            }
            JsonToken.BEGIN_OBJECT -> {
                skipValue(`in`)
                `in`.endObject()
                return ""
            }
            JsonToken.BEGIN_ARRAY -> {
                skipValue(`in`)
                `in`.endArray()
                return ""
            }
            else -> `in`?.skipValue()
        }
        return ""
    }

    override fun write(out: JsonWriter?, value: String?) {
        if (value == null) out?.value("")
        else out?.value(value)
    }
}

/**
 * 对象类型数据校验
 */
@Suppress("UNCHECKED_CAST")
class ObjectAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val delegate = gson.getDelegateAdapter(this, type)
        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter?, value: T) {
                delegate.write(out, value)
            }

            override fun read(`in`: JsonReader?): T {
                when (type.rawType) {
                    // javaObjectType为包装类，java为基本数据类型
                    Byte::class.javaObjectType,
                    Char::class.javaObjectType,
                    Short::class.javaObjectType,
                    Boolean::class.javaObjectType,
                    Int::class.javaObjectType,
                    Long::class.javaObjectType,
                    Float::class.javaObjectType,
                    Double::class.javaObjectType,
                    Byte::class.java,
                    Char::class.java,
                    Short::class.java,
                    Boolean::class.java,
                    Int::class.java,
                    Long::class.java,
                    Float::class.java,
                    Double::class.java,
                    String::class.java,
                    List::class.java,
                    ArrayList::class.java -> return delegate.read(`in`)
                }
                return when (`in`?.peek()) {
                    JsonToken.BEGIN_OBJECT -> {
                        delegate.read(`in`)
                    }
                    JsonToken.BEGIN_ARRAY -> {
                        while (`in`.hasNext()) {
                            when (`in`.peek()) {
                                JsonToken.BEGIN_ARRAY -> `in`.beginArray()
                                JsonToken.BEGIN_OBJECT -> delegate.read(`in`)
                                JsonToken.NAME -> `in`.nextName()
                                JsonToken.STRING -> `in`.nextString()
                                JsonToken.NUMBER -> `in`.nextInt()
                                JsonToken.BOOLEAN -> `in`.nextBoolean()
                                JsonToken.NULL -> `in`.nextNull()
                                else -> `in`.nextNull()
                            }
                        }
                        `in`.endArray()
                        type.rawType.newInstance() as T
                    }
                    else -> {
                        `in`?.skipValue()
                        type.rawType.newInstance() as T
                    }
                }
            }
        }
    }
}

/**
 * 数组类型数据校验
 */
@Suppress("UNCHECKED_CAST")
class ArrayAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val delegate = gson.getDelegateAdapter(this, type)
        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter?, value: T) {
                delegate.write(out, value)
            }

            override fun read(`in`: JsonReader?): T {
                when (type.rawType) {
                    // javaObjectType为包装类，java为基本数据类型
                    Byte::class.javaObjectType,
                    Char::class.javaObjectType,
                    Short::class.javaObjectType,
                    Boolean::class.javaObjectType,
                    Int::class.javaObjectType,
                    Long::class.javaObjectType,
                    Float::class.javaObjectType,
                    Double::class.javaObjectType,
                    Byte::class.java,
                    Char::class.java,
                    Short::class.java,
                    Boolean::class.java,
                    Int::class.java,
                    Long::class.java,
                    Float::class.java,
                    Double::class.java,
                    String::class.java -> return delegate.read(`in`)
                    // List数组类型
                    List::class.java,
                    ArrayList::class.java -> return when (`in`?.peek()) {
                        JsonToken.BOOLEAN,
                        JsonToken.NUMBER,
                        JsonToken.STRING -> {
                            `in`.skipValue()
                            ArrayList<T>() as T
                        }
                        JsonToken.BEGIN_OBJECT -> {
                            while (`in`.hasNext()) {
                                when (`in`.peek()) {
                                    JsonToken.BEGIN_ARRAY -> delegate.read(`in`)
                                    JsonToken.BEGIN_OBJECT -> `in`.beginObject()
                                    JsonToken.NAME -> `in`.nextName()
                                    JsonToken.STRING -> `in`.nextString()
                                    JsonToken.NUMBER -> `in`.nextInt()
                                    JsonToken.BOOLEAN -> `in`.nextBoolean()
                                    JsonToken.NULL -> `in`.nextNull()
                                    else -> `in`.nextNull()
                                }
                            }
                            `in`.endObject()
                            ArrayList<T>() as T
                        }
                        JsonToken.BEGIN_ARRAY -> {
                            return delegate.read(`in`)
                        }
                        else -> {
                            return delegate.read(`in`)
                        }
                    }
                    else -> return delegate.read(`in`)
                }
            }
        }
    }
}

/**
 * 解析并跳过Value
 */
fun skipValue(jsonReader: JsonReader) {
    while (jsonReader.hasNext()) {
        when (jsonReader.peek()) {
            JsonToken.BEGIN_OBJECT -> jsonReader.beginObject()
            JsonToken.BEGIN_ARRAY -> jsonReader.beginArray()
            JsonToken.NAME -> jsonReader.nextName()
            JsonToken.STRING -> jsonReader.nextString()
            JsonToken.NUMBER -> jsonReader.nextInt()
            JsonToken.BOOLEAN -> jsonReader.nextBoolean()
            JsonToken.NULL -> jsonReader.nextNull()
            else -> jsonReader.nextNull()
        }
    }
}

fun correctNumber(s: String): String {
    var tmp = s
    if (tmp.contains('.'))
        tmp = tmp.substring(0, tmp.indexOf('.'))
    if (tmp.contains('f'))
        tmp = tmp.replace('f', ' ').trim()
    if (tmp.contains('L'))
        tmp = tmp.replace('L', ' ').trim()
    return tmp
}
