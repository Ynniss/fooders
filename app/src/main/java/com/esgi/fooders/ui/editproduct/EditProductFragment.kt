package com.esgi.fooders.ui.editproduct

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.esgi.fooders.databinding.FragmentEditProductBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody

@AndroidEntryPoint
class EditProductFragment : Fragment() {
    private var _binding: FragmentEditProductBinding? = null
    private val binding get() = _binding!!
    val args: EditProductFragmentArgs by navArgs()

    private val editProductViewModel: EditProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product

        Log.d("PRODUCT", product.toString())
        binding.apply {

            btnModify.setOnClickListener {
                val body = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("code", product.data.code)
                    .addFormDataPart("product_name", inputProductName.text.toString())
                    .addFormDataPart("packaging", inputPackaging.text.toString())
                    .addFormDataPart("ingredients_text", inputIngredientList.text.toString())
                    .build()

                editProductViewModel.modifyProductInformations(body)

            }

            inputIngredientList.text =
                Editable.Factory.getInstance().newEditable(product.data.ingredients_text ?: "")

            inputProductName.text =
                Editable.Factory.getInstance().newEditable(product.data.product_name ?: "")

            inputPackaging.text =
                Editable.Factory.getInstance().newEditable(product.data.packaging ?: "")
        }

        editProductViewModel.productModificationEvent.observe(
            viewLifecycleOwner,
            { productModificationEvent ->
                if (productModificationEvent == "STATUS OK") {
                    Snackbar.make(
                        binding.root,
                        "Product Informations modified.",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    if (args.type == "auto") {
                        //requireActivity().supportFragmentManager.popBackStack()
                        requireActivity().onBackPressed()
                    } else {
                        findNavController().navigate(
                            EditProductFragmentDirections.actionEditProductFragmentToManualScanFragment(
                                product.data.code
                            )
                        )
                    }

                } else if (productModificationEvent == "STATUS NOT OK") {
                    Snackbar.make(
                        binding.root,
                        "An error occured. Please retry.",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}