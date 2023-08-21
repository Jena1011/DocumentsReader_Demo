package com.app.documentsreaderdemo

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import java.io.*

/**
 * This fragment has a big [ImageView] that shows PDF pages, and 2 [Button]s to move between pages.
 * We use a [PdfRenderer] to render PDF pages as [Bitmap]s.
 */
class ActionOpenDocumentFragment : Fragment() {

    private lateinit var markdownPageView: TextView

    companion object {
        private const val DOCUMENT_URI_ARGUMENT =
            "com.example.android.actionopendocument.args.DOCUMENT_URI_ARGUMENT"

        fun newInstance(documentUri: Uri): ActionOpenDocumentFragment {

            return ActionOpenDocumentFragment().apply {
                arguments = Bundle().apply {
                    putString(DOCUMENT_URI_ARGUMENT, documentUri.toString())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_action_open_document, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        markdownPageView = view.findViewById(R.id.markdown)

    }

    override fun onStart() {
        super.onStart()

        val documentUri = arguments?.getString(DOCUMENT_URI_ARGUMENT)?.toUri() ?: return
//        val documentUri = Uri.parse(arguments?.getString(DOCUMENT_URI_ARGUMENT))

        try {
            val inputStream: InputStream? = requireActivity().contentResolver.openInputStream(documentUri)
            val inputStreamReader = InputStreamReader(inputStream)
            val mBufferedReader = BufferedReader(inputStreamReader)
            var mReadText = ""
            var mTextLine = mBufferedReader.readLine()

            //一行一行取出文字字串裝入String裡，直到沒有下一行文字停止跳出
            while (mTextLine!=null)
            {
                mReadText += mTextLine+"\n"
                mTextLine = mBufferedReader.readLine()
            }
            markdownPageView.text = mReadText
            inputStream?.close()
            inputStreamReader.close()
            mBufferedReader.close()

        } catch (ioException: IOException) {
            Log.d(TAG, "Exception opening document", ioException)
        }
    }
}

private const val TAG = "OpenDocumentFragment"