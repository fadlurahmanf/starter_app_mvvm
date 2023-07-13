package com.fadlurahmanf.starterappmvvm.core.unknown.data.dto.event

class RxEvent {
    data class ChangeLanguageEvent(
        val languageCode: String
    )

    class ForceLogoutEvent

    class RefreshToken
    class ResetTimerAfterLogin
}