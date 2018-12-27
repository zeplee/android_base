package com.gxyj.base.helper.urlmanager.widget.vm

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gxyj.base.R
import com.gxyj.base.SPKEY_DEVICETOKEN
import com.gxyj.base.databinding.ItemSwitchEnvBinding
import com.gxyj.base.helper.urlmanager.UrlManager
import com.gxyj.base.helper.urlmanager.data.UrlConfigBean
import com.gxyj.base.mvvm.BaseVM
import com.gxyj.base.mvvm.adapter.SingleTypeRvAdapter
import com.gxyj.base.utils.CookieUtils


/**
 * @author lizepeng
 * @time 2018/08/24 13：29
 * @desc 切换环境弹窗
 */
class SwitchEnvDlgVM : BaseVM() {

    //如何是dialog时，可以用这个控制显示隐藏
    var mLiveIsShowDlg = MutableLiveData<Boolean>()

    lateinit var mLiveIsNotifyWeb: MutableLiveData<Boolean>
    var mLiveInputText = MutableLiveData<String>()
    private var mLiveIsOkVisible = MutableLiveData<Boolean>()
    var mAdapter = object : SingleTypeRvAdapter<ItemSwitchEnvBinding>(UrlManager.getAppEnvNames(), R.layout.item_switch_env) {
        override fun onItemBind(holderBinding: ItemSwitchEnvBinding, position: Int) {
            super.onItemBind(holderBinding, position)
            val envName = UrlManager.getAppEnvNames()[position]
            holderBinding.envName.text = envName
            holderBinding.envName.isChecked = UrlManager.urlConfigBean.appEnvMaps[envName]?.isChecked!!
            holderBinding.root.setOnClickListener {
                if (UrlManager.urlConfigBean.curEnvName == envName) return@setOnClickListener
                UrlManager.init(envName)
                mLiveInputText.postValue(UrlManager.getEnvBean(UrlManager.urlConfigBean.curEnvName).appServer)
                mLiveIsNotifyWeb.postValue(true)
                mLiveIsShowDlg.postValue(false)
                notifyDataSetChanged()
                CookieUtils.removeToken()
//                mBinding.executePendingBindings()
                ToastUtils.showLong("切换成功")
            }
        }
    }

    init {
        mLiveInputText.value = UrlManager.curEnvBean.appServer
    }

    override fun afterInitData() {

    }


    fun getDeviceToken() = "${DeviceUtils.getModel()}(${DeviceUtils.getSDKVersionName()}) " +
            "设备ID:${SPUtils.getInstance().getString(SPKEY_DEVICETOKEN, "")} "

    fun getVersion() = "Version：${AppUtils.getAppVersionCode()} v${AppUtils.getAppVersionName()} "

    fun getInputHint() = "输入IP 例: 10.1.102.161:8680"

    fun getIsOkVisible(): MutableLiveData<Boolean> {
        mLiveIsOkVisible.postValue(!TextUtils.isEmpty(mLiveInputText.value))
        return mLiveIsOkVisible
    }

    fun okOnclickListener() {
        if (!mLiveInputText.value?.contains(":")!! || mLiveInputText.value?.endsWith(",")!!) {
            ToastUtils.showLong("输入格式不对")
            return
        }
        UrlManager.init(UrlConfigBean.AppEnvBean(mLiveInputText.value!!))
        mAdapter.notifyDataSetChanged()
        mLiveIsNotifyWeb.postValue(true)
        mLiveIsShowDlg.postValue(false)
        ToastUtils.showLong("切换成功")
    }

    /**
     * 重新打开dialog时将手动误操作的inputText复原
     */
    fun recoverInputText() {
        UrlManager.urlConfigBean.appEnvMaps.forEach { (_, envBean) ->
            if (envBean.isChecked) {
                mLiveInputText.postValue(envBean.appServer)
                return
            }
        }
    }
}
