package com.example.paw1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private List<String> questions;
    private List<String[]> answers;
    private int[] userAnswers;

    public QuizAdapter(List<String> questions, List<String[]> answers, int[] userAnswers) {
        this.questions = questions;
        this.answers = answers;
        this.userAnswers = userAnswers;
    }

    @NonNull
    @Override
    public QuizAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.ViewHolder holder, int position) {
        holder.questionText.setText(questions.get(position));
        holder.option1.setText(answers.get(position)[0]);
        holder.option2.setText(answers.get(position)[1]);
        holder.option3.setText(answers.get(position)[2]);

        holder.answerGroup.setOnCheckedChangeListener(null);
        holder.answerGroup.clearCheck();

        if (userAnswers[position] != -1) {
            int selectedRadioButtonId = holder.answerGroup.getChildAt(userAnswers[position]).getId();
            holder.answerGroup.check(selectedRadioButtonId);
        }

        holder.answerGroup.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = group.findViewById(checkedId);
            int index = group.indexOfChild(radioButton);
            if (index != -1) {
                userAnswers[position] = index;
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionText;
        RadioGroup answerGroup;
        RadioButton option1, option2, option3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            answerGroup = itemView.findViewById(R.id.answerGroup);
            option1 = itemView.findViewById(R.id.option1);
            option2 = itemView.findViewById(R.id.option2);
            option3 = itemView.findViewById(R.id.option3);
        }
    }
}
