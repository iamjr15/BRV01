package com.riteshakya.student.feature.report.teacher_school

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                UnsolvedReports()
            }
            1 -> SolvedReports()
            else -> {
                return SolvedReports()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Unsolved Reports"
            1 -> "Solved Reports"
            else -> {
                return "Unsolved Reports"
            }
        }
    }

}