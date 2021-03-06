package hoonstudio.com.tutory

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hoonstudio.com.tutory.data.roomdb.entity.Song
import hoonstudio.com.tutory.data.viewmodel.SharedRecordingViewModel
import hoonstudio.com.tutory.data.viewmodel.SongViewModel
import hoonstudio.com.tutory.adapter.RecordingsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class RecordingsFragment : Fragment(), RecordingsAdapter.OnRecordingsItemClickListener {
    private lateinit var songViewModel: SongViewModel
    private lateinit var sharedRecordingViewModel: SharedRecordingViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecordingsAdapter
    private lateinit var recordingsList: List<Song>

    private lateinit var toast: Toast

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_recordings, container, false)
        initRecyclerView(view)
        initTouchHelper()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        songViewModel = ViewModelProviders.of(this).get(SongViewModel::class.java)
        songViewModel.getAllSongFromDb()

        sharedRecordingViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedRecordingViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        songViewModel.getAllSongFromDb().observe(this, Observer {
            Collections.reverse(it)
            adapter.setRecordingsList(it)
            recordingsList = it
        })




    }

    override fun onRecordingsItemClick(position: Int) {
        sharedRecordingViewModel.setSharedRecording(recordingsList.get(position))
        navigateToPlayRecordingFragment()
    }



    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)

        adapter = RecordingsAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun initTouchHelper() {
        val background = ColorDrawable()
        val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)
        val deleteIconIntrinsicWidth = deleteIcon!!.intrinsicWidth
        val deleteIconIntrinsicHeight = deleteIcon!!.intrinsicHeight
        0
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction == ItemTouchHelper.LEFT){
                    initAlertBuilder(viewHolder)
                }

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                // swipe to the left
                if(dX < 0) {
                    val itemView = viewHolder.itemView
                    val itemHeight = itemView.bottom - itemView.top

                    background.color = ResourcesCompat.getColor(resources, R.color.delete_item_color, null)
                    background.setBounds(
                        itemView.right,
                        itemView.top,
                        itemView.left,
                        itemView.bottom
                    )
                    background.draw(c)

                    val deleteIconTop = itemView.top + (itemHeight - deleteIconIntrinsicHeight) / 2
                    val deleteIconMargin = (itemHeight - deleteIconIntrinsicHeight) / 3
                    val deleteIconLeft = itemView.right - deleteIconMargin - deleteIconIntrinsicWidth
                    val deleteIconRight = itemView.right - deleteIconMargin
                    val deleteIconBottom = deleteIconTop + deleteIconIntrinsicHeight

                    deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                    deleteIcon.draw(c)

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun navigateToPlayRecordingFragment(){
        findNavController().navigate(R.id.action_recordingsFragment_to_playRecordingFragment)
    }

    private fun initAlertBuilder(viewHolder: RecyclerView.ViewHolder) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setTitle("Delete Confirmation")
        builder.setMessage("Are you sure you want to delete ${adapter.getRecordingAt(viewHolder.adapterPosition).audioName}?")
        builder.setPositiveButton("Delete") { _, _ ->
            songViewModel.deleteSong(recordingsList.get(viewHolder.adapterPosition))
          //  songViewModel.getAllSongFromDb()
            adapter.notifyItemRemoved(viewHolder.adapterPosition)
            showToast("Deleted")
        }

        builder.setNeutralButton("Cancel") { _, _ ->
            adapter.notifyItemChanged(viewHolder.adapterPosition)
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showToast(message: String) {
        if (::toast.isInitialized && toast != null) {
            toast.cancel()
        }
        toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}