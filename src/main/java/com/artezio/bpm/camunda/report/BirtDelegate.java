package com.artezio.bpm.camunda.report;

import com.artezio.bpm.camunda.report.render.*;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.*;
import org.mozilla.javascript.Context;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Named
public class BirtDelegate implements JavaDelegate {

    private static final String BIRT_REPORT_TEMPLATE_EXTENSION = ".rptdesign";

    @Inject
    private RepositoryService repositoryService;
    private static IReportEngineFactory factory;
    private static Map<OutputFormat, RenderOptionsFactory> renderOptionsFactories = new HashMap<>();
    static {
        renderOptionsFactories.put(OutputFormat.XLSX, new XlsxRenderOptionsFactory());
        renderOptionsFactories.put(OutputFormat.XLS, new XlsRenderOptionsFactory());
        renderOptionsFactories.put(OutputFormat.DOCX, new DocxRenderOptionsFactory());
        renderOptionsFactories.put(OutputFormat.DOC, new DocRenderOptionsFactory());
    }

    @Override
    public void execute(DelegateExecution execution) {
        String reportTemplateName = (String) execution.getVariable("template");
        Map<String, Object> reportParams = (Map<String, Object>) execution.getVariable("params");
        OutputFormat outputFormat = OutputFormat.valueOf(((String) execution.getVariable("outputFormat")).toUpperCase());
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(execution.getProcessDefinitionId());
        InputStream reportTemplate = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                reportTemplateName + BIRT_REPORT_TEMPLATE_EXTENSION);
        byte[] reportContent = generateReport(reportTemplate, outputFormat, reportParams);
        execution.setVariable("generatedReport", toFileValue(reportTemplateName, reportContent, outputFormat));
    }

    private static synchronized IReportEngine createReportEngine() throws BirtException {
        EngineConfig config = new EngineConfig();
        config.setLogConfig(null, Level.OFF);
        if (factory == null) {
            Platform.startup(config);
            factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
        }
        Context.enter().setOptimizationLevel(-1);
        return factory.createReportEngine(config);
    }

    private byte[] generateReport(InputStream reportTemplate, OutputFormat outputFormat, Map<String, Object> params) {
        IReportEngine engine = null;
        IRunAndRenderTask task = null;
        try {
            ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
            engine = createReportEngine();
            task = createEngineTask(engine, reportTemplate);
            setTaskParams(task, params);
            setTaskRenderOptions(task, outputFormat, reportOutput);
            task.run();
            return reportOutput.toByteArray();
        } catch (BirtException e) {
            throw new RuntimeException("Couldn't generate a report: ", e);
        } finally {
            if (task != null) task.close();
            if (engine != null) engine.destroy();
        }
    }

    private IRunAndRenderTask createEngineTask(IReportEngine engine, InputStream reportTemplate)
            throws EngineException {
        IReportRunnable design = engine.openReportDesign(reportTemplate);
        IRunAndRenderTask result = engine.createRunAndRenderTask(design);
        result.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader());
        return result;
    }

    private void setTaskParams(IRunAndRenderTask task, Map<String, Object> params) {
        task.setParameterValues(params);
        task.validateParameters();
    }

    private void setTaskRenderOptions(IRunAndRenderTask task, OutputFormat outputFormat,
                                      ByteArrayOutputStream reportOutput) {
        RenderOptionsFactory renderOptionsFactory = renderOptionsFactories.get(outputFormat);
        RenderOption options = renderOptionsFactory.create(reportOutput);
        task.setRenderOption(options);
    }

    private FileValue toFileValue(String fileName, byte[] content, OutputFormat outputFormat) {
        return Variables
                .fileValue(fileName + "." + outputFormat.getFileExtension())
                .file(content)
                .mimeType(outputFormat.getContentType())
                .encoding(StandardCharsets.UTF_8)
                .create();
    }

}
