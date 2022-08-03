package com.zeoharlem.append.xtremecardz.sealed

sealed class TimerEvent{
    object START : TimerEvent()
    object STOP : TimerEvent()
}
