package org.sammancoaching;

import org.junit.jupiter.api.Test;
import org.sammancoaching.dependencies.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PipelineTest {

    @Test
    void testePipelineComTestesAprovadosEImplantacaoBemSucedida() {
        Config config = mock(Config.class);
        Emailer emailer = mock(Emailer.class);
        CapturingLogger logger = new CapturingLogger();
        Project project = mock(Project.class);

        when(config.sendEmailSummary()).thenReturn(true);
        when(project.hasTests()).thenReturn(true);
        when(project.runTests()).thenReturn("success");
        when(project.deploy()).thenReturn("success");

        Pipeline pipeline = new Pipeline(config, emailer, logger);
        pipeline.run(project);

        verify(emailer).send("Deployment completed successfully");
        assertTrue(logger.getLoggedLines().contains("INFO: Tests passed"));
        assertTrue(logger.getLoggedLines().contains("INFO: Deployment successful"));
        assertTrue(logger.getLoggedLines().contains("INFO: Sending email"));
    }

    @Test
    void testePipelineComTestesFalhando() {
        Config config = mock(Config.class);
        Emailer emailer = mock(Emailer.class);
        CapturingLogger logger = new CapturingLogger();
        Project project = mock(Project.class);

        when(config.sendEmailSummary()).thenReturn(true);
        when(project.hasTests()).thenReturn(true);
        when(project.runTests()).thenReturn("failure");

        Pipeline pipeline = new Pipeline(config, emailer, logger);
        pipeline.run(project);

        verify(emailer).send("Tests failed");
        assertTrue(logger.getLoggedLines().contains("ERROR: Tests failed"));
        assertFalse(logger.getLoggedLines().contains("INFO: Deployment successful"));
    }

    @Test
    void testePipelineSemTestesEImplantacaoBemSucedida() {
        Config config = mock(Config.class);
        Emailer emailer = mock(Emailer.class);
        CapturingLogger logger = new CapturingLogger();
        Project project = mock(Project.class);

        when(config.sendEmailSummary()).thenReturn(true);
        when(project.hasTests()).thenReturn(false);
        when(project.deploy()).thenReturn("success");

        Pipeline pipeline = new Pipeline(config, emailer, logger);
        pipeline.run(project);

        verify(emailer).send("Deployment completed successfully");
        assertTrue(logger.getLoggedLines().contains("INFO: No tests"));
        assertTrue(logger.getLoggedLines().contains("INFO: Deployment successful"));
    }

    @Test
    void testePipelineComTestesAprovadosMasImplantacaoFalhando() {
        Config config = mock(Config.class);
        Emailer emailer = mock(Emailer.class);
        CapturingLogger logger = new CapturingLogger();
        Project project = mock(Project.class);

        when(config.sendEmailSummary()).thenReturn(true);
        when(project.hasTests()).thenReturn(true);
        when(project.runTests()).thenReturn("success");
        when(project.deploy()).thenReturn("failure");

        Pipeline pipeline = new Pipeline(config, emailer, logger);
        pipeline.run(project);

        verify(emailer).send("Deployment failed");
        assertTrue(logger.getLoggedLines().contains("INFO: Tests passed"));
        assertTrue(logger.getLoggedLines().contains("ERROR: Deployment failed"));
    }

    @Test
    void testePipelineComEmailDesabilitado() {
        Config config = mock(Config.class);
        Emailer emailer = mock(Emailer.class);
        CapturingLogger logger = new CapturingLogger();
        Project project = mock(Project.class);

        when(config.sendEmailSummary()).thenReturn(false);
        when(project.hasTests()).thenReturn(true);
        when(project.runTests()).thenReturn("success");
        when(project.deploy()).thenReturn("success");

        Pipeline pipeline = new Pipeline(config, emailer, logger);
        pipeline.run(project);

        verify(emailer, never()).send(any());
        assertTrue(logger.getLoggedLines().contains("INFO: Email disabled"));
    }

}
