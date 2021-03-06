package channel.message;

import channel.users.TwitchUser;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Dominic Hauton on 19/03/2016.
 *
 * Test for all functions in ImmutableTwitchMessageList
 */
public class ImmutableTwitchMessageListTest {

    private static final String user1 = "user1";
    private static final String user2 = "user2";

    private static final String payload1 = "msg1";

    private TwitchMessage twitchMessage1;
    private TwitchMessage twitchMessage2;
    private TwitchMessage twitchMessage3;
    private TwitchMessage twitchMessage4;

    @Before
    public void setUp() {
        DateTime dateTime = DateTime.now();
        twitchMessage1 = new TwitchMessage(payload1, user1, dateTime);
        twitchMessage2 = new TwitchMessage("msg2",   user2, dateTime.plusSeconds(1));
        twitchMessage3 = new TwitchMessage("msg3",   user1, dateTime.plusSeconds(2));
        twitchMessage4 = new TwitchMessage("msg4",   user2, dateTime.plusSeconds(3));
    }

    @Test
    public void testUserFilteringRemoveHalf() {
        Collection<TwitchMessage> originalTwitchMessages =
                Arrays.asList(twitchMessage1, twitchMessage2, twitchMessage3, twitchMessage4);

        Collection<TwitchMessage> expectedMessages = Arrays.asList(twitchMessage1, twitchMessage3);

        ImmutableTwitchMessageList immutableMessageList = new ImmutableTwitchMessageList(originalTwitchMessages);
        ImmutableTwitchMessageList actualFilteredMessageList = immutableMessageList.filterUser(new TwitchUser(user1));
        ImmutableTwitchMessageList expectedFilteredTwitchMessages  = new ImmutableTwitchMessageList(expectedMessages);

        Assert.assertEquals("Filtered out user1 messages", expectedFilteredTwitchMessages, actualFilteredMessageList);
    }

    @Test
    public void testUserFilteringRemoveNone() {
        Collection<TwitchMessage> originalTwitchMessages =
                Arrays.asList(twitchMessage2, twitchMessage4);

        Collection<TwitchMessage> expectedMessages = Arrays.asList(twitchMessage2, twitchMessage4);

        ImmutableTwitchMessageList immutableMessageList = new ImmutableTwitchMessageList(originalTwitchMessages);
        ImmutableTwitchMessageList actualFilteredMessageList = immutableMessageList.filterUser(new TwitchUser(user2));
        ImmutableTwitchMessageList expectedFilteredTwitchMessages  = new ImmutableTwitchMessageList(expectedMessages);

        Assert.assertEquals("Filtered out no messages", expectedFilteredTwitchMessages, actualFilteredMessageList);
    }

    @Test
    public void testUserFilteringRemoveAll() {
        Collection<TwitchMessage> originalTwitchMessages =
                Arrays.asList(twitchMessage2, twitchMessage4);

        ImmutableTwitchMessageList immutableMessageList = new ImmutableTwitchMessageList(originalTwitchMessages);
        ImmutableTwitchMessageList actualFilteredMessageList = immutableMessageList.filterUser(new TwitchUser(user1));
        ImmutableTwitchMessageList expectedFilteredTwitchMessages  = new ImmutableTwitchMessageList(new HashSet<>());

        Assert.assertEquals("Filtered out no messages", expectedFilteredTwitchMessages, actualFilteredMessageList);
    }

    @Test
    public void testImmutableMessageListGivenNull() {
        ImmutableTwitchMessageList twitchMessageList = new ImmutableTwitchMessageList(null);
        Assert.assertEquals("Ensure list is empty.", twitchMessageList.size(), 0);
    }

    @Test
    public void testSize() {
        Collection<TwitchMessage> twitchMessages = Arrays.asList(twitchMessage1, twitchMessage3);
        ImmutableTwitchMessageList twitchMessageList = new ImmutableTwitchMessageList(twitchMessages);
        Assert.assertEquals("Ensure counts both messages.", twitchMessageList.size(), 2);
    }

    @Test
    public void testTimeSpanNoMessages() {
        ImmutableTwitchMessageList twitchMessageList = new ImmutableTwitchMessageList(null);
        Assert.assertEquals("Ensure period zero is given if empty", twitchMessageList.getMessageTimePeriod(), Period.ZERO);
    }
    @Test
    public void testTimeSpanTwoMessages() {
        Collection<TwitchMessage> twitchMessages = Arrays.asList(twitchMessage1, twitchMessage2);
        ImmutableTwitchMessageList twitchMessageList = new ImmutableTwitchMessageList(twitchMessages);
        Assert.assertEquals("Ensure period zero is given if empty", twitchMessageList.getMessageTimePeriod(), new Period(0, 0, 1, 0));
    }

    @Test
    public void testTimeSpanThreeMessages() {
        Collection<TwitchMessage> twitchMessages = Arrays.asList(twitchMessage1, twitchMessage2, twitchMessage4);
        ImmutableTwitchMessageList twitchMessageList = new ImmutableTwitchMessageList(twitchMessages);
        Assert.assertEquals("Ensure period zero is given if empty", twitchMessageList.getMessageTimePeriod(), new Period(0, 0, 3, 0));
    }

    @Test
    public void stream(){
        Collection<TwitchMessage> twitchMessages = Arrays.asList(twitchMessage1, twitchMessage2, twitchMessage4);
        ImmutableTwitchMessageList twitchMessageList = new ImmutableTwitchMessageList(twitchMessages);
        Assert.assertEquals("Ensure stream returned has correct count", twitchMessageList.stream().count(), twitchMessageList.size());
    }

    @Test
    public void containsSimplePayloadTrueSimple() {
        Collection<TwitchMessage> twitchMessages = Arrays.asList(twitchMessage1, twitchMessage2, twitchMessage4);
        ImmutableTwitchMessageList twitchMessageList = new ImmutableTwitchMessageList(twitchMessages);
        Assert.assertTrue("Assert message is found correctly.", twitchMessageList.containsSimplePayload(payload1) >= 1);
    }

    @Test
    public void containsSimplePayloadTrueSpaces() {
        Collection<TwitchMessage> twitchMessages = Arrays.asList(twitchMessage1, twitchMessage2, twitchMessage4);
        ImmutableTwitchMessageList twitchMessageList = new ImmutableTwitchMessageList(twitchMessages);
        Assert.assertTrue("Assert message is found correctly.", twitchMessageList.containsSimplePayload(payload1 + " " ) >= 1);
    }

    @Test
    public void containsSimplePayloadTrueUpperCase() {
        Collection<TwitchMessage> twitchMessages = Arrays.asList(twitchMessage1, twitchMessage2, twitchMessage4);
        ImmutableTwitchMessageList twitchMessageList = new ImmutableTwitchMessageList(twitchMessages);
        Assert.assertTrue("Assert message is found correctly.", twitchMessageList.containsSimplePayload(payload1.toUpperCase()) >= 1);
    }

    @Test
    public void containsSimplePayloadTrueUpperCaseAndSpace() {
        Collection<TwitchMessage> twitchMessages = Arrays.asList(twitchMessage1, twitchMessage2, twitchMessage4);
        ImmutableTwitchMessageList twitchMessageList = new ImmutableTwitchMessageList(twitchMessages);
        Assert.assertTrue("Assert message is found correctly.", twitchMessageList.containsSimplePayload(payload1.toUpperCase() + " ") >= 1 );
    }

    @Test
    public void containsSimplePayloadFalse() {
        Collection<TwitchMessage> twitchMessages = Arrays.asList(twitchMessage1, twitchMessage2, twitchMessage4);
        ImmutableTwitchMessageList twitchMessageList = new ImmutableTwitchMessageList(twitchMessages);
        Assert.assertFalse("Assert message is found correctly.", twitchMessageList.containsSimplePayload(payload1 + "foobar") >= 1);
    }
}