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
        //return inflater.inflate(R.layout.fragment_bullet_graph, container, false)

        VIEW = inflater.inflate(R.layout.fragment_bullet_graph, container, false)

        return VIEW
    }


    override fun onStart() {
        super.onStart()

        bt.setOnClickListener {
            Toast.makeText(this@BulletGraphFragment.context, "click", Toast.LENGTH_SHORT).show()
        }
    }


//    override fun onStart() {
//        super.onStart()
//
//        // view 를 레이아웃에 먼저 추가
//        var textView = TextView(activity)
//        val id = genrrateViewId()
//        textView.id = id
//        textView.setText("!23")
//        bullet_layout.addView(textView)
//
//
//        var textView2 = TextView(activity)
//        val id2 = genrrateViewId()
//        textView2.id = id2
//        textView2.setText("한글")
//        bullet_layout.addView(textView2)
//
//        //
//        var constraintSet = ConstraintSet()
//        constraintSet.clone(bullet_layout)
//
//        constraintSet.connect(id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID)
//        constraintSet.connect(id2, ConstraintSet.LEFT, id, ConstraintSet.RIGHT, id)
//
//
//        constraintSet.applyTo(bullet_layout);
//
//    }
//
//
//    private fun genrrateViewId(): Int {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            return generateSdk17Under()
//        } else return View.generateViewId()
//    }
//
//    private fun generateSdk17Under() : Int {
//        var isTrue = true
//        var result = sNextGeneratedId.get()
//        while (isTrue) {
//            var newValue = result + 1
//            if (newValue > 0x00FFFFFF) newValue = 1
//
//            if (sNextGeneratedId.compareAndSet(result, newValue)) {
//                isTrue = false
//            }
//        }
//
//        return result
//    }
//
//    companion object {
//        private var sNextGeneratedId = AtomicInteger(1)
//    }
}

