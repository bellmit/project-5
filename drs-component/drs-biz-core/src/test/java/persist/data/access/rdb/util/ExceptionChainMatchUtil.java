package persist.data.access.rdb.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.contains;

public class ExceptionChainMatchUtil extends TypeSafeMatcher<Throwable> {

    private final Class<? extends Throwable>[] expectedClasses;
    private List<Class<? extends Throwable>> causualClasses;
    private Matcher<Iterable<? extends Class<? extends Throwable>>> matcher;
    private boolean messageFound = false;
    private String expectedMessage;

    public ExceptionChainMatchUtil(String expectedMessage, Class<? extends Throwable>... classes) {
        this.expectedMessage = expectedMessage;
        this.expectedClasses = classes;
    }

    @Override
    public void describeTo(Description description) {
        // copy of MatcherAssert.assertThat()
        description.appendText("")
                .appendText("\nExpected: ")
                .appendDescriptionOf(matcher)
                .appendText(" and a message ")
                .appendValue(expectedMessage);
        matcher.describeMismatch(causualClasses, description);
    }

    @Override
    protected boolean matchesSafely(Throwable item) {

        List<Class<? extends Throwable>> causes = new ArrayList<Class<? extends Throwable>>();
        while (item != null) {
            causes.add(item.getClass());
            if(item.getMessage().contains(expectedMessage))
                messageFound = true;
            item = item.getCause();
        }

        causualClasses = Collections.unmodifiableList(causes);

        // ordered test
        matcher = contains(expectedClasses);

        if(!messageFound) return false;

        return matcher.matches(causualClasses);
    }
}
