package com.example.healthcare.ui.main.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.healthcare.data.remote.dto.QuestionsTitleDto
import com.example.healthcare.databinding.QuestionTitleItemBinding

class QuestionTitleAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<QuestionsTitleDto>() {

        override fun areItemsTheSame(
            oldItem: QuestionsTitleDto,
            newItem: QuestionsTitleDto
        ): Boolean {
           return oldItem.question_title == newItem.question_title
        }

        override fun areContentsTheSame(
            oldItem: QuestionsTitleDto,
            newItem: QuestionsTitleDto
        ): Boolean {
           return  oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return QuestionViewHolder(
            QuestionTitleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is QuestionViewHolder -> {
                holder.bind(differ.currentList[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<QuestionsTitleDto>) {
        differ.submitList(list)
    }

    class QuestionViewHolder
    constructor(
        private val binding: QuestionTitleItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: QuestionsTitleDto, position: Int) = with(binding) {
            questionTitle.text = item.question_title

            root.setOnClickListener {
                interaction?.onItemSelected(position, item)
            }

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: QuestionsTitleDto)

    }
}