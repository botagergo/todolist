package hu.botagergo.todolist.core.adapter

import android.view.View
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemRemovableBinding
import hu.botagergo.todolist.core.util.NamedByResource

abstract class RemovableItem(var data: NamedByResource) : BindableItem<ItemRemovableBinding>() {

    abstract fun onRemoveClicked(view: View)

    override fun bind(viewBinding: ItemRemovableBinding, position: Int) {
        viewBinding.data = this
    }

    override fun getLayout() = R.layout.item_removable

}