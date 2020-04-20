package com.aaron.grainchaintest.ListScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaron.grainchaintest.R
import com.aaron.grainchaintest.adapters.ListAdapter
import com.aaron.grainchaintest.databinding.ListFragmentBinding
import com.aaron.grainchaintest.detailScreen.DetailScreenFragment
import com.aaron.grainchaintest.models.Route
import kotlinx.android.synthetic.main.list_fragment.view.*

class ListFragment : Fragment() {

    companion object {
        private var INSTANCE: ListFragment? = null

        @JvmStatic
        fun getInstance(): ListFragment {
            return INSTANCE ?: synchronized(this) {
                    INSTANCE ?: ListFragment().also { INSTANCE = it }
                }
        }
    }

    private lateinit var viewModel: ListFragmentViewModelInterface
    private lateinit var binder: ListFragmentBinding
    var recyclerAdapter: ListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binder = ListFragmentBinding.inflate(inflater)
        return binder.root
    }

    private var routeClickListener: (Route) -> Unit = { route: Route ->
        //TODO: open detail view
        Log.e("routeClick",route.alias ?: "alias")
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.content_fragment,DetailScreenFragment(route))
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListFragmentViewModel::class.java)
        binder.routesList.setHasFixedSize(true)
        binder.routesList.layoutManager = LinearLayoutManager(context)
        viewModel.load {
            viewModel.routes?.observe(viewLifecycleOwner, Observer { value ->
                recyclerAdapter = ListAdapter(value,routeClickListener)
                binder.routesList.adapter = recyclerAdapter
                recyclerAdapter?.notifyDataSetChanged()
            })
        }
    }

}
