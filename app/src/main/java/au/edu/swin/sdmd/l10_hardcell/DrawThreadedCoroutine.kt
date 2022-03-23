package au.edu.swin.sdmd.l10_hardcell

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DrawThreadedCoroutine {
  companion object : CoroutineScope {
    // setting up
    private lateinit var progressBar: ProgressBar
    private lateinit var job: Job

    /* From CoroutineScope */
    override val coroutineContext: CoroutineContext
      get() = Dispatchers.Main + job

    fun init(progressBar: ProgressBar) {
      // setup
      this.progressBar = progressBar
      job = Job() // coroutine setup
    }

    // draw on thread
     fun draw(rule: Int,
             imageView: ImageView,
             width: Int, height: Int) {
      imageView.setImageBitmap(null)
      progressBar.visibility = View.VISIBLE

      val ca = ElemCA(width, height)
      ca.setNumber(rule)

      launch {
        // one approach -- note the icon in the border
        val bm = withContext(Dispatchers.Default) {
          ca.processCA()
        }

        imageView.setImageBitmap(bm)
        // An alternative approach, for those who like async/await
        // val bm = async { ca.processCA() }
        // imageView.setImageBitmap(bm.await())
        progressBar.visibility = View.INVISIBLE
      }
    }

    fun onDestroy() {
      job.cancel()
    }
  }
}