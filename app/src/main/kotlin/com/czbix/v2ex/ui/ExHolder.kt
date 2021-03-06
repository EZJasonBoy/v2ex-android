package com.czbix.v2ex.ui

import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import com.airbnb.epoxy.EpoxyHolder
import com.bumptech.glide.Glide
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class ExHolder<T : View> : EpoxyHolder() {
    lateinit var view: T
    val glide by lazy(LazyThreadSafetyMode.NONE) { Glide.with(view) }

    @CallSuper
    override fun bindView(itemView: View) {
        @Suppress("UNCHECKED_CAST")
        view = itemView as T

        onCreate()
    }

    protected open fun onCreate() {
    }

    protected fun <V : View> bind(@IdRes id: Int): ReadOnlyProperty<ExHolder<T>, V> {
        return Lazy { holder: ExHolder<T>, prop ->
            holder.view.findViewById(id) as V?
                    ?: throw IllegalStateException("View ID $id for '${prop.name}' not found.")
        }
    }

    /**
     * Taken from Kotterknife.
     * https://github.com/JakeWharton/kotterknife
     */
    private class Lazy<T : View, V>(
            private val initializer: (ExHolder<T>, KProperty<*>) -> V
    ) : ReadOnlyProperty<ExHolder<T>, V> {
        private object EMPTY

        private var value: Any? = EMPTY

        override fun getValue(thisRef: ExHolder<T>, property: KProperty<*>): V {
            if (value == EMPTY) {
                value = initializer(thisRef, property)
            }
            @Suppress("UNCHECKED_CAST")
            return value as V
        }
    }
}
