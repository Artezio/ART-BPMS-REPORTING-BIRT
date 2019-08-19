package com.artezio.bpm.camunda.report.render;

import org.eclipse.birt.report.engine.api.RenderOption;

import java.io.ByteArrayOutputStream;

public class DocRenderOptionsFactory implements RenderOptionsFactory {

    private static final String DOC_OUTPUT_FORMAT = "doc";
    private static final String DOC_EMITTER_ID = "org.eclipse.birt.report.engine.emitter.word";

    @Override
    public RenderOption create(ByteArrayOutputStream baos) {
        RenderOption options = new RenderOption();
        options.setEmitterID(DOC_EMITTER_ID);
        options.setOutputFormat(DOC_OUTPUT_FORMAT);
        options.setOutputStream(baos);
        return options;
    }
}
