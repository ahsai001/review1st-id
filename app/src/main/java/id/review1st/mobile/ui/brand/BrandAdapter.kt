package id.review1st.mobile.ui.brand

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahsailabs.alcore.core.BaseRecyclerViewAdapter
import com.squareup.picasso.Picasso
import id.review1st.mobile.R
import id.review1st.mobile.ui.brand.models.Brand
import kotlinx.android.synthetic.main.brand_itemview.view.*


/**
 * Created by ahmad s on 07/10/20.
 */
class BrandAdapter(brandList: ArrayList<Brand>): BaseRecyclerViewAdapter<Brand, BrandAdapter.BrandViewHolder>(brandList) {

    class BrandViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val ivImage = itemView.ivImage
        private val tvTitle = itemView.tvTitle
        private val tvTotalDevice = itemView.tvTotalDevice

        fun bindViewHolder(brand: Brand){
            tvTitle.text = brand.title
            tvTotalDevice.text = "(${brand.totalDevice})"
            Picasso.get().load(brand.icon).into(ivImage)
        }
    }

    override fun doSettingViewWithModel(
        holder: BrandViewHolder?,
        dataModel: Brand?,
        position: Int
    ) {
        holder?.bindViewHolder(dataModel!!)
        setViewClickable(holder, holder?.itemView)
    }

    override fun getLayout(): Int {
        return R.layout.brand_itemview
    }

    override fun getViewHolder(rootView: View?): BrandViewHolder {
        return BrandViewHolder(rootView!!)
    }
}