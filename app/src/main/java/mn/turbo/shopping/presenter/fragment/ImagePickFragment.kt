package mn.turbo.shopping.presenter.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import mn.turbo.shopping.R
import mn.turbo.shopping.presenter.viewmodel.ShoppingViewModel

@AndroidEntryPoint
class ImagePickFragment : Fragment(R.layout.fragment_image_pick) {

    private lateinit var viewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
    }

}