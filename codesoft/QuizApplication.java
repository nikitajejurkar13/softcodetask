import java.util.*;
import java.util.concurrent.*;

public class QuizApplication {

    private static final int TIME_LIMIT = 10; // seconds for each question
    private static final List<Question> QUESTIONS = Arrays.asList(
        new Question("What is the capital of France?", 
                     new String[]{"A) Berlin", "B) Madrid", "C) Paris", "D) Rome"}, 
                     'C'),
        new Question("Which planet is known as the Red Planet?", 
                     new String[]{"A) Earth", "B) Mars", "C) Jupiter", "D) Venus"}, 
                     'B'),
        new Question("What is the largest ocean on Earth?", 
                     new String[]{"A) Atlantic Ocean", "B) Indian Ocean", "C) Arctic Ocean", "D) Pacific Ocean"}, 
                     'D')
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int score = 0;

        for (Question question : QUESTIONS) {
            System.out.println(question.getQuestion());
            for (String option : question.getOptions()) {
                System.out.println(option);
            }

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(() -> {
                return scanner.nextLine();
            });

            try {
                String answer = future.get(TIME_LIMIT, TimeUnit.SECONDS);
                if (question.isCorrectAnswer(answer.toUpperCase().charAt(0))) {
                    System.out.println("Correct!");
                    score++;
                } else {
                    System.out.println("Incorrect!");
                }
            } catch (TimeoutException e) {
                System.out.println("Time's up!");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }

            System.out.println();
        }

        System.out.println("Quiz over!");
        System.out.println("Your score: " + score + "/" + QUESTIONS.size());
        scanner.close();
    }

    private static class Question {
        private final String question;
        private final String[] options;
        private final char correctAnswer;

        public Question(String question, String[] options, char correctAnswer) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }

        public String getQuestion() {
            return question;
        }

        public String[] getOptions() {
            return options;
        }

        public boolean isCorrectAnswer(char answer) {
            return answer == correctAnswer;
        }
    }
}
