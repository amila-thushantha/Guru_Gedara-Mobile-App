package com.example.elementaryapp2.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.drag_classes.DragSentence;
import com.example.elementaryapp2.classes.face_activity_classes.QuizActivityQuestion;

import java.util.ArrayList;
import java.util.List;

public class DragFragment extends Fragment {

    private TextView terget1, terget2, terget3;
    private TextView draggableText1, draggableText2, draggableText3;
    private TextView p_1_1, p_1_2, p_2_1, p_2_2, p_3_1, p_3_2;

    private static final String ARG_QUESTION_OBJ = "question_object";
    private QuizActivityQuestion quizQuestion;

    private String correctAnswerForSentence1, correctAnswerForSentence2, correctAnswerForSentence3;

    public DragFragment() {}

    public static DragFragment newInstance(QuizActivityQuestion question) {
        DragFragment fragment = new DragFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION_OBJ, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizQuestion = (QuizActivityQuestion) getArguments().getSerializable(ARG_QUESTION_OBJ);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.drag_question_layout, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        terget1 = view.findViewById(R.id.terget_1);
        terget2 = view.findViewById(R.id.terget_2);
        terget3 = view.findViewById(R.id.terget_3);

        draggableText1 = view.findViewById(R.id.draggableText1);
        draggableText2 = view.findViewById(R.id.draggableText2);
        draggableText3 = view.findViewById(R.id.draggableText3);

        p_1_1 = view.findViewById(R.id.p_1_1);
        p_1_2 = view.findViewById(R.id.p_1_2);
        p_2_1 = view.findViewById(R.id.p_2_1);
        p_2_2 = view.findViewById(R.id.p_2_2);
        p_3_1 = view.findViewById(R.id.p_3_1);
        p_3_2 = view.findViewById(R.id.p_3_2);

        if (quizQuestion != null) {
            DragSentence sentence_1 = quizQuestion.dragQuestion.sentences.get(0);
            DragSentence sentence_2 = quizQuestion.dragQuestion.sentences.get(1);
            DragSentence sentence_3 = quizQuestion.dragQuestion.sentences.get(2);

            correctAnswerForSentence1 = sentence_1.correctAnswer;
            correctAnswerForSentence2 = sentence_2.correctAnswer;
            correctAnswerForSentence3 = sentence_3.correctAnswer;

            p_1_1.setText(sentence_1.p1);
            p_1_2.setText(sentence_1.p2);

            p_2_1.setText(sentence_2.p1);
            p_2_2.setText(sentence_2.p2);

            p_3_1.setText(sentence_3.p1);
            p_3_2.setText(sentence_3.p2);

            List<Answer> options = quizQuestion.dragQuestion.options;
            draggableText1.setText(options.get(0).answerName);
            draggableText2.setText(options.get(1).answerName);
            draggableText3.setText(options.get(2).answerName);

            // Set touch listeners for dragging
            // Set touch listeners for dragging
            draggableText1.setOnLongClickListener(v -> {
                // Start the drag event for draggableText1
                ClipData data = ClipData.newPlainText("", draggableText1.getText());  // Use draggableText1's text here
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            });

            draggableText2.setOnLongClickListener(v -> {
                // Start the drag event for draggableText2
                ClipData data = ClipData.newPlainText("", draggableText2.getText());  // Use draggableText2's text here
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            });

            draggableText3.setOnLongClickListener(v -> {
                // Start the drag event for draggableText3
                ClipData data = ClipData.newPlainText("", draggableText3.getText());  // Use draggableText3's text here
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            });

            List<View> allTargets = new ArrayList<>();
            allTargets.add(terget1);
            allTargets.add(terget2);
            allTargets.add(terget3);



            // Set drag listeners for target boxes
            // Setup drag listeners for target boxes
            terget1.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    return handleDragEvent(v, event, correctAnswerForSentence1, allTargets);
                }
            });
            terget2.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    return handleDragEvent(v, event, correctAnswerForSentence2, allTargets);
                }
            });
            terget3.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    return handleDragEvent(v, event, correctAnswerForSentence3, allTargets);
                }
            });
        }
    }

    private boolean handleDragEvent(View view, DragEvent event, String correctAnswer, List<View> allTargets) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // The drag has started; set the default background color (gray) for all targets
                resetTargetBackgroundColors(allTargets);
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                // Visual feedback when the drag enters the target view
                view.setBackgroundColor(0xFFFFFF00); // Yellow color to indicate valid drop area
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                // Optionally, handle continuous updates of the drag location
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                // Reset visual feedback when the drag exits the target view (gray)
                view.setBackgroundColor(0xFFD3D3D3); // Reset to original color (gray)
                return true;

            case DragEvent.ACTION_DROP:
                // Handle the drop action
                ClipData.Item item = event.getClipData().getItemAt(0);
                String droppedText = item.getText().toString();

                // Reset all targets to gray before handling the drop
                resetTargetBackgroundColors(allTargets);

                // If the dropped answer matches the correct one, update the view
                if (droppedText.equals(correctAnswer)) {
                    // Set the dropped text in the target box
                    if (view instanceof TextView) {
                        ((TextView) view).setText(droppedText);

                        // Change background to green to indicate correct answer
                        view.setBackgroundColor(0xFF32CD32); // Green color for correct answer

                        // Make the dragged word view invisible (find it from event)
                        View draggedView = (View) event.getLocalState();
                        draggedView.setVisibility(View.INVISIBLE);

                        // Show toast for correct answer
//                        Toast.makeText(view.getContext(), "Correct!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Reset if incorrect: set text back to placeholder
                    if (view instanceof TextView) {
                        ((TextView) view).setText("______"); // Use placeholder or empty text

                        // Set background color to red for incorrect answer
                        view.setBackgroundColor(0xFFFF6347); // Red color for incorrect answer

                        // Show toast for incorrect answer
//                        Toast.makeText(view.getContext(), "Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                // Clean up after the drag has ended (optional)
                if (event.getResult()) {
                    // Successfully dropped
//                    view.setBackgroundColor(0xFF32CD32); // Set green color for successful drop
                } else {
                    // Failed to drop, reset to default gray
                    view.setBackgroundColor(0xFFD3D3D3); // Reset to gray if unsuccessful
                }
                return true;

            default:
                return false;
        }
    }

    // Reset the background color of all targets to the default gray
    private void resetTargetBackgroundColors(List<View> allTargets) {
        for (View target : allTargets) {
            target.setBackgroundColor(0xFFD3D3D3); // Reset to gray
        }
    }








}
