package com.artezio.bpm.camunda.report;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BirtDelegateTest {

    private static final String REPORT_TEMPLATE_NAME = "testBirtDelegate";
    private static final String BIRT_REPORT_TEMPLATE_EXTENSION = ".rptdesign";

    @Mock
    private RepositoryService repositoryService;
    @InjectMocks
    private BirtDelegate birtDelegate = new BirtDelegate();

    @Test
    public void testExecute_OutputFormatIsXlsx() {
        String reportTemplateFullName = REPORT_TEMPLATE_NAME + BIRT_REPORT_TEMPLATE_EXTENSION;
        String processDefinitionId = "processDefinitionId";
        String deploymentId = "deploymentId";
        Map<String, String> reportParams = new HashMap<String, String>() {{
           put("testParameter", "testParameterValue");
        }};
        FileValue expected = Variables
                .fileValue(REPORT_TEMPLATE_NAME + ".xlsx")
                .mimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .file(getFileAsStream(REPORT_TEMPLATE_NAME + ".xlsx"))
                .create();
        DelegateExecution execution = mock(DelegateExecution.class);
        ProcessDefinition processDefinition = mock(ProcessDefinition.class);
        ArgumentCaptor<FileValue> fileValueCaptor = ArgumentCaptor.forClass(FileValue.class);

        when(execution.getVariable("reportTemplateName")).thenReturn(REPORT_TEMPLATE_NAME);
        when(execution.getVariable("reportParams")).thenReturn(reportParams);
        when(execution.getVariable("outputFormat")).thenReturn("xlsx");
        when(execution.getProcessDefinitionId()).thenReturn(processDefinitionId);
        when(repositoryService.getProcessDefinition(processDefinitionId)).thenReturn(processDefinition);
        when(processDefinition.getDeploymentId()).thenReturn(deploymentId);
        when(repositoryService.getResourceAsStream(deploymentId, reportTemplateFullName))
                .thenReturn(getFileAsStream(reportTemplateFullName));
        doNothing().when(execution).setVariable(eq("generatedReport"), fileValueCaptor.capture());

        birtDelegate.execute(execution);

        assertFileValueEquals(expected, fileValueCaptor.getValue());
    }

    @Test
    public void testExecute_OutputFormatIsXls() {
        String reportTemplateFullName = REPORT_TEMPLATE_NAME + BIRT_REPORT_TEMPLATE_EXTENSION;
        String processDefinitionId = "processDefinitionId";
        String deploymentId = "deploymentId";
        Map<String, String> reportParams = new HashMap<String, String>() {{
            put("testParameter", "testParameterValue");
        }};
        FileValue expected = Variables
                .fileValue(REPORT_TEMPLATE_NAME + ".xls")
                .mimeType("application/xls")
                .file(getFileAsStream(REPORT_TEMPLATE_NAME + ".xls"))
                .create();
        DelegateExecution execution = mock(DelegateExecution.class);
        ProcessDefinition processDefinition = mock(ProcessDefinition.class);
        ArgumentCaptor<FileValue> fileValueCaptor = ArgumentCaptor.forClass(FileValue.class);

        when(execution.getVariable("reportTemplateName")).thenReturn(REPORT_TEMPLATE_NAME);
        when(execution.getVariable("reportParams")).thenReturn(reportParams);
        when(execution.getVariable("outputFormat")).thenReturn("xls");
        when(execution.getProcessDefinitionId()).thenReturn(processDefinitionId);
        when(repositoryService.getProcessDefinition(processDefinitionId)).thenReturn(processDefinition);
        when(processDefinition.getDeploymentId()).thenReturn(deploymentId);
        when(repositoryService.getResourceAsStream(deploymentId, reportTemplateFullName))
                .thenReturn(getFileAsStream(reportTemplateFullName));
        doNothing().when(execution).setVariable(eq("generatedReport"), fileValueCaptor.capture());

        birtDelegate.execute(execution);

        assertFileValueEquals(expected, fileValueCaptor.getValue());
    }

    @Test
    public void testExecute_OutputFormatIsDocx() {
        String reportTemplateFullName = REPORT_TEMPLATE_NAME + BIRT_REPORT_TEMPLATE_EXTENSION;
        String processDefinitionId = "processDefinitionId";
        String deploymentId = "deploymentId";
        Map<String, String> reportParams = new HashMap<String, String>() {{
            put("testParameter", "testParameterValue");
        }};
        FileValue expected = Variables
                .fileValue(REPORT_TEMPLATE_NAME + ".docx")
                .mimeType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .file(getFileAsStream(REPORT_TEMPLATE_NAME + ".docx"))
                .create();
        DelegateExecution execution = mock(DelegateExecution.class);
        ProcessDefinition processDefinition = mock(ProcessDefinition.class);
        ArgumentCaptor<FileValue> fileValueCaptor = ArgumentCaptor.forClass(FileValue.class);

        when(execution.getVariable("reportTemplateName")).thenReturn(REPORT_TEMPLATE_NAME);
        when(execution.getVariable("reportParams")).thenReturn(reportParams);
        when(execution.getVariable("outputFormat")).thenReturn("docx");
        when(execution.getProcessDefinitionId()).thenReturn(processDefinitionId);
        when(repositoryService.getProcessDefinition(processDefinitionId)).thenReturn(processDefinition);
        when(processDefinition.getDeploymentId()).thenReturn(deploymentId);
        when(repositoryService.getResourceAsStream(deploymentId, reportTemplateFullName))
                .thenReturn(getFileAsStream(reportTemplateFullName));
        doNothing().when(execution).setVariable(eq("generatedReport"), fileValueCaptor.capture());

        birtDelegate.execute(execution);

        assertFileValueEquals(expected, fileValueCaptor.getValue());
    }

    private void assertFileValueEquals(FileValue expected, FileValue actual) {
        assertEquals(expected.getFilename(), actual.getFilename());
        assertEquals(expected.getMimeType(), actual.getMimeType());
    }

    private InputStream getFileAsStream(String fileName) {
        return getClass().getResourceAsStream("/" + fileName);
    }

}
