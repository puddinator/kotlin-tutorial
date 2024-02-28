package com.example.tutorial

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tutorial.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      _binding = FragmentFirstBinding.inflate(inflater, container, false)
      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            var usageStatsManager =
        requireContext().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val cal = Calendar.getInstance()
        val currentTime = cal.timeInMillis
        cal.add(Calendar.YEAR, -2) // Set year to beginning of desired period.
        val beginTime = cal.timeInMillis // Get begin time in milliseconds

        val queryUsageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_YEARLY,
            beginTime,
            currentTime
        )
        Log.d("TAG", queryUsageStats.toString());
        for (element in queryUsageStats) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.d("name", element.packageName);
                Log.d("time", element.totalTimeVisible.toString())
            };
        }

        binding.randomButton.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            val showCountTextView = view.findViewById<TextView>(R.id.textview_first)
            val currentCount = showCountTextView.text.toString().toInt()
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentCount)
            findNavController().navigate(action)
        }

//        view.findViewById<Button>(R.id.toast_button).setOnClickListener {
//        }
        binding.toastButton.setOnClickListener {
            // create a Toast with some text, to appear for a short time
            val myToast = Toast.makeText(context, queryUsageStats.toString(), Toast.LENGTH_SHORT)
            // show the Toast
            myToast.show()
        }

        binding.countButton.setOnClickListener {
            countMe(view)
        }
    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun countMe(view: View) {
    // Get the text view
    val showCountTextView = view.findViewById<TextView>(R.id.textview_first)
    // Get the value of the text view.
    val countString = showCountTextView.text.toString()
    // Convert value to a number and increment it
    var count = countString.toInt()
    count++
    // Display the new value in the text view.
    showCountTextView.text = count.toString()
}