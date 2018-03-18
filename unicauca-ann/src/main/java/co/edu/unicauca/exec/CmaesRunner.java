package co.edu.unicauca.exec;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import co.edu.unicauca.problem.cross_validation.AbstractCrossValidationEvaluator;
import co.edu.unicauca.problem.training_testing.TrainingTestingEvaluator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.CovarianceMatrixAdaptationEvolutionStrategy;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class CmaesRunner {

    static int MAXEFOS = 3000;
    static int DEFAULT_NUMBER_OF_EXPERIMENTS = 5;

    //Basic example: CMA-ES using TT validator
    public static void main(String[] args) throws Exception {
        execute(args);
    }

    public static void execute(String[] args) throws IOException {

        List problemsNames = new ArrayList();
        problemsNames.add("co.edu.unicauca.problem.cross_validation.Iris");
        problemsNames.add("co.edu.unicauca.problem.training_testing.Car");
        problemsNames.add("co.edu.unicauca.problem.training_testing.Hill");//Long TIME
        problemsNames.add("co.edu.unicauca.problem.training_testing.Hayes");//Long TIME
        problemsNames.add("co.edu.unicauca.problem.cross_validation.AutoMpg");

        Algorithm<DoubleSolution> algorithm = null;
        DoubleProblem problem = null;

        for (Object problemName : problemsNames) {
            System.out.println("Experiment for " + problemName);
            for (int i = 0; i < DEFAULT_NUMBER_OF_EXPERIMENTS; i++) {

                //Load a problem by name
                problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemName);

                //set a SEED
                JMetalRandom.getInstance().setSeed(i);

                if (!((String) problemName).contains("training_testing")) {
                    MAXEFOS = 3000 / 10;//only 300 EFOS, because count 10 Efos for 1 evaluation of objective funtion that make 10 ELM models
                }
                //set Parameters for CMA-ES
                algorithm = new CovarianceMatrixAdaptationEvolutionStrategy.Builder(problem, Integer.parseInt(10 + ""), Double.parseDouble(0.2 + ""), MAXEFOS).build();

                //Set solution representation scheme
                //vector=[weight,bias,weight,bias....] schema A or 0
                //vector=[weight,weight,weight,bias,bias.....] scheme B or 1
                ((AbstractELMEvaluator) problem).setType_solution(0);

                //start algorith
                AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                        .execute();
                //Get results
                DoubleSolution solution = (DoubleSolution) algorithm.getResult();
                System.out.println("Best Train " + solution.getObjective(0));
                double a = -1;
                if (((String) problemName).contains("training_testing")) {
                    a = ((TrainingTestingEvaluator) problem).test(solution);
                } else {
                    a = ((AbstractCrossValidationEvaluator) problem).test(solution);
                }

                System.out.println("Test " + (a));
                System.out.println("Time " + algorithmRunner.getComputingTime());

            }
        }
    }

}
