package com.example.healthinfo.ui.main.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.healthinfo.data.remote.dto.question_title_dto.QuestionTitleDataDto
import com.example.healthinfo.databinding.QuestionTitleItemBinding

class QuestionTitleAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<QuestionTitleDataDto>() {

        override fun areItemsTheSame(
            oldItem: QuestionTitleDataDto,
            newItem: QuestionTitleDataDto
        ): Boolean {
           return oldItem.question_title == newItem.question_title
        }

        override fun areContentsTheSame(
            oldItem: QuestionTitleDataDto,
            newItem: QuestionTitleDataDto
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

    fun submitList(list: List<QuestionTitleDataDto>) {
        differ.submitList(list)
    }

    class QuestionViewHolder
    constructor(
        private val binding: QuestionTitleItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: QuestionTitleDataDto, position: Int) = with(binding) {
            questionTitle.text = item.question_title
            answerBtn.text = item.answers_count.toString()
            viewBtn.text = item.view_count.toString()
            root.setOnClickListener {
                interaction?.onItemSelected(position, item)
            }

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: QuestionTitleDataDto)

    }
}