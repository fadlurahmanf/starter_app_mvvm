package com.fadlurahmanf.starterappmvvm.core.unknown.domain.common

import androidx.viewbinding.ViewBinding

abstract class BaseAfterLoginActivity<VB : ViewBinding>(inflate: InflateActivity<VB>) :
    BaseActivity<VB>(inflate) {
}