package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Letter extends TrainingTestingEvaluator {

    public Letter() throws IOException {
        super(20, new DataSet("src/resources-elm", "letter.train", 16), new DataSet("src/resources-elm", "letter.test", 16), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Letter", 3000);
    }

}