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

class TaskListMainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = ScreenSlidePagerAdapter(requireActivity().application as ToDoListApplication, childFragmentManager)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabRippleColor = null
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

}