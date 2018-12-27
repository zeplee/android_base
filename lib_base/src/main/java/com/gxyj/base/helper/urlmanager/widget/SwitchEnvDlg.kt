package com.gxyj.base.helper.urlmanager.widget

import android.arch.lifecycle.Observer
import android.view.Gravity
import com.gxyj.base.BR
import com.gxyj.base.R
import com.gxyj.base.databinding.DlgSwitchEnvBinding
import com.gxyj.base.helper.urlmanager.widget.vm.SwitchEnvDlgVM
import com.gxyj.base.mvvm.page.BaseAty
import com.gxyj.base.mvvm.widget.CommonDlg


/**
 * @author lizepeng
 * @time 2018/08/24 13：29
 * @desc 切换环境弹窗
 */
class SwitchEnvDlg(val mAty: BaseAty<*, *>) {

    lateinit var mDlg: CommonDlg<DlgSwitchEnvBinding, SwitchEnvDlgVM>

    init {
        initView()
        initData()
    }

    private fun initView() {
        mDlg = CommonDlg.Builder<DlgSwitchEnvBinding, SwitchEnvDlgVM>(mAty)
                .setStyle(R.style.BottomDialogTransparentBgStyle)
                .setViewModel(SwitchEnvDlgVM::class.java)
                .setLayout(R.layout.dlg_switch_env)
                .setLayoutVMId(BR.switchEnvDlgVM)
                .setGravity(Gravity.BOTTOM)
                .setSize(0.0, 1.0)
                .build()
    }

    private fun initData() {
        mDlg.mVM.mLiveIsShowDlg.observe(mAty, Observer { out ->
            out?.let {
                mDlg.showOrDismiss(it)
                if (it) {
                    mDlg.mVM.recoverInputText()
                }
            }
        })
    }
}
