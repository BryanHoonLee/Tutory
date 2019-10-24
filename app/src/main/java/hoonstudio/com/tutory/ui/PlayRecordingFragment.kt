package hoonstudio.com.tutory.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hoonstudio.com.tutory.R
import kotlinx.android.synthetic.main.activity_main.*

class PlayRecordingFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_recording, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        var activity = context as MainActivity
        activity.bottomNavigation.visibility = View.GONE
    }

    companion object {
        fun newInstance(): PlayRecordingFragment = PlayRecordingFragment()
    }
}