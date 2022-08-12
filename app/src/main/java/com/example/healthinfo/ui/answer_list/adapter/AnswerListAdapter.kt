package com.example.healthinfo.ui.answer_list.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.healthinfo.data.remote.dto.answer_dto.DataDto
import com.example.healthinfo.databinding.AnswerListItemsBinding

class AnswerListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataDto>() {

        override fun areItemsTheSame(oldItem: DataDto, newItem: DataDto): Boolean {
            TODO("not implemented")
        }

        override fun areContentsTheSame(oldItem: DataDto, newItem: DataDto): Boolean {
            TODO("not implemented")
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return AnswerListViewHolder(
            AnswerListItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AnswerListViewHolder -> {
                holder.bind(differ.currentList[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<DataDto>) {
        differ.submitList(list)
    }

    class AnswerListViewHolder
    constructor(
        private val binding: AnswerListItemsBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataDto, position: Int) = with(binding) {
            binding.apply {
                answerDetail.text = item.answer_text
                doctorName.text  =item.doctor_name
                voteCountValue.text = item.vote_count
//                answerImage.setImageURI(item.answer_image.toUri())
            }
            root.setOnClickListener {
                interaction?.onItemSelected(position, item)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: DataDto)
    }
}