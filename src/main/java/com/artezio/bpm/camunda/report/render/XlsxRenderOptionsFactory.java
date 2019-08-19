package com.artezio.bpm.camunda.report.render;

import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;

import java.io.ByteArrayOutputStream;

public class XlsxRenderOptionsFactory implements RenderOptionsFactory {

    private static final String XLSX_OUTPUT_FORMAT = "xlsx";
    private static final String XLSX_EMITTER_ID = "uk.co.spudsoft.birt.emitters.excel.XlsxEmitter";

    @Override
    public RenderOption create(ByteArrayOutputStream baos) {
        RenderOption options = new EXCELRenderOption();
        options.setEmitterID(XLSX_EMITTER_ID);
        options.setOutputFormat(XLSX_OUTPUT_FORMAT);
        options.setOption(ExcelEmitter.REMOVE_BLANK_ROWS, false);
        options.setOption(ExcelEmitter.DISABLE_GROUPING, true);
        options.setOutputStream(baos);
        return options;
    }

}
