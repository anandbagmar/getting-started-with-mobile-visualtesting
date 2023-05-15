import AppiumTests.FirstEyesTest;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class Runner {
    public static void main(String args[]) {
        System.out.println(">>>>>>>> Inside Main Method <<<<<<<<");
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(FirstEyesTest.class))
                .build();

        final Launcher launcher = LauncherFactory.create();
        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        System.out.println("# of containers found: " + summary.getContainersFoundCount());
        System.out.println("# of containers skipped: " + summary.getContainersSkippedCount());
        System.out.println("# of tests found: " + summary.getTestsFoundCount());
        System.out.println("# of tests skipped: " + summary.getTestsSkippedCount());
        System.out.println("# of tests Succeeded: " + summary.getTestsSucceededCount());
    }
}
