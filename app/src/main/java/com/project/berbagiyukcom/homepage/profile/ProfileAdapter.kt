package com.project.berbagiyukcom.homepage.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.berbagiyukcom.databinding.ItemCurrentProjectBinding
import com.project.berbagiyukcom.homepage.donate.DonateDetailActivity
import com.project.berbagiyukcom.homepage.donate.DonateModel
import java.text.DecimalFormat
import java.text.NumberFormat

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    private val donationList = ArrayList<DonateModel>()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<DonateModel>) {
        donationList.clear()
        donationList.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCurrentProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(donationList[position])
    }

    override fun getItemCount(): Int = donationList.size
    inner class ViewHolder(private val binding: ItemCurrentProjectBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: DonateModel) {
            with(binding) {
                val currentTimeInMillis = System.currentTimeMillis()
                val formatter: NumberFormat = DecimalFormat("#,###")

                Glide.with(itemView.context)
                    .load(model.image)
                    .into(roundedImageView)

                textView12.text = model.name
                donateValue.text = "Rp ${formatter.format(model.donateValue)}"


                val donateValuePercentage = model.nominal?.toDouble()
                    ?.let { model.donateValue?.toDouble()?.div(it) }

                if (donateValuePercentage != null) {
                    if(donateValuePercentage < 0.95) {
                        (binding.view9.layoutParams as ConstraintLayout.LayoutParams)
                            .matchConstraintPercentWidth = donateValuePercentage.toFloat()
                        binding.view9.requestLayout()
                    } else {
                        (binding.view9.layoutParams as ConstraintLayout.LayoutParams)
                            .matchConstraintPercentWidth = 0.95F
                        binding.view9.requestLayout()
                    }
                }

                if(currentTimeInMillis < model.dateEnd!!) {
                    val donationTimeLeftInMillis = model.dateEnd!! - currentTimeInMillis
                    val donationTimeLeft = (donationTimeLeftInMillis / (1000*60*60*24)) % 24

                    if(donationTimeLeft == 0L) {
                        donateDayLeft.text = "Hari Terakhir Donasi"
                    } else {
                        donateDayLeft.text = "$donationTimeLeft Hari Lagi"
                    }

                } else {
                    donateDayLeft.text = "Waktu Habis"
                }

                view7.setOnClickListener {
                    val intent = Intent(itemView.context, DonateDetailActivity::class.java)
                    intent.putExtra(DonateDetailActivity.EXTRA_DONATE, model)
                    itemView.context.startActivity(intent)
                }
            }

        }
    }
}