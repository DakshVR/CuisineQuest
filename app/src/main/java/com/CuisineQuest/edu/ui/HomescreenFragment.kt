
package com.CuisineQuest.edu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CuisineQuest.edu.R

class HomescreenFragment : Fragment(R.layout.homescreen) {

    private lateinit var searchResultsListRV: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.homescreen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBoxET: EditText = view.findViewById(R.id.search_view)
        val searchBtn: Button = view.findViewById(R.id.btn_search)

//        searchBtn.setOnClickListener{
//            val query = searchBoxET.text.toString()
//            val directions = HomescreenFragmentDirections.navigateToSearchFragment()
//            findNavController().navigate(directions)
//        }
    }

}