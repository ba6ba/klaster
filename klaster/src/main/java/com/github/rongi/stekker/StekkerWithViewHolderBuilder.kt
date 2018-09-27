package com.github.rongi.stekker

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class StekkerWithViewHolderBuilder<VH: RecyclerView.ViewHolder> {

  private var viewHolderBuilder: ((parent: ViewGroup, viewType: Int) -> VH)? = null

  private var binder: ((viewHolder: VH, position: Int) -> Unit)? = null

  private var getItemCount: (() -> Int)? = null

  private var getItemId: ((position: Int) -> Long)? = null

  fun viewHolder(createViewHolder: (parent: ViewGroup) -> VH): StekkerWithViewHolderBuilder<VH> {
    viewHolderBuilder = { parent: ViewGroup, _: Int ->
      createViewHolder(parent)
    }

    return this
  }

  fun itemCount(getItemsCount: (() -> Int)): StekkerWithViewHolderBuilder<VH> {
    this.getItemCount = getItemsCount
    return this
  }

  fun itemCount(count: Int): StekkerWithViewHolderBuilder<VH> {
    this.getItemCount = { count }
    return this
  }

  fun bind(binder: VH.(position: Int) -> Unit): StekkerWithViewHolderBuilder<VH> {
    this.binder = binder
    return this
  }

  fun getItemId(getItemId: (position: Int) -> Long): StekkerWithViewHolderBuilder<VH> {
    this.getItemId = getItemId
    return this
  }

  fun build(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
    if (getItemCount == null) throw StekkerException("Items count function must be provided")
    if (viewHolderBuilder == null) throw StekkerException("View holder builder must be provided")
    if (binder == null) throw StekkerException("bind() must be set")

    @Suppress("UNCHECKED_CAST")
    return StekkerAdapter(
      _getItemCount = getItemCount!!,
      createViewHolder = viewHolderBuilder!!,
      bindViewHolder = binder!!,
      _getItemId = getItemId
    ) as RecyclerView.Adapter<RecyclerView.ViewHolder>
  }

}