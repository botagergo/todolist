package hu.botagergo.todolist.feature_task.presentation.task_list

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import dagger.hilt.android.AndroidEntryPoint
import hu.botagergo.todolist.*
import hu.botagergo.todolist.databinding.FragmentTaskListMainBinding
import hu.botagergo.todolist.core.log.logd
import hu.botagergo.todolist.core.util.ObservableListChangedCallback
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TaskListMainFragment : Fragment() {

    @Inject lateinit var taskViewRepo: TaskViewRepository

    private lateinit var binding: FragmentTaskListMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        config.activeTaskViews.addOnListChangedCallback(
            object : ObservableListChangedCallback<UUID>() {
                override fun onChanged(sender: ObservableList<UUID>?) {
                    onSelectedTaskViewsChanged(sender)
                }
            })

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logd(this, "onViewCreated")

        if (config.hideViewTabsWhenOneSelected && config.activeTaskViews.size <= 1) {
            binding.tabLayout.visibility = View.GONE
        }

        taskViewRepo.getAll()

        initViewPager()
        initTabLayout()
    }

    private fun initViewPager() {
        binding.viewPager.adapter = ScreenSlidePagerAdapter(childFragmentManager)
        binding.viewPager.offscreenPageLimit = config.activeTaskViews.size
        binding.viewPager.currentItem = config.activeTaskViews.indexOfFirst {
            it == config.state.selectedTaskViewUuid
        }
        binding.viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                config.state.selectedTaskViewUuid = config.activeTaskViews[position]
                super.onPageSelected(position)
            }
        })
    }

    private fun initTabLayout() {
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.tabLayout.tabRippleColor = null
        binding.tabLayout.isLongClickable = false
        binding.tabLayout.setOnLongClickListener { false }
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = config.activeTaskViews.size

        override fun getItem(position: Int): Fragment {
            return TaskListFragment().apply {
                arguments = bundleOf(EXTRA_UUID to config.activeTaskViews[position])
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return taskViewRepo.get(config.activeTaskViews[position]).name
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_add) {
            findNavController().navigate(
                R.id.action_taskListFragment_to_taskFragment,
                bundleOf(EXTRA_IS_EDIT to false)
            )
            return true
        }
        return false
    }

    override fun onDestroy() {
        binding.viewPager.clearOnPageChangeListeners()
        super.onDestroy()
    }

    private fun onSelectedTaskViewsChanged(sender: ObservableList<UUID>?) {
        if (config.hideViewTabsWhenOneSelected) {
            if ((sender?.size ?: 0) > 1) {
                binding.tabLayout.visibility = View.VISIBLE
            } else {
                binding.tabLayout.visibility = View.GONE
            }
        }
        binding.viewPager.adapter?.notifyDataSetChanged()
    }

}