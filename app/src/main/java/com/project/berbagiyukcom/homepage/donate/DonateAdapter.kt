package com.project.berbagiyukcom.homepage.donate

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.berbagiyukcom.databinding.ItemDonateBinding
import com.project.berbagiyukcom.databinding.ItemDonateMenuBinding
import java.text.DecimalFormat
import java.text.NumberFormat

class DonateAdapter(private val option: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val donationList = ArrayList<DonateModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<DonateModel>) {
        donationList.clear()
        donationList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (option == "home") {
            val binding =
                ItemDonateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        } else {
            val binding =
                ItemDonateMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder2(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (option) {
            "home" -> {
                (holder as ViewHolder).bind(donationList[position])

            }
            "donate" -> {
                (holder as ViewHolder2).bind(donationList[position])
            }
        }
    }

    override fun getItemCount(): Int = donationList.size

    inner class ViewHolder(private val binding: ItemDonateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: DonateModel) {
            with(binding) {
                val currentTimeInMillis = System.currentTimeMillis()
                val formatter: NumberFormat = DecimalFormat("#,###")

                Glide.with(itemView.context)
                    .load(model.image)
                    .into(binding.roundedImageView)

                textView12.text = model.name
                donateValue.text = "Rp ${formatter.format(model.donateValue)}"


                val donateValuePercentage = model.nominal?.toDouble()
                    ?.let { model.donateValue?.toDouble()?.div(it) }

                if (donateValuePercentage != null) {
                    if (donateValuePercentage < 0.92) {
                        (binding.view9.layoutParams as ConstraintLayout.LayoutParams)
                            .matchConstraintPercentWidth = donateValuePercentage.toFloat()
                        binding.view9.requestLayout()
                    } else {
                        (binding.view9.layoutParams as ConstraintLayout.LayoutParams)
                            .matchConstraintPercentWidth = 0.92F
                        binding.view9.requestLayout()
                    }
                }

                if (currentTimeInMillis < model.dateEnd!!) {
                    val donationTimeLeftInMillis = model.dateEnd!! - currentTimeInMillis
                    val donationTimeLeft = (donationTimeLeftInMillis / (1000 * 60 * 60 * 24)) % 24

                    if (donationTimeLeft == 0L) {
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

    inner class ViewHolder2(private val binding: ItemDonateMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: DonateModel) {
            with(binding) {
                val currentTimeInMillis = System.currentTimeMillis()
                val formatter: NumberFormat = DecimalFormat("#,###")

                Glide.with(itemView.context)
                    .load(model.image)
                    .into(binding.roundedImageView)

                textView12.text = model.name
                donateValue.text = "Rp ${formatter.format(model.donateValue)}"


                val donateValuePercentage = model.nominal?.toDouble()
                    ?.let { model.donateValue?.toDouble()?.div(it) }

                if (donateValuePercentage != null) {
                    if (donateValuePercentage < 0.92) {
                        (binding.view9.layoutParams as ConstraintLayout.LayoutParams)
                            .matchConstraintPercentWidth = donateValuePercentage.toFloat()
                        binding.view9.requestLayout()
                    } else {
                        (binding.view9.layoutParams as ConstraintLayout.LayoutParams)
                            .matchConstraintPercentWidth = 0.92F
                        binding.view9.requestLayout()
                    }
                }

                if (currentTimeInMillis < model.dateEnd!!) {
                    val donationTimeLeftInMillis = model.dateEnd!! - currentTimeInMillis
                    val donationTimeLeft = (donationTimeLeftInMillis / (1000 * 60 * 60 * 24)) % 24

                    if (donationTimeLeft == 0L) {
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