package handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.SheetWriteHandlerContext;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Excel指定范围单元格合并
 * @author gaozijie
 * @since 2024-02-05
 */
public class CellRangeMergeHandler implements SheetWriteHandler {

    /**
     * 单元格合并范围集合
     */
    private final List<CellRangeAddress> cellRangeAddresses;

    public CellRangeMergeHandler(List<CellRangeAddress> cellRangeAddresses) {
        this.cellRangeAddresses = Objects.isNull(cellRangeAddresses) ? Collections.emptyList() : cellRangeAddresses;
    }

    @Override
    public void afterSheetCreate(SheetWriteHandlerContext context) {
        Sheet sheet = context.getWriteSheetHolder().getSheet();
        for (CellRangeAddress cellRangeAddress : cellRangeAddresses) {
            sheet.addMergedRegionUnsafe(cellRangeAddress);
        }
    }
}
