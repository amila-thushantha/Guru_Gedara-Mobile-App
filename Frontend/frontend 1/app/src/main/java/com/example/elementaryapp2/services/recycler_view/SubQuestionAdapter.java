package com.example.elementaryapp2.services.recycler_view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.MathEquation;
import com.example.elementaryapp2.classes.test_question_classes.ResultResponse;
import com.example.elementaryapp2.classes.test_question_classes.SubQuestion;
import com.example.elementaryapp2.classes.test_question_classes.TripleHashValue;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SubQuestionAdapter extends RecyclerView.Adapter<SubQuestionAdapter.ViewHolder> {
    private final List<SubQuestion> subQuestions;
    private final AdapterCallback callback;
    private Context context;
    private HashMap<Integer, TripleHashValue> selectedAnswersMap = new HashMap<>();

    public interface AdapterCallback {
        void onDataSent(ResultResponse data); // Method to send data to MainActivity
    }


    public SubQuestionAdapter(Context context, List<SubQuestion> subQuestions, AdapterCallback callback) {
        this.context= context;
        this.subQuestions = subQuestions;
        this.callback = callback;
    }

    @NonNull
    @Override
    public SubQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_sub_question, parent, false);
        return new SubQuestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubQuestionAdapter.ViewHolder holder, int position) {
        SubQuestion subQuestion = subQuestions.get(position);
        
        String questionType = subQuestion.questionType; //normal, withImage
        String optionsType = subQuestion.optionType; //image, text, enter, calc
        String category = subQuestion.category; //maths, sinhala
        String correctAnswer = subQuestion.correctAnswer;

        holder.tvSubQuestion.setText(subQuestion.subQuestion);

        // question type
        if (questionType.equals("withImage")) {
            if (subQuestion.imageResource != null) {
                holder.questionImage.setImageResource(subQuestion.imageResource);
            }
        } else {
            holder.questionImageSection.setVisibility(View.GONE);
        }

        // option type
        if (optionsType.equals("image")) {
            // Set the adapter
            AnswerAdapter adapter = new AnswerAdapter(subQuestion.options, answer -> handleAnswerClick(answer, holder.getBindingAdapterPosition(), category, correctAnswer));
            holder.answerRecyclerView.setAdapter(adapter);

            // Set up GridLayoutManager with 2 columns
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
            holder.answerRecyclerView.setLayoutManager(gridLayoutManager);

            holder.equationSection.setVisibility(View.GONE);
            holder.answerInput.setVisibility(View.GONE);
        }

        if (optionsType.equals("text")) {
            // Set the adapter
            ChoicesAdapter adapter = new ChoicesAdapter(subQuestion.options, answer -> handleAnswerClick(answer, holder.getBindingAdapterPosition(), category, correctAnswer));
            holder.answerRecyclerView.setAdapter(adapter);

            // Set up GridLayoutManager with 2 columns
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
            holder.answerRecyclerView.setLayoutManager(gridLayoutManager);

            holder.equationSection.setVisibility(View.GONE);
            holder.answerInput.setVisibility(View.GONE);
        }

        if (optionsType.equals("enter")) {
            holder.answerInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    addToHashMap(String.valueOf(s), holder.getBindingAdapterPosition(), category, correctAnswer);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            holder.answerRecyclerViewSection.setVisibility(View.GONE);
            holder.equationSection.setVisibility(View.GONE);
        }

        if (optionsType.equals("calc")) {
            MathEquation equation = subQuestion.mathEquation;
            holder.op_1.setText(String.valueOf(equation.op_1));
            holder.op_2.setText(String.valueOf(equation.op_2));
            holder.operation.setText(equation.operation);

            holder.equationAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    addToHashMap(String.valueOf(s), holder.getBindingAdapterPosition(), category, correctAnswer);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            holder.answerRecyclerViewSection.setVisibility(View.GONE);
            holder.answerInput.setVisibility(View.GONE);
        }

        // Set the click listener
//        holder.itemView.setOnClickListener(v -> {
//            if (clickListener != null) {
//                clickListener.onAnswerClick(currentAnswer);
//            }
//        });
    }

    private void handleAnswerClick(Answer answer, int position, String category, String correctAnswer) {
        addToHashMap(answer.answerName, position, category, correctAnswer);
//        clickListener.onAnswerClick(answer);
    }

    private void addToHashMap(String value, int position, String category, String correctAnswer) {
        if (selectedAnswersMap.containsKey(position)) {
            selectedAnswersMap.replace(position, new TripleHashValue(position, value, correctAnswer, category));
        } else {
            selectedAnswersMap.put(position, new TripleHashValue(position, value, correctAnswer, category));
        }
    }

    @Override
    public int getItemCount() {
        return subQuestions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView questionImageSection, answerRecyclerViewSection;
        LinearLayout equationSection;
        RecyclerView answerRecyclerView;
        ImageView questionImage;
        EditText answerInput, equationAnswer;

        TextView op_1, op_2, operation, tvSubQuestion;

        ViewHolder(View itemView) {
            super(itemView);
            questionImageSection = itemView.findViewById(R.id.questionImageSection);
            questionImage = itemView.findViewById(R.id.questionImage);

            answerRecyclerViewSection = itemView.findViewById(R.id.answersRecyclerViewSection);
            answerRecyclerView = itemView.findViewById(R.id.answersRecyclerView);

            equationSection = itemView.findViewById(R.id.equationSection);

            answerInput = itemView.findViewById(R.id.answerInput);

            op_1 = itemView.findViewById(R.id.op_1);
            op_2 = itemView.findViewById(R.id.op_2);
            operation = itemView.findViewById(R.id.operator);
            equationAnswer = itemView.findViewById(R.id.answer_1);

            tvSubQuestion = itemView.findViewById(R.id.tvSubQuestion);
        }
    }

    public void calculateMarks() {
        AtomicInteger totalMathsQuestions = new AtomicInteger();
        AtomicInteger totalSinhalaQuestions = new AtomicInteger();

        AtomicInteger incorrectMathsCount = new AtomicInteger();
        AtomicInteger incorrectSinhalaCount = new AtomicInteger();

        totalMathsQuestions.set(0);
        totalSinhalaQuestions.set(0);
        incorrectMathsCount.set(0);
        incorrectSinhalaCount.set(0);

        if (selectedAnswersMap.size() == subQuestions.size()) {
            boolean allValuesNotEmpty = selectedAnswersMap.values().stream()
                    .allMatch(value -> value.selectedAnswer != null && !value.selectedAnswer.trim().isEmpty());

            if (allValuesNotEmpty) {
                selectedAnswersMap.forEach((key, value) -> {
                    if (value.category.equals("maths")) {
                        totalMathsQuestions.set(totalMathsQuestions.get() + 1);
                        if (!value.selectedAnswer.equals(value.correctAnswer)) {
                            incorrectMathsCount.set(incorrectMathsCount.get() + 1);
                        }
                    } else {
                        totalSinhalaQuestions.set(totalSinhalaQuestions.get() + 1);
                        if (!value.selectedAnswer.equals(value.correctAnswer)) {
                            incorrectSinhalaCount.set(incorrectSinhalaCount.get() + 1);
                        }
                    }
                });

                ResultResponse response = new ResultResponse(totalMathsQuestions.get(), totalSinhalaQuestions.get(), incorrectMathsCount.get(), incorrectSinhalaCount.get());
                if (callback != null) {
                    callback.onDataSent(response); // Send data back to MainActivity
                }
            }
        }
    }
}
