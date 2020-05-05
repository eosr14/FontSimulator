package com.eosr14.fontsimulator.ui.main

import android.os.Bundle
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.eosr14.fontsimulator.R
import com.eosr14.fontsimulator.common.base.BaseActivity
import com.eosr14.fontsimulator.common.event.*
import com.eosr14.fontsimulator.databinding.ActivityMainBinding
import com.jakewharton.rxbinding3.widget.changeEvents
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val disposable = CompositeDisposable()

    private val viewModel: MainViewModel by lazy {
        MainViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this@MainActivity,
            R.layout.activity_main
        ).apply {
            bindView()
            viewModel = this@MainActivity.viewModel
            lifecycleOwner = this@MainActivity
        }
    }

    override fun onResume() {
        super.onResume()
        uiEventObserve()
        eventObserve()
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private fun bindView() {
        changeFontFamily(FontType.REGULAR)
    }

    private fun eventObserve() {
        disposable.add(RxEventBus.getSingleEventType(EventBusInterface::class.java)
            .compose(bindToLifecycle())
            .subscribe {
                when (it) {
                    is UpdateFontEvent -> changeFontFamily(it.fontType)
                    is UpdateFontSizeEvent -> changeFontSize(it.fontSize)
                    is UpdateLineSpacingEvent -> changeLineSpacing(it.lineSpacing)
                    is UpdateLetterSpacingEvent -> changeLetterSpacing(it.letterSpacing)
                }
            })
    }

    private fun uiEventObserve() {
        disposable.add(
            et_main_input.textChanges()
                .filter { it.isNotEmpty() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    changeTerm(it)
                }
        )
    }

    private fun changeTerm(term: CharSequence) {
        val threeTerm = "$term\n$term\n$term".trim()
        tv_result_default.text = term
        tv_result_letter_spacing.text = term
        tv_result_default_three.text = threeTerm
        tv_result_line_spacing.text = threeTerm
    }

    private fun changeFontFamily(fontType: FontType) {
        val typeFace = when (fontType) {
            FontType.REGULAR -> ResourcesCompat.getFont(
                this@MainActivity,
                R.font.noto_sans_kr_regular
            )
            FontType.MEDIUM -> ResourcesCompat.getFont(
                this@MainActivity,
                R.font.noto_sans_kr_medium
            )
            FontType.BOLD -> ResourcesCompat.getFont(this@MainActivity, R.font.noto_sans_kr_bold)
        }

        tv_result_default.typeface = typeFace
        tv_result_default_three.typeface = typeFace
        tv_result_letter_spacing.typeface = typeFace
        tv_result_line_spacing.typeface = typeFace
    }

    private fun changeFontSize(fontSize: Int) {
        tv_result_default.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
        tv_result_default_three.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
        tv_result_letter_spacing.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
        tv_result_line_spacing.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
    }

    private fun changeLineSpacing(lineSpacing: Int) {
        tv_result_line_spacing.setLineSpacing(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                lineSpacing.toFloat(),
                resources.displayMetrics
            ), 1.0f
        )
    }

    private fun changeLetterSpacing(letterSpacing: Float) {
        tv_result_letter_spacing.letterSpacing = letterSpacing
    }

}
