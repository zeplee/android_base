package com.aac.base.helper.urlmanager

import android.support.annotation.RawRes
import com.blankj.utilcode.util.ResourceUtils
import com.aac.base.helper.GsonUtils
import com.aac.base.helper.urlmanager.data.UrlConfigBean
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec
import kotlin.reflect.full.memberProperties

/**
 * Created by lizepeng on 2018/08/24
 * Description：
 */

class UrlManager {
    companion object {
        @RawRes
        var mResId: Int = 0
        var mDecodeKey: String = ""
        lateinit var urlConfigBean: UrlConfigBean
        lateinit var curEnvBean: UrlConfigBean.AppEnvBean//当前的环境域名对象
        lateinit var jsonEncrypt: String
        lateinit var json: String
        /**
         * 初始化
         */
        @JvmStatic
        fun init(@RawRes resId: Int, dcodeKey: String) {
            mResId = resId
            mDecodeKey = dcodeKey
            readUrls()
            curEnvBean = getEnvBean(urlConfigBean.curEnvName)
            saveUrls(curEnvBean)
        }

        /**
         * 点击切换环境
         */
        @JvmStatic
        fun init(targetEnvName: String) {
            readUrls()
            curEnvBean = getEnvBean(targetEnvName)
            urlConfigBean.curEnvName = targetEnvName
            saveUrls(curEnvBean)
        }

        /**
         * 手动输入自定义环境
         *
         * @param name
         */
        @JvmStatic
        fun init(targetEnvBean: UrlConfigBean.AppEnvBean) {
            readUrls()
            curEnvBean = targetEnvBean
            targetEnvBean.javaClass.kotlin.memberProperties.forEach {
                urlConfigBean.curEnvName += "${it.name}:${it.get(targetEnvBean)}"
            }
            saveUrls(targetEnvBean)
        }

        fun readUrls() {
            jsonEncrypt = ResourceUtils.readRaw2String(mResId, "utf-8")
            json = UrlEncryptUtil.decode(jsonEncrypt, mDecodeKey)
            urlConfigBean = GsonUtils.fromJson(json, UrlConfigBean::class.java)!!
        }

        fun saveUrls(targetEnvBean: UrlConfigBean.AppEnvBean) {
            urlConfigBean.appUrlMaps.forEach outside@{ outter ->
                targetEnvBean.javaClass.kotlin.memberProperties.forEach inside@{ inner ->
                    if (inner.name == outter.value.host) {
                        outter.value.url = inner.get(targetEnvBean).toString() + outter.value.url
                        return@outside
                    }
                }
            }
            urlConfigBean.appEnvMaps.forEach { (envName, envBean) ->
                envBean.isChecked = (urlConfigBean.curEnvName == envName)
            }
        }

        @JvmStatic
        fun getEnvBean(name: String): UrlConfigBean.AppEnvBean = urlConfigBean.appEnvMaps[name]!!

        @JvmStatic
        fun getUrlBean(name: String): UrlConfigBean.UrlBean = urlConfigBean.appUrlMaps[name]!!

        /**
         * 所有环境名称集合
         * 161，167等
         */
        @JvmStatic
        fun getAppEnvNames() = ArrayList(urlConfigBean.appEnvMaps.keys)

        /**
         * 所有环境域名类型名
         * appServer,webServer等
         */
        @JvmStatic
        fun getAppEnvServers() = ArrayList(UrlConfigBean.AppEnvBean::class.memberProperties)
    }
}

class UrlEncryptUtil {
    companion object {
        /**
         * 解密
         */
        fun decode(content: String, keyBytes: String): String {
            //解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
            val b = decrypt(parseHexStr2Byte(content), keyBytes)
            return String(b!!)
        }

        /**
         * 将16进制转换为二进制
         *
         * @param hexStr
         * @return
         */
        fun parseHexStr2Byte(hexStr: String): ByteArray? {
            if (hexStr.length < 1)
                return null
            val result = ByteArray(hexStr.length / 2)
            for (i in 0 until hexStr.length / 2) {
                val high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16)
                val low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                        16)
                result[i] = (high * 16 + low).toByte()
            }
            return result
        }

        fun decrypt(content: ByteArray?, password: String): ByteArray? {
            try {
                val keyStr = getKey(password)
                val key = SecretKeySpec(keyStr!!, "AES")
                val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")//algorithmStr
                cipher.init(Cipher.DECRYPT_MODE, key)
                return cipher.doFinal(content!!)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            } catch (e: IllegalBlockSizeException) {
                e.printStackTrace()
            } catch (e: BadPaddingException) {
                e.printStackTrace()
            }
            return null
        }

        fun getKey(password: String?): ByteArray? {
            val rByte: ByteArray
            if (password != null) {
                rByte = password.toByteArray()
            } else {
                rByte = ByteArray(24)
            }
            return rByte
        }
    }
}
