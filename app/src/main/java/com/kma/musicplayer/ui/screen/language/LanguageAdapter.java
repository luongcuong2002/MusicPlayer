package com.kma.musicplayer.ui.screen.language;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.kma.musicplayer.R;
import com.kma.musicplayer.model.LanguageModel;
import com.kma.musicplayer.model.Theme;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<LanguageModel> lists;
    private IClickLanguage iClickLanguage;

    private Theme theme = Theme.DARK;

    public void setTheme(Theme theme) {
        this.theme = theme;
        notifyDataSetChanged();
    }

    public LanguageAdapter(Context context, List<LanguageModel> lists, IClickLanguage iClickLanguage) {
        this.context = context;
        this.lists = lists;
        this.iClickLanguage = iClickLanguage;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new LanguageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_language, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        LanguageModel data = lists.get(position);
        if (holder instanceof LanguageViewHolder) {
            ((LanguageViewHolder) holder).bind(data);
            ((LanguageViewHolder) holder).relayEnglish.setOnClickListener(v -> iClickLanguage.onClick(data));

            if (data.isCheck()) {
                ((LanguageViewHolder) holder).relayEnglish.setBackgroundResource(R.drawable.border_item_language_select);
                ((LanguageViewHolder) holder).tvTitle.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                ((LanguageViewHolder) holder).relayEnglish.setBackgroundResource(theme.getImageLanguageItemBackgroundRes());
                ((LanguageViewHolder) holder).tvTitle.setTextColor(context.getResources().getColor(theme.getLanguageItemTextColorRes()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvTitle;
        CardView relayEnglish;

        public LanguageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.img_avatar);
            tvTitle = itemView.findViewById(R.id.tv_title);
            relayEnglish = itemView.findViewById(R.id.relay_english);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void bind(LanguageModel data) {
            ivAvatar.setImageDrawable(context.getDrawable(data.getImage()));
            tvTitle.setText(data.getLanguageName());
        }
    }

    public void setSelectLanguage(LanguageModel model) {
        for (LanguageModel data : lists) {
            if (data.getLanguageName().equals(model.getLanguageName())) {
                data.setCheck(true);
            } else {
                data.setCheck(false);
            }
        }
        notifyDataSetChanged();
    }
}
