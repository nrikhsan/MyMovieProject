package com.jisoo.mymovie.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jisoo.mymovie.activity.DetailPersonalListActivity
import com.jisoo.mymovie.databinding.CardPersonalListBinding
import com.jisoo.mymovie.response.PersonalListItem

class AdapterPersonalList: ListAdapter<PersonalListItem, AdapterPersonalList.ViewHolder>(COMPARATOR_ADAPTER) {

    private lateinit var onClick: OnClicked

    fun onClick( onClick: OnClicked){
        this.onClick = onClick
    }

    interface OnClicked {
        fun deleteList(listId: String)
    }

    companion object{
        object COMPARATOR_ADAPTER: DiffUtil.ItemCallback<PersonalListItem>() {
            override fun areItemsTheSame(oldItem: PersonalListItem, newItem: PersonalListItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PersonalListItem, newItem: PersonalListItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: CardPersonalListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PersonalListItem) {
            binding.apply {

                tvTitle.text = item.name
                tvListType.text = "${item.itemCount} ${item.listType}"

            }

            itemView.setOnLongClickListener {
                onClick.deleteList(item.id)
                return@setOnLongClickListener true
            }
            //on click item

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailPersonalListActivity::class.java)
                intent.putExtra(DetailPersonalListActivity.LIST_ID, item.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardPersonalListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.bind(item)
        }
    }
}