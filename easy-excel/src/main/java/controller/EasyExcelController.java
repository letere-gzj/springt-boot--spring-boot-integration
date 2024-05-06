package controller;

import bean.ExcelDemo;
import com.alibaba.excel.EasyExcel;
import handler.AutoColumnWidthHandler;
import jakarta.servlet.http.HttpServletResponse;
import listener.ExcelDemoListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.ExcelUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaozijie
 * @since 2024-05-06
 */
@RestController
public class EasyExcelController {

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public void upload(@RequestPart MultipartFile multipartFile) throws Exception{
        EasyExcel.read(multipartFile.getInputStream(), ExcelDemo.class, new ExcelDemoListener())
                .sheet()
                .doRead();
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws Exception{
        List<ExcelDemo> excelDemos = this.getExcelDemos();
        ExcelUtil.setExcelResponse(response, "下载");
        EasyExcel.write(response.getOutputStream(), ExcelDemo.class)
                .sheet()
                .registerWriteHandler(new AutoColumnWidthHandler())
                .doWrite(excelDemos);
    }

    private List<ExcelDemo> getExcelDemos() {
        List<ExcelDemo> excelDemos = new ArrayList<>();
        for (int i=0; i < 3; i++) {
            ExcelDemo excelDemo = new ExcelDemo();
            excelDemo.setName("demo" + i);
            excelDemo.setDate(LocalDateTime.now().plusHours(i));
            excelDemo.setScore(Double.valueOf(i + "." + i));
            excelDemos.add(excelDemo);
        }
        return excelDemos;
    }
}
