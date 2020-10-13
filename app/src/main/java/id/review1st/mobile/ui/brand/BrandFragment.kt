package id.review1st.mobile.ui.brand

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ahsailabs.alcore.core.BaseRecyclerViewAdapter
import com.ahsailabs.alutils.SwipeRefreshLayoutUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import id.review1st.mobile.Configs
import id.review1st.mobile.MainViewModel
import id.review1st.mobile.R
import id.review1st.mobile.ui.brand.models.Brand
import id.review1st.mobile.ui.brand.models.BrandResponse
import kotlinx.android.synthetic.main.fragment_brand.*

class BrandFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var brandAdapter: BrandAdapter
    private var isReadCache: Boolean = false
    private var swipeRefreshLayoutUtil: SwipeRefreshLayoutUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(activity as AppCompatActivity).get(MainViewModel::class.java)
        brandAdapter = BrandAdapter(mainViewModel.brandList)

        brandAdapter.addOnChildViewClickListener(object : BaseRecyclerViewAdapter.OnChildViewClickListener<Brand>{
            override fun onClick(view: View?, dataModel: Brand?, position: Int) {
                findNavController().navigate(R.id.action_brand_to_detail,
                    Bundle().apply {
                        putString("url",dataModel?.url)
                        putString("title",dataModel?.title)
                    })
            }

            override fun onLongClick(view: View?, dataModel: Brand?, position: Int) {
            }

        })
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(savedInstanceState != null || mainViewModel.brandList.size > 0){
            isReadCache = true
        }

        swipeRefreshLayoutUtil = SwipeRefreshLayoutUtil.init(srlBrand){
            loadData()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvBrand.apply {
            layoutManager = GridLayoutManager(activity,3)
            adapter = brandAdapter
        }
        swipeRefreshLayoutUtil?.refreshNow()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(){
        rvBrand.visibility = View.GONE
        srvBrand.visibility = View.VISIBLE
        srvBrand.showShimmerAdapter()
    }


    private fun hideLoading(){
        srvBrand.hideShimmerAdapter()
        rvBrand.visibility = View.VISIBLE
        srvBrand.visibility = View.GONE
        swipeRefreshLayoutUtil?.refreshDone()
    }

    private fun loadData() {
        if(!isReadCache){
            showLoading()
            AndroidNetworking.get(Configs.BRAND_URL)
                .setPriority(Priority.HIGH)
                .setTag("get-brands")
                .build()
                .getAsObject(BrandResponse::class.java,
                    object : ParsedRequestListener<BrandResponse> {
                        override fun onResponse(response: BrandResponse?) {
                            response?.let { it ->
                                if (it.status) {
                                    response.data?.let {
                                        mainViewModel.brandList.clear()
                                        mainViewModel.brandList.addAll(it)
                                        brandAdapter.notifyDataSetChanged()
                                    }
                                }
                            }
                            hideLoading()
                        }

                        override fun onError(anError: ANError?) {
                            hideLoading()
                        }

                    })
        } else {
            hideLoading()
        }

        isReadCache = false
    }

    override fun onDestroyView() {
        AndroidNetworking.cancel("get-brands")
        super.onDestroyView()
    }
}