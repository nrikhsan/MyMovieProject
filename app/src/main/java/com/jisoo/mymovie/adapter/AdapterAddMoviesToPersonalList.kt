package com.jisoo.mymovie.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jisoo.mymovie.databinding.CardMovieBinding
import com.jisoo.mymovie.response.ResultsItem
import com.jisoo.mymovie.utils.Constants.URL_IMAGE_POSTER
import com.jisoo.mymovie.utils.DateFormat

class AdapterAddMoviesToPersonalList: ListAdapter<ResultsItem, AdapterAddMoviesToPersonalList.ViewHolder>(COMPARATOR_ADAPTER) {

    private lateinit var addTo: AddTo

    fun addTo(addTo: AddTo){
        this.addTo = addTo
    }

    interface AddTo {
        fun addToPersonalList(movieId: String)
    }

    companion object{
        object COMPARATOR_ADAPTER: DiffUtil.ItemCallback<ResultsItem>() {
            override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: CardMovieBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResultsItem) {
            binding.apply {
                tvTitle.text = item.title
                tvDate.text = DateFormat.formatDate(item.releaseDate, "dd MMMM yyyy")
                tvOverview.text = item.overview

                Glide.with(itemView.context)
                    .load(URL_IMAGE_POSTER + item.posterPath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(RoundedCorners(20))
                    .into(imageList)
            }

            itemView.setOnClickListener {
                addTo.addToPersonalList(item.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.bind(item)
        }
    }
}