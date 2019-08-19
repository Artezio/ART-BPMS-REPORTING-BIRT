package com.artezio.bpm.camunda.report.render;

import org.eclipse.birt.report.engine.api.DocxRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

import java.io.ByteArrayOutputStream;

public class DocxRenderOptionsFactory implements RenderOptionsFactory {

    private static final String DOCX_OUTPUT_FORMAT = "docx";
    private static final String DOCX_EMITTER_ID = "org.eclipse.birt.report.engine.emitter.docx";

    @Override
    public RenderOption create(ByteArrayOutputStream baos) {
        RenderOption options = new DocxRenderOption();
        options.setEmitterID(DOCX_EMITTER_ID);
        options.setOutputFormat(DOCX_OUTPUT_FORMAT);
        options.setOutputStream(baos);
        return options;
    }
}
