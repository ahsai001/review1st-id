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
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import id.review1st.mobile.Configs
import id.review1st.mobile.R
import id.review1st.mobile.ui.brand.models.Brand
import id.review1st.mobile.ui.brand.models.BrandResponse
import kotlinx.android.synthetic.main.fragment_brand.*

class BrandFragment : Fragment() {
    private lateinit var dashboardViewModel: BrandViewModel
    private var brandAdapter: BrandAdapter? = null
    private var brandList: ArrayList<Brand> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        brandAdapter = BrandAdapter(brandList)
        brandAdapter?.addOnChildViewClickListener(object : BaseRecyclerViewAdapter.OnChildViewClickListener<Brand>{
            override fun onClick(view: View?, dataModel: Brand?, position: Int) {
                //WebViewActivity.start(activity,"https://www.review1st.id",dataModel?.url,dataModel?.title,"default message",android.R.color.black,"brand", false)

                findNavController().navigate(R.id.action_brand_to_detail,Bundle().apply { putString("url",dataModel?.url) })
            }

            override fun onLongClick(view: View?, dataModel: Brand?, position: Int) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(this).get(BrandViewModel::class.java)
        return inflater.inflate(R.layout.fragment_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvBrand.apply {
            layoutManager = GridLayoutManager(activity,3)
            adapter = brandAdapter
        }


        loadData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData() {
        AndroidNetworking.get(Configs.BRAND_URL)
            .setPriority(Priority.HIGH)
            .setTag("get-brands")
            .build()
            .getAsObject(BrandResponse::class.java,object:ParsedRequestListener<BrandResponse>{
                override fun onResponse(response: BrandResponse?) {
                    response?.let{ it ->
                        if(it.status) {
                            response.data?.let {
                                brandList.clear()
                                brandList.addAll(it)
                                brandAdapter?.notifyDataSetChanged()
                            }
                        }
                    }
                }

                override fun onError(anError: ANError?) {

                }

            })
    }

    override fun onDestroyView() {
        AndroidNetworking.cancel("get-brands")
        super.onDestroyView()
    }
}