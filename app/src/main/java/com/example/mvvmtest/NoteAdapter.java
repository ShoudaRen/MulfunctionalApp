package com.example.mvvmtest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/***
 * RecyclerView.Adapter<xxx> xxx是我们需要使用的ViewHolder
 */
public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {
    private OnItemClickListener listener;

    /**
     * ListAdapter 的具体实现
     */
    public NoteAdapter() {
        super(Diff_callback);
    }

    private static final DiffUtil.ItemCallback<Note> Diff_callback = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {

            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPriority() == newItem.getPriority();
        }
    };


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflate布局加载器
        // 获得实例
        View itemView = LayoutInflater.from(parent.getContext())
                //which VIew
                //传进来的布局会被加载成为一个View并直接返回
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        //转换XML的数据到内存
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
        holder.textViewDes.setText(currentNote.getDescription());

    }

//
//    // setter method
//    public void setNotes(List<Note> notes) {
//        this.notes = notes;
//        notifyDataSetChanged();
//    }

//
//    public void onItemMove(int from, int to) {
//        Collections.swap(notes, from, to);
//        notifyItemMoved(from, to);
//    }

    public Note getNoteAt(int position) {
        return getItem(position);

    }

    class NoteHolder extends RecyclerView.ViewHolder {
        //后端数据持有类 需要转换XML的数据到内存
        private TextView textViewTitle;
        private TextView textViewDes;
        private TextView textViewPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDes = itemView.findViewById(R.id.text_view_des);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.OnItemClick(getItem(position));
                }
            });
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(Note note);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }


}
