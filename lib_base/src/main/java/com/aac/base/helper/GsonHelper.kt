//package com.aac.base.helper
//
//import com.blankj.utilcode.util.LogUtils
//import com.aac.base.data.Result
//import com.aac.base.data.ResultList
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import java.lang.reflect.ParameterizedType
//import java.lang.reflect.Type
//
//object GsonHelper {
//    val TAG = "GsonUtil"
//
//    /**
//     * https://stackoverflow.com/questions/41554706/gson-illegal-type-variable-reference
//     * 返回加过数据部位为Bean调用
//     *
//     * @param json  json String
//     * @param clazz data字段类型 class
//     * @param <T>   data字段类型 type
//     * @return 结果 data字段为bean
//    </T> */
//    fun <T> JsonToResult(json: String, clazz: Class<T>): Result<T>? {
//        val gson = Gson()
//        val objectType = type(Result::class.java, clazz)
//        var result: Result<T>? = null
//        try {
//            result = gson.fromJson(json, objectType)
//        } catch (e: Exception) {
//            LogUtils.e(TAG, e)
//            return result
//        }
//
//        return result
//    }
//
//    /**
//     * 返回结果数据部位为List调用
//     *
//     * @param json  json String
//     * @param clazz data字段List bean类型 class
//     * @param <T>   data字段List bean类型 type
//     * @return 结果 data字段为List
//    </T> */
//    fun <T> JsonToResultList(json: String, clazz: Class<*>): ResultList<T>? {
//        val gson = Gson()
//        val objectType = type(ResultList::class.java, clazz)
//        var result: ResultList<T>? = null
//        try {
//            result = gson.fromJson(json, objectType)
//        } catch (e: Exception) {
//            LogUtils.e(TAG, e)
//            return result
//        }
//
//        return result
//    }
//
//
//    private fun type(raw: Class<*>, vararg args: Type): ParameterizedType {
//        return object : ParameterizedType {
//            override fun getActualTypeArguments(): Array<out Type> {
//                return args
//            }
//
//            override fun getRawType(): Type {
//                return raw
//            }
//
//
//            override fun getOwnerType(): Type? {
//                return null
//            }
//        }
//    }
//
//
//    /**
//     * javabean转成Json串
//     *
//     * @param bean 要转换成json串的对象
//     * @return json串
//     */
//    fun ObjectToJson(bean: Any): String {
//        var json = ""
//        try {
//            json = Gson().toJson(bean)
//        } catch (e: Exception) {
//            LogUtils.e(TAG, e)
//            return json
//        }
//
//        return json
//    }
//
//    /**
//     * Json串转成JavaBean
//     *
//     * @param json     json串
//     * @param classOfT 泛型T的class
//     * @param <T>      泛型T
//     * @return T Bean
//    </T> */
//    fun <T> JsonToBean(json: String, classOfT: Class<T>): T {
//        if (classOfT == String::class.java) {
//            return json as T
//        }
//        var bean: T = Any() as T
//        try {
//            bean = Gson().fromJson(json, classOfT)
//        } catch (e: Exception) {
//            LogUtils.e(TAG, e)
//            return bean
//        }
//        return bean
//    }
//
//    /**
//     * json转换成List对象
//     *
//     * @param json     json串
//     * @param classOfT 泛型T class
//     * @param <F>      泛型T
//     * @return List<T> 对象
//    </T></F> */
//    fun <F> JsonToList(json: String): List<F>? {
//        var list: List<F>? = null
//        try {
//            list = Gson().fromJson(json, object : TypeToken<List<F>>() {
//
//            }.getType())
//        } catch (e: Exception) {
//            LogUtils.e(TAG, e)
//            return list
//        }
//
//        return list
//    }
//}
