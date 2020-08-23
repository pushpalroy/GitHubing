package com.gitsurfer.gitsurf.ui.repo.files.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gitsurfer.gitsurf.R
import com.gitsurfer.gitsurf.data.network.models.response.RepoFile
import com.gitsurfer.gitsurf.databinding.ItemRepoFileBinding
import com.gitsurfer.gitsurf.ui.repo.files.adapter.FilesAdapter.FileViewHolder
import com.gitsurfer.gitsurf.utils.GithubUtil.TYPE_FILE

class FilesAdapter constructor(
  private val fileClickListener: FileClickListener
) :
  RecyclerView.Adapter<FileViewHolder>() {

  private var repoFilesList: List<RepoFile?> = listOf()

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): FileViewHolder {
    return FileViewHolder(
      DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.item_repo_file,
        parent, false
      ), fileClickListener
    )
  }

  fun setItems(items: List<RepoFile?>) {
    repoFilesList = items
  }

  override fun getItemCount(): Int {
    return repoFilesList.size
  }

  override fun onBindViewHolder(
    holder: FileViewHolder,
    position: Int
  ) {
    val repoFile = repoFilesList[position]
    repoFile?.let {
      holder.bind(it, position)
    }
  }

  class FileViewHolder(
    private val binding: ItemRepoFileBinding,
    private val fileClickListener: FileClickListener
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
      repoFile: RepoFile,
      position: Int
    ) {
      binding.tvFileName.text = repoFile.fileName
      if (repoFile.size == 0) {
        binding.tvFileSize.visibility = View.GONE
      } else {
        binding.tvFileSize.text = repoFile.size.toString() + "B"
      }
      if (repoFile.type.equals(TYPE_FILE)) {
        binding.ivFileIcon.setImageResource(R.drawable.ic_file)
      } else {
        binding.ivFileIcon.setImageResource(R.drawable.ic_folder)
      }

      binding.root.setOnClickListener {
        fileClickListener.onFileClicked(position)
      }
    }
  }
}