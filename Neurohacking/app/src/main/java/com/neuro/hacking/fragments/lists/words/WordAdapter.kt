package com.neuro.hacking.fragments.lists.words

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.neuro.hacking.R
import com.neuro.hacking.model.Word
import com.neuro.hacking.databinding.ItemWordBinding

class WordAdapter(private val listener: WordClickListener) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    private var wordsList = emptyList<Word>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = wordsList[position]

        holder.textViewWord.text = word.word
        holder.textViewWord.setOnClickListener {
            listener.onClick(word)
        }
        holder.imageViewMoreButton.setOnClickListener {
            listener.onMoreButtonClick(word, holder.itemView)
        }
    }

    override fun getItemCount(): Int {
        return wordsList.size
    }

    fun setData(newWordsList: List<Word>) {
        val diffUtil = WordsDiffUtil(wordsList, newWordsList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        wordsList = newWordsList
        diffResults.dispatchUpdatesTo(this)
    }

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemWordBinding.bind(itemView)

        var textViewWord = binding.textViewWord
        var imageViewMoreButton = binding.imageViewMoreButton
    }
}