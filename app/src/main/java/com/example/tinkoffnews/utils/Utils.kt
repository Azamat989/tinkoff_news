@file:Suppress("UNCHECKED_CAST")

package com.example.tinkoffnews.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance

inline fun <reified TViewModel, TFragment> TFragment.viewModel(
    tag: String? = null
): Lazy<TViewModel>
        where TViewModel : ViewModel,
              TFragment : KodeinAware,
              TFragment : Fragment {

    return lazy {

        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>) =
                kodein.direct.instance<TViewModel>(tag) as T
        })
            .get(TViewModel::class.java)
    }
}

inline fun <reified TViewModel, reified TArgument, TFragment> TFragment.viewModel(
    tag: String? = null,
    crossinline argFactory: () -> TArgument
): Lazy<TViewModel>
        where TViewModel : ViewModel,
              TFragment : KodeinAware,
              TFragment : Fragment {

    return lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(aClass: Class<T>) =
                    kodein.direct.instance<TArgument, TViewModel>(tag, argFactory()) as T
            })
            .get(TViewModel::class.java)
    }
}