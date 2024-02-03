package com.example.silver_screen;

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.os.CountDownTimer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


public class TriviaFragment extends Fragment implements View.OnClickListener {

    private TriviaQuestionViewModel triviaQuestionViewModel;
    private NavController navController;

    private Button option1Btn, option2Btn, option3Btn, nextQnBt;
    private TextView questionTv, ansFeedbackTv, questionNumber, timerCountTv;
    private ImageView closeTriviaBtn;
    private ProgressBar progressBar;
    private int currentQueNo = 0;
    private String triviaId;
    private TriviaQuestionViewModel viewModel;
    private long totalQuestions;
    private boolean canAnswer = false;

    private long timer;

    private CountDownTimer countDownTimer;

    private int NotAnswered = 0;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;

    private int notAnswered = 0;
    private String answer = "";


//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        triviaListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()))
//                .get(TriviaListViewModel.class);
//    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()))
                .get(TriviaQuestionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trivia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        closeTriviaBtn = view.findViewById(R.id.cancelTrivia);
        option1Btn = view.findViewById(R.id.option1);
        option2Btn = view.findViewById(R.id.option2);
        option3Btn = view.findViewById(R.id.option3);
        nextQnBt = view.findViewById(R.id.nextQuestion);
        questionTv = view.findViewById(R.id.question);
        ansFeedbackTv = view.findViewById(R.id.verifyAnswer);
        timerCountTv = view.findViewById(R.id.questionTimer);
        progressBar = view.findViewById(R.id.triviaprogressBar);
        questionNumber = view.findViewById(R.id.numberOfQuestions);



        triviaId = TriviaFragmentArgs.fromBundle(getArguments()).getTriviaId();
        totalQuestions = TriviaFragmentArgs.fromBundle(getArguments()).getTotalQnsCount();
        if (triviaId == null) {
            triviaId = "1";
            viewModel.setTriviaId(triviaId);
        } else {
            viewModel.setTriviaId(triviaId);

        }

        option1Btn.setOnClickListener(this);
        option2Btn.setOnClickListener(this);
        option3Btn.setOnClickListener(this);
        nextQnBt.setOnClickListener(this);

        closeTriviaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_triviaFragment_to_triviaListFragment);
            }
        });

        loadData();
    }

    private void loadData(){
        enableOptions();
        loadQuestions(1);
    }

    private void enableOptions(){
        option1Btn.setVisibility(View.VISIBLE);
        option2Btn.setVisibility(View.VISIBLE);
        option3Btn.setVisibility(View.VISIBLE);

        //enable buttons , hide feedback tv , hide nextQuiz btn
        option1Btn.setEnabled(true);
        option2Btn.setEnabled(true);
        option3Btn.setEnabled(true);

        ansFeedbackTv.setVisibility(View.INVISIBLE);
        nextQnBt.setVisibility(View.INVISIBLE);
    }

    private void loadQuestions(int i){

        currentQueNo = i;
        viewModel.getTriviaMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<TriviaQuestionModel>>() {
            @Override
            public void onChanged(List<TriviaQuestionModel> questionModels) {
                questionTv.setText(String.valueOf(currentQueNo) + ") " + questionModels.get(i - 1).getQuestion());

                option1Btn.setText(questionModels.get(i - 1).getOption_a());
                option2Btn.setText(questionModels.get(i - 1).getOption_b());
                option3Btn.setText(questionModels.get(i - 1).getOption_c());
                timer = questionModels.get(i - 1).getTimer();
                answer = questionModels.get(i-1).getAnswer();

                questionNumber.setText(String.valueOf(currentQueNo));
                startTimer(i);
            }
        });

        canAnswer = true;
    }

    private void startTimer(int i){
        timerCountTv.setText(String.valueOf(timer));
        timerCountTv.setText(String.valueOf(timer));
        progressBar.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(timer * 1000 , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // update time
                timerCountTv.setText(millisUntilFinished / 1000 + "");

                Long percent = millisUntilFinished/(timer*10);
                progressBar.setProgress(percent.intValue());
            }

            @Override
            public void onFinish() {
                canAnswer = false;
                ansFeedbackTv.setText("Times Up !! No answer selected");
                NotAnswered ++;
                showNextBtn();
            }
        }.start();
    }

    private void showNextBtn() {
        if (currentQueNo == totalQuestions){
            nextQnBt.setText("Submit");
            nextQnBt.setEnabled(true);
            nextQnBt.setVisibility(View.VISIBLE);
        }else{
            nextQnBt.setVisibility(View.VISIBLE);
            nextQnBt.setEnabled(true);
            ansFeedbackTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.option1) {
            verifyAnswer(option1Btn);
        } else if (v.getId() == R.id.option2) {
            verifyAnswer(option2Btn);
        } else if (v.getId() == R.id.option3) {
            verifyAnswer(option3Btn);
        } else if (v.getId() == R.id.nextQuestion) {
            if (currentQueNo == totalQuestions) {
                submitResults();
            } else {
                currentQueNo++;
                loadQuestions(currentQueNo);
                resetOptions();
            }
        }
    }




    private void resetOptions(){
        ansFeedbackTv.setVisibility(View.INVISIBLE);
        nextQnBt.setVisibility(View.INVISIBLE);
        nextQnBt.setEnabled(false);
        // Retrieve the existing drawable from the button_style_transparent of Register Button
        Drawable existingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.button_style_transparent);

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{existingDrawable});

        // Add a new shape for the stroke
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{8, 8, 8, 8, 8, 8, 8, 8}, null, null));
        shapeDrawable.getPaint().setColor(ContextCompat.getColor(getContext(), R.color.white));
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawable.getPaint().setStrokeWidth(2); // Set the stroke width

        // Set the stroke drawable at the desired layer index
        layerDrawable.setDrawableByLayerId(android.R.id.background, shapeDrawable);

        // Set the updated LayerDrawable as the background for the buttons
        option1Btn.setBackground(layerDrawable);
        option2Btn.setBackground(layerDrawable);
        option3Btn.setBackground(layerDrawable);

    }

    private void submitResults() {
        HashMap<String , Object> resultMap = new HashMap<>();
        resultMap.put("correct" , correctAnswer);
        resultMap.put("wrong" , wrongAnswer);
        resultMap.put("notAnswered" , notAnswered);


//        QuizragmentDirections.ActionQuizragmentToResultFragment action =
//                QuizragmentDirections.actionQuizragmentToResultFragment();
//        action.setQuizId(quizId);
//        navController.navigate(action);

        // TriviaResultFragment not working
//        navController.navigate(R.id.action_triviaFragment_to_triviaResultFragment);
        navController.navigate(R.id.action_triviaFragment_to_triviaListFragment);
    }

    private void verifyAnswer(Button button) {
        if (canAnswer) {
            String buttonText = button.getText().toString().trim();

            if (answer.equals(buttonText)) {
                button.setBackground(ContextCompat.getDrawable(getContext(), R.color.greenColor));
                correctAnswer++;
                ansFeedbackTv.setText("Correct Answer");
            } else {
                button.setBackground(ContextCompat.getDrawable(getContext(), R.color.redColor));
                wrongAnswer++;
                ansFeedbackTv.setText("Wrong Answer \nCorrect Answer :" + answer);
            }

            canAnswer = false;
            countDownTimer.cancel();
            showNextBtn();
        }
    }



}