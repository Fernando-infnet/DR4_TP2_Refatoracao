package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

public class Pipeline {
    private final Config config;
    private final Emailer emailer;
    private final Logger log;

    public Pipeline(Config config, Emailer emailer, Logger log) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;
    }

    public void run(Project project) {
        boolean testsPassed = runTests(project);
        boolean deploySuccessful = testsPassed && deploy(project);
        sendEmailSummaryIfEnabled(testsPassed, deploySuccessful);
    }

    private boolean runTests(Project project) {
        if (project.hasTests()) {
            String testResult = project.runTests();
            if ("success".equals(testResult)) {
                log.info("Tests passed");
                return true;
            } else {
                log.error("Tests failed");
                return false;
            }
        } else {
            log.info("No tests");
            return true;
        }
    }

    private boolean deploy(Project project) {
        String deployResult = project.deploy();
        if ("success".equals(deployResult)) {
            log.info("Deployment successful");
            return true;
        } else {
            log.error("Deployment failed");
            return false;
        }
    }

    private void sendEmailSummaryIfEnabled(boolean testsPassed, boolean deploySuccessful) {
        if (config.sendEmailSummary()) {
            log.info("Sending email");
            String message = determineEmailMessage(testsPassed, deploySuccessful);
            emailer.send(message);
        } else {
            log.info("Email disabled");
        }
    }

    private String determineEmailMessage(boolean testsPassed, boolean deploySuccessful) {
        if (testsPassed) {
            return deploySuccessful ? "Deployment completed successfully" : "Deployment failed";
        } else {
            return "Tests failed";
        }
    }
}
