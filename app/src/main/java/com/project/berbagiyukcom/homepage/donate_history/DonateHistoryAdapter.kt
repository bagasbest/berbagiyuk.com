package com.project.berbagiyukcom.homepage.donate_history

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ItemProgrssOrderBinding
import java.text.DecimalFormat
import java.text.NumberFormat

class DonateHistoryAdapter : RecyclerView.Adapter<DonateHistoryAdapter.ViewHolder>() {

    private val donateHistoryList = ArrayList<DonateHistoryModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<DonateHistoryModel>) {
        donateHistoryList.clear()
        donateHistoryList.addAll(items)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemProgrssOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: DonateHistoryModel) {
            val formatter: NumberFormat = DecimalFormat("#,###")

            with(binding) {
                username.text = "Username: ${model.username}"
                date.text = "Username: ${model.date}"
                nominal.text = "Nominal: Rp ${formatter.format(model.nominal)}"
                status.text = model.status

                when (model.status) {
                    "Menunggu" -> {
                        bgStatus.backgroundTintList =
                            ContextCompat.getColorStateList(itemView.context, R.color.orange)
                    }
                    "Sukses" -> {
                        bgStatus.backgroundTintList =
                            ContextCompat.getColorStateList(itemView.context, R.color.green_dark)
                    }
                    else -> {
                        bgStatus.backgroundTintList = ContextCompat.getColorStateList(itemView.context, android.R.color.holo_red_dark)
                    }
                }

                cv.setOnClickListener {
                    val intent = Intent(itemView.context, DonateHistoryDetailActivity::class.java)
                    intent.putExtra(DonateHistoryDetailActivity.EXTRA_DONATE, model)
                    itemView.context.startActivity(intent)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemProgrssOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(donateHistoryList[position])
    }

    override fun getItemCount(): Int = donateHistoryList.size
}