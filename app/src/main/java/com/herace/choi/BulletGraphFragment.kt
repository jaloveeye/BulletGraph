package com.herace.choi


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.fragment_bullet_graph.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * A simple [Fragment] subclass.
 */
class BulletGraphFragment : Fragment() {

    private lateinit var VIEW: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        VIEW = inflater.inflate(R.layout.fragment_bullet_graph, container, false)

        return VIEW
    }
}

