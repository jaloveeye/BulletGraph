package com.herace.choi


import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
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

    var timer : CountDownTimer? = null
    var value : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        VIEW = inflater.inflate(R.layout.fragment_bullet_graph, container, false)

        return VIEW
    }

    override fun onStart() {
        super.onStart()

        timer = object: CountDownTimer(60000, 100) {
            override fun onTick(millisUntilFinished: Long) {

                if (value < 100) {
                    value = value + 1

                    bullet_graph_3.value = value

                    bullet_graph_3.isWarning = !bullet_graph_3.isWarning

                    bullet_graph_1.value = value
                    bullet_graph_1.isWarning = !bullet_graph_1.isWarning
                    bullet_graph_1.isTop = !bullet_graph_1.isTop

                } else timer?.cancel()

            }
            override fun onFinish() {
            }
        }
        timer?.start()
    }
}

