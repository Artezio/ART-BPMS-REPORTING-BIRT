package com.artezio.bpm.camunda.report.render;

import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;

import java.io.ByteArrayOutputStream;

public class XlsRenderOptionsFactory implements RenderOptionsFactory {

    private static final String XLS_OUTPUT_FORMAT = "xls_spudsoft";
    private static final String XLS_EMITTER_ID = "uk.co.spudsoft.birt.emitters.excel.XlsEmitter";

    @Override
    public RenderOption create(ByteArrayOutputStream baos) {
        RenderOption options = new EXCELRenderOption();
        options.setEmitterID(XLS_EMITTER_ID);
        options.setOutputFormat(XLS_OUTPUT_FORMAT);
        options.setOption(ExcelEmitter.REMOVE_BLANK_ROWS, false);
        options.setOption(ExcelEmitter.DISABLE_GROUPING, true);
        options.setOutputStream(baos);
        return options;
    }

}
