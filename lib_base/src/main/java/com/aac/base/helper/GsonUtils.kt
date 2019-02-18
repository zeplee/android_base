package com.aac.base.helper

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.Reader
import java.lang.reflect.Type


/**
 * <pre>
 * author: Blankj
 * blog  : https://github.com/Blankj/AndroidUtilCode/blob/master/subutil/src/main/java/com/blankj/subutil/util/GsonUtils.java
 * time  : 2018/04/05
 * desc  : utils about gson
</pre> *
 */
class GsonUtils private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {

        private val GSON = createGson(true)

        private val GSON_NO_NULLS = createGson(false)
        /**
         * Create a pre-configured [Gson] instance.
         *
         * @param serializeNulls determines if nulls will be serialized.
         * @return [Gson] instance.
         */

        private fun createGson(serializeNulls: Boolean): Gson {
            val builder = GsonBuilder()
            if (serializeNulls) builder.serializeNulls()
            return builder.create()
        }


        /**
         * Gets pre-configured [Gson] instance.
         *
         * @param serializeNulls determines if nulls will be serialized.
         * @return [Gson] instance.
         */
        @JvmStatic
        fun getGson(serializeNulls: Boolean): Gson {
            return if (serializeNulls) GSON_NO_NULLS else GSON
        }

        /**
         * Serializes an object into json.
         *
         * @param object       the object to serialize.
         * @param includeNulls determines if nulls will be included.
         * @return object serialized into json.
         */
        @JvmStatic
        fun toJson(any: Any, includeNulls: Boolean = true): String {
            return if (includeNulls) GSON.toJson(any) else GSON_NO_NULLS.toJson(any)
        }


        /**
         * Converts [String] to given type.
         *
         * @param json the json to convert.
         * @param type type type json will be converted to.
         * @return instance of type
         */
        @JvmStatic
        fun <T> fromJson(json: String, type: Class<T>): T? {
            try {
                return GSON.fromJson(json, type)
            } catch (e: Exception) {
                LogUtils.e(e)
                return null
            }
        }

//        /**
//         * Converts [String] to given type.
//         *
//         * @param json the json to convert.
//         * @param type type type json will be converted to.
//         * @return instance of type
//         */
//        @JvmStatic
//        fun <T> fromJsonType(json: String, type: Type): T? {
//            try {
//                return GSON.fromJson(json, type)
//            } catch (e: Exception) {
//                LogUtils.e(e)
//                return null
//            }
//        }
//
//        fun type(raw: Class<*>, vararg args: Type): ParameterizedType {
//            return object : ParameterizedType {
//                override fun getActualTypeArguments(): Array<out Type> {
//                    return args
//                }
//
//                override fun getRawType(): Type {
//                    return raw
//                }
//
//
//                override fun getOwnerType(): Type? {
//                    return null
//                }
//            }
//        }

        /**
         * Converts [Reader] to given type.
         *
         * @param reader the reader to convert.
         * @param type   type type json will be converted to.
         * @return instance of type
         */
        @JvmStatic
        fun <T> fromJson(reader: Reader, type: Class<T>): T {
            return GSON.fromJson(reader, type)
        }

        /**
         * Converts [Reader] to given type.
         *
         * @param reader the reader to convert.
         * @param type   type type json will be converted to.
         * @return instance of type
         */
        @JvmStatic
        fun <T> fromJson(reader: Reader, type: Type): T {
            return GSON.fromJson(reader, type)
        }

        //    inline fun <reified T> Gson.fromJson(json: String) = fromJson(json, T::class.java)
//val fromJson = Gson().fromJson<String>("")
    }
}
