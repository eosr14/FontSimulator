package com.eosr14.fontsimulator.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eosr14.fontsimulator.common.base.BaseViewModel
import com.eosr14.fontsimulator.common.event.*

enum class FontType {
    REGULAR,
    MEDIUM,
    BOLD
}

class MainViewModel : BaseViewModel() {

    private val _currentFont = MutableLiveData(FontType.REGULAR)
    val currentFont: LiveData<FontType> = _currentFont

    private val _currentFontSize = MutableLiveData(12)
    val currentFontSize: LiveData<Int> = _currentFontSize

    private val _currentLetterSpacing = MutableLiveData(0f)
    val currentLetterSpacing: LiveData<Float> = _currentLetterSpacing

    private val _currentLineSpacing = MutableLiveData(0)
    val currentLineSpacing: LiveData<Int> = _currentLineSpacing

    fun onFontRadioButtonClick(fontType: FontType) {
        _currentFont.value = fontType
        RxEventBus.sendEvent(UpdateFontEvent(fontType))
    }

    fun changeFontSize(fontSize: Int) {
        _currentFontSize.value = fontSize
        RxEventBus.sendEvent(UpdateFontSizeEvent(fontSize))
    }


    fun changeLineSpacing(lineSpacing: Int) {
        _currentLineSpacing.value = lineSpacing
        RxEventBus.sendEvent(UpdateLineSpacingEvent(lineSpacing))
    }


    fun changeLetterSpacing(letterSpacing: Int) {
        _currentLetterSpacing.value = getConvertedValue(letterSpacing)
        RxEventBus.sendEvent(UpdateLetterSpacingEvent(getConvertedValue(letterSpacing)))
    }

    private fun getConvertedValue(integer: Int): Float {
        return integer.toFloat() / 100
    }

}