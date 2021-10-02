package hu.botagergo.todolist.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import hu.botagergo.todolist.R
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.databinding.FragmentTaskListMainBinding


class TaskListMainFragment : Fragment() {

    private lateinit var binding: FragmentTaskListMainBinding

    private val app: ToDoListApplication by lazy {
        requireActivity().application as ToDoListApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
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
        initViewPager()
        initTabLayout()
    }

    private fun initViewPager() {
        binding.viewPager.adapter = ScreenSlidePagerAdapter(app, childFragmentManager)
        binding.viewPager.currentItem = app.configuration.taskListViews.indexOfFirst {
            it.name == app.configuration.state.selectedTaskListViewName
        }
        binding.viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                app.configuration.state.selectedTaskListViewName = app.configuration.taskListViews[position].name
                super.onPageSelected(position)
            }
        })
    }

    private fun initTabLayout() {
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.tabLayout.tabRippleColor = null
    }

    private inner class ScreenSlidePagerAdapter(val app: ToDoListApplication, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = app.configuration.taskListViews.size

        override fun getItem(position: Int): Fragment {
            return TaskListFragment(app.configuration.taskListViews[position])
        }

        override fun getPageTitle(position: Int): CharSequence {
            return app.configuration.taskListViews[position].name
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_add) {
            findNavController().navigate(R.id.action_taskListFragment_to_addTaskFragment)
            return true
        }
        return false
    }

    override fun onDestroy() {
        binding.viewPager.clearOnPageChangeListeners()
        super.onDestroy()
    }

}