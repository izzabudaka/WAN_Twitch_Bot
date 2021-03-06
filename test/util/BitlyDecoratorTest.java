package util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;
import util.config.AppInjector;
import util.config.Environment;

/**
 * Created by Dominic Hauton on 25/03/2016.
 *
 * Unit test to ensure Bitly lib decorator is working as required.
 */
public class BitlyDecoratorTest {

    @Test
    public void shortenURLIntegrationTest() throws Exception {
        Injector injector = Guice.createInjector(new AppInjector(Environment.DEV));
        BitlyDecorator bitlyDecorator = injector.getInstance(BitlyDecorator.class);
        final String exampleURLString = "http://www.example.com";
        final String expectedShortenedURL = "http://bit.ly/1FU7TNG";
        final String actualShortenedURL = bitlyDecorator.shortenURL(exampleURLString);
        Assert.assertEquals("Check " + exampleURLString + " shortens to expected URL.",
                expectedShortenedURL,
                actualShortenedURL);
    }
}