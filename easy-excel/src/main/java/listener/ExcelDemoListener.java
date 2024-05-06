package listener;

import bean.ExcelDemo;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

/**
 * @author gaozijie
 * @since 2024-05-06
 */
public class ExcelDemoListener implements ReadListener<ExcelDemo> {

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(ExcelDemo data, AnalysisContext context) {
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
