package com.fadlurahmanf.starterappmvvm.base

data class BaseViewState<T> (
    var data:T ?= null,
    var error:String ?= null,
    var state:STATE ?= null
)

enum class STATE{
    IDLE, LOADING, SUCCESS, FAILED
}