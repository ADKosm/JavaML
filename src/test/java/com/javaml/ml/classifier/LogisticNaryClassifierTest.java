package com.javaml.ml.classifier;

import com.javaml.image.AsciiImage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class LogisticNaryClassifierTest {
    private AsciiImage image = new AsciiImage(1, 1);
    private List<AsciiImage> train = new ArrayList<>();
    private List<Integer> labels = new ArrayList<>();
    private Integer numberOfLabels = 2;
    private LogisticNaryClassifier classifier = new LogisticNaryClassifier(0, 0.0, numberOfLabels);


    @Before
    public void setUp() {
        image.setPixel('.', 0, 0);
        train.add(image);
        labels.add(0);
    }

    @Test
    public void testPredictRange() {
        classifier.fit(train, labels);
        Integer label = classifier.predict(image);
        assertTrue(label >= 0);
        assertTrue(label < numberOfLabels);
    }
}
