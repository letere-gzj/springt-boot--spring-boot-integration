package controller;

import bean.DemoExcel;
import com.alibaba.excel.EasyExcel;
import handler.AutoColumnWidthHandler;
import jakarta.servlet.http.HttpServletResponse;
import listener.DemoExcelListener;
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
        EasyExcel.read(multipartFile.getInputStream(), DemoExcel.class, new DemoExcelListener())
                .sheet()
                .doRead();
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws Exception{
        List<DemoExcel> demoExcels = this.getDemoExcels();
        ExcelUtil.setExcelResponse(response, "下载");
        EasyExcel.write(response.getOutputStream(), DemoExcel.class)
                .sheet()
                .registerWriteHandler(new AutoColumnWidthHandler())
                .doWrite(demoExcels);
    }

    private List<DemoExcel> getDemoExcels() {
        List<DemoExcel> demoExcels = new ArrayList<>();
        for (int i=0; i < 3; i++) {
            DemoExcel demoExcel = new DemoExcel();
            demoExcel.setName("demo" + i);
            demoExcel.setDate(LocalDateTime.now().plusHours(i));
            demoExcel.setScore(Double.valueOf(i + "." + i));
            demoExcels.add(demoExcel);
        }
        return demoExcels;
    }
}
