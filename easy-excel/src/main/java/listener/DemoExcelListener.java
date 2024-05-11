package listener;

import bean.DemoExcel;
import com.alibaba.excel.context.AnalysisContext;

/**
 * @author gaozijie
 * @since 2024-05-06
 */
public class DemoExcelListener extends BaseExcelListener<DemoExcel> {

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(DemoExcel data, AnalysisContext context) {
        System.out.println("解析到数据：" + data);
    }

    /**
     * 所有数据解析完成
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("全部数据解析完毕");
    }
}
