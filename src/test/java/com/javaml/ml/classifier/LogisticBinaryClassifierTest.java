package com.javaml.ml.classifier;

import com.javaml.exception.UnmatchedTensorAndLabelNumbersException;
import com.javaml.exception.UnmatchedTensorSizesException;
import com.javaml.image.AsciiImage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class LogisticBinaryClassifierTest {
    private AsciiImage image = new AsciiImage(1, 1);
    private List<AsciiImage> train = new ArrayList<>();
    private List<Boolean> labels = new ArrayList<>();
    private BinaryClassifier classifier = new LogisticBinaryClassifier(0, 0.0, 0.5);


    @Before
    public void setUp() {
        image.setPixel('.', 0, 0);
        train.add(image);
        labels.add(true);
    }

    @Test
    public void testPredictProbaRange() {
        classifier.fit(train, labels);
        Double proba = classifier.predict_proba(image);
        assertTrue(proba <= 1.0);
        assertTrue(proba >= 0.0);
    }

    @Test(expected = UnmatchedTensorAndLabelNumbersException.class)
    public void testUnmatchedTensorAndLabelNumbersException() {
        labels.add(true);
        classifier.fit(train, labels);
    }

    @Test(expected = UnmatchedTensorSizesException.class)
    public void testUnmatchedTensorSizesException() {
        AsciiImage biggerImage = new AsciiImage(5, 5);
        train.add(biggerImage);
        labels.add(true);
        classifier.fit(train, labels);
    }
}
